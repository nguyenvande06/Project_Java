package com.fe.service.ai;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class AIServiceProvider implements IAIServiceProvider {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAs");

    @Override
    public String getAIResponse(List<String> chatHistory, String currentPrompt) {
    	
        String userQuestion = currentPrompt.toLowerCase().trim();
        System.out.println("\n=== [DEBUG AI] BẮT ĐẦU XỬ LÝ REQUEST ===");
        System.out.println("[DEBUG AI] Câu hỏi gốc của User: '" + currentPrompt + "'");
        System.out.println("[DEBUG AI] Câu hỏi sau chuẩn hóa (userQuestion): '" + userQuestion + "'");

        EntityManager em = emf.createEntityManager();
        
        try {
            // 2. Lấy toàn bộ danh sách kỹ năng từ Database
            List<Object[]> skillList = fetchAllSkillsFromDB(em);
            System.out.println("[DEBUG AI] Tổng số skill lấy được từ DB: " + (skillList != null ? skillList.size() : 0));

            if (skillList == null || skillList.isEmpty()) {
                System.out.println("[DEBUG AI] CẢNH BÁO: Không có dữ liệu nào trong bảng skill_node!");
            }

            // 3. Quét và so khớp từ khóa
            Object[] matchedSkill = findMatchingSkill(skillList, userQuestion);

            // 4. Nếu tìm thấy kỹ năng, tiến hành xây dựng câu trả lời lộ trình
            if (matchedSkill != null) {
                System.out.println("[DEBUG AI] => KẾT QUẢ: Đã khớp thành công với kỹ năng: " + matchedSkill[1]);
                return buildRouteResponse(em, matchedSkill);
            }

            // 5. Kịch bản mặc định khi không tìm thấy (foundSkill = false)
            System.out.println("[DEBUG AI] => KẾT QUẢ: Không tìm thấy skill nào khớp. Rơi vào kịch bản mặc định.");
            return handleDefaultScenarios(userQuestion, currentPrompt);

        } catch (Exception e) {
            System.out.println("[DEBUG AI] LỖI HỆ THỐNG: " + e.getMessage());
            e.printStackTrace();
            return "❌ [AI Error]: Không thể truy vấn tri thức hệ thống: " + e.getMessage();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
                System.out.println("[DEBUG AI] Đã đóng EntityManager.\n");
            }
        }
    }

    /**
     * Hàm 1: Truy vấn toàn bộ dữ liệu thô từ bảng skill_node
     */
    private List<Object[]> fetchAllSkillsFromDB(EntityManager em) {
        Query query = em.createNativeQuery("SELECT nodeId, skillName, priorityLevel, parent_node_id FROM skill_node");
        return query.getResultList();
    }

    /**
     * Hàm 2: Thuật toán chấm điểm và tính tỷ lệ phủ từ khóa (Keyword Coverage Percentage)
     * Đã đồng bộ lowercase tuyệt đối và chặn việc bắt nhầm skill thiếu từ khóa quan trọng.
     */
    private Object[] findMatchingSkill(List<Object[]> skillList, String userQuestion) {
        if (skillList == null || skillList.isEmpty()) return null;

        // 1. Danh sách từ đệm tiếng Việt cần lọc
        Set<String> stopWords = new HashSet<>(Arrays.asList(
            "tôi", "muốn", "biết", "về", "cho", "xin", "học", "môn", "kỹ", "năng", "đi", "tìm", "hiểu", "là", "gì", "với"
        ));

        // 2. Làm sạch và đưa câu hỏi của user về LOWERCASE hoàn toàn
        String cleanQuestion = userQuestion.toLowerCase().trim();
        for (String word : stopWords) {
            cleanQuestion = cleanQuestion.replaceAll("\\b" + word + "\\b", " ");
        }
        cleanQuestion = cleanQuestion.replaceAll("\\s+", " ").trim();
        
        System.out.println("[DEBUG AI SMART] Câu hỏi sau khi lọc từ đệm & lowercase: '" + cleanQuestion + "'");

        // TÌNH HUỐNG 1: Khớp trọn vẹn cụm từ lớn (Tuyệt đối hoặc chứa nhau)
        Object[] bestMatchSkill = null;
        int maxMatchLength = 0;
        
        for (Object[] skill : skillList) {
            if (skill[1] == null) continue;
            String skillNameLower = skill[1].toString().toLowerCase().trim();

            // So sánh bằng lowercase hai chiều
            if (userQuestion.toLowerCase().contains(skillNameLower) || skillNameLower.contains(cleanQuestion)) {
                if (skillNameLower.length() > maxMatchLength) {
                    maxMatchLength = skillNameLower.length();
                    bestMatchSkill = skill;
                }
            }
        }

        if (bestMatchSkill != null) {
            System.out.println("[DEBUG AI SMART] -> Khớp cụm từ thành công: [" + bestMatchSkill[1] + "]");
            return bestMatchSkill;
        }

        // TÌNH HUỐNG 2: Chấm điểm dựa trên tỷ lệ phần trăm từ khóa xuất hiện
        System.out.println("[DEBUG AI SMART] Không khớp cụm từ lớn. Đang tính tỷ lệ phủ Keywords...");
        String[] keywords = cleanQuestion.split(" ");
        int totalKeywords = 0;
        
        // Đếm số từ khóa thực sự có nghĩa (bỏ qua từ quá ngắn như ký tự thừa)
        for (String kw : keywords) {
            if (kw.trim().length() > 1) {
                totalKeywords++;
            }
        }

        Object[] highestScoringSkill = null;
        int maxScore = 0;

        for (Object[] skill : skillList) {
            if (skill[1] == null) continue;
            String skillNameLower = skill[1].toString().toLowerCase().trim();
            
            int currentScore = 0;
            for (String kw : keywords) {
                String kwTrimmed = kw.trim();
                if (kwTrimmed.length() <= 1) continue;
                
                // So khớp bằng lowercase độc lập từng từ
                if (skillNameLower.contains(kwTrimmed)) {
                    currentScore++;
                }
            }

            if (currentScore > maxScore) {
                maxScore = currentScore;
                highestScoringSkill = skill;
            }
        }

        // THUẬT TOÁN ĐIỀU KIỆN CHẶN THÔNG MINH:
        // Tính toán xem từ khóa khớp được chiếm bao nhiêu % trong tổng số từ user hỏi
        double coveragePercentage = 0.0;
        if (totalKeywords > 0) {
            coveragePercentage = (double) maxScore / totalKeywords;
        }

        System.out.println("[DEBUG AI SMART] Kết quả điểm cao nhất: " + maxScore + "/" + totalKeywords + " từ khóa. Tỷ lệ phủ: " + (coveragePercentage * 100) + "%");

        // Chỉ chấp nhận kết quả nếu tỷ lệ phủ từ khóa đạt từ 70% trở lên
        // Ví dụ: "lập trình web" (2 từ), nếu chỉ khớp "lập trình" (1/2 = 50%) -> Sẽ bị loại ngay, chuyển xuống hàm gợi ý chung chung.
        if (coveragePercentage >= 0.70 && highestScoringSkill != null) { 
            System.out.println("[DEBUG AI SMART] -> Độ tin cậy cao (>=70%). Khớp thành công: [" + highestScoringSkill[1] + "]");
            return highestScoringSkill;
        }

        System.out.println("[DEBUG AI SMART] -> Độ tin cậy thấp hoặc từ khóa bị lệch. Trả về null để chuyển sang kịch bản gợi ý danh sách.");
        return null;
    }

    /**
     * Hàm 3: Xây dựng chuỗi câu trả lời (Lộ trình, môn tiên quyết, môn con)
     */
    private String buildRouteResponse(EntityManager em, Object[] skill) {
        StringBuilder aiReply = new StringBuilder();
        
        String skillId = skill[0].toString();
        String skillName = skill[1].toString();
        String priority = skill[2] != null ? skill[2].toString() : "1";
        String parentId = skill[3] != null ? skill[3].toString() : null;

        aiReply.append("🤖 [AI Advisor]: Tôi thấy bạn đang quan tâm đến kỹ năng **").append(skillName).append("**.\n");
        aiReply.append("- Mức độ ưu tiên học tập (Priority Level): Cấp độ ").append(priority).append("\n");
        
        // Truy vấn môn tiên quyết (Parent)
        if (parentId != null) {
            System.out.println("[DEBUG AI ROUTE] Skill này có môn tiên quyết (parent_node_id = " + parentId + "). Đang truy vấn...");
            Query parentQuery = em.createNativeQuery("SELECT skillName FROM skill_node WHERE nodeId = ?");
            parentQuery.setParameter(1, skill[3]); 
            List<String> parentNameList = parentQuery.getResultList();
            
            if (parentNameList != null && !parentNameList.isEmpty()) {
                aiReply.append("- ⚠️ Môn tiên quyết cần học trước: Bạn nên hoàn thành môn **")
                       .append(parentNameList.get(0))
                       .append("** trước khi học môn này nhé.\n");
            }
        } else {
            aiReply.append("- 💡 Đây là kỹ năng nền tảng (Root Node), bạn có thể bắt đầu học ngay lập tức!\n");
        }
        
        // Truy vấn các môn kế thừa (Children)
        System.out.println("[DEBUG AI ROUTE] Đang tìm các môn con phụ thuộc vào nodeId = " + skillId + "...");
        Query childQuery = em.createNativeQuery("SELECT skillName FROM skill_node WHERE parent_node_id = ?");
        childQuery.setParameter(1, skill[0]); 
        List<String> children = childQuery.getResultList();
        
        if (children != null && !children.isEmpty()) {
            aiReply.append("- 🚀 Hướng phát triển tiếp theo sau khi học xong: ");
            for (int i = 0; i < children.size(); i++) {
                aiReply.append("**").append(children.get(i)).append("**");
                if (i < children.size() - 1) aiReply.append(", ");
            }
            aiReply.append("\n");
        }
        
        return aiReply.toString();
    }

    /**
     * Hàm 4: Xử lý kịch bản mặc định khi không nhận diện được từ khóa
     */
    
    private String handleDefaultScenarios(String userQuestion, String currentPrompt) {
    	userQuestion = userQuestion.toLowerCase().trim();
        if (userQuestion.contains("xin chào") || userQuestion.contains("hello") || userQuestion.contains("chào bạn")) {
            return "🤖 Chào bạn! Tôi là Trợ lý AI lộ trình học tập nội bộ của bạn. Bạn muốn hỏi về môn học hay kỹ năng nào trong hệ thống?";
        }
        if (userQuestion.contains("danh sách") || userQuestion.contains("môn học") || userQuestion.contains("kỹ năng")) {
            return "🤖 Hiện tại hệ thống đang quản lý các nhóm kỹ năng cốt lõi. Hãy gõ tên kỹ năng bạn muốn hỏi (Ví dụ: C# Advanced, Lập trình Python, Kiến trúc phần mềm, Học máy...) để tôi phân tích lộ trình nhé!";
        }
        
        StringBuilder suggestReply = new StringBuilder();
        suggestReply.append("🤖 Xin lỗi, tôi chưa tìm thấy lộ trình chính xác cho câu hỏi \"").append(currentPrompt).append("\".\n");
        
        // Tự khởi tạo EntityManager nội bộ để truy vấn danh sách gợi ý
        EntityManager emSuggest = null;
        try {
            emSuggest = emf.createEntityManager();
            Query query = emSuggest.createNativeQuery("SELECT skillName FROM skill_node");
            List<String> allSkillNames = query.getResultList();
            
            Set<String> suggestions = new HashSet<>();
            String[] keywords = userQuestion.split(" ");
            
            // Quét tìm các kỹ năng có từ khóa liên quan
            for (String name : allSkillNames) {
                if (name == null) continue;
                for (String kw : keywords) {
                    if (kw.length() > 2 && name.toLowerCase().contains(kw)) {
                        suggestions.add(name);
                    }
                }
            }
            
            // Nếu tìm thấy các kỹ năng có tên tương tự
            if (!suggestions.isEmpty()) {
                suggestReply.append("💡 Hệ thống tìm thấy một số kỹ năng có từ khóa gần giống, bạn có đang tìm hiểu về cái nào không:\n");
                for (String sga : suggestions) {
                    suggestReply.append("- **").append(sga).append("**\n");
                }
                suggestReply.append("\nBạn hãy gõ chính xác lại tên môn học phía trên để tôi tra cứu môn tiên quyết nhé!");
                return suggestReply.toString();
            }
            
        } catch (Exception e) {
            System.out.println("[DEBUG AI SUGGEST] Lỗi khi tạo gợi ý từ DB: " + e.getMessage());
        } finally {
            // Luôn giải phóng kết nối nội bộ sau khi dùng xong
            if (emSuggest != null && emSuggest.isOpen()) {
                emSuggest.close();
                System.out.println("[DEBUG AI SUGGEST] Đã đóng EntityManager nội bộ của hàm gợi ý.");
            }
        }

        // Kịch bản cuối cùng nếu không tìm ra từ nào liên quan trong DB
        return "🤖 Xin lỗi, hệ thống chưa có dữ liệu về kỹ năng này. Bạn có thể gõ chính xác các tên môn học cốt lõi (Ví dụ: C#, Python, Cấu trúc dữ liệu, Lập trình Web...) để tôi hỗ trợ tra cứu nhé!";
    }
}