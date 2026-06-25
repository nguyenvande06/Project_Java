package com.fe.dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.User;

public class UserDAO extends GenericDAOImpl<User, Long> {

	public UserDAO(EntityManager em) {
		super(em, User.class);
	}
	// tìm dựa trên email
	public User findByEmail(String email) {
        try {
            String jpql = "SELECT u FROM User u WHERE u.email = :email";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            return null; 
        }
    }

}
