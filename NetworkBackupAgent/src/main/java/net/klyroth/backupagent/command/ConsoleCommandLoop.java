package net.klyroth.backupagent.command;

import java.util.Arrays;
import java.util.Scanner;

public final class ConsoleCommandLoop {
    private final CommandManager commandManager;
    private final CommandContext context;
    private final ConsoleCommandSender sender = new ConsoleCommandSender();

    public ConsoleCommandLoop(CommandManager commandManager, CommandContext context) {
        this.commandManager = commandManager;
        this.context = context;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("backup> ");
            String input;
            try { input = scanner.nextLine().trim(); }
            catch (Exception ignored) { return; }
            if (input.isBlank()) continue;
            String[] split = input.split("\\s+");
            String name = split[0].toLowerCase();
            String[] args = Arrays.copyOfRange(split, 1, split.length);
            Command command = commandManager.get(name);
            if (command == null) {
                sender.sendMessage("[WARN] Unbekannter Befehl. Nutze: help");
                continue;
            }
            try { command.execute(sender, context, args); }
            catch (Exception exception) {
                sender.sendMessage("[ERROR] Befehl fehlgeschlagen: " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }
}
