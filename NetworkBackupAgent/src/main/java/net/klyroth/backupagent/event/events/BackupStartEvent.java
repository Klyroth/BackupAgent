package net.klyroth.backupagent.event.events;

import net.klyroth.backupagent.backup.BackupRequest;
import net.klyroth.backupagent.event.Cancellable;
import net.klyroth.backupagent.event.Event;

public final class BackupStartEvent implements Event, Cancellable {
    private final BackupRequest request;
    private boolean cancelled;

    public BackupStartEvent(BackupRequest request) { this.request = request; }
    public BackupRequest getRequest() { return request; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
