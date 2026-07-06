package com.fe.service.ai;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.fe.dao.ChatMessageDAO;
import com.fe.dao.MentorSessionDAO;
import com.fe.dao.StudentProfileDAO;
import com.fe.pojo.ChatMessage;
import com.fe.pojo.MentorSession;
import com.fe.pojo.StudentProfile;

public class ServiceAI implements IServiceAI {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAs");
    
    private IAIServiceProvider aiProvider;

    public ServiceAI(IAIServiceProvider aiProvider) {
        this.aiProvider = aiProvider;
    }

    // 1. LẤY DANH SÁCH PHIÊN CHAT CŨ CỦA SINH VIÊN
    @Override
    public List<MentorSession> getSessions(Long studentId) {
        EntityManager em = emf.createEntityManager();
        try {
            MentorSessionDAO dao = new MentorSessionDAO(em);
            return dao.findByStudentId(studentId);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // 2. TẠO PHIÊN CHAT MỚI TỪ CÂU HỎI ĐẦU TIÊN
    @Override
    public MentorSession createSession(Long studentId, String firstMessage) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            StudentProfileDAO studentDAO = new StudentProfileDAO(em);
            StudentProfile student = studentDAO.findById(studentId);

            MentorSession session = new MentorSession();
            session.setStudent(student);
            
            // Cắt ngắn tiêu đề tối đa 50 ký tự cho gọn bảng dữ liệu
            String title = firstMessage.length() > 50 ? firstMessage.substring(0, 50) + "..." : firstMessage;
            session.setTitle(title);
            session.setCreatedAt(LocalDateTime.now());

            em.persist(session);
            em.getTransaction().commit();
            return session;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // 3. TẢI LẠI LỊCH SỬ TIN NHẮN CỦA MỘT PHIÊN CHAT
    @Override
    public List<ChatMessage> getMessages(Long sessionId) {
        EntityManager em = emf.createEntityManager();
        try {
            ChatMessageDAO dao = new ChatMessageDAO(em);
            return dao.findBySessionId(sessionId);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // 4. LUỒNG TRÒ CHUYỆN: ĐỌC LỊCH SỬ -> GỌI AI ĐỘC LẬP -> LƯU CẢ HAI TIN NHẮN VÀO DB
    @Override
    public String sendMessage(MentorSession session, String userText) {
        EntityManager em = emf.createEntityManager();
        try {
            ChatMessageDAO dao = new ChatMessageDAO(em);

            // 1. Đọc lại lịch sử hội thoại cũ làm bối cảnh cho AI
            List<ChatMessage> history = dao.findBySessionId(session.getSessionId());
            List<String> chatHistoryText = new ArrayList<>();
            
            for (ChatMessage msg : history) {
                String rolePrefix = "USER".equals(msg.getSenderType()) ? "user: " : "model: ";
                chatHistoryText.add(rolePrefix + msg.getMessageText());
            }

            // 2. GỌI ĐẾN HÀM getAIResponse CỦA BẠN ĐỂ LẤY CÂU TRẢ LỜI ĐÁP LẠI USER
            String aiResponse = ">> [AI Không phản hồi]";
            if (this.aiProvider != null) {
                // Truyền bối cảnh lịch sử + câu hỏi hiện tại từ bàn phím
                aiResponse = this.aiProvider.getAIResponse(chatHistoryText, userText);
            }

            // 3. Mở transaction đồng bộ lưu dữ liệu vào DB sau khi có kết quả
            em.getTransaction().begin();
            
            MentorSession mergedSession = em.merge(session);

            // Lưu tin nhắn của User vừa nhập
            ChatMessage userMsg = new ChatMessage();
            userMsg.setSession(mergedSession);
            userMsg.setSenderType("USER");
            userMsg.setMessageText(userText);
            userMsg.setSentAt(LocalDateTime.now());
            em.persist(userMsg);

            // Lưu phản hồi sinh ra từ hàm getAIResponse của AI
            ChatMessage aiMsg = new ChatMessage();
            aiMsg.setSession(mergedSession);
            aiMsg.setSenderType("AI");
            aiMsg.setMessageText(aiResponse);
            aiMsg.setSentAt(LocalDateTime.now());
            em.persist(aiMsg);

            em.getTransaction().commit();
            return aiResponse;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return ">> Lỗi kết nối hệ thống nghiệp vụ AI: " + e.getMessage();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}