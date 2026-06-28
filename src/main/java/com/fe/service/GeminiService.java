package com.fe.service;

import java.util.List;

import org.json.JSONObject;

public class GeminiService {

	public String chat(List<JSONObject> history, String userMessage) {
		String msg = userMessage.toLowerCase().trim();

		// Chào hỏi
		if (msg.contains("xin chào") || msg.contains("hello") || msg.contains("hi") || msg.contains("chào")) {
			return "Xin chào! Tôi là AI Mentor, sẵn sàng tư vấn định hướng nghề nghiệp IT cho bạn. "
					+ "Bạn muốn hỏi về lộ trình học tập, kỹ năng cần thiết, hay xu hướng thị trường?";
		}

		// Backend
		if (msg.contains("backend") || msg.contains("java") || msg.contains("spring")) {
			return "🎯 Lộ trình Backend Developer (Java):\n" + "   1. Java Core (OOP, Collections, Exception)\n"
					+ "   2. SQL & Database (SQL Server, PostgreSQL)\n"
					+ "   3. Spring Boot (REST API, JPA/Hibernate)\n" + "   4. Git & Docker\n"
					+ "   5. Kiến trúc Microservices\n"
					+ "💡 Thị trường đang cần nhiều Backend Java — đây là lựa chọn tốt!";
		}

		// Frontend
		if (msg.contains("frontend") || msg.contains("react") || msg.contains("javascript") || msg.contains("html")) {
			return "🎯 Lộ trình Frontend Developer:\n" + "   1. HTML, CSS, JavaScript cơ bản\n"
					+ "   2. ReactJS hoặc VueJS\n" + "   3. TypeScript\n" + "   4. REST API integration\n"
					+ "   5. Testing (Jest)\n" + "💡 ReactJS đang chiếm ~60% thị phần frontend tại Việt Nam!";
		}

		// DevOps
		if (msg.contains("devops") || msg.contains("docker") || msg.contains("kubernetes") || msg.contains("ci/cd")) {
			return "🎯 Lộ trình DevOps Engineer:\n" + "   1. Linux cơ bản\n" + "   2. Docker & Docker Compose\n"
					+ "   3. CI/CD (Jenkins, GitHub Actions)\n" + "   4. Kubernetes\n" + "   5. Cloud (AWS, Azure)\n"
					+ "💡 DevOps đang thiếu nhân lực trầm trọng, lương cao!";
		}

		// AI/ML
		if (msg.contains("ai") || msg.contains("machine learning") || msg.contains("python") || msg.contains("data")) {
			return "🎯 Lộ trình AI/Data Engineer:\n" + "   1. Python (NumPy, Pandas)\n"
					+ "   2. Machine Learning cơ bản (Scikit-learn)\n" + "   3. Deep Learning (TensorFlow, PyTorch)\n"
					+ "   4. SQL & Data Warehouse\n" + "   5. MLOps\n"
					+ "💡 AI/ML là ngành hot nhất 2024-2025, nhưng cần nền tảng Toán tốt!";
		}

		// Kỹ năng mềm
		if (msg.contains("kỹ năng") || msg.contains("skill") || msg.contains("mềm") || msg.contains("tiếng anh")) {
			return "💼 Kỹ năng quan trọng ngoài lập trình:\n" + "   ✅ Tiếng Anh (đọc tài liệu, giao tiếp)\n"
					+ "   ✅ Git (quản lý code, làm việc nhóm)\n" + "   ✅ Đọc hiểu documentation\n"
					+ "   ✅ Problem-solving & Debug\n" + "   ✅ Communication trong team\n"
					+ "💡 Tiếng Anh tốt giúp tăng lương 20-30%!";
		}

		// Thực tập/việc làm
		if (msg.contains("thực tập") || msg.contains("intern") || msg.contains("việc làm") || msg.contains("tuyển dụng")
				|| msg.contains("lương")) {
			return "💼 Thông tin tuyển dụng IT 2026:\n" + "   📊 Fresher Backend Java: 8-12 triệu/tháng\n"
					+ "   📊 Fresher Frontend React: 8-12 triệu/tháng\n" + "   📊 Junior DevOps: 15-20 triệu/tháng\n"
					+ "   🏢 Công ty tuyển nhiều: FPT, VNG, Shopee, Tiki, Momo\n"
					+ "💡 Có project thực tế trên GitHub giúp tăng cơ hội 50%!";
		}

		// Portfolio/GitHub
		if (msg.contains("portfolio") || msg.contains("github") || msg.contains("project") || msg.contains("dự án")) {
			return "📁 Xây dựng Portfolio hiệu quả:\n" + "   1. Tạo GitHub profile đẹp (README.md)\n"
					+ "   2. Làm 2-3 project thực tế (không phải bài tập)\n"
					+ "   3. Deploy project lên cloud (Heroku, Railway)\n"
					+ "   4. Viết README.md chi tiết cho mỗi project\n"
					+ "   5. Contribute vào Open Source nếu có thể\n"
					+ "💡 Nhà tuyển dụng sẽ xem GitHub của bạn trước khi phỏng vấn!";
		}

		// Phỏng vấn
		if (msg.contains("phỏng vấn") || msg.contains("interview") || msg.contains("câu hỏi")) {
			return "🎯 Chuẩn bị phỏng vấn IT:\n" + "   📚 Technical: OOP, Data Structure, Algorithm, DB\n"
					+ "   📚 Java: JVM, GC, Collections, Multithreading\n"
					+ "   📚 Spring Boot: IoC, DI, REST API, JPA\n"
					+ "   💬 Soft skill: Giới thiệu bản thân, xử lý áp lực\n"
					+ "   🔥 LeetCode Easy/Medium (30 bài là đủ cho fresher)\n"
					+ "💡 Luyện nói to khi giải thuật toán — interviewer muốn biết tư duy!";
		}

		// Năm 1-2
		if (msg.contains("năm 1") || msg.contains("năm 2") || msg.contains("mới vào") || msg.contains("bắt đầu")) {
			return "🌱 Lộ trình cho sinh viên năm 1-2:\n"
					+ "   ✅ Học chắc nền tảng: Lập trình C/Java, CTDL & GT, CSDL\n" + "   ✅ Học Git ngay từ đầu\n"
					+ "   ✅ Chọn 1 ngôn ngữ và đào sâu (đừng học lan man)\n" + "   ✅ Tham gia CLB lập trình ở trường\n"
					+ "   ✅ Làm quen với Linux cơ bản\n"
					+ "💡 Đừng học quá nhiều thứ cùng lúc — hãy học 1 thứ đến nơi đến chốn!";
		}

		// Năm 3-4
		if (msg.contains("năm 3") || msg.contains("năm 4") || msg.contains("sắp ra trường")
				|| msg.contains("tốt nghiệp")) {
			return "🎓 Lộ trình cho sinh viên năm 3-4:\n" + "   ✅ Chọn hướng chuyên sâu (Backend/Frontend/DevOps/AI)\n"
					+ "   ✅ Làm ít nhất 1 project thực tế hoàn chỉnh\n" + "   ✅ Xin thực tập sớm (học kỳ 6 trở đi)\n"
					+ "   ✅ Build GitHub profile chắc chắn\n" + "   ✅ Luyện LeetCode + chuẩn bị phỏng vấn\n"
					+ "💡 Thực tập sớm = kinh nghiệm thực tế = lợi thế lớn khi ra trường!";
		}

		// Toán
		if (msg.contains("toán") || msg.contains("giải tích") || msg.contains("đại số") || msg.contains("xác suất")) {
			return "📐 Toán trong IT quan trọng như thế nào?\n" + "   🔹 Backend/Web: Không cần Toán cao cấp nhiều\n"
					+ "   🔹 AI/ML: Cần Đại số tuyến tính, Giải tích, Xác suất thống kê\n"
					+ "   🔹 Game Dev: Cần Hình học, Vector, Ma trận\n"
					+ "   🔹 Security: Cần Toán rời rạc, Lý thuyết số\n"
					+ "💡 Chọn hướng phù hợp với thế mạnh của bạn!";
		}

		// Câu hỏi về hệ thống này
		if (msg.contains("hệ thống") || msg.contains("app") || msg.contains("ứng dụng này")) {
			return "ℹ️ Đây là hệ thống 'Personalized Career Orientation & Learning Roadmap Platform'\n"
					+ "   📌 Giúp sinh viên IT định hướng nghề nghiệp cá nhân hóa\n"
					+ "   📌 Xem lộ trình kỹ năng (Skill Tree) theo mục tiêu\n"
					+ "   📌 Phân tích khoảng cách kỹ năng (Skill Gap)\n" + "   📌 Xem xu hướng thị trường tuyển dụng\n"
					+ "   📌 Tư vấn định hướng qua AI Mentor (đây là tôi 😊)";
		}

		// Tạm biệt
		if (msg.contains("bye") || msg.contains("tạm biệt") || msg.contains("cảm ơn") || msg.contains("thank")) {
			return "Cảm ơn bạn đã trò chuyện! Chúc bạn học tập hiệu quả và sớm đạt được mục tiêu nghề nghiệp. "
					+ "Hẹn gặp lại! 👋";
		}

		// Câu hỏi toán/logic đơn giản
		if (msg.contains("1+1") || msg.contains("2+2") || msg.contains("bao nhiêu")) {
			return "😄 Tôi là AI Mentor chuyên về định hướng nghề nghiệp IT, "
					+ "không phải máy tính! Bạn có câu hỏi nào về lộ trình học tập hoặc nghề nghiệp IT không?";
		}

		// Mặc định
		return "🤔 Tôi hiểu bạn đang hỏi về: \"" + userMessage + "\"\n\n" + "Tôi có thể tư vấn về:\n"
				+ "   💻 Lộ trình Backend, Frontend, DevOps, AI/ML\n" + "   📚 Kỹ năng cần học theo từng giai đoạn\n"
				+ "   💼 Thông tin thực tập và việc làm IT\n" + "   📁 Xây dựng Portfolio và GitHub\n"
				+ "   🎯 Chuẩn bị phỏng vấn\n\n" + "Bạn muốn hỏi về chủ đề nào?";
	}
}