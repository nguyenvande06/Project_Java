package dao;

import java.util.List;

import org.hibernate.Session;

import pojo.StudentSkillProgress;
import util.HibernateUtil;

public class StudentSkillProgressDAO {

	public StudentSkillProgress findByStudentAndNode(Long studentId, Long nodeId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			List<StudentSkillProgress> list = session
					.createQuery("FROM StudentSkillProgress WHERE student.studentId = :sid AND node.nodeId = :nid",
							StudentSkillProgress.class)
					.setParameter("sid", studentId).setParameter("nid", nodeId).list();
			return list.isEmpty() ? null : list.get(0);
		}
	}
}