package com.fe.dao;

import javax.persistence.EntityManager;
import java.util.List;
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
		return em.createQuery("SELECT c FROM ChatMessage c WHERE c.session.sessionId = :sessionId ORDER BY c.sentAt ASC", ChatMessage.class)
				 .setParameter("sessionId", sessionId)
				 .getResultList();
	}
}