package net.klyroth.backupagent.scheduler;

import net.klyroth.backupagent.backup.BackupRequest;
import net.klyroth.backupagent.backup.BackupResult;
import net.klyroth.backupagent.backup.BackupService;
import net.klyroth.backupagent.config.BackupAgentConfig;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class BackupScheduler {
    private final BackupAgentConfig config;
    private final BackupService backupService;
    private ScheduledExecutorService executorService;

    public BackupScheduler(BackupAgentConfig config, BackupService backupService) {
        this.config = config;
        this.backupService = backupService;
    }

    public void start() {
        stop();
        executorService = Executors.newSingleThreadScheduledExecutor();
        long initialDelay = config.backup().runOnStartup() ? 0 : config.backup().intervalMinutes();
        executorService.scheduleAtFixedRate(this::runBackup, initialDelay, config.backup().intervalMinutes(), TimeUnit.MINUTES);
        System.out.println("[SCHEDULER] Gestartet. Intervall: " + config.backup().intervalMinutes() + " Minuten");
    }

    public void stop() {
        if (executorService != null) executorService.shutdownNow();
    }

    private void runBackup() {
        BackupResult result = backupService.createBackup(BackupRequest.system());
        if (result.success()) System.out.println("[SCHEDULER] Backup fertig: " + result.backupName());
        else System.err.println("[SCHEDULER] Backup fehlgeschlagen: " + result.message());
    }
}
