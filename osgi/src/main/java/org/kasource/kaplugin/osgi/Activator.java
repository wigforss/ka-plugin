/**
 * 
 */
package org.kasource.kaplugin.osgi;

import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.kasource.kaplugin.ExtensionPoint;
import org.kasource.kaplugin.OsgiPluginManager;
import org.kasource.kaplugin.PluginManager;
import org.kasource.kaplugin.manager.PluginManagerImpl;
import org.kasource.kaplugin.repository.PluginRepository;
import org.kasource.kaplugin.repository.SimplePluginRepository;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * @author rikardwigforss
 * 
 */
public class Activator implements BundleActivator, BundleListener {

    private BundleContext bundleContext;
    private PluginManager pluginManager;
    private PluginRepository pluginRepository;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        initialze();
       
    }

    @Override
    public void stop(BundleContext bundlecontext) throws Exception {
       
    }

    @Override
    public void bundleChanged(BundleEvent bundleEvent) {
        Bundle bundle = bundleEvent.getBundle();
        if (bundleEvent.getType() == BundleEvent.STARTED) {
            addExtensionPoints(bundle);
            addPlugins(bundle);
        } else if (bundleEvent.getType() == BundleEvent.STOPPED) {
            removeExtensionPoints(bundle);
        }

    }

    

    private void initialze() {
        pluginRepository = new SimplePluginRepository();
        pluginManager = new OsgiPluginManager(pluginRepository);

        // Find all @ExtensionPoint annotated
        // interfaces in active bundles and register them
        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle.getState() == Bundle.ACTIVE) {
                addExtensionPoints(bundle);
            }
        }
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
        bundleContext.registerService(PluginManager.class.getName(), pluginManager, new Properties());
    }

    private void addExtensionPoints(Bundle bundle) {
        Set<Class<?>> interfaceClasses = getInterfaces(bundle);
        for(Class<?> clazz : interfaceClasses) {
            if (clazz.isAnnotationPresent(ExtensionPoint.class)) {
                pluginRepository.addExtensionPoint(clazz);
            }
        }
    }
    
    private void removeExtensionPoints(Bundle bundle) {
        Set<Class<?>> interfaceClasses = getInterfaces(bundle);
        for(Class<?> clazz : interfaceClasses) {
            if (clazz.isAnnotationPresent(ExtensionPoint.class)) {
                pluginRepository.removeExtensionPoint(clazz);
            }
        } 
    }
    
    private  Set<Class<?>> getInterfaces(Bundle bundle) {
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
                String path = url.getPath();
                int index = path.lastIndexOf("/");
                int endIndex = path.length() - 6;// Strip ".class" substring
                String className = path.substring(index + 1, endIndex);
                String fullClassName = packageName + "." + className;
                try {
                    Class<?> clazz = bundle.loadClass(fullClassName);
                    if(clazz.isInterface()) {
                        interfaceClasses.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
        return interfaceClasses;
    }

    private void addPlugins(Bundle bundle) {
        ServiceReference[] services = bundle.getServicesInUse();
        if (services != null) {
            for (ServiceReference serviceReference : services) {
                Object service = bundleContext.getService(serviceReference);
                if (service != null) {
                    pluginRepository.registerPlugin(service);
                }

            }
        }
    }

   
}
