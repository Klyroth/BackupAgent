package net.klyroth.backupagent.api;

import net.klyroth.backupagent.command.Command;
import net.klyroth.backupagent.command.CommandManager;

public final class DefaultCommandApi implements CommandApi {
    private final CommandManager commandManager;

    public DefaultCommandApi(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void register(String name, Command command) {
        commandManager.register(name, command);
    }

    @Override
    public void unregister(String name) {
        commandManager.unregister(name);
    }
}
