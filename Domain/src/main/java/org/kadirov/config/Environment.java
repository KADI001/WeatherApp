package org.kadirov.config;

import lombok.NonNull;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;

public class Environment {

    public static final Set<Supplier<Properties>> propertiesSupplier = new HashSet<>();

    public static boolean registerPropertiesSupplier(Supplier<Properties> propertiesProvider) {
        return propertiesSupplier.add(propertiesProvider);
    }

    public static String getProperty(@NonNull final String key) {
        String value = null;

        for (Supplier<Properties> propertiesProvider : propertiesSupplier) {
            value = propertiesProvider.get().getProperty(key);

            if (value != null)
                break;
        }

        return value;
    }

    public static Properties getPropertiesByStartsWith(@NonNull final String startsWith) {
        return getProperties().entrySet().stream()
                .filter(entry -> entry.getKey().toString().startsWith(startsWith))
                .collect(Properties::new, (props, entry) -> props.setProperty(entry.getKey().toString(), entry.getValue().toString()), Properties::putAll);
    }

    public static Properties getProperties() {
        return propertiesSupplier.stream()
                .map(Supplier::get)
                .flatMap(properties -> properties.entrySet().stream())
                .collect(Properties::new, (props, entry) -> props.setProperty(entry.getKey().toString(), entry.getValue().toString()), Properties::putAll);
    }
}
