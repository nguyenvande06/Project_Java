package com.fe.service;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.fe.dao.StudentProfileDAO;
import com.fe.pojo.StudentProfile;
import com.fe.util.JPAUtil;

public class StudentProfileService {

	public StudentProfile getProfile(Long studentId) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			StudentProfileDAO dao = new StudentProfileDAO(em);
			return dao.findById(studentId);
		} finally {
			em.close();
		}
	}

	public void updateMssv(Long studentId, String mssv) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			StudentProfileDAO dao = new StudentProfileDAO(em);
			StudentProfile student = dao.findById(studentId);

			if (student == null) {
				System.out.println(">> Không tìm thấy sinh viên.");
				em.getTransaction().rollback();
				return;
			}

			student.setMssv(mssv);
			dao.save(student);

			em.getTransaction().commit();
			System.out.println(">> Đã cập nhật MSSV: " + mssv);
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public void linkGithub(Long studentId, String githubUsername) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			StudentProfileDAO dao = new StudentProfileDAO(em);
			StudentProfile student = dao.findById(studentId);

			if (student == null) {
				System.out.println(">> Không tìm thấy sinh viên.");
				em.getTransaction().rollback();
				return;
			}

			student.setGithubUsername(githubUsername);
			dao.save(student);

			em.getTransaction().commit();
			System.out.println(">> Đã liên kết GitHub: " + githubUsername);
			System.out.println(">> (Mô phỏng: AI đang quét README các repository để trích xuất tech stack...)");
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public String generateOrGetShareToken(Long studentId) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			StudentProfileDAO dao = new StudentProfileDAO(em);
			StudentProfile student = dao.findById(studentId);

			if (student == null) {
				System.out.println(">> Không tìm thấy sinh viên.");
				em.getTransaction().rollback();
				return null;
			}

			String token = student.getPortfolioShareToken();
			if (token == null || token.isEmpty()) {
				token = UUID.randomUUID().toString().substring(0, 8);
				student.setPortfolioShareToken(token);
				dao.save(student);
				em.getTransaction().commit();
			} else {
				em.getTransaction().rollback();
			}

			return token;
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}
}