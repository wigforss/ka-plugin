package org.kasource.kaplugin.spring.repository;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import org.kasource.kaplugin.repository.SimplePluginRepository;

@ManagedResource(objectName = "Ka-Plugin:name=PluginRepository", description = "Plugin Repository")
public class SpringSimplePluginRepository extends SimplePluginRepository {

    @ManagedOperation(description = "Returns all registered extension point interfaces which has plugins registered")
    public String showRegisteredExtensionPoints() {
       return getPlugins().keySet().toString();
    }

    @ManagedOperation(description = "Returns all plugins registered on the class name provided (extension point interface)")
    @ManagedOperationParameters({
    @ManagedOperationParameter(name = "className", description = "Fully qualified className of the extension point interface")})
    public String getPluginsForClass(String className) throws ClassNotFoundException  {
       Class<?> clazz = Class.forName(className);
       return super.getPluginsFor(clazz).toString();
    }
}
