package org.kadirov.util;

@FunctionalInterface
public interface ThrowableFunction<T, R> {
    R apply(T t) throws Exception;
}
