package net.klyroth.backupagent.backup;

import net.klyroth.backupagent.config.BackupAgentConfig;
import net.klyroth.backupagent.event.EventManager;
import net.klyroth.backupagent.event.events.BackupFailedEvent;
import net.klyroth.backupagent.event.events.BackupStartEvent;
import net.klyroth.backupagent.event.events.BackupSuccessEvent;
import net.klyroth.backupagent.storage.StorageGuard;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public final class BackupService {
    private final BackupAgentConfig config;
    private final StorageGuard storageGuard;
    private final EventManager eventManager;
    private final BackupNameGenerator nameGenerator = new BackupNameGenerator();
    private final ZipBackupWriter writer = new ZipBackupWriter();
    private final AtomicBoolean running = new AtomicBoolean(false);

    public BackupService(BackupAgentConfig config, StorageGuard storageGuard, EventManager eventManager) {
        this.config = config;
        this.storageGuard = storageGuard;
        this.eventManager = eventManager;
    }

    public BackupResult createBackup(BackupRequest request) {
        if (!running.compareAndSet(false, true)) {
            return new BackupResult(false, null, null, 0, Duration.ZERO, "Es läuft bereits ein Backup.");
        }
        Instant start = Instant.now();
        BackupStartEvent startEvent = new BackupStartEvent(request);
        eventManager.call(startEvent);
        if (startEvent.isCancelled()) {
            running.set(false);
            return new BackupResult(false, null, null, 0, Duration.ZERO, "Backup wurde durch ein Plugin abgebrochen.");
        }

        try {
            storageGuard.ensureEnoughStorage();
            Path outputFolder = Path.of(config.backup().outputFolder()).toAbsolutePath();
            Files.createDirectories(outputFolder);
            String backupName = nameGenerator.generate(config.backup().filePrefix());
            Path zipFile = outputFolder.resolve(backupName);
            System.out.println("[BACKUP] Starte: " + backupName + " | Grund: " + request.reason() + " | Von: " + request.requestedBy());
            writer.writeZip(zipFile, config.backup());
            long size = Files.size(zipFile);
            BackupResult result = new BackupResult(true, backupName, zipFile, size, Duration.between(start, Instant.now()), "Backup erfolgreich erstellt.");
            eventManager.call(new BackupSuccessEvent(result));
            return result;
        } catch (Exception exception) {
            eventManager.call(new BackupFailedEvent(request, exception));
            return new BackupResult(false, null, null, 0, Duration.between(start, Instant.now()), exception.getMessage());
        } finally {
            running.set(false);
        }
    }

    public boolean isRunning() {
        return running.get();
    }
}
