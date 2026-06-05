package net.klyroth.backupagent.backup;

import java.nio.file.Path;
import java.time.Instant;

public record BackupInfo(String name, Path path, long sizeBytes, Instant createdAt) {}
