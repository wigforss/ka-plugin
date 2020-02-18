package org.kasource.kaplugin.spring.registration;

import org.kasource.kaplugin.PluginRegistration;
import org.kasource.kaplugin.annotation.Plugin;
import org.kasource.kaplugin.manager.PluginManager;
import org.kasource.kaplugin.repository.PluginRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class PluginProcessorTest {

    @Mock
    private PluginManager pluginManager;

    @Mock
    private Object bean;

    @Mock
    private PluginRepository pluginRepository;

    private PluginProcessor processor;

    @Before
    public void setup() {
        processor = new PluginProcessor(pluginManager);
    }

    @Test
    public void postProcessBeforeInitialization() {
        assertThat(processor.postProcessBeforeInitialization(bean, "beanName"), is(equalTo(bean)));
    }

    @Test
    public void postProcessAfterInitialization() {
        RunnablePlugin plugin = new RunnablePlugin();

        when(pluginManager.getPluginRepository()).thenReturn(pluginRepository);

        assertThat(processor.postProcessAfterInitialization(plugin, "beanName"), is(equalTo(plugin)));

        verify(pluginRepository).registerPlugin(Mockito.isA(PluginRegistration.class));
    }

    @Test
    public void postProcessAfterInitializationNoAnnotated() {
        assertThat(processor.postProcessAfterInitialization(bean, "beanName"), is(equalTo(bean)));

        verify(pluginManager, times(0)).getPluginRepository();
    }

    @Plugin(name = "runnablePlugin",
            vendor = "example.org",
            version = "1.0",
            priority = 1)
    private static class RunnablePlugin implements Runnable {

        @Override
        public void run() {
        }
    }
}
