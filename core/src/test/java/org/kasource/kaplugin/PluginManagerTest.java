package org.kasource.kaplugin;

import java.sql.Statement;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.kasource.kaplugin.manager.PluginManagerImpl;
import org.kasource.kaplugin.repository.PluginRepository;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class PluginManagerTest {

	@SuppressWarnings("unused")
	@Mock
	@InjectIntoByType
	private PluginRepository pluginRepository;
	
	@TestedObject
	private PluginManagerImpl pluginManager = new PluginManagerImpl(pluginRepository);
	
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
