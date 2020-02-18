# Ka Plugin Core

A simple plugin framework.

## Application
When an application wants to allow 3rd party plugins to be used, the application can register which interfaces it will use for that purpose and annotated those with the @ExtensionPoint annotation.

An Extension point is created as a proxy for all registered plugins, which allows the application to just invoke the extension point implementation of the interface.

For example if you want plugins to be able to act on life cycle events of your application,
the following two extension points can be created.
 ```
@ExtensionPoint
public interface ApplicationStarted {

    void onApplicationStarted();
}
 ```
 ```
@ExtensionPoint
public interface ApplicationStopped {
    void onApplicationStopped();
}
 ```

A plugin can then be written which implements these ExtensionPoints (optionally, just choose which interfaces to implement).

```
@Plugin(name = "applicationPlugin",
        vendor = "example.org",
        version = "1.0",
        priority = Integer.MAX_VALUE)
public class ApplicationPlugin implements ApplicationStarted, ApplicationStopped {
    @Override
    public void onApplicationStarted() {
        System.out.println("Application Started");
    }

    @Override
    public void onApplicationStopped() {
        System.out.println("Application Stopped");
    }
}
```
This plugin must then be registered with the applications PluginRegistrar somewhere in its initialization code.
```
...
 void registerPlugins(PluginRegistrar pluginRegistrar) {
    ApplicationPlugin plugin = new ApplicationPlugin();
    Application.getInstance().getPluginRegistrar().registerPlugin(new PluginRegistration.Builder(plugin).build());
}
...
```
The application itself can invoke any registered plugin by retrieving and invoking the extension point proxy implementation.
 ```
public class Application {

    private static Application instance = new Application();

    private PluginManager pluginManager = new PluginManagerImpl();

    public static void main(String[] args) {
        Application application = new Application();
        application.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> application.shutdown()));
    }

    public Application getInstance() {
        return instance;
    }

    public PluginRegistrar getPluginRegistrar() {
        return pluginManager.getPluginRepository();
    }

    private void start() {
        ApplicationStarted extensionPoint = pluginManager.getExtensionPointFor(ApplicationStarted.class);
        extensionPoint.onApplicationStarted();
    }

    private void shutdown() {
        ApplicationStopped extensionPoint = pluginManager.getExtensionPointFor(ApplicationStopped.class);
        extensionPoint.onApplicationStopped();
    }
}

