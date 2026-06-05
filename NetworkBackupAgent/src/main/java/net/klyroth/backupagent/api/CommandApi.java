package net.klyroth.backupagent.api;

import net.klyroth.backupagent.command.Command;

public interface CommandApi {
    void register(String name, Command command);
    void unregister(String name);
}
