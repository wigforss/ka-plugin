package org.kasource.kaplugin.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kasource.kaplugin.ExtensionPoint;
import org.kasource.kaplugin.Plugin;

public class SimplePluginRepository implements PluginRepository {

    private PluginSorter pluginSorter;
    private Map<Class<?>, Set<Object>> plugins = new HashMap<Class<?>, Set<Object>>();

    private Set<Class<?>> extensionPoints = new HashSet<Class<?>>();
    protected boolean autoRegisterAnnotatedExtensionPoints = true;
    protected boolean allowPluginsWithoutAnnotation;
    
    @Override
    public <T> void registerPlugin(Class<T> extensionPointInterface, T pluginImpl) {
        validatePlugin(pluginImpl);
        if (!extensionPointInterface.isInterface()) {
            throw new IllegalArgumentException(extensionPointInterface.getName() + " must be an interface!");
        }
        extensionPoints.add(extensionPointInterface);
        addPlugin(extensionPointInterface, pluginImpl);
    }

    @Override
    public void registerPlugin(Object object) {
       
        Set<Class<?>> interfaces = getInterfaces(object);
        
        for (Class<?> interfaceClass : interfaces) {
            if (isExtensionPoint(interfaceClass)) {
                validatePlugin(object);
                addPlugin(interfaceClass, object);
            }
        }
    }
    
    private Set<Class<?>> getInterfaces(Object object) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        interfaces.addAll(Arrays.asList(object.getClass().getInterfaces()));
        Class<?> clazz = object.getClass();
        while(!clazz.getSuperclass().equals(Object.class)) {
            clazz = clazz.getSuperclass();
            interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
        }
        return interfaces;
    }

    private void validatePlugin(Object object) {
        if(!allowPluginsWithoutAnnotation && !object.getClass().isAnnotationPresent(Plugin.class)) {
            throw new IllegalArgumentException(object.getClass()+ " is not annotated with @" + Plugin.class);
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
        if(pluginSorter != null) {
            pluginList = pluginSorter.sort(pluginList);
        }
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
        if(autoRegisterAnnotatedExtensionPoints && interfaceClass.isAnnotationPresent(ExtensionPoint.class)) {
            extensionPoints.add(interfaceClass);
        }
        return extensionPoints.contains(interfaceClass);
    }

   
    @Override
    public void removeExtensionPoint(Class<?> extensionPoint) {
       if(extensionPoints.remove(extensionPoint)) {
           plugins.remove(extensionPoint);
       }
    }

    /**
     * @param extensionPoints the extensionPoints to set
     */
    public void setExtensionPoints(Set<Class<?>> extensionPoints) {
        this.extensionPoints = extensionPoints;
    }

    /**
     * @param autoRegisterAnnotatedExtensionPoints the autoRegisterAnnotatedExtensionPoints to set
     */
    public void setAutoRegisterAnnotatedExtensionPoints(boolean autoRegisterAnnotatedExtensionPoints) {
        this.autoRegisterAnnotatedExtensionPoints = autoRegisterAnnotatedExtensionPoints;
    }

    /**
     * @param pluginSorter the pluginSorter to set
     */
    public void setPluginSorter(PluginSorter pluginSorter) {
        this.pluginSorter = pluginSorter;
    }

    /**
     * @param allowPluginsWithoutAnnotation the allowPluginsWithoutAnnotation to set
     */
    public void setAllowPluginsWithoutAnnotation(boolean allowPluginsWithoutAnnotation) {
        this.allowPluginsWithoutAnnotation = allowPluginsWithoutAnnotation;
    }

    /**
     * @return the extensionPoints
     */
    public Set<Class<?>> getExtensionPoints() {
        return extensionPoints;
    }

   

   
}
