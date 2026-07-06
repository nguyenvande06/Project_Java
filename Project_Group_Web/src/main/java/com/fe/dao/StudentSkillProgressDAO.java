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
	 * 
	 */
	public StudentSkillProgress findByStudentAndNode(Long studentId, Long nodeId) {
	    try {
	        return em.createQuery(
	            "SELECT p FROM StudentSkillProgress p WHERE p.student.studentId = :studentId AND p.node.nodeId = :nodeId", 
	            StudentSkillProgress.class)
	            .setParameter("studentId", studentId)
	            .setParameter("nodeId", nodeId)
	            .getSingleResult();
	    } catch (javax.persistence.NoResultException e) {
	        return null; 
	    }
	}
	public List<StudentSkillProgress> findByStudentId(Long studentId) {

		return em.createQuery("SELECT p FROM StudentSkillProgress p WHERE p.student.studentId = :studentId", StudentSkillProgress.class)

		.setParameter("studentId", studentId)

		.getResultList();

		}
}