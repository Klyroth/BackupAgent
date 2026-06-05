package de.example.discord;

import net.klyroth.backupagent.event.events.BackupFailedEvent;
import net.klyroth.backupagent.event.events.BackupSuccessEvent;
import net.klyroth.backupagent.plugin.BackupPlugin;

public final class DiscordAddonExample extends BackupPlugin {
    @Override
    public void onEnable() {
        getAgent().events().register(BackupSuccessEvent.class, event -> {
            System.out.println("[DiscordAddonExample] Backup erfolgreich: " + event.getResult().backupName());
        });

        getAgent().events().register(BackupFailedEvent.class, event -> {
            System.out.println("[DiscordAddonExample] Backup fehlgeschlagen: " + event.getThrowable().getMessage());
        });

        getAgent().commands().register("discordtest", (sender, context, args) -> {
            sender.sendMessage("DiscordAddonExample funktioniert.");
        });
    }

    @Override
    public void onDisable() {
        System.out.println("[DiscordAddonExample] deaktiviert.");
    }
}
