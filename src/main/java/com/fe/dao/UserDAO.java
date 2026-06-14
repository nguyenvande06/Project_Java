package com.fe.dao;

import javax.persistence.EntityManager;
import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.User;

public class UserDAO extends GenericDAOImpl<User, Long> {

	public UserDAO(EntityManager em) {
		super(em, User.class);
	}

}
