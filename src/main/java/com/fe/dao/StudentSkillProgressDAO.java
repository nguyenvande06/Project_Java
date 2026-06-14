package com.fe.dao;

import javax.persistence.EntityManager;
import java.util.List;
import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.StudentSkillProgress;

public class StudentSkillProgressDAO extends GenericDAOImpl<StudentSkillProgress, Long> {

	public StudentSkillProgressDAO(EntityManager em) {
		super(em, StudentSkillProgress.class);
	}

	/**
	 * Lấy toàn bộ tiến độ kỹ năng của một sinh viên để phục vụ chức năng phân tích khoảng cách (Skill Gap)
	 */
	public List<StudentSkillProgress> findByStudentId(Long studentId) {
		return em.createQuery("SELECT p FROM StudentSkillProgress p WHERE p.student.studentId = :studentId", StudentSkillProgress.class)
				 .setParameter("studentId", studentId)
				 .getResultList();
	}
}