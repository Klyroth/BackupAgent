package net.klyroth.backupagent.plugin;

import net.klyroth.backupagent.api.BackupAgent;

public abstract class BackupPlugin {
    private BackupAgent agent;
    private PluginDescription description;

    public final void initialize(BackupAgent agent, PluginDescription description) {
        this.agent = agent;
        this.description = description;
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public BackupAgent getAgent() { return agent; }
    public PluginDescription getDescription() { return description; }
    public String getName() { return description.name(); }
}
