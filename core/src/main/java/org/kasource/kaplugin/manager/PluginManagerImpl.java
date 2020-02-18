package org.kasource.kaplugin.manager;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.kasource.kaplugin.proxy.ExtensionPointProxy;
import org.kasource.kaplugin.repository.PluginRepository;
import org.kasource.kaplugin.repository.SimplePluginRepository;


public class PluginManagerImpl implements PluginManager {
    private PluginRepository pluginRepository;

    private Map<Class<?>, Object> proxies = new HashMap<>();

    public PluginManagerImpl() {
        this.pluginRepository = new SimplePluginRepository();
    }

    public PluginManagerImpl(final PluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T getExtensionPointFor(Class<T> extensionPointInterface) {
        if (!extensionPointInterface.isInterface()) {
            throw new IllegalArgumentException(extensionPointInterface.getName() + " must be an interface!");
        }
        T proxy = (T) proxies.get(extensionPointInterface);
        if (proxy == null) {
            proxy = (T) Proxy.newProxyInstance(extensionPointInterface.getClassLoader(),
                    new Class[]{extensionPointInterface},
                    new ExtensionPointProxy(extensionPointInterface, pluginRepository));
            proxies.put(extensionPointInterface, proxy);

        }
        return proxy;
    }


    @Override
    public PluginRepository getPluginRepository() {

        return pluginRepository;
    }
}
