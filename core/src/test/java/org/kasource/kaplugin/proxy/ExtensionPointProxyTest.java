package org.kasource.kaplugin.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.collection.builder.ListBuilder;
import org.kasource.commons.collection.builder.SetBuilder;
import org.kasource.kaplugin.PluginRegistration;
import org.kasource.kaplugin.repository.PluginRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.unitils.inject.util.InjectionUtils;

@RunWith(MockitoJUnitRunner.class)
public class ExtensionPointProxyTest {

	
	@Mock
	private PluginRepository pluginRepository;

	@Mock
	private PluginRegistration registration;

	@Mock
	private Runnable runnable;


	private ExtensionPointProxy proxy;

	@Before
	public void setup() {
		Set<PluginRegistration> plugins = new SetBuilder<PluginRegistration>().add(registration).build();

		when(pluginRepository.getPluginsFor(Runnable.class)).thenReturn(plugins);

		proxy = new ExtensionPointProxy(Runnable.class, pluginRepository);
	}
	
	@Test
	public void invokeTest() throws Throwable {
		InjectionUtils.injectInto(
				new ListBuilder<PluginRegistration>(new CopyOnWriteArrayList<>()).add(registration).build()
				,proxy,
			    "plugins");

		when(registration.getPlugin()).thenReturn(runnable);

		proxy.invoke(null, Runnable.class.getDeclaredMethod("run"), null);

        verify(runnable).run();
	}


	@Test
	public void repoChanged() {
		Set<PluginRegistration> plugins = new SetBuilder<PluginRegistration>().add(registration).build();

		when(pluginRepository.getPluginsFor(Runnable.class)).thenReturn(plugins);
		when(registration.getPriority()).thenReturn(1);

		proxy.pluginRepositoryChanged(pluginRepository, Runnable.class);
	}

	@Test
	public void repoChangedNonValidExtension() {
		proxy.pluginRepositoryChanged(pluginRepository, EventListener.class);

		verify(pluginRepository, times(0)).getPluginsFor(EventListener.class);
	}

	static class RunnablePlugin implements Runnable {
		boolean hasRun = false;
		public void run(){
			hasRun = true;
		}
	}
}
