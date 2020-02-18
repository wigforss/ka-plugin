package org.kasource.kaplugin.repository;

import org.kasource.kaplugin.PluginRegistration;

/**
 * Registers a plugin.
 **/
public interface PluginRegistrar {

    /**
     * Register a plugin.
     *
     * @param plugin The plugin registration.
     **/
    void registerPlugin(PluginRegistration plugin);
}
