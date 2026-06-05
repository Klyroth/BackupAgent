package net.klyroth.backupagent.storage;

import net.klyroth.backupagent.backup.BackupFileScanner;
import net.klyroth.backupagent.config.BackupAgentConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public final class StorageGuard {
    private final StorageService storageService;
    private final BackupFileScanner backupFileScanner;
    private final BackupAgentConfig config;

    public StorageGuard(StorageService storageService, BackupFileScanner backupFileScanner, BackupAgentConfig config) {
        this.storageService = storageService;
        this.backupFileScanner = backupFileScanner;
        this.config = config;
    }

    public void ensureEnoughStorage() throws IOException {
        if (!config.storage().enabled()) return;
        Path outputFolder = Path.of(config.backup().outputFolder()).toAbsolutePath();
        while (storageService.getReport(outputFolder).usableGb() < config.storage().minFreeSpaceGb()) {
            if (!config.storage().deleteOldestIfLowSpace()) {
                throw new IOException("Zu wenig Speicherplatz! Auto-Löschen ist deaktiviert.");
            }
            File oldest = backupFileScanner.findOldestFile(outputFolder);
            if (oldest == null) throw new IOException("Zu wenig Speicherplatz, Es wurden keine alten Backup gefunden.");
            System.out.println("[STORAGE] Lösche ältestes Backup wegen Speicherplatzmangel: " + oldest.getName());
            if (!oldest.delete()) throw new IOException("Altes Backup konnte nicht gelöscht werden: " + oldest.getAbsolutePath());
        }
    }
}
