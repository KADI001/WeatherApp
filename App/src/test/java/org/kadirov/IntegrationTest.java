package org.kadirov;

import lombok.extern.slf4j.Slf4j;
import org.kadirov.config.Environment;
import org.kadirov.config.TemplatedFilePropertiesSupplier;

@Slf4j
public abstract class IntegrationTest {
    static {
        Environment.registerPropertiesSupplier(new TemplatedFilePropertiesSupplier("application-test.properties"));
        log.debug("Application test properties loaded. Listed:");
        Environment.getProperties().forEach((key, value) -> {
            log.debug("{} = {}", key, value);
        });
    }
}
