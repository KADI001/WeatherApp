package org.kadirov.dao;

import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BaseEntityDAO<T, U> implements EntityDAO<T, U> {

    private final Class<T> entityClass;
    private final SessionFactory sessionFactory;

    protected BaseEntityDAO(@NonNull Class<T> entityClass, @NonNull SessionFactory sessionFactory) {
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<T> findAll() {
        return executeWithTransaction((Function<Session, List<T>>) session ->
                session.createQuery(String.format("from %s", entityClass.getSimpleName()), entityClass).getResultList());
    }

    @Override
    public Optional<T> findById(@NonNull final U id) {
        return executeWithTransaction((Function<Session, Optional<T>>) session ->
                Optional.ofNullable(session.get(entityClass, id)));
    }

    @Override
    public @NonNull T save(@NonNull final T entity) {
        return executeWithTransaction(session -> {
            session.persist(entity);
            return entity;
        });
    }

    @Override
    public @NonNull T update(@NonNull final T entity) {
        return executeWithTransaction((Function<Session, T>) session ->
                session.merge(entity));
    }

    @Override
    public void deleteById(@NonNull final U id) {
        executeWithTransaction((Consumer<Session>) session ->
                session.remove(session.get(entityClass, id)));
    }

    public <R> R executeWithTransaction(@NonNull final Function<Session, R> function) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        R result;

        try {
            transaction.begin();
            result = function.apply(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive())
                transaction.rollback();

            throw e;
        } finally {
            session.close();
        }

        return result;
    }

    public void executeWithTransaction(@NonNull final Consumer<Session> consumer) {
        executeWithTransaction(session -> {
            consumer.accept(session);
            return null;
        });
    }
}
