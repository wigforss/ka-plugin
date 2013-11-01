package org.kasource.kaplugin.spring.repository;

import org.kasource.kaplugin.repository.SimplePluginRepository;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "Ka-Plugin:name=PluginRepository", description = "Plugin Repository")
public class SpringSimplePluginRepository extends SimplePluginRepository {
    
    
    
  
    @ManagedAttribute(description = "Does the repository allow extension points to be added indirectly by the @ExtensionPoint annotation")
    public boolean isAutoRegisterAnnotatedExtensionPoints() {
        return autoRegisterAnnotatedExtensionPoints;
    }

   
    @ManagedAttribute(description = "Does the repository allow plugins without the @Plugin annotation to be registered")
    public boolean isAllowPluginsWithoutAnnotation() {
        return allowPluginsWithoutAnnotation;
    }
    
    @ManagedOperation(description = "Returns all registered extension point interfaces")
    public String showRegisteredExtensionPoints() {
       return getExtensionPoints().toString(); 
    }
    
    @ManagedOperation(description = "Returns all plugins registered on the class name provided (extension point interface)")
    @ManagedOperationParameters({
    @ManagedOperationParameter(name = "className", description = "Fully qualified className of the extension point interface")})
    public String getPluginsForClass(String className) throws ClassNotFoundException  {
       Class<?> clazz = Class.forName(className);
       return super.getPluginsFor(clazz).toString();
    }

   

}
