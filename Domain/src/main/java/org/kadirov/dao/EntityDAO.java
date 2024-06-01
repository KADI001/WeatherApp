package org.kadirov.dao;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface EntityDAO<T, U> {

    List<T> findAll();

    Optional<T> findById(@NonNull U id);

    @NonNull T save(@NonNull T entity);

    @NonNull T update(@NonNull T entity);

    void deleteById(@NonNull U id);
}
