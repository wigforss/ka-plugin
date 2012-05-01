package org.kasource.kaplugin.osgi;

import org.kasource.kaplugin.PluginManager;
import org.kasource.kaplugin.manager.PluginManagerImpl;
import org.kasource.kaplugin.repository.PluginRepository;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

/**
 * @author rikardwigforss
 *
 */
public class OsgiPluginManager extends PluginManagerImpl implements PluginManager, ServiceFactory{

    public OsgiPluginManager(PluginRepository pluginRepository) {
        super(pluginRepository);
    }
   
    @Override
    public Object getService(Bundle bundle, ServiceRegistration serviceReg) {
        return this;
    }

   
    @Override
    public void ungetService(Bundle bundle, ServiceRegistration serviceReg, Object arg2) {       
    }

}
