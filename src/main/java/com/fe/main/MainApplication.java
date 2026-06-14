package com.fe.main;

import javax.persistence.EntityManager;

import com.fe.console.CareerPathMenu;
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

		CareerPathMenu menu = new CareerPathMenu();
		menu.show(student);
	}
}