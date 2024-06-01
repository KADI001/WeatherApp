package org.kadirov.dao;

import lombok.NonNull;
import org.hibernate.SessionFactory;

import java.lang.reflect.Constructor;

public class EntityDAOFactory implements AutoCloseable {

    private final SessionFactory sessionFactory;

    public EntityDAOFactory(@NonNull final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T extends BaseEntityDAO<?, ?>> @NonNull T createInstanceOf(@NonNull Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(SessionFactory.class);
            return constructor.newInstance(sessionFactory);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to get instance of '%s' EntityDAO class", clazz.getName()), e);
        }
    }

    @Override
    public void close() {
        sessionFactory.close();
    }
}
