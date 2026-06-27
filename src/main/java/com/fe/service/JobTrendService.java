package com.fe.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.fe.dao.JobTrendDAO;
import com.fe.pojo.JobTrend;
import com.fe.util.JPAUtil;

public class JobTrendService {

	public List<LocalDate> getAvailableDates() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			JobTrendDAO dao = new JobTrendDAO(em);
			return dao.getDistinctDates();
		} finally {
			em.close();
		}
	}

	public void showTrendByDate(LocalDate selectedDate, LocalDate benchmarkDate) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			JobTrendDAO dao = new JobTrendDAO(em);
			List<String> portals = Arrays.asList("TOPCV", "LINKEDIN");

			System.out.println("\n-------------------------------------------------------------------------");
			System.out.println("[KẾT QUẢ PHÂN TÍCH TẦN SUẤT TỪ KHÓA TRONG NGÀY " + selectedDate + "]");
			System.out.println("-------------------------------------------------------------------------");

			Map<String, Map<String, Integer>> benchmarkMap = new HashMap<>();
			if (benchmarkDate != null) {
				for (JobTrend t : dao.findByDate(benchmarkDate)) {
					benchmarkMap.computeIfAbsent(t.getJobPortal(), k -> new HashMap<>()).put(t.getSkillName(),
							t.getFrequencyCount());
				}
			}

			for (String portal : portals) {
				List<JobTrend> trends = dao.findByDateAndPortal(selectedDate, portal);
				if (trends.isEmpty())
					continue;

				int total = 0;
				for (JobTrend t : trends)
					total += t.getFrequencyCount();

				System.out.println("\n🌐 CỔNG TUYỂN DỤNG: " + portal);
				for (JobTrend t : trends) {
					double percent = total == 0 ? 0 : (t.getFrequencyCount() * 100.0 / total);
					System.out.printf("   - %-20s: %3d lượt xuất hiện (Chiếm %.1f%%)%n", t.getSkillName(),
							t.getFrequencyCount(), percent);
				}
			}

			if (benchmarkDate != null && !benchmarkMap.isEmpty()) {
				System.out.println("\n-------------------------------------------------------------------------");
				System.out.println("[XU HƯỚNG THỊ TRƯỜNG - So với ngày " + benchmarkDate + "]");

				Map<String, Integer> selectedSkillTotal = new LinkedHashMap<>();
				for (String portal : portals) {
					for (JobTrend t : dao.findByDateAndPortal(selectedDate, portal)) {
						selectedSkillTotal.merge(t.getSkillName(), t.getFrequencyCount(), Integer::sum);
					}
				}

				Map<String, Integer> benchmarkSkillTotal = new LinkedHashMap<>();
				for (Map<String, Integer> portalMap : benchmarkMap.values()) {
					portalMap.forEach((skill, count) -> benchmarkSkillTotal.merge(skill, count, Integer::sum));
				}

				int selectedTotal = selectedSkillTotal.values().stream().mapToInt(i -> i).sum();
				int benchmarkTotal = benchmarkSkillTotal.values().stream().mapToInt(i -> i).sum();

				for (Map.Entry<String, Integer> entry : selectedSkillTotal.entrySet()) {
					String skill = entry.getKey();
					double selectedPct = selectedTotal == 0 ? 0 : (entry.getValue() * 100.0 / selectedTotal);
					double benchmarkPct = benchmarkSkillTotal.containsKey(skill) && benchmarkTotal > 0
							? (benchmarkSkillTotal.get(skill) * 100.0 / benchmarkTotal)
							: 0;
					double diff = selectedPct - benchmarkPct;

					String trend = diff > 0 ? "📈" : diff < 0 ? "📉" : "➡";
					System.out.printf(" %s %-20s ---> [%s%.1f%%]%n", trend, skill,
							diff > 0 ? "TĂNG: " : diff < 0 ? "GIẢM: " : "GIỮ NGUYÊN: ", Math.abs(diff));
				}
			}

			System.out.println("=========================================================================");

		} finally {
			em.close();
		}
	}
}