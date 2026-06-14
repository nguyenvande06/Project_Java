package com.fe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.fe.dao.SkillNodeDAO;
import com.fe.dao.StudentProfileDAO;
import com.fe.dao.StudentSkillProgressDAO;
import com.fe.dao.TechPathDAO;
import com.fe.pojo.LearningResource;
import com.fe.pojo.SkillNode;
import com.fe.pojo.StudentProfile;
import com.fe.pojo.StudentSkillProgress;
import com.fe.pojo.TechPath;
import com.fe.util.JPAUtil;

public class CareerPathService {

	public List<TechPath> getAllActivePaths() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TechPathDAO dao = new TechPathDAO(em);
			return dao.findAllActivePaths(); // <-- đổi tên method
		} finally {
			em.close();
		}
	}

	public void setTargetPath(Long studentId, Long pathId) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			TechPathDAO pathDAO = new TechPathDAO(em);
			StudentProfileDAO studentDAO = new StudentProfileDAO(em);

			TechPath path = pathDAO.findById(pathId);
			StudentProfile student = studentDAO.findById(studentId);

			if (path == null || student == null) {
				System.out.println(">> Không tìm thấy lộ trình hoặc sinh viên.");
				em.getTransaction().rollback();
				return;
			}

			student.setTargetPath(path);
			studentDAO.save(student);

			em.getTransaction().commit();
			System.out.println(">> Đã cập nhật mục tiêu nghề nghiệp: " + path.getPathName());
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public void printSkillTree(StudentProfile studentInput) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			StudentProfileDAO studentDAO = new StudentProfileDAO(em);
			StudentProfile student = studentDAO.findById(studentInput.getStudentId());

			TechPath path = student.getTargetPath();
			if (path == null) {
				System.out.println(">> Bạn chưa chọn lộ trình nghề nghiệp nào.");
				return;
			}

			System.out.println("============ LỘ TRÌNH CHI TIẾT: " + path.getPathName().toUpperCase() + " ===========");

			SkillNodeDAO skillNodeDAO = new SkillNodeDAO(em);
			StudentSkillProgressDAO progressDAO = new StudentSkillProgressDAO(em);

			List<SkillNode> rootNodes = skillNodeDAO.findRootNodesByPathId(path.getPathId());

			// Lấy toàn bộ tiến độ của sinh viên 1 lần, đưa vào Map để tra cứu nhanh
			Map<Long, String> statusMap = new HashMap<>();
			for (StudentSkillProgress p : progressDAO.findByStudentId(student.getStudentId())) {
				statusMap.put(p.getNode().getNodeId(), p.getStatus());
			}

			int total = countAllNodes(rootNodes);
			int completed = countCompletedNodes(rootNodes, statusMap);
			int percent = total == 0 ? 0 : (completed * 100 / total);
			int filled = percent / 10;

			StringBuilder bar = new StringBuilder();
			for (int i = 0; i < 10; i++) {
				bar.append(i < filled ? "█" : "░");
			}
			System.out.println("Tiến độ tổng thể: [" + bar + "] " + percent + "%");

			int index = 1;
			for (SkillNode root : rootNodes) {
				printNode(root, index, 0, statusMap);
				index++;
			}

			System.out.println("==============================================================");
		} finally {
			em.close();
		}
	}

	private void printNode(SkillNode node, int index, int level, Map<Long, String> statusMap) {
		String status = statusMap.getOrDefault(node.getNodeId(), "NOT_STARTED");
		String mark;
		if (status.equals("COMPLETED")) {
			mark = "[X]";
		} else if (status.equals("LEARNING")) {
			mark = "[▶]";
		} else {
			mark = "[ ]";
		}

		StringBuilder prefixBuilder = new StringBuilder();
		for (int i = 0; i < level; i++)
			prefixBuilder.append("  ");
		String prefix = prefixBuilder.toString();

		String numbering = level == 0 ? index + ". " : "";

		System.out.println(prefix + numbering + mark + " KỸ NĂNG: " + node.getSkillName() + " (Mức độ: "
				+ priorityToText(node.getPriorityLevel()) + ") - TRẠNG THÁI: " + statusToText(status));

		if (node.getResources() != null) {
			for (LearningResource res : node.getResources()) {
				System.out.println(prefix + "   🔹 [" + res.getResourceType() + "] " + res.getTitle() + " (Link: "
						+ res.getUrl() + ")");
			}
		}

		if (node.getChildren() != null) {
			int childIndex = 1;
			for (SkillNode child : node.getChildren()) {
				printNode(child, childIndex, level + 1, statusMap);
				childIndex++;
			}
		}
	}

	private int countAllNodes(List<SkillNode> nodes) {
		int count = 0;
		for (SkillNode n : nodes) {
			count++;
			if (n.getChildren() != null)
				count += countAllNodes(n.getChildren());
		}
		return count;
	}

	private int countCompletedNodes(List<SkillNode> nodes, Map<Long, String> statusMap) {
		int count = 0;
		for (SkillNode n : nodes) {
			if ("COMPLETED".equals(statusMap.getOrDefault(n.getNodeId(), "NOT_STARTED")))
				count++;
			if (n.getChildren() != null)
				count += countCompletedNodes(n.getChildren(), statusMap);
		}
		return count;
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

	private String statusToText(String status) {
		if (status.equals("COMPLETED"))
			return "HOÀN THÀNH";
		if (status.equals("LEARNING"))
			return "ĐANG HỌC";
		return "CHƯA HỌC";
	}
}