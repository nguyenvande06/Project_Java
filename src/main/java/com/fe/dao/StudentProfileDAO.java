package com.fe.dao;

import javax.persistence.EntityManager;
import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.StudentProfile;

public class StudentProfileDAO extends GenericDAOImpl<StudentProfile, Long> {

    public StudentProfileDAO(EntityManager em) {
        super(em, StudentProfile.class);
    }
}