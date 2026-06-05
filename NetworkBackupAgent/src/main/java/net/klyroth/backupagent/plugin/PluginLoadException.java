package net.klyroth.backupagent.plugin;

public final class PluginLoadException extends RuntimeException {
    public PluginLoadException(String message) { super(message); }
    public PluginLoadException(String message, Throwable cause) { super(message, cause); }
}
