package org.kasource.kaplugin.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.kasource.kaplugin.PluginRegistration;
import org.kasource.kaplugin.repository.PluginRepository;
import org.kasource.kaplugin.repository.PluginRepositoryListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExtensionPointProxy implements InvocationHandler, PluginRepositoryListener {
    private static final Logger LOG = LoggerFactory.getLogger(ExtensionPointProxy.class);
    private List<PluginRegistration> plugins = new CopyOnWriteArrayList();
    private Class<?> extensionPointInterface;
    public ExtensionPointProxy(final Class<?> extensionPointInterface,
                               final PluginRepository pluginRepository) {
        this.extensionPointInterface = extensionPointInterface;
        loadPlugins(pluginRepository);
        pluginRepository.addListener(this);
    }

    /**
    * Invokes methods on all plugins.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method The method to invoke
     * @param args   The supplied arguments
     *
     * @return last return value
     **/
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Object result = null;
        for (PluginRegistration plugin : plugins) {
            try {
                result = method.invoke(plugin.getPlugin(), args);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                LOG.warn("Error executing plugin  " + plugin + " invoking " + method + " with args " + Arrays.asList(args));
            }
        }
        return result;
    }

    private void loadPlugins(PluginRepository pluginRepository) {
        // Sort highest priority first
        plugins = new CopyOnWriteArrayList<>(pluginRepository.getPluginsFor(extensionPointInterface)
                .stream()
                .sorted(Comparator.comparingInt(PluginRegistration::getPriority).reversed())
                .collect(Collectors.toList()));
    }

    @Override
    public void pluginRepositoryChanged(PluginRepository pluginRepository, Class<?> extensionPoint) {
        if (this.extensionPointInterface.equals(extensionPoint)) {
            loadPlugins(pluginRepository);
        }
    }
}
