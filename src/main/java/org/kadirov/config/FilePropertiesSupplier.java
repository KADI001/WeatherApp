package org.kadirov.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

@Slf4j
@Getter
public class FilePropertiesSupplier implements Supplier<Properties> {

    private final String fileName;
    protected Properties properties;

    public FilePropertiesSupplier(@NonNull String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Properties get() {
        if (properties == null)
            initProperties();

        return properties;
    }

    protected void initProperties() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        final InputStream in = contextClassLoader.getResourceAsStream(fileName);

        if (in == null) {
            throw new RuntimeException(String.format("Properties file '%s' not found", fileName));
        }

        properties = new Properties();

        try {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}