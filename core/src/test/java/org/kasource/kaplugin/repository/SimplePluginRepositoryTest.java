package org.kasource.kaplugin.repository;

import static org.junit.Assert.assertEquals;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaplugin.repository.PluginSorter;
import org.kasource.kaplugin.repository.SimplePluginRepository;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;



@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SimplePluginRepositoryTest {
	@Mock
	@InjectIntoByType
	private PluginSorter pluginSorter;
	
	@TestedObject
	private SimplePluginRepository repo;
	
	
	@Test
	public void registerPluginTest() {
		Capture<Set<Object>> pluginCapture = new Capture<Set<Object>>();
		EventListener impl = new EventListener() {};
		Set<Object> returnSet = new HashSet<Object>();
		returnSet.add(impl);
		EasyMock.expect(pluginSorter.sort(EasyMock.capture(pluginCapture))).andReturn(returnSet);
		
		EasyMockUnitils.replay();	
		repo.registerPlugin(EventListener.class, impl);
		repo.getPluginsFor(EventListener.class);
		Set<Object> pluginSet = pluginCapture.getValue();
		assertEquals(1, pluginSet.size());
		assertEquals(true, pluginSet.contains(impl));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void registerNonInterfacePluginTest() {
		repo.registerPlugin(String.class, "Test");
	}
}
