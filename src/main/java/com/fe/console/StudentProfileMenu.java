package com.fe.console;

import java.util.Scanner;

import com.fe.pojo.StudentProfile;
import com.fe.service.StudentProfileService;

public class StudentProfileMenu {

	private StudentProfileService profileService = new StudentProfileService();
	private Scanner scanner = new Scanner(System.in);

	public void show(StudentProfile studentInput) {
		while (true) {
			StudentProfile student = profileService.getProfile(studentInput.getStudentId());

			System.out.println("\n========== QUẢN LÝ HỒ SƠ CÁ NHÂN & E-PORTFOLIO ===========");
			System.out.println("[Thông tin hiện tại]:");
			System.out.println("- MSSV: " + (student.getMssv() != null ? student.getMssv() : "(chưa có)"));
			System.out.println("- GitHub Username: "
					+ (student.getGithubUsername() != null ? student.getGithubUsername() : "(chưa liên kết)"));

			String token = student.getPortfolioShareToken();
			String portfolioUrl = (token != null && !token.isEmpty()) ? "http://smart-career.vn/portfolio/" + token
					: "(chưa tạo)";
			System.out.println("- URL E-Portfolio: " + portfolioUrl);

			System.out.println("\nLỰA CHỌN CHỨC NĂNG:");
			System.out.println("1. Cập nhật thông tin cá nhân (MSSV)");
			System.out.println("2. Liên kết GitHub (Đồng bộ Repo & Kích hoạt AI trích xuất README)");
			System.out.println("3. Sinh mã liên kết chia sẻ E-Portfolio (URL)");
			System.out.println("0. Quay lại Menu chính");
			System.out.println("=============================================================");
			System.out.print("Lựa chọn của bạn (0-3): ");

			String choice = scanner.nextLine();

			if (choice.equals("1")) {
				updatePersonalInfo(student);
			} else if (choice.equals("2")) {
				linkGithub(student);
			} else if (choice.equals("3")) {
				generateShareLink(student);
			} else if (choice.equals("0")) {
				return;
			} else {
				System.out.println(">> Lựa chọn không hợp lệ!");
			}
		}
	}

	private void updatePersonalInfo(StudentProfile student) {
		System.out.print("Nhập MSSV mới: ");
		String mssv = scanner.nextLine().trim();

		if (mssv.isEmpty()) {
			System.out.println(">> MSSV không được để trống!");
			return;
		}

		profileService.updateMssv(student.getStudentId(), mssv);
	}

	private void linkGithub(StudentProfile student) {
		System.out.print("Nhập GitHub Username: ");
		String githubUsername = scanner.nextLine().trim();

		if (githubUsername.isEmpty()) {
			System.out.println(">> GitHub Username không được để trống!");
			return;
		}

		profileService.linkGithub(student.getStudentId(), githubUsername);
	}

	private void generateShareLink(StudentProfile student) {
		String token = profileService.generateOrGetShareToken(student.getStudentId());

		if (token != null) {
			System.out.println(">> URL E-Portfolio của bạn: http://smart-career.vn/portfolio/" + token);
		}
	}
}
