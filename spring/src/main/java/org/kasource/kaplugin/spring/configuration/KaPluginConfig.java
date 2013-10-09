package org.kasource.kaplugin.spring.configuration;


import org.kasource.kaplugin.PluginManager;
import org.kasource.kaplugin.manager.PluginManagerImpl;
import org.kasource.kaplugin.repository.PluginRepository;
import org.kasource.kaplugin.spring.registration.PluginProcessor;
import org.kasource.kaplugin.spring.repository.SpringSimplePluginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KaPluginConfig {

    private static final String BEAN_ID_REPO = "ka-plugin.repo";
    private static final String BEAN_ID_MANAGER = "ka-plugin.manager";
    private static final String BEAN_ID_PROCESSOR = "ka-plugin.bean.processor";
    
    @Bean(name = BEAN_ID_REPO)
    public PluginRepository getPluginRepository() {
        return new SpringSimplePluginRepository();
    }
    
    @Autowired
    @Bean(name = BEAN_ID_MANAGER)
    public PluginManager getPluginManager(PluginRepository pluginRepository) {
        return new PluginManagerImpl(pluginRepository);
    }
    
    @Autowired
    @Bean(name = BEAN_ID_PROCESSOR)
    public static PluginProcessor getPostBeanProcessor(PluginManager pluginManager) {
        PluginProcessor pluginProcessor = new PluginProcessor();
        pluginProcessor.setPluginManager(pluginManager);
        return pluginProcessor;
    }
    
}
