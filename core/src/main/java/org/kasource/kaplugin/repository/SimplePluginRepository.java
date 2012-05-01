package org.kasource.kaplugin.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimplePluginRepository implements PluginRepository {

    private PluginSorter pluginSorter;
    private Map<Class<?>, Set<Object>> plugins = new HashMap<Class<?>, Set<Object>>();

    private Set<Class<?>> extensionPoints = new HashSet<Class<?>>();

    @Override
    public <T> void registerPlugin(Class<T> extensionPointInterface, T pluginImpl) {
        if (!extensionPointInterface.isInterface()) {
            throw new IllegalArgumentException(extensionPointInterface.getName() + " must be an interface!");
        }
        extensionPoints.add(extensionPointInterface);
        addPlugin(extensionPointInterface, pluginImpl);
    }

    @Override
    public void registerPlugin(Object object) {
        Class<?>[] interfaces = object.getClass().getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            if (isExtensionPoint(interfaceClass)) {
                addPlugin(interfaceClass, object);
            }
        }
    }

    private void addPlugin(Class<?> extensionPointInterface, Object pluginImpl) {
        Set<Object> pluginSet = plugins.get(extensionPointInterface);
        if (pluginSet == null) {
            pluginSet = new HashSet<Object>();
            plugins.put(extensionPointInterface, pluginSet);
        }
        pluginSet.add(pluginImpl);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getPluginsFor(Class<T> extensionPointInterface) {
        if (!extensionPointInterface.isInterface()) {
            throw new IllegalArgumentException(extensionPointInterface.getName() + " must be an interface!");
        }
        Collection<Object> pluginList = plugins.get(extensionPointInterface);
        pluginList = pluginSorter.sort(pluginList);
        List<T> resultList = new ArrayList<T>();
        for (Object plugin : pluginList) {
            resultList.add((T) plugin);
        }
        return resultList;
    }

    @Override
    public void addExtensionPoint(Class<?> extensionPoint) {
        extensionPoints.add(extensionPoint);

    }

    @Override
    public boolean isExtensionPoint(Class<?> interfaceClass) {
        return extensionPoints.contains(interfaceClass);
    }

   
    @Override
    public void removeExtensionPoint(Class<?> extensionPoint) {
       if(extensionPoints.remove(extensionPoint)) {
           plugins.remove(extensionPoint);
       }
    }

}
