package net.klyroth.backupagent.backup;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class BackupFileScanner {
    public List<File> findBackupFiles(Path backupFolder) {
        File[] files = backupFolder.toFile().listFiles((dir, name) -> name.endsWith(".zip"));
        if (files == null) return List.of();
        return Arrays.asList(files);
    }

    public List<BackupInfo> findBackups(Path backupFolder) {
        return findBackupFiles(backupFolder).stream().map(this::toInfo).toList();
    }

    public List<BackupInfo> search(Path backupFolder, String query) {
        String q = query == null ? "" : query.toLowerCase();
        return findBackups(backupFolder).stream().filter(b -> b.name().toLowerCase().contains(q)).toList();
    }

    public List<BackupInfo> sort(Path backupFolder, String mode) {
        Comparator<BackupInfo> comparator = switch (mode == null ? "newest" : mode.toLowerCase()) {
            case "oldest" -> Comparator.comparing(BackupInfo::createdAt);
            case "size" -> Comparator.comparingLong(BackupInfo::sizeBytes).reversed();
            default -> Comparator.comparing(BackupInfo::createdAt).reversed();
        };
        return findBackups(backupFolder).stream().sorted(comparator).toList();
    }

    public File findOldestFile(Path backupFolder) {
        return findBackupFiles(backupFolder).stream().min(Comparator.comparingLong(File::lastModified)).orElse(null);
    }

    public File findLatestFile(Path backupFolder) {
        return findBackupFiles(backupFolder).stream().max(Comparator.comparingLong(File::lastModified)).orElse(null);
    }

    public BackupInfo findOldest(Path backupFolder) {
        File file = findOldestFile(backupFolder);
        return file == null ? null : toInfo(file);
    }

    public BackupInfo findLatest(Path backupFolder) {
        File file = findLatestFile(backupFolder);
        return file == null ? null : toInfo(file);
    }

    public BackupInfo toInfo(File file) {
        return new BackupInfo(file.getName(), file.toPath().toAbsolutePath(), file.length(), Instant.ofEpochMilli(file.lastModified()));
    }
}
