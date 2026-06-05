package net.klyroth.backupagent.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class ConfigService {
    private final Path configPath;
    private final Yaml yaml = new Yaml();

    public ConfigService(Path configPath) {
        this.configPath = configPath;
    }

    public boolean exists() {
        return Files.exists(configPath);
    }

    @SuppressWarnings("unchecked")
    public BackupAgentConfig load() throws IOException {
        try (InputStream inputStream = Files.newInputStream(configPath)) {
            Map<String, Object> root = yaml.load(inputStream);
            if (root == null) root = Map.of();

            Map<String, Object> backup = map(root, "backup");
            Map<String, Object> storage = map(root, "storage");
            Map<String, Object> plugins = map(root, "plugins");
            Map<String, Object> update = map(root, "update");

            BackupConfig backupConfig = new BackupConfig(
                    integer(backup, "interval-minutes", 20),
                    string(backup, "output-folder", "backups"),
                    string(backup, "file-prefix", "network-backup"),
                    integer(backup, "compression-level", 6),
                    bool(backup, "run-on-startup", true),
                    list(backup, "paths"),
                    list(backup, "exclude")
            );

            StorageConfig storageConfig = new StorageConfig(
                    bool(storage, "enabled", true),
                    integer(storage, "min-free-space-gb", 30),
                    bool(storage, "delete-oldest-if-low-space", false)
            );

            PluginConfig pluginConfig = new PluginConfig(
                    bool(plugins, "enabled", true),
                    string(plugins, "folder", "plugins")
            );

            UpdateConfig updateConfig = new UpdateConfig(
                    bool(update, "enabled", false),
                    string(update, "source", ""),
                    string(update, "current-jar", "KlyrothBackupAgent.jar")
            );

            return new BackupAgentConfig(backupConfig, storageConfig, pluginConfig, updateConfig);
        }
    }

    public void writeDefaultConfig() throws IOException {
        if (configPath.getParent() != null) Files.createDirectories(configPath.getParent());
        try (Writer writer = Files.newBufferedWriter(configPath)) {
            writer.write("""
                    app:
                      name: "KlyrothBackupAgent"
                      version: "1.0.0"

                    backup:
                      interval-minutes: 20
                      output-folder: "backups"
                      file-prefix: "network-backup"
                      compression-level: 6
                      run-on-startup: true
                      paths:
                        - "/home/minecraft/cloudnet"
                        - "/home/minecraft/velocity"
                      exclude:
                        - "logs"
                        - "cache"
                        - "temp"
                        - "backups"
                        - ".tmp"

                    storage:
                      enabled: true
                      min-free-space-gb: 30
                      delete-oldest-if-low-space: false

                    plugins:
                      enabled: true
                      folder: "plugins"

                    update:
                      enabled: true
                      source: "/home/minecraft/update/KlyrothBackupAgent.jar"
                      current-jar: "KlyrothBackupAgent.jar"
                    """);
        }
    }

    public Path getConfigPath() { return configPath; }

    @SuppressWarnings("unchecked")
    private Map<String, Object> map(Map<String, Object> root, String key) {
        Object value = root.get(key);
        return value instanceof Map<?, ?> m ? (Map<String, Object>) m : Map.of();
    }

    @SuppressWarnings("unchecked")
    private List<String> list(Map<String, Object> root, String key) {
        Object value = root.get(key);
        return value instanceof List<?> l ? (List<String>) l : List.of();
    }

    private String string(Map<String, Object> root, String key, String def) {
        Object value = root.get(key);
        return value == null ? def : value.toString();
    }

    private int integer(Map<String, Object> root, String key, int def) {
        Object value = root.get(key);
        if (value instanceof Number n) return n.intValue();
        if (value == null) return def;
        return Integer.parseInt(value.toString());
    }

    private boolean bool(Map<String, Object> root, String key, boolean def) {
        Object value = root.get(key);
        return value == null ? def : Boolean.parseBoolean(value.toString());
    }
}
