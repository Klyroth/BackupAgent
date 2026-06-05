package net.klyroth.backupagent.event;

@FunctionalInterface
public interface EventListener<T extends Event> {
    void handle(T event);
}
