package net.klyroth.backupagent.event;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
