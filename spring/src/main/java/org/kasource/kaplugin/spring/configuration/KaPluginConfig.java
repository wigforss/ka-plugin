package org.kasource.kaplugin.spring.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.kasource.kaplugin.manager.PluginManager;
import org.kasource.kaplugin.manager.PluginManagerImpl;
import org.kasource.kaplugin.repository.PluginRepository;
import org.kasource.kaplugin.spring.registration.PluginProcessor;
import org.kasource.kaplugin.spring.repository.SpringSimplePluginRepository;

@Configuration
public class KaPluginConfig {

    private static final String BEAN_ID_REPO = "ka-plugin.repo";
    private static final String BEAN_ID_MANAGER = "ka-plugin.manager";
    private static final String BEAN_ID_PROCESSOR = "ka-plugin.bean.processor";

    @Bean(name = BEAN_ID_REPO)
    public PluginRepository pluginRepository() {
        return new SpringSimplePluginRepository();
    }

    @Autowired
    @Bean(name = BEAN_ID_MANAGER)
    public PluginManager pluginManager(PluginRepository pluginRepository) {
        return new PluginManagerImpl(pluginRepository);
    }

    @Autowired
    @Bean(name = BEAN_ID_PROCESSOR)
    public static PluginProcessor postBeanProcessor(PluginManager pluginManager) {
        return new PluginProcessor(pluginManager);
    }

}
