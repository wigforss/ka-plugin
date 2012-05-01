package org.kasource.kaplugin.proxy;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.kasource.kaplugin.repository.PluginRepository;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class InterfaceCallProxyTest {

	
	@Mock
	@InjectIntoByType
	private PluginRepository pluginRepository;
	
	@TestedObject
	private InterfaceCallProxy proxy = new InterfaceCallProxy(null);
	
	
	@Test
	public void invokeTest() throws SecurityException, NoSuchMethodException, Throwable {
		List<Runnable> plugins = new ArrayList<Runnable>();
		RunnablePlugin plugin = new RunnablePlugin();
		plugins.add(plugin);
		EasyMock.expect(pluginRepository.getPluginsFor(Runnable.class)).andReturn(plugins);
		EasyMockUnitils.replay();
		proxy.invoke(null, Runnable.class.getDeclaredMethod("run"), null);
		assertTrue(plugin.hasRun);
	}
	
	
	

	
	class RunnablePlugin implements Runnable {
		boolean hasRun = false;
		public void run(){
			hasRun = true;
		}
	}
}
