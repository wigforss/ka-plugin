package org.kasource.kaplugin.repository;

import java.util.List;


public interface PluginRepository {
	public <T> List<T> getPluginsFor(Class<T> extensionPointInterface);
	
	public <T> void registerPlugin(Class<T> extensionPointInterface, T pluginImpl);
	
	public void registerPlugin(Object object);
	
	public void addExtensionPoint(Class<?> extensionPoint);
	
	public void removeExtensionPoint(Class<?> extensionPoint);
	
	public boolean isExtensionPoint(Class<?> interfaceClass);
}
