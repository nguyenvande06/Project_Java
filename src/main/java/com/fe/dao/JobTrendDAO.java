package com.fe.dao;

import javax.persistence.EntityManager;
import java.util.List;
import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.JobTrend;

public class JobTrendDAO extends GenericDAOImpl<JobTrend, Long> {

	public JobTrendDAO(EntityManager em) {
		super(em, JobTrend.class);
	}

	/**
	 * Lấy thống kê xu hướng từ khóa công nghệ tăng dần/giảm dần theo nền tảng tuyển dụng
	 */
	public List<JobTrend> findByPortal(String portalName) {
		return em.createQuery("SELECT j FROM JobTrend j WHERE j.jobPortal = :portal ORDER BY j.frequencyCount DESC", JobTrend.class)
				 .setParameter("portal", portalName)
				 .getResultList();
	}
}