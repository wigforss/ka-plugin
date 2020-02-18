package org.kasource.kaplugin.repository;

import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.collection.builder.SetBuilder;
import org.kasource.kaplugin.PluginRegistration;
import org.kasource.kaplugin.annotation.ExtensionPoint;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.unitils.inject.util.InjectionUtils;


@RunWith(MockitoJUnitRunner.class)
public class SimplePluginRepositoryTest {

	@Mock
	private Map<Class<?>, Set<PluginRegistration>> plugins = new HashMap<>();

	@Mock
	private PluginRegistration registration;

	@Mock
	private Set<PluginRegistration> pluginSet;

	@Mock
	private PluginRepositoryListener listener;

	private Set<PluginRepositoryListener> listeners = new HashSet<>();


	@Captor
	private ArgumentCaptor<Set<PluginRegistration>> pluginSetCaptor;


	private SimplePluginRepository repo = new SimplePluginRepository();

	@Before
	public void setup() {
		InjectionUtils.injectInto(plugins, repo, "plugins");
		listeners.add(listener);
		InjectionUtils.injectInto(listeners, repo, "listeners");
	}

	@Test
	public void registerPluginAndInvokeListeners() {
		SomeInterface impl = new SomeInterface() {};

		when(registration.getPlugin()).thenReturn(impl);
		when(plugins.get(SomeInterface.class)).thenReturn(pluginSet);
		when(pluginSet.add(registration)).thenReturn(true);

		repo.registerPlugin(registration);

		verify(pluginSet).add(registration);
		verify(listener).pluginRepositoryChanged(repo, SomeInterface.class);

	}

	@Test
	public void registerPluginDontInvokeListeners() {
		SomeInterface impl = new SomeInterface() {};

		when(registration.getPlugin()).thenReturn(impl);
		when(plugins.get(SomeInterface.class)).thenReturn(pluginSet);
		when(pluginSet.add(registration)).thenReturn(false);

		repo.registerPlugin(registration);

		verify(pluginSet).add(registration);
		verify(listener, times(0)).pluginRepositoryChanged(repo, SomeInterface.class);

	}

	@Test
	public void registerPluginAndExtensionPointSet() {
		SomeInterface impl = new SomeInterface() {};

		when(registration.getPlugin()).thenReturn(impl);
		when(plugins.get(SomeInterface.class)).thenReturn(null);
		when(plugins.put(eq(SomeInterface.class), pluginSetCaptor.capture())).thenReturn(null);

		repo.registerPlugin(registration);

		assertThat(pluginSetCaptor.getValue(), contains(registration));
	}

	@Test
	public void registerPluginNotAnnotated() {
		EventListener impl = new EventListener() {};

		when(registration.getPlugin()).thenReturn(impl);

		repo.registerPlugin(registration);

		verifyZeroInteractions(plugins);
	}

	@Test
	public void getPluginsFor() {
		when(plugins.get(SomeInterface.class)).thenReturn(new SetBuilder<PluginRegistration>().add(registration).build());

		Set<PluginRegistration> pluginsFound = repo.getPluginsFor(SomeInterface.class);

		assertThat(pluginsFound, contains(registration));
	}

	@Test
	public void removeExtensionPointAndInvokeListeners() {
		SomeInterface impl = new SomeInterface() {};

		when(plugins.remove(SomeInterface.class)).thenReturn(pluginSet);

		repo.removeExtensionPoint(SomeInterface.class);

		verify(listener).pluginRepositoryChanged(repo, SomeInterface.class);
	}

	@Test
	public void removeExtensionPointDontInvokeListeners() {
		SomeInterface impl = new SomeInterface() {};

		when(plugins.remove(SomeInterface.class)).thenReturn(null);

		repo.removeExtensionPoint(SomeInterface.class);

		verify(listener, times(0)).pluginRepositoryChanged(repo, SomeInterface.class);
	}

	@ExtensionPoint
	public interface SomeInterface {

	}
}
