package com.fe.console;

import java.util.Scanner;

import com.fe.pojo.StudentProfile;
import com.fe.service.SkillGapService;

public class SkillGapMenu {

	private SkillGapService skillGapService = new SkillGapService();
	private Scanner scanner = new Scanner(System.in);

	public void show(StudentProfile student) {
		while (true) {
			System.out.println("\n===== PHÂN TÍCH KHOẢNG CÁCH KỸ NĂNG (SKILL GAP) =====");
			System.out.println("1. Chạy phân tích & Xuất báo cáo");
			System.out.println("0. Quay lại Menu chính");
			System.out.print("Lựa chọn của bạn (0-1): ");

			String choice = scanner.nextLine();

			if (choice.equals("1")) {
				skillGapService.analyzeSkillGap(student);
			} else if (choice.equals("0")) {
				return;
			} else {
				System.out.println(">> Lựa chọn không hợp lệ!");
			}
		}
	}
}