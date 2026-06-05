package net.klyroth.backupagent.command;

import net.klyroth.backupagent.backup.BackupFileScanner;
import net.klyroth.backupagent.backup.BackupService;
import net.klyroth.backupagent.config.ConfigService;
import net.klyroth.backupagent.plugin.PluginManager;
import net.klyroth.backupagent.scheduler.BackupScheduler;
import net.klyroth.backupagent.storage.StorageService;
import net.klyroth.backupagent.update.UpdateService;

public record CommandContext(
        ConfigService configService,
        BackupService backupService,
        BackupFileScanner backupFileScanner,
        StorageService storageService,
        UpdateService updateService,
        BackupScheduler scheduler,
        PluginManager pluginManager
) {}
