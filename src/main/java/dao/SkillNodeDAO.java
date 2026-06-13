package dao;

import java.util.List;

import org.hibernate.Session;

import pojo.SkillNode;
import util.HibernateUtil;

public class SkillNodeDAO {

	public List<SkillNode> getRootNodesByPath(Long pathId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			List<SkillNode> roots = session
					.createQuery("FROM SkillNode WHERE path.pathId = :pathId AND parentNode IS NULL", SkillNode.class)
					.setParameter("pathId", pathId).list();

			for (SkillNode node : roots) {
				initializeTree(node);
			}

			return roots;
		}
	}

	private void initializeTree(SkillNode node) {
		node.getChildren().size();
		node.getResources().size();

		for (SkillNode child : node.getChildren()) {
			initializeTree(child);
		}
	}
}