package org.kasource.kaplugin;

import java.util.Objects;

import org.kasource.commons.reflection.util.AnnotationUtils;
import org.kasource.kaplugin.annotation.Plugin;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("checkstyle:finalclass")
public class PluginRegistration {
    private static final String ERROR_MESSAGE = "The %s is missing, consider annotating the plugin %s with " + Plugin.class.getName();

    private Object plugin;
    private String name;
    private String version;
    private String vendor;
    private int priority;

    private PluginRegistration(final Builder builder) {
        this.plugin = builder.plugin;
        this.name = builder.name;
        this.version = builder.version;
        this.vendor = builder.vendor;
        this.priority = builder.priority;
    }


    public Object getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getVendor() {
        return vendor;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "PluginRegistration{"
                + "plugin=" + plugin
                + ", name='" + name + '\''
                + ", version='" + version + '\''
                + ", vendor='" + vendor + '\''
                + ", priority=" + priority
                + '}';
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PluginRegistration that = (PluginRegistration) o;
        return priority == that.priority
                 && Objects.equals(plugin, that.plugin)
                && Objects.equals(name, that.name)
                && Objects.equals(version, that.version)
                && Objects.equals(vendor, that.vendor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugin, name, version, vendor, priority);
    }

    @SuppressWarnings("checkstyle:hiddenfield")
    public static class Builder {
        private Object plugin;
        private String name;
        private String version;
        private String vendor;
        private int priority;

        public Builder(final Object plugin) {
            this.plugin = plugin;
            Plugin pluginAnnotation = AnnotationUtils.getAnnotation(plugin.getClass(), Plugin.class);
            if (pluginAnnotation != null) {
                this.name = pluginAnnotation.name();
                this.version = pluginAnnotation.version();
                this.vendor = pluginAnnotation.vendor();
                this.priority = pluginAnnotation.priority();
            }
        }

        public PluginRegistration build() {
            validate();
            return new PluginRegistration(this);
        }

        private void validate() {
            if (StringUtils.isEmpty(this.name)) {
                throw new IllegalStateException(String.format(ERROR_MESSAGE, "name", plugin));
            }
            if (StringUtils.isEmpty(this.version)) {
                throw new IllegalStateException(String.format(ERROR_MESSAGE, "version", plugin));
            }
            if (StringUtils.isEmpty(this.vendor)) {
                throw new IllegalStateException(String.format(ERROR_MESSAGE, "vendor", plugin));
            }
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder vendor(String vendor) {
            this.vendor = vendor;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }
    }
}
