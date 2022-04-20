package ru.gaidamaka.db.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K extends Number, T> {

    List<T> findAll();

    Optional<T> findById(K id);

    boolean delete(K id);

    boolean create(T entity);

    T update(T entity);
}
