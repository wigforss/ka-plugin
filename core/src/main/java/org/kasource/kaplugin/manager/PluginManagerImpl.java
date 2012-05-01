package org.kasource.kaplugin.manager;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.kasource.kaplugin.PluginManager;
import org.kasource.kaplugin.proxy.InterfaceCallProxy;
import org.kasource.kaplugin.repository.PluginRepository;



public class PluginManagerImpl implements PluginManager {
	private PluginRepository pluginRepository;
	
	private Map<Class<?>, Object> proxies = new HashMap<Class<?>, Object>();
	
	public PluginManagerImpl(PluginRepository pluginRepository){
	    this.pluginRepository = pluginRepository;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getExtensionPointFor(Class<T> extensionPointInterface) {
		if(!extensionPointInterface.isInterface()) {
			throw new IllegalArgumentException(extensionPointInterface.getName()+" must be an interface!");
		}
		T proxy = (T) proxies.get(extensionPointInterface);
		if(proxy == null) {
			proxy = (T) Proxy.newProxyInstance(extensionPointInterface.getClassLoader(),
	                 new Class[] { extensionPointInterface },
	                 new InterfaceCallProxy(pluginRepository));
			proxies.put(extensionPointInterface, proxy);

		}
		return proxy;
	}

  
    @Override
    public PluginRepository getPluginRepository() {
        
        return pluginRepository;
    }
}
