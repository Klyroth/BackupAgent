# Klyroth BackupAgent

Backup-Agent für Minecraft-Netzwerke und Server-Infrastruktur.

## Start

```bash
mvn clean package
cp target/KlyrothBackupAgent.jar .
./start.sh
```

## Screen

```bash
screen -S backups
./start.sh
```

Screen verlassen: `CTRL + A`, danach `D`.

## Befehle

```text
help
backup
status
search <query>
sort newest|oldest|size
delete <name>|oldest|latest
config
plugins
reload
update
stop
```

## Plugin-System

Addon-JARs kommen in den Ordner `plugins/` und brauchen eine `backup-plugin.yml` im JAR-Root.

```yml
name: DiscordAddon
version: 1.0.0
main: de.example.discord.DiscordAddon
author: Developer
description: Discord Integration
```

Die Main-Klasse muss von `net.klyroth.backupagent.plugin.BackupPlugin` erben.
