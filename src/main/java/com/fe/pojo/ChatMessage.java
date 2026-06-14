package com.fe.pojo;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chat_message")
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	private Long messageId;

	@Column(name = "sender_type", length = 20)
	private String senderType;

	@Column(name = "message_text", columnDefinition = "TEXT")
	private String messageText;

	@Column(name = "sent_at")
	private LocalDateTime sentAt = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name = "session_id")
	private MentorSession session;


	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getSenderType() {
		return senderType;
	}

	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

	public MentorSession getSession() {
		return session;
	}

	public void setSession(MentorSession session) {
		this.session = session;
	}
}