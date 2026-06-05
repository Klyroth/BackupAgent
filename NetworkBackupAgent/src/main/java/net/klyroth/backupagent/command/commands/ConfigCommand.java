package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.command.*;

import java.nio.file.Files;
import java.util.List;

public final class ConfigCommand implements Command {
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        try {
            List<String> lines = Files.readAllLines(context.configService().getConfigPath());
            sender.sendMessage("===== config.yml =====");
            for (String line : lines) {
                if (line.toLowerCase().contains("password") || line.toLowerCase().contains("token")) {
                    sender.sendMessage(line.replaceAll(":.*", ": ********"));
                } else sender.sendMessage(line);
            }
            sender.sendMessage("======================");
        } catch (Exception e) { sender.sendMessage("[ERROR] Config konnte nicht gelesen werden: " + e.getMessage()); }
    }
}
