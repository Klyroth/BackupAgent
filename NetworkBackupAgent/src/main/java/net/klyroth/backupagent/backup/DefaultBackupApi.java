package net.klyroth.backupagent.backup;

import net.klyroth.backupagent.api.BackupApi;
import net.klyroth.backupagent.config.BackupAgentConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class DefaultBackupApi implements BackupApi {
    private final BackupService backupService;
    private final BackupFileScanner scanner;
    private final BackupAgentConfig config;

    public DefaultBackupApi(BackupService backupService, BackupFileScanner scanner, BackupAgentConfig config) {
        this.backupService = backupService;
        this.scanner = scanner;
        this.config = config;
    }

    @Override
    public CompletableFuture<BackupResult> createBackup(BackupRequest request) {
        return CompletableFuture.supplyAsync(() -> backupService.createBackup(request));
    }

    @Override
    public List<BackupInfo> getBackups() { return scanner.findBackups(folder()); }

    @Override
    public List<BackupInfo> searchBackups(String query) { return scanner.search(folder(), query); }

    @Override
    public List<BackupInfo> sortBackups(String mode) { return scanner.sort(folder(), mode); }

    @Override
    public boolean deleteBackup(String backupName) {
        if (backupName == null || backupName.isBlank() || backupName.contains("/") || backupName.contains("\\") || !backupName.endsWith(".zip")) return false;
        File file = folder().resolve(backupName).toFile();
        return file.exists() && file.isFile() && file.delete();
    }

    @Override
    public BackupInfo getLatestBackup() { return scanner.findLatest(folder()); }

    @Override
    public BackupInfo getOldestBackup() { return scanner.findOldest(folder()); }

    private Path folder() { return Path.of(config.backup().outputFolder()).toAbsolutePath(); }
}
