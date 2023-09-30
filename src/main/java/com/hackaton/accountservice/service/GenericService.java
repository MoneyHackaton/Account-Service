package com.hackaton.accountservice.service;

import java.util.List;

public interface GenericService<T> {
    T add(T entity);

    T getById(Long id);

    List<T> getAll();

    void deleteById(Long id);

    T update(T entity);
}
