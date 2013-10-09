package org.kasource.kaplugin.spring.registration;

import org.kasource.kaplugin.Plugin;
import org.kasource.kaplugin.PluginManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Registers any @Plugin annotated bean with the PluginManager.
 * 
 * @author rikardwi
 **/
public class PluginProcessor implements BeanPostProcessor {

  
    private PluginManager pluginManager;
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {    
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().isAnnotationPresent(Plugin.class)) {
            pluginManager.getPluginRepository().registerPlugin(bean);
        }
        return bean;
    }

    /**
     * @param pluginManager the pluginManager to set
     */
    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

}
