package com.fe.service.ai;

import java.util.List;
import com.fe.pojo.ChatMessage;
import com.fe.pojo.MentorSession;

public interface IServiceAI {
    
    List<MentorSession> getSessions(Long studentId);
    
    MentorSession createSession(Long studentId, String firstMessage);
    
    List<ChatMessage> getMessages(Long sessionId);
    
    String sendMessage(MentorSession session, String userText);
}