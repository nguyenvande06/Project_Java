package com.fe.dao;

import java.util.List;

import javax.persistence.EntityManager;

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
		return em
				.createQuery("SELECT s FROM SkillNode s WHERE s.techPath.pathId = :pathId ORDER BY s.priorityLevel ASC",
						SkillNode.class)
				.setParameter("pathId", pathId).getResultList();
	}

	public List<SkillNode> findRootNodesByPathId(Long pathId) {
		return em.createQuery(
				"SELECT s FROM SkillNode s WHERE s.path.pathId = :pathId AND s.parentNode IS NULL ORDER BY s.priorityLevel ASC",
				SkillNode.class).setParameter("pathId", pathId).getResultList();
	}
}