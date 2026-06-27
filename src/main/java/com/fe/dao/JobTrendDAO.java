package com.fe.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.JobTrend;

public class JobTrendDAO extends GenericDAOImpl<JobTrend, Long> {

	public JobTrendDAO(EntityManager em) {
		super(em, JobTrend.class);
	}

	public List<LocalDate> getDistinctDates() {
		return em.createQuery("SELECT DISTINCT j.recordedDate FROM JobTrend j ORDER BY j.recordedDate DESC",
				LocalDate.class).getResultList();
	}

	public List<JobTrend> findByDate(LocalDate date) {
		return em.createQuery(
				"SELECT j FROM JobTrend j WHERE j.recordedDate = :date ORDER BY j.jobPortal, j.frequencyCount DESC",
				JobTrend.class).setParameter("date", date).getResultList();
	}

	public List<JobTrend> findByDateAndPortal(LocalDate date, String portal) {
		return em.createQuery(
				"SELECT j FROM JobTrend j WHERE j.recordedDate = :date AND j.jobPortal = :portal ORDER BY j.frequencyCount DESC",
				JobTrend.class).setParameter("date", date).setParameter("portal", portal).getResultList();
	}
}