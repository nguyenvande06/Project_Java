package com.fe.dao;

import javax.persistence.EntityManager;
import java.util.List;
import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.LearningResource;

public class LearningResourceDAO extends GenericDAOImpl<LearningResource, Long> {

	public LearningResourceDAO(EntityManager em) {
		super(em, LearningResource.class);
	}

	/**
	 * Tìm kiếm tài liệu học tập theo nút kỹ năng cụ thể
	 */
	public List<LearningResource> findBySkillNode(Long nodeId) {
		return em.createQuery("SELECT r FROM LearningResource r WHERE r.skillNode.nodeId = :nodeId", LearningResource.class)
				 .setParameter("nodeId", nodeId)
				 .getResultList();
	}
}