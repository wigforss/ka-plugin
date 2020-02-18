package org.kasource.kaplugin.repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import org.kasource.commons.reflection.ClassIntrospectorImpl;
import org.kasource.commons.reflection.filter.builder.ClassFilterBuilder;
import org.kasource.kaplugin.PluginRegistration;
import org.kasource.kaplugin.annotation.ExtensionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of PluginRepository.
 */
public class SimplePluginRepository implements PluginRepository {
    private static final Logger LOG = LoggerFactory.getLogger(SimplePluginRepository.class);
    private Map<Class<?>, Set<PluginRegistration>> plugins = new ConcurrentHashMap<>();
    private Set<PluginRepositoryListener> listeners = new CopyOnWriteArraySet<>();

    @Override
    public void registerPlugin(PluginRegistration plugin) {

        Set<Class<?>> interfaces = new ClassIntrospectorImpl(plugin.getPlugin().getClass())
                .getDeclaredInterfaces(new ClassFilterBuilder().annotated(ExtensionPoint.class).build());

        // Add plugin for each interface
        interfaces.stream().forEach(i -> addPlugin(i, plugin));
    }

    @Override
    public void removeExtensionPoint(Class<?> extensionPointInterface) {

        if (plugins.remove(extensionPointInterface) != null) {
            listeners.stream().forEach(l -> l.pluginRepositoryChanged(this, extensionPointInterface));
        }
    }

    @Override
    public void addListener(PluginRepositoryListener listener) {
        listeners.add(listener);
    }


    private void addPlugin(Class<?> extensionPointInterface, PluginRegistration plugin) {
        Set<PluginRegistration> pluginSet = plugins.get(extensionPointInterface);
        if (pluginSet == null) {
            pluginSet = new HashSet<>();
            plugins.put(extensionPointInterface, pluginSet);
        }
        if (pluginSet.add(plugin)) {
            listeners.stream().forEach(l -> l.pluginRepositoryChanged(this, extensionPointInterface));
            LOG.info("Plugin " + plugin + " registered.");
        }
    }

    @Override
    public Set<PluginRegistration> getPluginsFor(Class<?> extensionPointInterface) {
        Set<PluginRegistration> pluginRegistrationSet = plugins.get(extensionPointInterface);
        if (pluginRegistrationSet != null) {
            return pluginRegistrationSet.stream()
                    .collect(Collectors.toSet());
        }
        return Collections.EMPTY_SET;

    }

    protected Map<Class<?>, Set<PluginRegistration>> getPlugins() {
        return plugins;
    }
}
