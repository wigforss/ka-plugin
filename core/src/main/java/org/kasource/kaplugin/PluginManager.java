/**
 * 
 */
package org.kasource.kaplugin;

import org.kasource.kaplugin.repository.PluginRepository;

/**
 * @author rikardwigforss
 *
 */
public interface PluginManager {

  
    public abstract <T> T getExtensionPointFor(Class<T> extensionPointInterface);

    public PluginRepository getPluginRepository();
    
}