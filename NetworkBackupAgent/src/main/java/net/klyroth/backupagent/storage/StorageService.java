package net.klyroth.backupagent.storage;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;

public final class StorageService {
    public StorageReport getReport(Path path) throws IOException {
        Files.createDirectories(path);
        FileStore store = Files.getFileStore(path);
        long free = store.getUsableSpace();
        long total = store.getTotalSpace();
        return new StorageReport(free, total, free / 1024 / 1024 / 1024);
    }
}
