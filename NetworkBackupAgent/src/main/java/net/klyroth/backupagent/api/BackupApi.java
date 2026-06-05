package net.klyroth.backupagent.api;

import net.klyroth.backupagent.backup.BackupInfo;
import net.klyroth.backupagent.backup.BackupRequest;
import net.klyroth.backupagent.backup.BackupResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BackupApi {
    CompletableFuture<BackupResult> createBackup(BackupRequest request);
    List<BackupInfo> getBackups();
    List<BackupInfo> searchBackups(String query);
    List<BackupInfo> sortBackups(String mode);
    boolean deleteBackup(String backupName);
    BackupInfo getLatestBackup();
    BackupInfo getOldestBackup();
}
