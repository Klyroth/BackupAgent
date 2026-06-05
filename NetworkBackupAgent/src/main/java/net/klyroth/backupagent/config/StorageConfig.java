package net.klyroth.backupagent.config;

public record StorageConfig(boolean enabled, long minFreeSpaceGb, boolean deleteOldestIfLowSpace) {}
