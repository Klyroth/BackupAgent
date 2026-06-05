package net.klyroth.backupagent.config;

public record ApiConfig(boolean enabled, String host, int port, String token) {}
