package net.klyroth.backupagent.backup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class BackupNameGenerator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    public String generate(String prefix) { return prefix + "_" + LocalDateTime.now().format(FORMATTER) + ".zip"; }
}
