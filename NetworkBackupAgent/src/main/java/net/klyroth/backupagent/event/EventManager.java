package net.klyroth.backupagent.event;

import net.klyroth.backupagent.api.EventBus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager implements EventBus {
    private final Map<Class<?>, List<EventListener<?>>> listeners = new ConcurrentHashMap<>();

    @Override
    public <T extends Event> void register(Class<T> eventClass, EventListener<T> listener) {
        listeners.computeIfAbsent(eventClass, ignored -> new CopyOnWriteArrayList<>()).add(listener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void call(Event event) {
        List<EventListener<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners == null) return;
        for (EventListener<?> listener : eventListeners) {
            try {
                ((EventListener<Event>) listener).handle(event);
            } catch (Exception exception) {
                System.err.println("[EVENT] Listener-Fehler: " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }
}
