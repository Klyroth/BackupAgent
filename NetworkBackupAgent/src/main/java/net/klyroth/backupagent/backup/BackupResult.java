package net.klyroth.backupagent.backup;

import java.nio.file.Path;
import java.time.Duration;

public record BackupResult(boolean success, String backupName, Path file, long sizeBytes, Duration duration, String message) {}
