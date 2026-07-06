package com.project_java.web.Project_Group_Web.controller;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fe.pojo.StudentProfile;
import com.fe.pojo.User;
import com.fe.service.account.IStudentProfileService;
import com.fe.service.account.StudentProfileService;
import com.fe.service.aut.AuthService;
import com.fe.service.aut.IAuthService;

@Controller
public class HomeController {
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAs");
	IAuthService authService = new AuthService();
	IStudentProfileService studentprofile = new StudentProfileService();
	@RequestMapping(value="/")
	public ModelAndView test(HttpServletResponse response) throws IOException{
		return new ModelAndView("login");
	}
	// Xử lý khi bấm nút "Đăng nhập"
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ModelAndView handleLogin(
            @RequestParam("email") String email, 
            @RequestParam("password") String password,
            HttpServletRequest request) {
        
        ModelAndView mav = new ModelAndView();
        
        User user = authService.loginLocal(email, password);

 
        if (user != null) {
            // Nếu ĐÚNG: Lưu thông tin vào Session để hiển thị lên giao diện (Tên, MSSV...)
            StudentProfile profile = studentprofile.getProfile(user.getUserId());
            request.getSession().setAttribute("currentUser", user.getFullName());
            request.getSession().setAttribute("Mssv", profile.getMssv());
            String token = "http://smart-career.vn/portfolio/"+ profile.getPortfolioShareToken();
            request.getSession().setAttribute("Portfolio", token);
            request.getSession().setAttribute("userObj", user);
            request.getSession().setAttribute("github", profile.getGithubUsername());
            
            javax.persistence.EntityManager em = emf.createEntityManager();
            try {
                String jpql = "SELECT t.pathName FROM TechPath t WHERE t.isActive = true";
                java.util.List<String> techPathNames = em.createQuery(jpql, String.class).getResultList();
                

                request.getSession().setAttribute("lstTechPaths", techPathNames);
                System.out.println(">> [Login] Đã nạp thành công " + techPathNames.size() + " mục tiêu vào Session.");
            } catch (Exception e) {
                System.out.println(">> [Login Lỗi DB]: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }

            // Chuyển hướng thẳng sang trang home
            mav.setViewName("home"); 
        } else {
            // Nếu SAI: Quay lại trang login kèm theo một thông báo lỗi
            mav.setViewName("login");
            mav.addObject("error", "Tài khoản hoặc mật khẩu không chính xác!");
        }
        
        return mav;
    }
    @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView showRegisterPage() {
        return new ModelAndView("register"); // Mở file register.jsp
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ModelAndView handleRegister(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email, 
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register"); 

        if (!password.equals(confirmPassword)) {
            mav.addObject("error", "Mật khẩu nhập lại không trùng khớp!");
            return mav;
        }
        
        
        boolean isSuccess = authService.registerLocal(fullName, email, password);
        
        if (isSuccess) {
           
            mav.setViewName("login");
            mav.addObject("message", "Đăng ký tài khoản thành công! Mời bạn đăng nhập.");
        } else {
            
            mav.addObject("error", "Email này đã được sử dụng trong hệ thống!");
        }
        
        return mav;
    }
    @RequestMapping(value = "/api/profile/updateMssv", method = RequestMethod.POST)
    @org.springframework.web.bind.annotation.ResponseBody
    public String updateStudentMssv(@RequestParam("mssv") String mssv, HttpServletRequest request) {
        HttpSession session = request.getSession();
        com.fe.pojo.User currentUser = (com.fe.pojo.User) session.getAttribute("userObj");
        
        if (currentUser == null) {
            return "error_auth";
        }

        try {
            studentprofile.updateInfo(currentUser.getUserId(), mssv);
            
            session.setAttribute("Mssv", mssv);
            
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    @RequestMapping(value = "/api/profile/generateToken", method = RequestMethod.POST)
    @org.springframework.web.bind.annotation.ResponseBody
    public String generatePortfolioToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        
        
        com.fe.pojo.User currentUser = (com.fe.pojo.User) session.getAttribute("userObj");
        
        if (currentUser == null) {
            return "error_auth";
        }

        try {
            
            studentprofile.generateOrGetShareToken(currentUser.getUserId());
            
            
            com.fe.pojo.StudentProfile updatedProfile = studentprofile.getProfile(currentUser.getUserId());
            
          
            String token = updatedProfile.getPortfolioShareToken();
            
            
            session.setAttribute("Portfolio", token);
            
            
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    @RequestMapping(value = "/api/profile/linkGithub", method = RequestMethod.POST)
    @org.springframework.web.bind.annotation.ResponseBody
    public String linkStudentGithub(@RequestParam("github") String githubUsername, HttpServletRequest request) {
        HttpSession session = request.getSession();
        com.fe.pojo.User currentUser = (com.fe.pojo.User) session.getAttribute("userObj");
        
        if (currentUser == null) {
            return "error_auth";
        }

        try {
            studentprofile.linkGithub(currentUser.getUserId(), githubUsername);
            session.setAttribute("Github", githubUsername);
            
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    
}
