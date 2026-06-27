package com.fe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.fe.dao.SkillNodeDAO;
import com.fe.dao.StudentProfileDAO;
import com.fe.dao.StudentSkillProgressDAO;
import com.fe.pojo.SkillNode;
import com.fe.pojo.StudentProfile;
import com.fe.pojo.StudentSkillProgress;
import com.fe.pojo.TechPath;
import com.fe.util.JPAUtil;

public class SkillGapService {

	public void analyzeSkillGap(StudentProfile studentInput) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			StudentProfileDAO studentDAO = new StudentProfileDAO(em);
			StudentProfile student = studentDAO.findById(studentInput.getStudentId());

			TechPath path = student.getTargetPath();
			if (path == null) {
				System.out.println(">> Bạn chưa chọn lộ trình nghề nghiệp nào.");
				System.out.println(">> Vui lòng vào Chức năng 2 để chọn Tech Path trước.");
				return;
			}

			SkillNodeDAO skillNodeDAO = new SkillNodeDAO(em);
			StudentSkillProgressDAO progressDAO = new StudentSkillProgressDAO(em);

			// Lấy tất cả node của lộ trình (không chỉ root — lấy hết)
			List<SkillNode> allNodes = skillNodeDAO.findByPathId(path.getPathId());

			// Lấy tất cả tiến độ của sinh viên 1 lần
			Map<Long, String> statusMap = new HashMap<>();
			for (StudentSkillProgress p : progressDAO.findByStudentId(student.getStudentId())) {
				statusMap.put(p.getNode().getNodeId(), p.getStatus());
			}

			// Phân loại kỹ năng
			List<SkillNode> completed = new ArrayList<>();
			List<SkillNode> learning = new ArrayList<>();
			List<SkillNode> notStarted = new ArrayList<>();

			for (SkillNode node : allNodes) {
				String status = statusMap.getOrDefault(node.getNodeId(), "NOT_STARTED");
				if (status.equals("COMPLETED")) {
					completed.add(node);
				} else if (status.equals("LEARNING")) {
					learning.add(node);
				} else {
					notStarted.add(node);
				}
			}

			int total = allNodes.size();
			int completedCount = completed.size();
			int percent = total == 0 ? 0 : (completedCount * 100 / total);

			// In báo cáo
			System.out.println("\n╔══════════════════════════════════════════════════════╗");
			System.out.println("║      BÁO CÁO PHÂN TÍCH KHOẢNG CÁCH KỸ NĂNG          ║");
			System.out.println("╚══════════════════════════════════════════════════════╝");
			System.out.println("Sinh viên  : " + (student.getMssv() != null ? student.getMssv() : "Chưa có MSSV"));
			System.out.println("Lộ trình   : " + path.getPathName());
			System.out.println("Tổng kỹ năng: " + total);

			// Thanh tiến độ
			int filled = percent / 10;
			StringBuilder bar = new StringBuilder();
			for (int i = 0; i < 10; i++)
				bar.append(i < filled ? "█" : "░");
			System.out.println(
					"Hoàn thành : [" + bar + "] " + percent + "% (" + completedCount + "/" + total + " kỹ năng)");

			System.out.println("\n✅ KỸ NĂNG ĐÃ HOÀN THÀNH (" + completed.size() + "):");
			System.out.println("──────────────────────────────────────────────────────");
			if (completed.isEmpty()) {
				System.out.println("   (Chưa có kỹ năng nào hoàn thành)");
			} else {
				for (SkillNode node : completed) {
					System.out.println(
							"   [X] " + node.getSkillName() + " (" + priorityToText(node.getPriorityLevel()) + ")");
				}
			}

			System.out.println("\n▶ KỸ NĂNG ĐANG HỌC (" + learning.size() + "):");
			System.out.println("──────────────────────────────────────────────────────");
			if (learning.isEmpty()) {
				System.out.println("   (Không có kỹ năng nào đang học)");
			} else {
				for (SkillNode node : learning) {
					System.out.println(
							"   [▶] " + node.getSkillName() + " (" + priorityToText(node.getPriorityLevel()) + ")");
				}
			}

			System.out.println("\n❌ KHOẢNG CÁCH KỸ NĂNG — CẦN HỌC THÊM (" + notStarted.size() + "):");
			System.out.println("──────────────────────────────────────────────────────");
			if (notStarted.isEmpty()) {
				System.out.println("   🎉 Bạn đã học đủ tất cả kỹ năng trong lộ trình!");
			} else {
				// Ưu tiên hiển thị kỹ năng "Bắt buộc" trước
				System.out.println("   [Ưu tiên cao - Bắt buộc]");
				boolean hasRequired = false;
				for (SkillNode node : notStarted) {
					if (node.getPriorityLevel() != null && node.getPriorityLevel() == 1) {
						System.out.println("   ⚠ " + node.getSkillName());
						hasRequired = true;
					}
				}
				if (!hasRequired)
					System.out.println("   (Không có)");

				System.out.println("\n   [Ưu tiên trung bình - Nên biết]");
				boolean hasMedium = false;
				for (SkillNode node : notStarted) {
					if (node.getPriorityLevel() != null && node.getPriorityLevel() == 2) {
						System.out.println("   • " + node.getSkillName());
						hasMedium = true;
					}
				}
				if (!hasMedium)
					System.out.println("   (Không có)");

				System.out.println("\n   [Nâng cao]");
				boolean hasAdvanced = false;
				for (SkillNode node : notStarted) {
					if (node.getPriorityLevel() != null && node.getPriorityLevel() == 3) {
						System.out.println("   ○ " + node.getSkillName());
						hasAdvanced = true;
					}
				}
				if (!hasAdvanced)
					System.out.println("   (Không có)");
			}

			System.out.println("\n══════════════════════════════════════════════════════");
			System.out.println("💡 GỢI Ý: Hãy tập trung vào các kỹ năng \"Bắt buộc\" trước!");
			System.out.println("══════════════════════════════════════════════════════");

		} finally {
			em.close();
		}
	}

	private String priorityToText(Integer level) {
		if (level == null)
			return "Không xác định";
		switch (level) {
		case 1:
			return "Bắt buộc";
		case 2:
			return "Nên biết";
		case 3:
			return "Nâng cao";
		default:
			return "Không xác định";
		}
	}
}