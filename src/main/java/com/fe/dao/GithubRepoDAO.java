package com.fe.dao;

import javax.persistence.EntityManager;
import java.util.List;
import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.GithubRepo;

public class GithubRepoDAO extends GenericDAOImpl<GithubRepo, Long> {

	public GithubRepoDAO(EntityManager em) {
		super(em, GithubRepo.class);
	}

	/**
	 * Lấy toàn bộ danh sách Repo GitHub của một sinh viên cụ thể
	 */
	public List<GithubRepo> findByStudentId(Long studentId) {
		return em.createQuery("SELECT r FROM GithubRepo r WHERE r.student.studentId = :studentId", GithubRepo.class)
				 .setParameter("studentId", studentId)
				 .getResultList();
	}
}