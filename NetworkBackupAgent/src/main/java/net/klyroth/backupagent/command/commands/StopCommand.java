package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.command.*;

public final class StopCommand implements Command {
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        sender.sendMessage("[STOP] BackupAgent wird beendet...");
        context.scheduler().stop();
        context.pluginManager().disablePlugins();
        System.exit(0);
    }
}
