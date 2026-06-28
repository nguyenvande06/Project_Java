package com.fe.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.MentorSession;

public class MentorSessionDAO extends GenericDAOImpl<MentorSession, Long> {

	public MentorSessionDAO(EntityManager em) {
		super(em, MentorSession.class);
	}

	/**
	 * Lấy toàn bộ các phiên chat của một sinh viên (Sắp xếp phiên mới nhất lên đầu)
	 */
	public List<MentorSession> findByStudentId(Long studentId) {
		return em
				.createQuery("SELECT m FROM MentorSession m WHERE m.student.studentId = :sid ORDER BY m.createdAt DESC",
						MentorSession.class)
				.setParameter("sid", studentId).getResultList();
	}

}