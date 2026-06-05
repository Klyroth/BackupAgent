package net.klyroth.backupagent.plugin;

import net.klyroth.backupagent.api.BackupAgent;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public final class PluginManager {
    private final Path pluginsFolder;
    private final BackupAgent agent;
    private final List<PluginContainer> plugins = new ArrayList<>();

    public PluginManager(Path pluginsFolder, BackupAgent agent) {
        this.pluginsFolder = pluginsFolder;
        this.agent = agent;
    }

    public void loadPlugins() throws Exception {
        Files.createDirectories(pluginsFolder);
        try (var stream = Files.list(pluginsFolder)) {
            for (Path jar : stream.filter(path -> path.toString().endsWith(".jar")).toList()) loadPlugin(jar);
        }
    }

    public void enablePlugins() {
        for (PluginContainer container : plugins) {
            try {
                container.getPlugin().onEnable();
                container.setState(PluginState.ENABLED);
                System.out.println("[PLUGIN] Aktiviert: " + container.getDescription().name());
            } catch (Exception exception) {
                container.setState(PluginState.FAILED);
                System.err.println("[PLUGIN] Fehler beim Aktivieren: " + container.getDescription().name());
                exception.printStackTrace();
            }
        }
    }

    public void disablePlugins() {
        for (PluginContainer container : plugins) {
            try {
                if (container.getState() == PluginState.ENABLED) {
                    container.getPlugin().onDisable();
                    container.setState(PluginState.DISABLED);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void loadPlugin(Path jarPath) throws Exception {
        PluginDescription description = readDescription(jarPath);
        URL[] urls = { jarPath.toUri().toURL() };
        PluginClassLoader loader = new PluginClassLoader(urls, getClass().getClassLoader());
        Class<?> mainClass = Class.forName(description.main(), true, loader);
        if (!BackupPlugin.class.isAssignableFrom(mainClass)) throw new PluginLoadException("Main-Klasse muss BackupPlugin erweitern: " + description.main());
        Constructor<?> constructor = mainClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        BackupPlugin plugin = (BackupPlugin) constructor.newInstance();
        plugin.initialize(agent, description);
        plugins.add(new PluginContainer(jarPath, plugin, description));
        System.out.println("[PLUGIN] Geladen: " + description.name());
    }

    private PluginDescription readDescription(Path jarPath) throws Exception {
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            var entry = jarFile.getJarEntry("backup-plugin.yml");
            if (entry == null) throw new PluginLoadException("backup-plugin.yml fehlt in " + jarPath);
            try (InputStream in = jarFile.getInputStream(entry)) {
                Map<String, Object> data = new Yaml().load(in);
                return new PluginDescription(str(data, "name"), str(data, "version"), str(data, "main"), str(data, "author"), str(data, "description"));
            }
        }
    }

    private String str(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value == null ? "" : value.toString();
    }

    public List<PluginContainer> getPlugins() { return List.copyOf(plugins); }
}
