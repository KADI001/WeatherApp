package org.kadirov.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;

import static org.kadirov.util.EnvPropUtils.getEnvProp;

@Slf4j
public class TemplatedFilePropertiesSupplier extends FilePropertiesSupplier {

    public static final String PLACEHOLDER_PATTERN = "^\\$\\{.+}$";

    public TemplatedFilePropertiesSupplier(@NonNull String propertiesFileName) {
        super(propertiesFileName);
    }

    @Override
    protected void initProperties() {
        super.initProperties();

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            if (value.matches(PLACEHOLDER_PATTERN)) {
                String placeholder = value.substring(2, value.length() - 1);
                String[] parts = placeholder.split(":");

                if (parts.length == 0) {
                    log.error("Invalid placeholder syntax '{}' of property with name '{}' in file: '{}'", value, key, getFileName());
                    continue;
                }

                String defaultValue = null;

                if (parts.length >= 2) {
                    String[] defaultValueParts = Arrays.stream(parts)
                            .skip(1)
                            .toArray(String[]::new);

                    defaultValue = String.join(":", defaultValueParts);
                    log.debug("Default value '{}' of property '{}'", defaultValue, key);
                }

                String resolvedValue = getEnvProp(parts[0]);
                log.debug("Resolved value '{}' of property '{}'", resolvedValue, key);

                if (resolvedValue == null) {
                    log.warn("Couldn't resolve property '{}' with name '{}' in properties file: '{}'", value, key, getFileName());
                    resolvedValue = defaultValue;
                }

                if (resolvedValue != null)
                    properties.replace(key, resolvedValue);

                log.debug("Processed property '{}' with raw value '{}' and resolved value '{}'", key, value, resolvedValue);
            }
        }
    }
}