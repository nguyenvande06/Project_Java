package service;

import java.util.List;

import dao.SkillNodeDAO;
import dao.StudentProfileDAO;
import dao.StudentSkillProgressDAO;
import dao.TechPathDao;
import pojo.LearningResource;
import pojo.SkillNode;
import pojo.StudentProfile;
import pojo.StudentSkillProgress;
import pojo.TechPath;

public class CareerPathService {

	private TechPathDao techPathDAO = new TechPathDao();
	private SkillNodeDAO skillNodeDAO = new SkillNodeDAO();
	private StudentSkillProgressDAO progressDAO = new StudentSkillProgressDAO();
	private StudentProfileDAO studentDAO = new StudentProfileDAO();

	public List<TechPath> getAllActivePaths() {
		return techPathDAO.getAllActivePaths();
	}

	public void setTargetPath(Long studentId, Long pathId) {
		TechPath path = techPathDAO.getById(pathId);
		if (path == null) {
			System.out.println(">> Không tìm thấy lộ trình.");
			return;
		}
		studentDAO.updateTargetPath(studentId, path);
		System.out.println(">> Đã cập nhật mục tiêu nghề nghiệp: " + path.getPathName());
	}

	public void printSkillTree(StudentProfile student) {
		student = studentDAO.getById(student.getStudentId());

		TechPath path = student.getTargetPath();
		if (path == null) {
			System.out.println(">> Bạn chưa chọn lộ trình nghề nghiệp nào.");
			return;
		}

		System.out.println("============ LỘ TRÌNH CHI TIẾT: " + path.getPathName().toUpperCase() + " ===========");

		List<SkillNode> rootNodes = skillNodeDAO.getRootNodesByPath(path.getPathId());

		int total = countAllNodes(rootNodes);
		int completed = countCompletedNodes(rootNodes, student);
		int percent = total == 0 ? 0 : (completed * 100 / total);
		int filled = percent / 10;

		StringBuilder bar = new StringBuilder();
		for (int i = 0; i < 10; i++)
			bar.append(i < filled ? "█" : "░");
		System.out.println("Tiến độ tổng thể: [" + bar + "] " + percent + "%");

		int index = 1;
		for (SkillNode root : rootNodes) {
			printNode(root, student, index, 0);
			index++;
		}

		System.out.println("==============================================================");
	}

	private void printNode(SkillNode node, StudentProfile student, int index, int level) {
		String status = getStatus(student, node);
		String mark;

		switch (status) {
		case "COMPLETED":
			mark = "[X]";
			break;
		case "LEARNING":
			mark = "[▶]";
			break;
		default:
			mark = "[ ]";
			break;
		}
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < level; i++) {
			sb.append("  ");
		}

		String prefix = sb.toString();
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
				printNode(child, student, childIndex, level + 1);
				childIndex++;
			}
		}
	}

	private String getStatus(StudentProfile student, SkillNode node) {
		StudentSkillProgress progress = progressDAO.findByStudentAndNode(student.getStudentId(), node.getNodeId());
		return progress != null ? progress.getStatus() : "NOT_STARTED";
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

	private int countCompletedNodes(List<SkillNode> nodes, StudentProfile student) {
		int count = 0;
		for (SkillNode n : nodes) {
			if ("COMPLETED".equals(getStatus(student, n)))
				count++;
			if (n.getChildren() != null)
				count += countCompletedNodes(n.getChildren(), student);
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
		switch (status) {
		case "COMPLETED":
			return "HOÀN THÀNH";
		case "LEARNING":
			return "ĐANG HỌC";
		default:
			return "CHƯA HỌC";
		}
	}
}