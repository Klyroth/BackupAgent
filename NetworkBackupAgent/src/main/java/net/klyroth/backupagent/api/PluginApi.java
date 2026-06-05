package net.klyroth.backupagent.api;

import net.klyroth.backupagent.plugin.PluginContainer;

import java.util.List;

public interface PluginApi {
    List<PluginContainer> getPlugins();
    boolean isPluginEnabled(String name);
}
