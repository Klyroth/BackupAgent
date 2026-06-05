package net.klyroth.backupagent.event.events;

import net.klyroth.backupagent.backup.BackupRequest;
import net.klyroth.backupagent.event.Event;

public final class BackupFailedEvent implements Event {
    private final BackupRequest request;
    private final Throwable throwable;
    public BackupFailedEvent(BackupRequest request, Throwable throwable) {
        this.request = request;
        this.throwable = throwable;
    }
    public BackupRequest getRequest() { return request; }
    public Throwable getThrowable() { return throwable; }
}
