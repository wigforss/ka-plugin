package org.kasource.kaplugin.spring.configuration;

import org.kasource.kaplugin.manager.PluginManager;
import org.kasource.kaplugin.repository.PluginRepository;
import org.kasource.kaplugin.spring.registration.PluginProcessor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KaPluginConfigTest {

    @Mock
    private PluginRepository pluginRepository;

    @Mock
    private PluginManager pluginManager;

    private KaPluginConfig config = new KaPluginConfig();

    @Test
    public void pluginRepository() {
        assertThat(config.pluginRepository(), is(instanceOf(PluginRepository.class)));
    }

    @Test
    public void pluginManager() {
        assertThat(config.pluginManager(pluginRepository), is(instanceOf(PluginManager.class)));
    }

    @Test
    public void postBeanProcessor() {
        assertThat(config.postBeanProcessor(pluginManager), is(instanceOf(PluginProcessor.class)));
    }
}
