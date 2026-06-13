import console.CareerPathMenu;
import dao.StudentProfileDAO;
import pojo.StudentProfile;

public class Main {
	public static void main(String[] args) {
		StudentProfileDAO studentDAO = new StudentProfileDAO();
		StudentProfile student = studentDAO.getById(1L);

		if (student == null) {
			System.out.println("Chưa có StudentProfile với ID = 1. Hãy thêm data mẫu vào DB.");
			return;
		}

		CareerPathMenu menu = new CareerPathMenu();
		menu.show(student);
	}
}