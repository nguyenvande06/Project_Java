package com.fe.service.ai;

import java.util.List;

public interface IAIServiceProvider {
    // Nhận vào danh sách lịch sử (dạng text thuần) và câu hỏi mới, trả về kết quả text từ AI
    String getAIResponse(List<String> chatHistory, String currentPrompt);
}