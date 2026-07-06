package com.fe.dao;

import javax.persistence.EntityManager;
import java.util.List;
import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.TechPath;

public class TechPathDAO extends GenericDAOImpl<TechPath, Long> {

	public TechPathDAO(EntityManager em) {
		super(em, TechPath.class);
	}

	/**
	 * Chỉ lấy các lộ trình đang kích hoạt (Active) để hiển thị lên Menu chọn
	 */
	public List<TechPath> findAllActivePaths() {
		return em.createQuery("SELECT t FROM TechPath t WHERE t.isActive = true", TechPath.class)
				 .getResultList();
	}
}