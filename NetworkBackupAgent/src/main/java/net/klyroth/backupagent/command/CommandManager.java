package net.klyroth.backupagent.command;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CommandManager {
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void register(String name, Command command) { commands.put(name.toLowerCase(), command); }
    public void unregister(String name) { commands.remove(name.toLowerCase()); }
    public Command get(String name) { return commands.get(name.toLowerCase()); }
    public Map<String, Command> getCommands() { return Collections.unmodifiableMap(commands); }
}
