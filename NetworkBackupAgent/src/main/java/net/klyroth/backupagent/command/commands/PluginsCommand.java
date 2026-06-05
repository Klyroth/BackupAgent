package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.command.*;
import net.klyroth.backupagent.plugin.PluginContainer;

public final class PluginsCommand implements Command {
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        if (context.pluginManager().getPlugins().isEmpty()) {
            sender.sendMessage("Keine Plugins geladen.");
            return;
        }
        sender.sendMessage("Geladene Plugins:");
        for (PluginContainer plugin : context.pluginManager().getPlugins()) {
            sender.sendMessage("- " + plugin.getDescription().name() + " v" + plugin.getDescription().version() + " by " + plugin.getDescription().author() + " [" + plugin.getState() + "]");
        }
    }
}
