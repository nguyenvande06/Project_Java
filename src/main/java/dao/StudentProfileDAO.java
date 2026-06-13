package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import pojo.StudentProfile;
import pojo.TechPath;
import util.HibernateUtil;

public class StudentProfileDAO {

	public StudentProfile getById(Long studentId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(StudentProfile.class, studentId);
		}
	}

	public void updateTargetPath(Long studentId, TechPath path) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			StudentProfile student = session.get(StudentProfile.class, studentId);
			student.setTargetPath(path);
			session.update(student);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}