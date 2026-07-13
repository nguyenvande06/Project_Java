# Project_Java
# 🚀 EduPathAI: Personalized Career Orientation & Learning Roadmap Platform

**EduPathAI** là một nền tảng định hướng nghề nghiệp và xây dựng lộ trình học tập cá nhân hóa dành riêng cho sinh viên ngành Kỹ thuật Phần mềm (Software Engineering). Hệ thống giải quyết bài toán định hướng sớm bằng cách giúp sinh viên chuyển từ "Generalist" (biết mỗi thứ một ít) sang "Specialist" (chuyên gia một lĩnh vực như DevOps, Data Engineer, Cloud Architect,...) ngay từ năm 2 hoặc năm 3 để sẵn sàng cho các kỳ thực tập chất lượng cao.

---

## 🌟 Tính năng cốt lõi (Core Features)

### 1. 🧠 AI Virtual Mentor (Trợ lý Ảo AI)
- Hỗ trợ giải đáp thắc mắc về lộ trình nghề nghiệp qua giao diện Chat tự nhiên.
- Tích hợp **LLM APIs (Gemini/GPT-4)** để đưa ra các lời khuyên chuyên môn sâu sắc.
- Tự động phân tích dữ liệu từ **bảng điểm (Transcript)** và **hồ sơ GitHub công khai** do người dùng tải lên để cá nhân hóa câu trả lời.

### 2. 🗺️ Dynamic Roadmap (Cây lộ trình động)
- Cho phép lựa chọn **Target Career Role** mong muốn (DevOps, Mobile Dev, Full-stack,...).
- Tự động sinh ra cây kỹ năng phân cấp (**Hierarchical Skill Tree**) trực quan theo thứ tự ưu tiên học tập.
- Mỗi nút kỹ năng cung cấp ít nhất **2 nguồn tài liệu chất lượng** (YouTube, Official Documentation).
- Cập nhật tiến độ học tập theo thời gian thực (Real-time Progress tracking) khi người dùng tích chọn "Completed".

### 3. 📊 Skill Gap Analysis (Phân tích khoảng cách kỹ năng)
- Cho phép người dùng chọn nhanh các kỹ năng hiện có từ danh sách định nghĩa sẵn.
- Đối chiếu (Mapping analysis) kỹ năng hiện tại với yêu cầu của vị trí mục tiêu.
- Xuất báo cáo trực quan hoặc **file PDF** chỉ ra các lỗ hổng kiến thức kèm danh sách ưu tiên học tập khẩn cấp.

### 4. 📈 Market Pulse (Nhịp đập thị trường)
- Hệ thống cào dữ liệu tự động hàng ngày (**Daily Scheduled Scraper**) từ các cổng tuyển dụng lớn như LinkedIn, TopCV.
- Phân tích tần suất từ khóa (**Keyword Frequency Analysis**) trong bản mô tả công việc (JD) để tìm ra các công nghệ đang là xu hướng.
- Hiển thị biểu đồ tương tác xu hướng tăng trưởng hoặc sụt giảm nhu cầu của các kỹ năng IT.

### 5. 💼 E-Portfolio Management (Quản lý Hồ sơ Điện tử)
- Đồng bộ hóa danh sách các Repository công khai từ tài khoản GitHub cá nhân.
- Ứng dụng AI để tự động trích xuất, tóm tắt mục tiêu dự án và tech-stack từ file **README.md** của từng Repo.
- Tạo một **Unique Shareable URL** cho E-Portfolio cá nhân để sinh viên gửi trực tiếp tới nhà tuyển dụng.

---

## 💻 Công nghệ sử dụng (Tech Stack)

Hệ thống được thiết kế và phát triển dựa trên nền tảng công nghệ vững chắc:

- **Backend Enterprise:** Java, Spring MVC, Hibernate/JPA
- **Quản lý dự án & Thư viện:** Maven (`pom.xml`)
- **Giao diện người dùng:** JSP (JavaServer Pages), JSTL, CSS, JavaScript
- **Database & Caching:** SQL Server / MySQL (Kết nối qua JPA), Redis
- **AI Integration:** OpenAI API / Gemini API (Tích hợp tại lớp AI Service)

---

## 🛠️ Hướng dẫn cài đặt & Khởi chạy (Local Deployment)

### Yêu cầu hệ thống
- **Java Development Kit (JDK):** JDK 8 hoặc JDK 11/17 (Tùy thuộc vào cấu hình Maven của bạn)
- **Apache Tomcat:** Phiên bản 9.0 trở lên (Dành cho ứng dụng Web Java)
- **Cơ sở dữ liệu:** SQL Server hoặc MySQL
- **IDE hỗ trợ:** Eclipse (Đã cấu hình sẵn file `.project` và `.classpath`) hoặc IntelliJ IDEA.
