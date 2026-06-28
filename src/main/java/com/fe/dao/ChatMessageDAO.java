package com.fe.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.fe.dao.impl.GenericDAOImpl;
import com.fe.pojo.ChatMessage;

public class ChatMessageDAO extends GenericDAOImpl<ChatMessage, Long> {

	public ChatMessageDAO(EntityManager em) {
		super(em, ChatMessage.class);
	}

	/**
	 * Lấy toàn bộ tin nhắn thuộc một phiên chat (Sắp xếp theo thứ tự thời gian gửi)
	 */
	public List<ChatMessage> findBySessionId(Long sessionId) {
		return em.createQuery("SELECT c FROM ChatMessage c WHERE c.session.sessionId = :sid ORDER BY c.sentAt ASC",
				ChatMessage.class).setParameter("sid", sessionId).getResultList();
	}
}