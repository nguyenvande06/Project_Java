package com.fe.service.techpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.fe.dao.SkillNodeDAO;
import com.fe.dao.StudentProfileDAO;
import com.fe.dao.StudentSkillProgressDAO;
import com.fe.dao.TechPathDAO;
import com.fe.pojo.LearningResource;
import com.fe.pojo.SkillNode;
import com.fe.pojo.StudentProfile;
import com.fe.pojo.StudentSkillProgress;
import com.fe.pojo.TechPath;

public class CareerPathService implements ICareerPathService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAs");
    
    @Override
    public void selectAndSetTargetPath(Long studentId, int choice) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            StudentProfileDAO studentDAO = new StudentProfileDAO(em);
            StudentProfile student = studentDAO.findById(studentId);

            if (student == null) {
                System.out.println(">> Lỗi: Không tìm thấy tài khoản sinh viên.");
                em.getTransaction().rollback();
                return;
            }

            TechPathDAO pathDAO = new TechPathDAO(em);
            Long pathId = (long) choice;
            TechPath path = pathDAO.findById(pathId);

            if (path == null || !path.getIsActive()) {
                System.out.println(">> Lỗi: Lộ trình này không tồn tại hoặc đã ngừng kích hoạt.");
                em.getTransaction().rollback();
                return;
            }

            student.setTargetPath(path);
            studentDAO.save(student);

            em.getTransaction().commit();
            System.out.println("\n>> [THÀNH CÔNG]: Đã lưu mục tiêu nghề nghiệp: " + path.getPathName().toUpperCase());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void executeSkillGapAnalysis(Long studentId) {
        EntityManager em = emf.createEntityManager();
        try (Scanner input = new Scanner(System.in)){
            StudentProfileDAO studentDAO = new StudentProfileDAO(em);
            StudentProfile student = studentDAO.findById(studentId);

            if (student == null || student.getTargetPath() == null || !student.getTargetPath().getIsActive()) {
                System.out.println("\n⚠️ [CHẶN]: Bạn phải chọn Mục tiêu nghề nghiệp (Chức năng 2) trước khi phân tích!");
                return;
            }

            TechPath path = student.getTargetPath();
            SkillNodeDAO skillNodeDAO = new SkillNodeDAO(em);

            // lấy toàn bộ skillnode của techpath 
            List<SkillNode> rootNodes = skillNodeDAO.findRootNodesByPathId(path.getPathId());
            List<SkillNode> flatList = new ArrayList<>();
            flattenNodes(rootNodes, flatList); // Phẳng hóa cấu trúc cây

            System.out.println("\n================ [FR3.1] ĐÁNH GIÁ NĂNG LỰC ĐẦU VÀO ================");
            System.out.println("Mục tiêu nghề nghiệp: " + path.getPathName().toUpperCase());
            System.out.println("Hệ thống đối chiếu danh sách kỹ năng chuẩn. Vui lòng tự đánh giá:\n");


            List<SkillNode> missingNodes = new ArrayList<>();

            for (SkillNode node : flatList) {
                System.out.println("👉 Kỹ năng: " + node.getSkillName() + " (" + priorityToText(node.getPriorityLevel()) + ")");
                System.out.println("   [1] Tôi ĐÃ BIẾT kỹ năng này");
                System.out.println("   [2] Tôi CHƯA BIẾT kỹ năng này");
                System.out.print("   Lựa chọn của bạn (1 hoặc 2): ");
                
                int userChoice = input.nextInt();
                

                if (userChoice == 2) {
                    missingNodes.add(node);
                }
            }

            System.out.println("\n================ [FR3.2 & FR3.3] BÁO CÁO KHOẢNG CÁCH KỸ NĂNG ================");
            System.out.println("Vị trí đối chiếu: " + path.getPathName().toUpperCase());
            
            int totalSkills = flatList.size();
            int missingSkillsCount = missingNodes.size();

            if (missingNodes.isEmpty()) {
                System.out.println("🎉 Tuyệt vời! Bạn tự đánh giá đã nắm được 100% nền tảng của lộ trình này.");
            } else {

                List<SkillNode> mandatoryGaps = new ArrayList<>(); // Mức 1
                List<SkillNode> recommendedGaps = new ArrayList<>(); // Mức 2
                List<SkillNode> advancedGaps = new ArrayList<>(); // Mức 3

                for (SkillNode node : missingNodes) {
                    Integer pLevel = node.getPriorityLevel();
                    if (pLevel != null && pLevel == 1) {
                        mandatoryGaps.add(node);
                    } else if (pLevel != null && pLevel == 2) {
                        recommendedGaps.add(node);
                    } else {
                        advancedGaps.add(node);
                    }
                }

                System.out.println("\n🔥 [MỨC ĐỘ: BẮT BUỘC] - Cần ưu tiên học ngay lập tức để làm được việc:");
                if (mandatoryGaps.isEmpty()) {
                    System.out.println("   (Bạn không khuyết kỹ năng nền tảng nào thuộc nhóm này!)");
                } else {
                    for (SkillNode node : mandatoryGaps) {
                        printMissingNodeWithResources(node);
                    }
                }


                System.out.println("\n⏱️ [MỨC ĐỘ: NÊN BIẾT] - Giúp tối ưu hiệu suất và tăng lợi thế cạnh tranh:");
                if (recommendedGaps.isEmpty()) {
                    System.out.println("   (Bạn không khuyết kỹ năng nào thuộc nhóm này!)");
                } else {
                    for (SkillNode node : recommendedGaps) {
                        printMissingNodeWithResources(node);
                    }
                }

                System.out.println("\n🚀 [MỨC ĐỘ: NÂNG CAO] - Phục vụ định hướng chuyên sâu / tối ưu kiến trúc:");
                if (advancedGaps.isEmpty()) {
                    System.out.println("   (Bạn không khuyết kỹ năng nâng cao nào!)");
                } else {
                    for (SkillNode node : advancedGaps) {
                        printMissingNodeWithResources(node);
                    }
                }
            }

            // TỔNG KẾT ĐÁNH GIÁ CHUNG
            System.out.println("\n--------------------------------------------------------------------------------");
            int currentMatchPercent = ((totalSkills - missingSkillsCount) * 100) / totalSkills;
            System.out.println("📊 ĐÁNH GIÁ CHUNG: Độ tương thích năng lực hiện tại của bạn đạt: " + currentMatchPercent + "%");
            System.out.println(">> Hệ thống đã ghi nhận. Hãy bám sát các đề xuất trên để hoàn thiện lộ trình!");
            System.out.println("=========================================================================================");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Hàm trợ lý in ra tên kỹ năng thiếu và lấy tài liệu lộ trình tương ứng từ DB lên
     */
    private void printMissingNodeWithResources(SkillNode node) {
        System.out.println("   - Thiếu kỹ năng: '" + node.getSkillName() + "'");
        if (node.getResources() != null && !node.getResources().isEmpty()) {
            System.out.println("     💡 Đề xuất tài liệu lộ trình:");
            for (LearningResource res : node.getResources()) {
                System.out.println("        🔹 [" + res.getResourceType() + "] " + res.getTitle() + " -> " + res.getUrl());
            }
        } else {
            System.out.println("     💡 Đề xuất: Chưa cấu hình link học, bạn có thể chủ động tra cứu tài liệu.");
        }
    }
    private void flattenNodes(List<SkillNode> nodes, List<SkillNode> flatList) {
        if (nodes == null) return;
        for (SkillNode node : nodes) {
            flatList.add(node); 
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                flattenNodes(node.getChildren(), flatList);
            }
        }
    }


    @Override
    public void printFullSkillTree(Long studentId) {
        EntityManager em = emf.createEntityManager();
        try {
            StudentProfileDAO studentDAO = new StudentProfileDAO(em);
            StudentProfile student = studentDAO.findById(studentId);

            if (student == null || student.getTargetPath() == null || !student.getTargetPath().getIsActive()) {
                System.out.println("\n[Cảnh báo]: Bạn chưa chọn mục tiêu nghề nghiệp hoặc lộ trình đã bị ẩn!");
                System.out.println(">> Vui lòng chọn tính năng số 2 trước.");
                return;
            }

            TechPath path = student.getTargetPath();
            System.out.println("\n============ LỘ TRÌNH CHI TIẾT: " + path.getPathName().toUpperCase() + " ============");

            SkillNodeDAO skillNodeDAO = new SkillNodeDAO(em);
            StudentSkillProgressDAO progressDAO = new StudentSkillProgressDAO(em);

            List<SkillNode> rootNodes = skillNodeDAO.findRootNodesByPathId(path.getPathId());
            List<StudentSkillProgress> progressList = progressDAO.findByStudentId(student.getStudentId());
            
            Map<Long, String> statusMap = new HashMap<>();
            for (StudentSkillProgress p : progressList) {
                if (p.getNode() != null) statusMap.put(p.getNode().getNodeId(), p.getStatus());
            }

            int total = countAllNodes(rootNodes);
            int completed = countCompletedNodes(rootNodes, statusMap);
            int percent = total == 0 ? 0 : (completed * 100 / total);
            int filled = percent / 10;

            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < 10; i++) bar.append(i < filled ? "█" : "░");
            System.out.println("Tiến độ tổng thể: [" + bar + "] " + percent + "%");
            System.out.println("--------------------------------------------------------------");

            int index = 1;
            for (SkillNode root : rootNodes) {
                printNodeRecursive(root, index, 0, statusMap);
                index++;
            }
            System.out.println("==============================================================");
        } finally {
            em.close();
        }
    }

    private void printNodeRecursive(SkillNode node, int index, int level, Map<Long, String> statusMap) {
        String status = statusMap.getOrDefault(node.getNodeId(), "NOT_STARTED");
        String mark = "NOT_STARTED".equals(status) ? "[ ]" : ("LEARNING".equals(status) ? "[▶]" : "[X]");

        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < level; i++) prefix.append("   ");
        String numbering = level == 0 ? index + ". " : "└── ";

        System.out.println(prefix.toString() + numbering + mark + " KỸ NĂNG: " + node.getSkillName() 
                + " (" + priorityToText(node.getPriorityLevel()) + ") - TRẠNG THÁI: " + statusToText(status));

        if (node.getResources() != null) {
            for (LearningResource res : node.getResources()) {
                System.out.println(prefix.toString() + "     🔹 [" + res.getResourceType() + "] " + res.getTitle() + " -> " + res.getUrl());
            }
        }

        if (node.getChildren() != null) {
            int childIndex = 1;
            for (SkillNode child : node.getChildren()) {
                printNodeRecursive(child, childIndex, level + 1, statusMap);
                childIndex++;
            }
        }
    }

    private int countAllNodes(List<SkillNode> nodes) {
        int count = 0;
        if (nodes == null) return 0;
        for (SkillNode n : nodes) {
            count++;
            if (n.getChildren() != null) count += countAllNodes(n.getChildren());
        }
        return count;
    }

    private int countCompletedNodes(List<SkillNode> nodes, Map<Long, String> statusMap) {
        int count = 0;
        if (nodes == null) return 0;
        for (SkillNode n : nodes) {
            if ("COMPLETED".equals(statusMap.getOrDefault(n.getNodeId(), "NOT_STARTED"))) count++;
            if (n.getChildren() != null) count += countCompletedNodes(n.getChildren(), statusMap);
        }
        return count;
    }

    private String priorityToText(Integer level) {
        if (level == null) return "Tùy chọn";
        return level == 1 ? "Bắt buộc" : (level == 2 ? "Nên biết" : "Nâng cao");
    }

    private String statusToText(String status) {
        if ("COMPLETED".equals(status)) return "HOÀN THÀNH";
        if ("LEARNING".equals(status)) return "ĐANG HỌC";
        return "CHƯA HỌC";
    }
}