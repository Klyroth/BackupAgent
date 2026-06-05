package net.klyroth.backupagent.plugin;

import java.nio.file.Path;

public final class PluginContainer {
    private final Path jarPath;
    private final BackupPlugin plugin;
    private final PluginDescription description;
    private PluginState state;

    public PluginContainer(Path jarPath, BackupPlugin plugin, PluginDescription description) {
        this.jarPath = jarPath;
        this.plugin = plugin;
        this.description = description;
        this.state = PluginState.LOADED;
    }

    public Path getJarPath() { return jarPath; }
    public BackupPlugin getPlugin() { return plugin; }
    public PluginDescription getDescription() { return description; }
    public PluginState getState() { return state; }
    public void setState(PluginState state) { this.state = state; }
}
