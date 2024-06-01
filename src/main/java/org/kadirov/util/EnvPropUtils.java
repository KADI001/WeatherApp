package org.kadirov.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

import static java.lang.String.format;

@Slf4j
public final class EnvPropUtils {

    private EnvPropUtils() {
    }

    public static String getEnvProp(String name) {
        String value = getValue(name, System::getProperty);

        log.debug("Property value '{}' by name '{}'", value, name);

        if (value == null){
            value = getValue(name, System::getenv);
            log.debug("Env value '{}' by name '{}'", value, name);
        }

        return value;
    }

    private static String getValue(String name, Function<String, ?> valueProvider) {
        return (String) valueProvider.apply(name);
    }
}
