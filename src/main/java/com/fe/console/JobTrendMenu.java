package com.fe.console;

import java.util.Scanner;

import com.fe.service.JobTrendService;

public class JobTrendMenu {

	private JobTrendService jobTrendService = new JobTrendService();
	private Scanner scanner = new Scanner(System.in);

	public void show() {
		while (true) {
			System.out.println("\n===== KHẢO SÁT THỊ TRƯỜNG TUYỂN DỤNG (JOB TREND) =====");
			System.out.println("1. Xem thống kê xu hướng công nghệ");
			System.out.println("0. Quay lại Menu chính");
			System.out.print("Lựa chọn của bạn (0-1): ");

			String choice = scanner.nextLine();

			if (choice.equals("1")) {
				jobTrendService.showJobTrend();
			} else if (choice.equals("0")) {
				return;
			} else {
				System.out.println(">> Lựa chọn không hợp lệ!");
			}
		}
	}
}