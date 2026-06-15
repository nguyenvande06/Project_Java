package com.fe.main;

import javax.persistence.EntityManager;

import com.fe.console.StudentProfileMenu;
import com.fe.dao.StudentProfileDAO;
import com.fe.pojo.StudentProfile;
import com.fe.util.JPAUtil;

public class MainApplication {
	public static void main(String[] args) {
		EntityManager em = JPAUtil.getEntityManager();
		StudentProfileDAO studentDAO = new StudentProfileDAO(em);
		StudentProfile student = studentDAO.findById(1L);
		em.close();

		if (student == null) {
			System.out.println("Chưa có StudentProfile với ID = 1. Hãy thêm data mẫu vào DB.");
			return;
		}

		StudentProfileMenu menu = new StudentProfileMenu();
		menu.show(student);
	}
}