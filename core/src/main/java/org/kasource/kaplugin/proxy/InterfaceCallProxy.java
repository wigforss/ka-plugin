package org.kasource.kaplugin.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import org.kasource.kaplugin.repository.PluginRepository;



public class InterfaceCallProxy implements InvocationHandler{
	private PluginRepository pluginRepository;
	
	public InterfaceCallProxy(PluginRepository pluginRepository){
		this.pluginRepository = pluginRepository;
	}
	
	/**
	 * TODO
	 * 
	 * Throw exception if no plugin found? To able to tell if null was
	 * returned by a method invocation or no plugins wasn't found
	 * 
	 * Handle multiple results first, last or throw exception
	 **/
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		List<?> plugins = pluginRepository.getPluginsFor(method.getDeclaringClass());
		Object result = null;
		for(Object plugin : plugins) {
			result = method.invoke(plugin, args);
		}
		return result;
	}

}
