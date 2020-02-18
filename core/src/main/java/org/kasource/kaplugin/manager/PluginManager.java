package org.kasource.kaplugin.manager;

import org.kasource.kaplugin.repository.PluginRepository;

/**
 * Plugin Manager.
 */
public interface PluginManager {

     /**
      * Returns an extension point implementation (proxy) for the the extensionPointInterface supplied.
      *
      * Use this method to get a proxy, which in turn invokes any registered plugin.
      *
      * @param extensionPointInterface The interface to get a an interface proxy for.
      * @param <T>                     The type of the interface
      *
      * @return an ExtensionPointProxy for the supplied extensionPointInterface.
      **/
     <T> T getExtensionPointFor(Class<T> extensionPointInterface);

     /**
      * Returns the Plugin Repository used.
      *
      * @return The PluginRepository used.
      **/
     PluginRepository getPluginRepository();
}
