package net.klyroth.backupagent.api;

public interface BackupAgent {
    BackupApi backups();
    PluginApi plugins();
    EventBus events();
    CommandApi commands();
    ServiceRegistry services();
}
