package net.klyroth.backupagent.api;

public final class DefaultBackupAgent implements BackupAgent {
    private final BackupApi backupApi;
    private final PluginApi pluginApi;
    private final EventBus eventBus;
    private final CommandApi commandApi;
    private final ServiceRegistry serviceRegistry;

    public DefaultBackupAgent(BackupApi backupApi, PluginApi pluginApi, EventBus eventBus, CommandApi commandApi, ServiceRegistry serviceRegistry) {
        this.backupApi = backupApi;
        this.pluginApi = pluginApi;
        this.eventBus = eventBus;
        this.commandApi = commandApi;
        this.serviceRegistry = serviceRegistry;
    }

    public BackupApi backups() { return backupApi; }
    public PluginApi plugins() { return pluginApi; }
    public EventBus events() { return eventBus; }
    public CommandApi commands() { return commandApi; }
    public ServiceRegistry services() { return serviceRegistry; }
}
