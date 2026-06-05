package net.klyroth.backupagent.registry;

import net.klyroth.backupagent.api.ServiceRegistry;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultServiceRegistry implements ServiceRegistry {
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    @Override
    public <T> void register(Class<T> serviceClass, T implementation) {
        services.put(serviceClass, implementation);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Class<T> serviceClass) {
        return Optional.ofNullable((T) services.get(serviceClass));
    }

    @Override
    public <T> void unregister(Class<T> serviceClass) {
        services.remove(serviceClass);
    }
}
