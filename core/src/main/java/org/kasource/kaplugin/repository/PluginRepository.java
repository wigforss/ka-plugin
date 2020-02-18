package org.kasource.kaplugin.repository;


import java.util.Set;

import org.kasource.kaplugin.PluginRegistration;

/**
 * Repository of registered plugins.
 **/
public interface PluginRepository extends PluginRegistrar {

    /**
     * Returns a all plugins for a specific extension point.
     *
     * @param extensionPointInterface The org.kasource.kaplugin.annotation.ExtensionPoint annotated interface lookup plugins for.
     *
     * @return Set of PluginRegistration for the specified extension point (may be empty).
     **/
    Set<PluginRegistration> getPluginsFor(Class<?> extensionPointInterface);

    /**
     * Remove all registered plugins for the supplied extensionPointInterface.
     *
     * @param extensionPointInterface The interface to remove registered plugins for.
     **/
    void removeExtensionPoint(Class<?> extensionPointInterface);

    /**
     * Adds listener which will be invoked when the repository is changed.
     *
     * @param listener The listener to invoke.
     **/
    void addListener(PluginRepositoryListener listener);
}
