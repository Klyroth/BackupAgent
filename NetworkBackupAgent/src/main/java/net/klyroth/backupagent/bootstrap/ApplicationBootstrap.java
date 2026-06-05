package net.klyroth.backupagent.bootstrap;

import net.klyroth.backupagent.api.*;
import net.klyroth.backupagent.backup.*;
import net.klyroth.backupagent.command.*;
import net.klyroth.backupagent.command.commands.*;
import net.klyroth.backupagent.config.*;
import net.klyroth.backupagent.event.EventManager;
import net.klyroth.backupagent.event.events.AgentShutdownEvent;
import net.klyroth.backupagent.event.events.AgentStartEvent;
import net.klyroth.backupagent.plugin.*;
import net.klyroth.backupagent.registry.DefaultServiceRegistry;
import net.klyroth.backupagent.scheduler.BackupScheduler;
import net.klyroth.backupagent.storage.*;
import net.klyroth.backupagent.update.UpdateService;

import java.nio.file.Path;

public final class ApplicationBootstrap {
    private static final Path CONFIG_PATH = Path.of("config.yml");

    public void start() {
        printHeader();
        try {
            ConfigService configService = new ConfigService(CONFIG_PATH);
            if (!configService.exists()) {
                configService.writeDefaultConfig();
                System.out.println("[SETUP] config.yml wurde erstellt. Du findest du im Hauptordner.");
                return;
            }

            BackupAgentConfig config = configService.load();
            EventManager eventManager = new EventManager();
            CommandManager commandManager = new CommandManager();
            DefaultServiceRegistry serviceRegistry = new DefaultServiceRegistry();

            StorageService storageService = new StorageService();
            BackupFileScanner backupFileScanner = new BackupFileScanner();
            StorageGuard storageGuard = new StorageGuard(storageService, backupFileScanner, config);
            BackupService backupService = new BackupService(config, storageGuard, eventManager);
            BackupApi backupApi = new DefaultBackupApi(backupService, backupFileScanner, config);
            UpdateService updateService = new UpdateService(config);

            DefaultPluginApi pluginApi = new DefaultPluginApi();
            DefaultCommandApi commandApi = new DefaultCommandApi(commandManager);
            BackupAgent agent = new DefaultBackupAgent(backupApi, pluginApi, eventManager, commandApi, serviceRegistry);
            PluginManager pluginManager = new PluginManager(Path.of(config.plugins().folder()), agent);
            pluginApi.setPluginManager(pluginManager);

            BackupScheduler scheduler = new BackupScheduler(config, backupService);
            CommandContext context = new CommandContext(configService, backupService, backupFileScanner, storageService, updateService, scheduler, pluginManager);

            registerCoreCommands(commandManager);

            if (config.plugins().enabled()) {
                pluginManager.loadPlugins();
                pluginManager.enablePlugins();
            }

            eventManager.call(new AgentStartEvent());
            scheduler.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                eventManager.call(new AgentShutdownEvent());
                pluginManager.disablePlugins();
                scheduler.stop();
            }));

            new ConsoleCommandLoop(commandManager, context).start();
        } catch (Exception exception) {
            System.err.println("[FATAL] BackupAgent konnte nicht gestartet werden.");
            exception.printStackTrace();
        }
    }

    private void registerCoreCommands(CommandManager manager) {
        manager.register("help", new HelpCommand(manager));
        manager.register("backup", new BackupCommand());
        manager.register("status", new StatusCommand());
        manager.register("search", new SearchCommand());
        manager.register("sort", new SortCommand());
        manager.register("delete", new DeleteCommand());
        manager.register("config", new ConfigCommand());
        manager.register("plugins", new PluginsCommand());
        manager.register("reload", new ReloadCommand());
        manager.register("update", new UpdateCommand());
        manager.register("stop", new StopCommand());
    }

    private void printHeader() {
        System.out.println("==========================================");
        System.out.println(" Klyroth BackupAgent v1.0.0");
        System.out.println(" Minecraft Network Backup System");
        System.out.println("==========================================");
        System.out.println("Tippe 'help' für Befehle.");
        System.out.println();
    }
}
