package net.klyroth.backupagent.backup;

import net.klyroth.backupagent.config.BackupConfig;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipBackupWriter {
    public void writeZip(Path zipFile, BackupConfig config) throws IOException {
        Files.createDirectories(zipFile.getParent());
        Path outputFolder = Path.of(config.outputFolder()).toAbsolutePath().normalize();
        try (ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(zipFile)))) {
            zip.setLevel(normalizeCompression(config.compressionLevel()));
            for (String source : config.paths()) {
                Path path = Path.of(source).toAbsolutePath().normalize();
                if (!Files.exists(path)) {
                    System.out.println("[WARN] Pfad existiert nicht: " + path);
                    continue;
                }
                if (path.startsWith(outputFolder)) {
                    System.out.println("[WARN] Backup-Output wird nicht mitgesichert: " + path);
                    continue;
                }
                if (Files.isDirectory(path)) zipDirectory(path, zip, config, outputFolder);
                else zipSingleFile(path, path.getParent(), zip);
            }
        }
    }

    private void zipDirectory(Path sourceDir, ZipOutputStream zip, BackupConfig config, Path outputFolder) throws IOException {
        Path parent = sourceDir.getParent();
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                Path normalized = dir.toAbsolutePath().normalize();
                if (normalized.startsWith(outputFolder) || shouldExclude(dir, config)) return FileVisitResult.SKIP_SUBTREE;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                try {
                    if (!file.toAbsolutePath().normalize().startsWith(outputFolder) && !shouldExclude(file, config)) {
                        zipSingleFile(file, parent, zip);
                    }
                } catch (Exception e) {
                    System.err.println("[WARN] Datei übersprungen: " + file + " | " + e.getMessage());
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void zipSingleFile(Path file, Path basePath, ZipOutputStream zip) throws IOException {
        if (!Files.isRegularFile(file)) return;
        String entryName = basePath.relativize(file).toString().replace("\\", "/");
        zip.putNextEntry(new ZipEntry(entryName));
        Files.copy(file, zip);
        zip.closeEntry();
    }

    private boolean shouldExclude(Path path, BackupConfig config) {
        String normalized = path.toString().replace("\\", "/").toLowerCase();
        for (String exclude : config.exclude()) {
            String ex = exclude.toLowerCase();
            if (normalized.contains("/" + ex + "/") || normalized.endsWith("/" + ex) || normalized.contains(ex)) return true;
        }
        return false;
    }

    private int normalizeCompression(int level) { return (level < 0 || level > 9) ? 6 : level; }
}
