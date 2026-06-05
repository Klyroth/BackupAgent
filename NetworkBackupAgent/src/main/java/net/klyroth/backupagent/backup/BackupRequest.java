package net.klyroth.backupagent.backup;

public record BackupRequest(String reason, String requestedBy) {
    public static BackupRequest system() { return new BackupRequest("scheduled", "system"); }
}
