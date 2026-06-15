package com.fe.console;

import java.util.List;
import java.util.Scanner;

import com.fe.pojo.StudentProfile;
import com.fe.pojo.TechPath;
import com.fe.service.CareerPathService;

public class CareerPathMenu {

	private CareerPathService careerPathService = new CareerPathService();
	private Scanner scanner = new Scanner(System.in);

	public void show(StudentProfile student) {
		while (true) {
			System.out.println("\n===== CHỌN MỤC TIÊU NGHỀ NGHIỆP & XEM LỘ TRÌNH =====");
			System.out.println("1. Chọn / Đổi lộ trình nghề nghiệp (Tech Path)");
			System.out.println("2. Xem Lộ trình cá nhân hóa (Skill Tree)");
			System.out.println("0. Quay lại Menu chính");
			System.out.print("Lựa chọn của bạn (0-2): ");

			String choice = scanner.nextLine();

			if (choice.equals("1")) {
				chooseTechPath(student);
			} else if (choice.equals("2")) {
				careerPathService.printSkillTree(student);
			} else if (choice.equals("0")) {
				return;
			} else {
				System.out.println(">> Lựa chọn không hợp lệ!");
			}
		}
	}

	private void chooseTechPath(StudentProfile student) {
		List<TechPath> paths = careerPathService.getAllActivePaths();

		if (paths.isEmpty()) {
			System.out.println(">> Hiện chưa có lộ trình nào trong hệ thống.");
			return;
		}

		System.out.println("\n===== DANH SÁCH LỘ TRÌNH NGHỀ NGHIỆP =====");
		for (int i = 0; i < paths.size(); i++) {
			System.out.println((i + 1) + ". " + paths.get(i).getPathName());
		}
		System.out.print("Chọn vị trí (số thứ tự): ");

		try {
			int idx = Integer.parseInt(scanner.nextLine()) - 1;
			if (idx >= 0 && idx < paths.size()) {
				TechPath selected = paths.get(idx);
				careerPathService.setTargetPath(student.getStudentId(), selected.getPathId());
			} else {
				System.out.println(">> Lựa chọn không hợp lệ!");
			}
		} catch (NumberFormatException e) {
			System.out.println(">> Vui lòng nhập số!");
		}
	}
}