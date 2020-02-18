package org.kasource.kaplugin.spring.registration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import org.kasource.kaplugin.PluginRegistration;
import org.kasource.kaplugin.annotation.Plugin;
import org.kasource.kaplugin.manager.PluginManager;

/**
 * Registers any @Plugin annotated bean with the PluginManager.
 *
 * @author rikardwi
 **/
public class PluginProcessor implements BeanPostProcessor {

    private PluginManager pluginManager;

    public PluginProcessor(final PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Plugin.class)) {
            pluginManager.getPluginRepository().registerPlugin(new PluginRegistration.Builder(bean).build());
        }
        return bean;
    }
}
