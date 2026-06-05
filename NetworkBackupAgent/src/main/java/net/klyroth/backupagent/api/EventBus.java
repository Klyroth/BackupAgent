package net.klyroth.backupagent.api;

import net.klyroth.backupagent.event.Event;
import net.klyroth.backupagent.event.EventListener;

public interface EventBus {
    <T extends Event> void register(Class<T> eventClass, EventListener<T> listener);
    void call(Event event);
}
