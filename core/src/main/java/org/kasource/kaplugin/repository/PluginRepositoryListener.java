package org.kasource.kaplugin.repository;

/**
 * Plugin Repository Listener.
 **/
public interface PluginRepositoryListener {

    /**
     * Invoked when the plugins for a specific extentionPointInterface have changed.
     *
     * @param pluginRepository          The PluginRepository that has changed.
     * @param extensionPointInterface   The ExtensionPointInterface which plugins have been changed.
     **/
    void pluginRepositoryChanged(PluginRepository pluginRepository, Class<?> extensionPointInterface);
}
