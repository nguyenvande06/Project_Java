package com.fe.service.account;

import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.fe.dao.StudentProfileDAO;
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
    public void updateInfo(Long studentId, String newFullName, String newMssv) {
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

            User user = student.getUser();
            if (user != null) {
                user.setFullName(newFullName);
                em.merge(user); 
            }

    
            student.setMssv(newMssv);
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

            student.setGithubUsername(githubUsername);
            dao.save(student);

            em.getTransaction().commit();
            System.out.println(">> Đã liên kết GitHub: " + githubUsername);
            System.out.println(">> (Mô phỏng: AI đang quét README các repository để trích xuất tech stack...)");
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public String generateOrGetShareToken(Long studentId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            StudentProfileDAO dao = new StudentProfileDAO(em);
            StudentProfile student = dao.findById(studentId);

            if (student == null) {
                System.out.println(">> Không tìm thấy sinh viên.");
                em.getTransaction().rollback();
                return null;
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

            return token;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            return null;
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