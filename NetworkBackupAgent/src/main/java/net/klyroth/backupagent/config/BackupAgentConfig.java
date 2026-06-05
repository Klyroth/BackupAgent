package net.klyroth.backupagent.config;

public record BackupAgentConfig(
        BackupConfig backup,
        StorageConfig storage,
        PluginConfig plugins,
        UpdateConfig update
) {}
