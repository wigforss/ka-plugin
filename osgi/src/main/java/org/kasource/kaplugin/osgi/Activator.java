package org.kasource.kaplugin.osgi;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.kasource.commons.reflection.util.AnnotationUtils;
import org.kasource.kaplugin.OsgiPluginManager;
import org.kasource.kaplugin.PluginRegistration;
import org.kasource.kaplugin.annotation.ExtensionPoint;
import org.kasource.kaplugin.annotation.Plugin;
import org.kasource.kaplugin.manager.PluginManager;
import org.kasource.kaplugin.repository.PluginRepository;
import org.kasource.kaplugin.repository.SimplePluginRepository;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rikardwigforss
 *
 */
public class Activator implements BundleActivator, BundleListener {
    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

    private BundleContext bundleContext;
    private PluginManager pluginManager;
    private PluginRepository pluginRepository;

    @Override
    public void start(BundleContext context) throws Exception {
        this.bundleContext = context;
        initialize();
    }

    @Override
    public void stop(BundleContext bundlecontext) throws Exception {

    }

    @Override
    public void bundleChanged(BundleEvent bundleEvent) {
        Bundle bundle = bundleEvent.getBundle();
        if (bundleEvent.getType() == BundleEvent.STARTED) {
            addPlugins(bundle);
        } else if (bundleEvent.getType() == BundleEvent.STOPPED) {
            removeExtensionPoints(bundle);
        }

    }


    private void removeExtensionPoints(Bundle bundle) {
        getInterfaces(bundle).stream()
                .filter(i -> i.isAnnotationPresent(ExtensionPoint.class))
                .forEach(i -> pluginRepository.removeExtensionPoint(i));

    }


    private void initialize() {
        pluginRepository = new SimplePluginRepository();
        pluginManager = new OsgiPluginManager(pluginRepository);


        Bundle[] bundles = bundleContext.getBundles();

        // Find all services in active bundles that implements any @ExtensionPoint
        // interface and register those as plugins
        for (Bundle bundle : bundles) {
            if (bundle.getState() == Bundle.ACTIVE) {
                addPlugins(bundle);
            }
        }

        // Listen to bundle changes
        bundleContext.addBundleListener(this);
        // Register the PluginManager as a service
        bundleContext.registerService(PluginManager.class.getName(), pluginManager, new Hashtable<>());
    }


    private Set<Class<?>> getInterfaces(Bundle bundle) {
        ServiceReference ref = bundleContext.getServiceReference(PackageAdmin.class.getName());
        PackageAdmin pa = (PackageAdmin) ref;
        ExportedPackage[] packages = pa.getExportedPackages(bundle);
        Set<Class<?>> interfaceClasses = new HashSet<Class<?>>();
        for (ExportedPackage ePackage : packages) {
            String packageName = ePackage.getName();
            String packagePath = "/" + packageName.replace('.', '/');
            // find all the class files in current exported package
            Enumeration<URL> clazzes = bundle.findEntries(packagePath, "*.class", false);

            while (clazzes.hasMoreElements()) {
                URL url = clazzes.nextElement();
                String className =  StringUtils.removeEnd(StringUtils.substringAfterLast(url.getPath(), "/"), ".class");
                String fullClassName = packageName + "." + className;
                try {
                    Class<?> clazz = bundle.loadClass(fullClassName);
                    if (clazz.isInterface()) {
                        interfaceClasses.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    LOG.warn("Class " + fullClassName + " from bundle "
                            + bundle.getBundleId() + " "
                            + bundle.getSymbolicName() + ":" + bundle.getVersion()
                            + " not found ", e);
                }
            }
        }
        return interfaceClasses;
    }

    private void addPlugins(Bundle bundle) {

        ServiceReference[] services = bundle.getServicesInUse();
        if (services != null) {
            for (ServiceReference serviceReference : services) {
                addPlugin(bundle, serviceReference);
            }
        }
    }

    private void addPlugin(Bundle bundle, ServiceReference serviceReference) {
        Object service = bundleContext.getService(serviceReference);
        if (service != null) {
            PluginRegistration.Builder builder = new PluginRegistration.Builder(service);
            if (!AnnotationUtils.isAnnotationPresent(service.getClass(), Plugin.class)) {
                builder.version(bundle.getVersion().toString())
                        .vendor(bundle.getHeaders().get("Bundle-Vendor"))
                        .name(bundle.getSymbolicName());
            }
            pluginRepository.registerPlugin(builder.build());
        }
    }


}
