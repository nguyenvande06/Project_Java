package com.fe.dao;

import java.util.List;

public interface IGenericDAO<T, ID> {
    void save(T entity);
    T findById(ID id);
    List<T> findAll();
    void delete(ID id);
}