package com.fe.console;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import com.fe.service.JobTrendService;

public class JobTrendMenu {

	private JobTrendService jobTrendService = new JobTrendService();
	private Scanner scanner = new Scanner(System.in);

	public void show() {
		while (true) {
			List<LocalDate> dates = jobTrendService.getAvailableDates();

			System.out.println("\n======================= KHẢO SÁT THỊ TRƯỜNG TUYỂN DỤNG =======================");

			if (dates.isEmpty()) {
				System.out.println(">> Chưa có dữ liệu xu hướng thị trường.");
				System.out.println("0. Quay lại Menu chính");
				System.out.print("Lựa chọn: ");
				if (scanner.nextLine().equals("0"))
					return;
				continue;
			}

			System.out.println("📊 HỆ THỐNG ĐANG CÓ DỮ LIỆU CỦA CÁC ĐỢT QUÉT SAU:");
			for (int i = 0; i < dates.size(); i++) {
				String label = i == dates.size() - 1 ? " (Mốc đối chiếu xu hướng)" : "";
				System.out.println("   [" + (i + 1) + "] Đợt quét ngày: " + dates.get(i) + label);
			}
			System.out.println("   [0] Quay lại Menu chính");
			System.out.print("\n👉 Chọn đợt quét bạn muốn xem (0-" + dates.size() + "): ");

			try {
				int choice = Integer.parseInt(scanner.nextLine().trim());

				if (choice == 0)
					return;

				if (choice < 1 || choice > dates.size()) {
					System.out.println(">> Lựa chọn không hợp lệ!");
					continue;
				}

				LocalDate selectedDate = dates.get(choice - 1);
				LocalDate benchmarkDate = dates.size() > 1 ? dates.get(dates.size() - 1) : null;

				if (selectedDate.equals(benchmarkDate))
					benchmarkDate = null;

				jobTrendService.showTrendByDate(selectedDate, benchmarkDate);

				System.out.print("\nNhấn Enter để quay lại...");
				scanner.nextLine();

			} catch (NumberFormatException e) {
				System.out.println(">> Vui lòng nhập số!");
			}
		}
	}
}