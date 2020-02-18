package org.kasource.kaplugin.manager;

import java.sql.Statement;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaplugin.repository.PluginRepository;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PluginManagerImplTest {

	@Mock
	private PluginRepository pluginRepository;

	private PluginManagerImpl pluginManager = new PluginManagerImpl(pluginRepository);

	@Before
	public void setup() {
		pluginManager = new PluginManagerImpl(pluginRepository);
	}
	
	@Test
	public void getExtensionPointTest() {
		Statement extPoint = pluginManager.getExtensionPointFor(Statement.class);
		assertTrue(Statement.class.isAssignableFrom(extPoint.getClass()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getExtensionPointNotAnInterfaceTest() {
		String extPoint = pluginManager.getExtensionPointFor(String.class);
		assertTrue(Statement.class.isAssignableFrom(extPoint.getClass()));
	}
}
