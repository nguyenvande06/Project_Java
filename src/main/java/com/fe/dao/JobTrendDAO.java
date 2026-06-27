package com.fe.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.JobTrend;

public class JobTrendDAO extends GenericDAOImpl<JobTrend, Long> {

	public JobTrendDAO(EntityManager em) {
		super(em, JobTrend.class);
	}

	public List<JobTrend> findAllOrderByCount() {
		return em.createQuery("SELECT j FROM JobTrend j ORDER BY j.frequencyCount DESC", JobTrend.class)
				.getResultList();
	}
}