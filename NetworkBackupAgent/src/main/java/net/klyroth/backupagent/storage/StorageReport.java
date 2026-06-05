package net.klyroth.backupagent.storage;

public record StorageReport(long freeBytes, long totalBytes, long usableGb) {}
