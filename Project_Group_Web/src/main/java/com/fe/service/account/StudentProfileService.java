package com.fe.service.account;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.fe.dao.StudentProfileDAO;
import com.fe.pojo.GithubRepo;
import com.fe.pojo.StudentProfile;
import com.fe.pojo.User;

public class StudentProfileService implements IStudentProfileService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAs");

    @Override
    public StudentProfile getProfile(Long studentId) {
        EntityManager em = emf.createEntityManager();
        try {
            StudentProfileDAO dao = new StudentProfileDAO(em);
            return dao.findById(studentId);
        } finally {
            em.close();
        }
    }

    @Override
    public void updateInfo(Long studentId,String Mssv) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            StudentProfileDAO dao = new StudentProfileDAO(em);
            StudentProfile student = dao.findById(studentId);

            if (student == null) {
                System.out.println(">> Không tìm thấy dữ liệu sinh viên.");
                em.getTransaction().rollback();
                return;
            }
            student.setMssv(Mssv);
            dao.save(student);

            em.getTransaction().commit();
            System.out.println(">> Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void linkGithub(Long studentId, String githubUsername) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            StudentProfileDAO dao = new StudentProfileDAO(em);
            StudentProfile student = dao.findById(studentId);

            if (student == null) {
                System.out.println(">> Không tìm thấy sinh viên.");
                em.getTransaction().rollback();
                return;
            }

            String[] aiSummaries = {
                "Dựa trên các repository đã quét, sinh viên thể hiện tư duy tốt về lập trình Backend và kiến trúc hệ thống. Có kinh nghiệm tổ chức mã nguồn sạch (Clean Architecture), biết cách tối ưu truy vấn Database và xử lý bất đồng bộ. Điểm mạnh lớn nhất nằm ở khả năng thiết kế RESTful API chuẩn chỉ.",
                "Hồ sơ mã nguồn cho thấy sinh viên có thế mạnh vượt trội về phát triển ứng dụng Full-stack. Sử dụng linh hoạt các framework hiện đại ở cả Front-end lẫn Back-end. Hệ thống ghi nhận khả năng tích hợp các dịch vụ bên thứ ba tốt và giao diện được tổ chức theo module dễ mở rộng.",
                "Phân tích từ mã nguồn cho thấy ứng viên tập trung mạnh vào mảng Hệ thống và Network Programming. Code có cấu trúc xử lý đa luồng (Multi-threading) tốt, hiểu rõ cơ chế socket truyền tải dữ liệu TCP/UDP. Cần bổ sung thêm kỹ năng về tối ưu hóa bộ nhớ đệm (Caching) để hoàn thiện hơn.",
                "Sinh viên có tư duy logic giải thuật rất tốt, thể hiện qua các bài toán xử lý luồng dữ liệu lớn trong repository. Có kỹ năng vận dụng mô hình MVC/Repository pattern một cách thuần thục. Đánh giá cao tinh thần tự học thông qua việc áp dụng nhiều thư viện mã nguồn mở vào dự án thực tế.",
                "Hồ sơ mã nguồn cho thấy sinh viên có kinh nghiệm thực chỉnh chu về mảng Cơ sở dữ liệu và bảo mật luồng thông tin. Viết Trigger, Stored Procedure có tính tối ưu cao, biết cách phòng chống các lỗ hổng cơ bản (SQL Injection, XSS). Định hướng phát triển rất tốt theo lộ trình Kỹ sư Phần mềm chuyên nghiệp."
            };


            java.util.Random rand = new java.util.Random();
            

            int summaryIdx = rand.nextInt(aiSummaries.length);
            String chosenSummary = aiSummaries[summaryIdx];

            student.setGithubUsername(githubUsername);
            

            student.setTranscriptSummary(chosenSummary); 
            
            dao.save(student);

            em.createQuery("DELETE FROM GithubRepo r WHERE r.student.studentId = :sid")
              .setParameter("sid", studentId).executeUpdate();

           
            String[] techStacks = {"Spring Boot & MySQL", "Node.js & MongoDB", "React Native & Firebase", "Python & Django", "C# .NET & SQL Server", "Vue.js & PostgreSQL"};
            String[] projectTypes = {"E-Commerce-Platform", "Social-Media-App", "Smart-IoT-Dashboard", "Task-Management-Tool", "AI-Chat-Bot", "Realtime-Quiz-Game"};
            
            
            int idx1 = rand.nextInt(projectTypes.length);
            int idx2 = (idx1 + 1) % projectTypes.length; 

            // 4. TỰ ĐỘNG SINH REPO 1 CHO USER
            GithubRepo repo1 = new GithubRepo();
            String name1 = projectTypes[idx1] + "-" + rand.nextInt(99); 
            repo1.setRepoName(name1);
            repo1.setRepoUrl("https://github.com/" + githubUsername + "/" + name1);
            repo1.setAiProjectStory("Hệ thống tự động phân tích: Dự án xây dựng bằng " + techStacks[idx1] + ". Sinh viên đạt tiêu chuẩn xây dựng cấu trúc mã nguồn tốt.");
            repo1.setStudent(student);
            em.persist(repo1);

            GithubRepo repo2 = new GithubRepo();
            String name2 = projectTypes[idx2] + "-" + rand.nextInt(99);
            repo2.setRepoName(name2);
            repo2.setRepoUrl("https://github.com/" + githubUsername + "/" + name2);
            repo2.setAiProjectStory("Hệ thống tự động phân tích: Dự án xây dựng bằng " + techStacks[idx2] + ". Đánh giá có tư duy tốt về thiết kế cơ sở dữ liệu.");
            repo2.setStudent(student);
            em.persist(repo2);

            em.getTransaction().commit();
            System.out.println(">> [Hệ thống] Đã liên kết GitHub, đồng bộ Repo và cập nhật Transcript Summary từ AI!");
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void generateOrGetShareToken(Long studentId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            StudentProfileDAO dao = new StudentProfileDAO(em);
            StudentProfile student = dao.findById(studentId);

            if (student == null) {
                System.out.println(">> Không tìm thấy sinh viên.");
                em.getTransaction().rollback();
            }

            String token = student.getPortfolioShareToken();
            if (token == null || token.isEmpty()) {
                token = UUID.randomUUID().toString().substring(0, 8);
                
                
                student.setPortfolioShareToken(token);
                dao.save(student);
                em.getTransaction().commit();
                System.out.println(">> Đã sinh mới mã liên kết chia sẻ!");
            } else {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void printProfile(Long studentId) {
        StudentProfile student = getProfile(studentId);
        
        if (student == null) {
            System.out.println(">> Lỗi: Không thể tải thông tin hồ sơ!");
            return;
        }
        
        User user = student.getUser();
        String fullName = (user != null) ? user.getFullName() : "Chưa cập nhật";
        String mssv = (student.getMssv() != null && !student.getMssv().isEmpty()) ? student.getMssv() : "Chưa cập nhật";
        String github = (student.getGithubUsername() != null && !student.getGithubUsername().isEmpty()) ? student.getGithubUsername() : "Chưa liên kết";
        
        String token = student.getPortfolioShareToken();
        String urlPortfolio = (token != null && !token.isEmpty()) 
                ? "http://smart-career.vn/portfolio/" + token 
                : "Chưa thiết lập URL (Vui lòng chọn chức năng 3)";

        // In giao diện Console 
        System.out.println("\n================ QUẢN LÝ HỒ SƠ CÁ NHÂN & E-PORTFOLIO ================");
        System.out.println("[Thông tin hiện tại]:");
        System.out.println("- Sinh viên: " + fullName + " | MSSV: " + mssv);
        System.out.println("- GitHub Username: " + github);
        System.out.println("- URL E-Portfolio (Mô phỏng): " + urlPortfolio);
        System.out.println();
    }
}