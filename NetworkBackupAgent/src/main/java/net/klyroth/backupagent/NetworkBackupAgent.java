package net.klyroth.backupagent;

import net.klyroth.backupagent.bootstrap.ApplicationBootstrap;

public final class NetworkBackupAgent {
    private NetworkBackupAgent() {}

    public static void main(String[] args) {
        new ApplicationBootstrap().start();
    }
}
