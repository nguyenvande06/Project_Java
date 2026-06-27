package com.fe.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.fe.dao.JobTrendDAO;
import com.fe.pojo.JobTrend;
import com.fe.util.JPAUtil;

public class JobTrendService {

	public void showJobTrend() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			JobTrendDAO dao = new JobTrendDAO(em);
			List<JobTrend> trends = dao.findAll();

			if (trends.isEmpty()) {
				System.out.println(">> Chưa có dữ liệu xu hướng thị trường.");
				return;
			}

			// Gộp frequency_count theo skill_name
			Map<String, Integer> skillCount = new LinkedHashMap<>();
			Map<String, String> skillPortal = new LinkedHashMap<>();

			for (JobTrend t : trends) {
				String skill = t.getSkillName();
				int count = t.getFrequencyCount();
				skillCount.put(skill, skillCount.getOrDefault(skill, 0) + count);
				skillPortal.put(skill, t.getJobPortal());
			}

			// Sắp xếp giảm dần theo frequency
			List<Map.Entry<String, Integer>> sorted = new ArrayList<>(skillCount.entrySet());
			sorted.sort((a, b) -> b.getValue() - a.getValue());

			// In bảng
			System.out.println("\n╔══════════════════════════════════════════════════════════╗");
			System.out.println("║         THỐNG KÊ XU HƯỚNG THỊ TRƯỜNG TUYỂN DỤNG         ║");
			System.out.println("╚══════════════════════════════════════════════════════════╝");
			System.out.printf("%-5s %-20s %-15s %-10s%n", "STT", "Công nghệ", "Nguồn", "Số lần đề cập");
			System.out.println("──────────────────────────────────────────────────────────");

			int rank = 1;
			for (Map.Entry<String, Integer> entry : sorted) {
				String medal = rank == 1 ? "🥇" : rank == 2 ? "🥈" : rank == 3 ? "🥉" : "  ";
				System.out.printf("%-5s %-20s %-15s %-10d%n", medal + rank, entry.getKey(),
						skillPortal.getOrDefault(entry.getKey(), "-"), entry.getValue());
				rank++;
			}

			System.out.println("──────────────────────────────────────────────────────────");
			System.out.println("Tổng số công nghệ: " + sorted.size());
			System.out.println("💡 Top công nghệ được tuyển dụng nhiều nhất: "
					+ (sorted.isEmpty() ? "N/A" : sorted.get(0).getKey()));
			System.out.println("══════════════════════════════════════════════════════════");

		} finally {
			em.close();
		}
	}
}