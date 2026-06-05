package net.klyroth.backupagent.event.events;

import net.klyroth.backupagent.backup.BackupResult;
import net.klyroth.backupagent.event.Event;

public final class BackupSuccessEvent implements Event {
    private final BackupResult result;
    public BackupSuccessEvent(BackupResult result) { this.result = result; }
    public BackupResult getResult() { return result; }
}
