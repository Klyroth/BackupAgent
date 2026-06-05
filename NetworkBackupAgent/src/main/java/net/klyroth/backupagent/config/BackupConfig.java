package net.klyroth.backupagent.config;

import java.util.List;

public record BackupConfig(
        int intervalMinutes,
        String outputFolder,
        String filePrefix,
        int compressionLevel,
        boolean runOnStartup,
        List<String> paths,
        List<String> exclude
) {}
