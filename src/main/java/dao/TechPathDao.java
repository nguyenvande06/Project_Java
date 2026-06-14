package dao;

import java.util.List;

import org.hibernate.Session;

import pojo.TechPath;
import util.HibernateUtil;

public class TechPathDao {

	public List<TechPath> getAllActivePaths() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM TechPath WHERE isActive = true", TechPath.class).list();
		}
	}

	public TechPath getById(Long pathId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(TechPath.class, pathId);
		}
	}
}