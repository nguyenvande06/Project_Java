package com.fe.console;

import java.util.List;
import java.util.Scanner;

import com.fe.pojo.MentorSession;
import com.fe.pojo.StudentProfile;
import com.fe.service.AIMentorService;

public class AIMentorMenu {

	private AIMentorService mentorService = new AIMentorService();
	private Scanner scanner = new Scanner(System.in);

	public void show(StudentProfile student) {
		while (true) {
			List<MentorSession> sessions = mentorService.getSessions(student.getStudentId());

			System.out.println("\n======== TRỢ LÝ ẢO AI VIRTUAL MENTOR ============");
			System.out.println("📜 LỊCH SỬ CÁC PHIÊN TƯ VẤN CỦA BẠN:");

			for (int i = 0; i < sessions.size(); i++) {
				MentorSession s = sessions.get(i);
				System.out.printf("   [%d] %s (Ngày: %s)%n", i + 1, s.getTitle(), s.getCreatedAt().toLocalDate());
			}

			int newSessionIdx = sessions.size() + 1;
			System.out.println("   [" + newSessionIdx + "] Tạo cuộc hội thoại MỚI hoàn toàn");
			System.out.println("   [0] Quay lại Menu chính");
			System.out.print("\n👉 Vui lòng chọn phiên chat (0-" + newSessionIdx + "): ");

			try {
				int choice = Integer.parseInt(scanner.nextLine().trim());

				if (choice == 0)
					return;

				if (choice == newSessionIdx) {
					// Tạo phiên chat mới
					startNewSession(student);
				} else if (choice >= 1 && choice <= sessions.size()) {
					// Tiếp tục phiên cũ
					continueSession(sessions.get(choice - 1));
				} else {
					System.out.println(">> Lựa chọn không hợp lệ!");
				}

			} catch (NumberFormatException e) {
				System.out.println(">> Vui lòng nhập số!");
			}
		}
	}

	private void startNewSession(StudentProfile student) {
		System.out.println("\n📌 [Hướng dẫn]: Gõ 'exit' để dừng và lưu cuộc trò chuyện.");
		System.out.print("\n💬 Sinh viên: ");
		String firstMessage = scanner.nextLine().trim();

		if (firstMessage.equalsIgnoreCase("exit") || firstMessage.isEmpty())
			return;

		// Tạo session mới
		MentorSession session = mentorService.createSession(student.getStudentId(), firstMessage);
		if (session == null) {
			System.out.println(">> Lỗi tạo phiên chat!");
			return;
		}

		System.out.println("\n------------------- [NỘI DUNG CUỘC TRÒ CHUYỆN] -------------------");
		System.out.println("👨‍🎓 Sinh viên: " + firstMessage);
		System.out.print("🤖 Virtual Mentor: ");
		String response = mentorService.sendMessage(session, firstMessage);
		System.out.println(response);

		// Tiếp tục chat
		chatLoop(session);
	}

	private void continueSession(MentorSession session) {
		System.out.println("\n📌 [Hướng dẫn]: Gõ 'exit' để dừng và lưu cuộc trò chuyện.");
		System.out.println("\n⚙️ [Hệ thống]: Đã khôi phục thành công ngữ cảnh lịch sử cũ.");
		System.out.println("👋 Mời bạn tiếp tục trò chuyện với Mentor!");
		System.out.println("\n------------------- [NỘI DUNG CUỘC TRÒ CHUYỆN] -------------------");

		// Hiển thị lịch sử
		mentorService.getMessages(session.getSessionId()).forEach(msg -> {
			if (msg.getSenderType().equals("USER")) {
				System.out.println("👨‍🎓 Sinh viên: " + msg.getMessageText());
			} else {
				System.out.println("🤖 Virtual Mentor: " + msg.getMessageText());
			}
		});

		chatLoop(session);
	}

	private void chatLoop(MentorSession session) {
		while (true) {
			System.out.print("\n💬 Sinh viên: ");
			String input = scanner.nextLine().trim();

			if (input.equalsIgnoreCase("exit")) {
				System.out.println("\n👉 Hệ thống đang lưu dữ liệu chat mới... Đang quay lại Menu chính.");
				System.out.println("===========================================================");
				return;
			}

			if (input.isEmpty())
				continue;

			System.out.print("🤖 Virtual Mentor: ");
			String response = mentorService.sendMessage(session, input);
			System.out.println(response);
		}
	}
}