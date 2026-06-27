package com.fe.main;

import javax.persistence.EntityManager;

import com.fe.console.JobTrendMenu;
import com.fe.util.JPAUtil;

public class MainApplication {
	public static void main(String[] args) {
		EntityManager em = JPAUtil.getEntityManager();
		em.close();

		JobTrendMenu menu = new JobTrendMenu();
		menu.show();
	}
}