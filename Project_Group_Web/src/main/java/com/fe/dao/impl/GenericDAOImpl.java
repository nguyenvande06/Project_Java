package com.fe.dao.impl;

import javax.persistence.EntityManager;
import java.util.List;
import com.fe.dao.IGenericDAO;

public class GenericDAOImpl<T, ID> implements IGenericDAO<T, ID> {

    protected EntityManager em;
    private Class<T> entityClass;

    public GenericDAOImpl(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    @Override
    public void save(T entity) {
            em.persist(entity);
    }

    @Override
    public T findById(ID id) {
        return em.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
    }

    @Override
    public void delete(ID id) {
        T entity = findById(id);
        if (entity != null) {
            em.remove(entity);
        }
    }
}