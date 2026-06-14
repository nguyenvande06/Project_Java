package com.fe.dao;

import javax.persistence.EntityManager;
import java.util.List;
import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.SkillNode;

public class SkillNodeDAO extends GenericDAOImpl<SkillNode, Long> {

	public SkillNodeDAO(EntityManager em) {
		super(em, SkillNode.class);
	}

	/**
	 * Lấy toàn bộ cây kỹ năng của một lộ trình cụ thể
	 */
	public List<SkillNode> findByPathId(Long pathId) {
		return em.createQuery("SELECT s FROM SkillNode s WHERE s.techPath.pathId = :pathId ORDER BY s.priorityLevel ASC", SkillNode.class)
				 .setParameter("pathId", pathId)
				 .getResultList();
	}
}