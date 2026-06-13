public class TestDriver {
	public static void main(String[] args) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("OK - Driver loaded!");
		} catch (ClassNotFoundException e) {
			System.out.println("FAIL - Driver NOT found: " + e.getMessage());
		}
	}
}