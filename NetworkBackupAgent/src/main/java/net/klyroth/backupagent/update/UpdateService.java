package net.klyroth.backupagent.update;

import net.klyroth.backupagent.config.BackupAgentConfig;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class UpdateService {
    private final BackupAgentConfig config;

    public UpdateService(BackupAgentConfig config) { this.config = config; }

    public void update() throws Exception {
        if (!config.update().enabled()) throw new IllegalStateException("Update-System ist deaktiviert.");
        String source = config.update().source();
        if (source == null || source.isBlank()) throw new IllegalStateException("Keine Update-Quelle gesetzt.");

        Path currentJar = Path.of(config.update().currentJar()).toAbsolutePath();
        Path oldJar = Path.of(config.update().currentJar() + ".old").toAbsolutePath();
        Path newJar = Path.of(config.update().currentJar() + ".new").toAbsolutePath();

        if (source.startsWith("http://") || source.startsWith("https://")) {
            try (InputStream inputStream = URI.create(source).toURL().openStream()) {
                Files.copy(inputStream, newJar, StandardCopyOption.REPLACE_EXISTING);
            }
        } else {
            Path sourcePath = Path.of(source).toAbsolutePath();
            if (!Files.exists(sourcePath)) throw new IllegalStateException("Update-Datei nicht gefunden: " + sourcePath);
            Files.copy(sourcePath, newJar, StandardCopyOption.REPLACE_EXISTING);
        }

        if (Files.size(newJar) <= 0) throw new IllegalStateException("Update-Datei ist leer.");
        if (Files.exists(currentJar)) Files.copy(currentJar, oldJar, StandardCopyOption.REPLACE_EXISTING);
        Files.move(newJar, currentJar, StandardCopyOption.REPLACE_EXISTING);
    }
}
