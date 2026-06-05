package net.klyroth.backupagent.plugin;

import net.klyroth.backupagent.api.PluginApi;

import java.util.List;

public final class DefaultPluginApi implements PluginApi {
    private PluginManager pluginManager;

    public void setPluginManager(PluginManager pluginManager) { this.pluginManager = pluginManager; }

    @Override
    public List<PluginContainer> getPlugins() {
        if (pluginManager == null) return List.of();
        return pluginManager.getPlugins();
    }

    @Override
    public boolean isPluginEnabled(String name) {
        return getPlugins().stream().anyMatch(p -> p.getDescription().name().equalsIgnoreCase(name) && p.getState() == PluginState.ENABLED);
    }
}
