package com.fe.service.aut;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.stereotype.Service;

import com.fe.dao.StudentProfileDAO;
import com.fe.dao.UserDAO;
import com.fe.pojo.StudentProfile;
import com.fe.pojo.User;

@Service
public class AuthService implements IAuthService {
	
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAs");


    @Override
    public boolean registerLocal(String fullName, String email, String password) {
        EntityManager em = emf.createEntityManager();
        UserDAO userDAO = new UserDAO(em); 
        StudentProfileDAO studentProfileDAO = new StudentProfileDAO(em);
        
        try {
            em.getTransaction().begin();

            
            if (userDAO.findByEmail(email) != null) {
                System.out.println("[Lỗi]: Email này đã được đăng ký trên hệ thống!");
                return false;
            }

            IHashPassword passwordHasher = new HashPassword();
            
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            
            // Băm trước khi lưu mật khẩu
            String hashedPassword = passwordHasher.hash(password);
            user.setPasswordHash(hashedPassword);
            user.setAuthProvider("LOCAL");
            user.setRole("STUDENT");
            userDAO.save(user); 
            StudentProfile profile = new StudentProfile();
            profile.setUser(user);
            profile.setMssv("");
            profile.setGithubUsername("");
            profile.setTranscriptSummary("");
            profile.setPortfolioShareToken("");
            

            studentProfileDAO.save(profile); 

            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    @Override
    public boolean registerGoogleMock(String fullName, String email) {
        EntityManager em = emf.createEntityManager();
        UserDAO userDAO = new UserDAO(em); 
        StudentProfileDAO studentProfileDAO = new StudentProfileDAO(em);
        
        try {
            em.getTransaction().begin();

            if (userDAO.findByEmail(email) != null) {
                System.out.println("[Hệ thống]: Tài khoản Google đã tồn tại. Tiến hành đăng nhập...");
                em.getTransaction().commit(); 
                return true;
            }

            // Đăng ký qua Google
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPasswordHash(null); 
            user.setAuthProvider("GOOGLE");
            user.setRole("STUDENT");
            
            userDAO.save(user);

            StudentProfile profile = new StudentProfile();
            profile.setUser(user);
            profile.setMssv("");
            profile.setGithubUsername("");
            profile.setTranscriptSummary("");
            profile.setPortfolioShareToken("");

            studentProfileDAO.save(profile); 
            em.getTransaction().commit();
            return true;
            
        } catch (Exception e) {
            // Nếu lỗi thì rollback lại để không bị rác DB
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            // 4. LUÔN LUÔN PHẢI ĐÓNG EM ĐỂ TRÁNH LỖI PHIÊN LÀM VIỆC
            em.close();
        }
    }
    // đăng nhập local
    @Override
    public User loginLocal(String email, String password) {
        EntityManager em = emf.createEntityManager();
        UserDAO userDAO = new UserDAO(em); 
        
        try {
        	IHashPassword passwordHasher = new HashPassword();
            User user = userDAO.findByEmail(email);
           
            if (user == null) {
                System.out.println("[Lỗi]: Tài khoản hoặc mật khẩu không chính xác!");
                return null;
            }
           
            if ("LOCAL".equals(user.getAuthProvider())) {
            	String inputHashed = passwordHasher.hash(password);
                if (user.getPasswordHash().equals(inputHashed)) {
                    return user; 
                } else {
                    System.out.println("[Lỗi]: Tài khoản hoặc mật khẩu không chính xác!");
                    return null;
                }
            } else {
                System.out.println("[Lỗi]: Tài khoản này đăng ký qua Google. Vui lòng chọn phương thức đăng nhập bằng Google!");
                return null;
            }
        } finally {
            em.close(); 
        }
    }

    // đăng nhập google
    @Override
    public User loginGoogleMock(String email) {
        EntityManager em = emf.createEntityManager();
        UserDAO userDAO = new UserDAO(em); 
        
        try {
            User user = userDAO.findByEmail(email);
            
            if (user == null) {
                System.out.println("[Lỗi]: Email Google này chưa được đăng ký trên hệ thống!");
                return null;
            }
            
            if ("GOOGLE".equals(user.getAuthProvider())) {
                return user; 
            } else {
                System.out.println("[Lỗi]: Email này thuộc tài khoản thông thường (Local). Vui lòng đăng nhập bằng mật khẩu!");
                return null;
            }
        } finally {
            em.close();
        }
    }

}
