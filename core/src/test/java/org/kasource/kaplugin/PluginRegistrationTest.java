package org.kasource.kaplugin;

import java.util.Arrays;

import org.kasource.kaplugin.annotation.Plugin;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PluginRegistrationTest {

    @Test
    public void buildWithAnnotations() {
        RunnablePlugin runnablePlugin = new RunnablePlugin();
        PluginRegistration registration = new PluginRegistration.Builder(runnablePlugin).build();

        assertThat(registration.getPlugin(), is(equalTo(runnablePlugin)));
        assertThat(registration.getName(), is(equalTo("runnablePlugin")));
        assertThat(registration.getVendor(), is(equalTo("example.org")));
        assertThat(registration.getVersion(), is("1.0"));
        assertThat(registration.getPriority(), is(1));
    }

    @Test(expected = IllegalStateException.class)
    public void buildWithoutAnnotationsMissingName() {
        AnotherPlugin anotherPlugin = new AnotherPlugin();
        new PluginRegistration.Builder(anotherPlugin).build();
    }

    @Test(expected = IllegalStateException.class)
    public void buildWithoutAnnotationsMissingVersion() {
        String pluginName = "pluginName";
        AnotherPlugin anotherPlugin = new AnotherPlugin();
        new PluginRegistration.Builder(anotherPlugin).name(pluginName).build();
    }

    @Test(expected = IllegalStateException.class)
    public void buildWithoutAnnotationsMissingVendor() {
        String pluginName = "pluginName";
        String version = "version";

        AnotherPlugin anotherPlugin = new AnotherPlugin();
        new PluginRegistration.Builder(anotherPlugin)
                .name(pluginName)
                .version(version)
                .build();
    }
    @Test
    public void buildWithoutAnnotations() {
        String pluginName = "pluginName";
        String version = "version";
        String vendor = "vendor";

        AnotherPlugin anotherPlugin = new AnotherPlugin();
        PluginRegistration registration =  new PluginRegistration.Builder(anotherPlugin)
                .name(pluginName)
                .version(version)
                .vendor(vendor)
                .build();

        assertThat(registration.getPlugin(), is(equalTo(anotherPlugin)));
        assertThat(registration.getName(), is(equalTo(pluginName)));
        assertThat(registration.getVendor(), is(equalTo(vendor)));
        assertThat(registration.getVersion(), is(version));
        assertThat(registration.getPriority(), is(0));
    }

    @Test
    public void buildWithoutAnnotationsAndPrio() {
        String pluginName = "pluginName";
        String version = "version";
        String vendor = "vendor";
        final int prio = 1;

        AnotherPlugin anotherPlugin = new AnotherPlugin();
        PluginRegistration registration =  new PluginRegistration.Builder(anotherPlugin)
                .name(pluginName)
                .version(version)
                .vendor(vendor)
                .priority(prio)
                .build();

        assertThat(registration.getPlugin(), is(equalTo(anotherPlugin)));
        assertThat(registration.getName(), is(equalTo(pluginName)));
        assertThat(registration.getVendor(), is(equalTo(vendor)));
        assertThat(registration.getVersion(), is(version));
        assertThat(registration.getPriority(), is(prio));

        assertThat(registration.toString(), stringContainsInOrder(Arrays.asList(
                "plugin=", anotherPlugin.toString(),
                "name=", pluginName,
                "version=", version,
                "vendor=", vendor,
                "priority=", Integer.toString(prio)
        )));
    }

    @Test
    public void equals() {
        RunnablePlugin runnablePlugin = new RunnablePlugin();
        PluginRegistration registration = new PluginRegistration.Builder(runnablePlugin).build();

        String pluginName = "pluginName";
        String version = "version";
        String vendor = "vendor";
        AnotherPlugin anotherPlugin = new AnotherPlugin();


        PluginRegistration anotherRegistration =  new PluginRegistration.Builder(anotherPlugin)
                .name(pluginName)
                .version(version)
                .vendor(vendor)
                .build();

        assertThat(registration.equals(registration), is(true));
        assertThat(registration.equals(null), is(false));
        assertThat(registration.equals(Integer.valueOf(1)), is(false));
        assertThat(registration.equals(anotherRegistration), is(false));

        assertThat(anotherRegistration.equals(new PluginRegistration.Builder(runnablePlugin)
                .name(pluginName)
                .version(version)
                .vendor(vendor)
                .build()), is(false));

        assertThat(anotherRegistration.equals(new PluginRegistration.Builder(anotherPlugin)
                .name("anotherName")
                .version(version)
                .vendor(vendor)
                .build()), is(false));

        assertThat(anotherRegistration.equals(new PluginRegistration.Builder(anotherPlugin)
                .name(pluginName)
                .version("anotherVersion")
                .vendor(vendor)
                .build()), is(false));

        assertThat(anotherRegistration.equals(new PluginRegistration.Builder(anotherPlugin)
                .name(pluginName)
                .version(version)
                .vendor("anotherVendor")
                .build()), is(false));

        assertThat(anotherRegistration.equals(new PluginRegistration.Builder(anotherPlugin)
                .name(pluginName)
                .version(version)
                .vendor(vendor)
                .build()), is(true));
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

    private static class AnotherPlugin implements Runnable {
        @Override
        public void run() {
        }
    }
}
