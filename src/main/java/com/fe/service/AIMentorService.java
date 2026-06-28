package com.fe.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fe.dao.ChatMessageDAO;
import com.fe.dao.MentorSessionDAO;
import com.fe.dao.StudentProfileDAO;
import com.fe.pojo.ChatMessage;
import com.fe.pojo.MentorSession;
import com.fe.pojo.StudentProfile;
import com.fe.util.JPAUtil;

public class AIMentorService {

	private GeminiService geminiService = new GeminiService();

	// Lấy danh sách phiên chat của sinh viên
	public List<MentorSession> getSessions(Long studentId) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			MentorSessionDAO dao = new MentorSessionDAO(em);
			return dao.findByStudentId(studentId);
		} finally {
			em.close();
		}
	}

	// Tạo phiên chat mới
	public MentorSession createSession(Long studentId, String firstMessage) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			StudentProfileDAO studentDAO = new StudentProfileDAO(em);
			StudentProfile student = studentDAO.findById(studentId);

			MentorSession session = new MentorSession();
			session.setStudent(student);
			// Tự động đặt tiêu đề từ câu hỏi đầu tiên (tối đa 50 ký tự)
			String title = firstMessage.length() > 50 ? firstMessage.substring(0, 50) + "..." : firstMessage;
			session.setTitle(title);
			session.setCreatedAt(LocalDateTime.now());

			em.persist(session);
			em.getTransaction().commit();
			return session;
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	// Lấy lịch sử tin nhắn của 1 phiên
	public List<ChatMessage> getMessages(Long sessionId) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			ChatMessageDAO dao = new ChatMessageDAO(em);
			return dao.findBySessionId(sessionId);
		} finally {
			em.close();
		}
	}

	// Gửi tin nhắn + nhận phản hồi AI
	// Gửi tin nhắn + nhận phản hồi AI
	public String sendMessage(MentorSession session, String userText) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			ChatMessageDAO dao = new ChatMessageDAO(em);

			// 1. Lấy lịch sử ĐANG CÓ TRƯỚC ĐÓ để gửi kèm cho Gemini (Chưa bao gồm tin nhắn
			// mới)
			List<ChatMessage> history = dao.findBySessionId(session.getSessionId());
			List<JSONObject> geminiHistory = new ArrayList<>();
			for (ChatMessage msg : history) {
				JSONObject geminiMsg = new JSONObject();
				geminiMsg.put("role", msg.getSenderType().equals("USER") ? "user" : "model");
				JSONArray parts = new JSONArray();
				parts.put(new JSONObject().put("text", msg.getMessageText()));
				geminiMsg.put("parts", parts);
				geminiHistory.add(geminiMsg);
			}

			// 2. Gọi Gemini API ngay với ngữ cảnh sạch (Tin nhắn mới truyền riêng qua tham
			// số thứ 2)
			String aiResponse = geminiService.chat(geminiHistory, userText);

			// 3. Sau khi có kết quả từ AI, mở một Transaction duy nhất để lưu CẢ HAI tin
			// nhắn vào DB
			em.getTransaction().begin();
			MentorSession mergedSession = em.merge(session);

			// Lưu tin nhắn của User
			ChatMessage userMsg = new ChatMessage();
			userMsg.setSession(mergedSession);
			userMsg.setSenderType("USER");
			userMsg.setMessageText(userText);
			userMsg.setSentAt(LocalDateTime.now());
			em.persist(userMsg);

			// Lưu phản hồi của AI (chỉ lưu nếu API trả về không lỗi)
			ChatMessage aiMsg = new ChatMessage();
			aiMsg.setSession(mergedSession);
			aiMsg.setSenderType("AI");
			aiMsg.setMessageText(aiResponse);
			aiMsg.setSentAt(LocalDateTime.now());
			em.persist(aiMsg);

			em.getTransaction().commit();

			return aiResponse;

		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
			return ">> Lỗi hệ thống: " + e.getMessage();
		} finally {
			em.close();
		}
	}
}