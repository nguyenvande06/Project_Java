package com.project_java.web.Project_Group_Web.controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fe.pojo.SkillNode;
import com.fe.pojo.StudentProfile;
import com.fe.pojo.StudentSkillProgress;
import com.fe.pojo.User;
import com.fe.service.account.IStudentProfileService;
import com.fe.service.account.StudentProfileService;
import com.fe.service.ai.AIServiceProvider;
import com.fe.service.ai.IAIServiceProvider;
import com.fe.service.aut.AuthService;
import com.fe.service.aut.IAuthService;



@Controller
public class HomeController {
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAs");
	IAuthService authService = new AuthService();
	IStudentProfileService studentprofile = new StudentProfileService();
	IAIServiceProvider aiServiceProvider = new AIServiceProvider();
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
        	// Làm việc với session ở đây
        	
            StudentProfile profile = studentprofile.getProfile(user.getUserId());
            request.getSession().setAttribute("currentUser", user.getFullName());
            request.getSession().setAttribute("Mssv", profile.getMssv());
            String token = "http://smart-career.vn/portfolio/"+ profile.getPortfolioShareToken();
            request.getSession().setAttribute("Portfolio", token);
            request.getSession().setAttribute("userObj", user);
            request.getSession().setAttribute("github", profile.getGithubUsername());
            
            String userTarget = "Chưa cập nhật";
            
            // Kiểm tra an toàn: Đảm bảo profile không null VÀ đối tượng liên kết TechPath không null
            if (profile != null && profile.getTargetPath() != null) {
                userTarget = profile.getTargetPath().getPathName(); 
            }
            request.getSession().setAttribute("JobRole", userTarget);
            
            javax.persistence.EntityManager em = emf.createEntityManager();
            try {
                String jpql = "SELECT t.pathName FROM TechPath t WHERE t.isActive = true";
                java.util.List<String> techPathNames = em.createQuery(jpql, String.class).getResultList();
                
                request.getSession().setAttribute("lstTechPaths", techPathNames);
                System.out.println(">> [Login] Đã nạp thành công " + techPathNames.size() + " mục tiêu vào Session.");

                // ... [Đoạn code JPQL lấy techPathNames và thiết lập Session cũ giữ nguyên] ...

                if (profile != null && profile.getTargetPath() != null) {
                    Long currentPathId = profile.getTargetPath().getPathId();

                    String hqlAllNodes = "SELECT sn FROM SkillNode sn WHERE sn.path.pathId = :pathId";
                    
                    java.util.List<com.fe.pojo.SkillNode> allNodes = em.createQuery(hqlAllNodes, com.fe.pojo.SkillNode.class)
                            .setParameter("pathId", currentPathId)
                            .getResultList();
                    
                    request.getSession().setAttribute("leafSkillNodes", allNodes);
                    System.out.println(">> [SkillGap] Đã nạp thành công " + allNodes.size() + " TOÀN BỘ kỹ năng vào Session.");

                    // 🌟 THÊM NGAY TẠI ĐÂY: Khởi tạo DAO tiến độ và lưu vào Session
                    com.fe.dao.StudentSkillProgressDAO progressDAO = new com.fe.dao.StudentSkillProgressDAO(em);
                    java.util.List<com.fe.pojo.StudentSkillProgress> userProgressList = progressDAO.findByStudentId(profile.getStudentId());
                    
                    // Đẩy danh sách tiến độ thực tế từ DB này vào Session để bên JSP bốc ra dùng
                    request.getSession().setAttribute("userProgressList", userProgressList);
                    System.out.println(">> [Roadmap] Đã nạp thành công " + userProgressList.size() + " bản ghi tiến độ vào Session.");

                } else {
                    request.getSession().setAttribute("leafSkillNodes", new java.util.ArrayList<com.fe.pojo.SkillNode>());
                    // Thêm list rỗng phòng hờ lỗi null
                    request.getSession().setAttribute("userProgressList", new java.util.ArrayList<com.fe.pojo.StudentSkillProgress>());
                }

                
            } catch (Exception e) {
                System.out.println(">> [Login/SkillGap Lỗi DB]: " + e.getMessage());
                e.printStackTrace();
                request.getSession().setAttribute("leafSkillNodes", new java.util.ArrayList<com.fe.pojo.SkillNode>());
            } finally {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }
            
            //

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
    @RequestMapping(value = "/api/profile/updateTarget", method = RequestMethod.POST)
    @org.springframework.web.bind.annotation.ResponseBody 
    public String updateStudentTarget(@RequestParam("selectedTarget") String selectedTarget, HttpServletRequest request) {
        HttpSession session = request.getSession();
        com.fe.pojo.User currentUser = (com.fe.pojo.User) session.getAttribute("userObj");

        if (currentUser == null) {
            return "error_auth";
        }

        javax.persistence.EntityManager em = emf.createEntityManager();
        javax.persistence.EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            com.fe.pojo.StudentProfile student = studentprofile.getProfile(currentUser.getUserId());

            if (student != null) {
                String jpqlTech = "SELECT t FROM TechPath t WHERE t.pathName = :name AND t.isActive = true";
                com.fe.pojo.TechPath targetPath = em.createQuery(jpqlTech, com.fe.pojo.TechPath.class)
                        .setParameter("name", selectedTarget)
                        .getSingleResult();

                if (targetPath != null) {
                    student.setTargetPath(targetPath); 

                    em.merge(student);
                    trans.commit();

                    session.setAttribute("JobRole", selectedTarget);

                    System.out.println(">> [Cập nhật thành công] Sinh viên ID " + currentUser.getUserId() + " đã đổi mục tiêu sang: " + selectedTarget);
                    return "success";
                }
            }

            return "error";
        } catch (Exception e) {
            if (trans != null && trans.isActive()) {
                trans.rollback(); 
            }
            System.err.println(">> [Lỗi cập nhật mục tiêu nghề nghiệp]: " + e.getMessage());
            e.printStackTrace();
            return "error";
        } finally {
            if (em != null && em.isOpen()) {
                em.close(); 
            }
        }
    }
    // trở về login 
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView handleLogout(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.invalidate(); 
        }
        
        mav.setViewName("login");
        
        mav.addObject("success_msg", "Bạn đã đăng xuất khỏi hệ thống thành công!");
        
        return mav;
    }
    //
    @RequestMapping(value = "/load-gap-resources", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody 
    public String handleLoadGapResources(
            @RequestParam(value = "missingIds[]", required = false) java.util.List<Long> missingIds, 
            HttpServletRequest request) {
        
        if (missingIds == null || missingIds.isEmpty()) {
            return "[]";
        }
        
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        javax.persistence.EntityManager em = null; 
        try {
            em = emf.createEntityManager(); 
            com.fe.dao.LearningResourceDAO resourceDAO = new com.fe.dao.LearningResourceDAO(em);
            
            System.out.println(">> [SkillGap API] Đang xử lý yêu cầu tìm tài liệu cho ID: " + missingIds);
            boolean isFirst = true;
            
            for (Long nodeId : missingIds) {
                java.util.List<com.fe.pojo.LearningResource> resources = resourceDAO.findBySkillNode(nodeId);
                
                if (resources != null) {
                    for (com.fe.pojo.LearningResource res : resources) {
                        if (!isFirst) {
                            json.append(",");
                        }
                        json.append("{");
                        json.append("\"nodeId\":").append(nodeId).append(",");
                        json.append("\"title\":\"").append(res.getTitle().replace("\"", "\\\"")).append("\",");
                        json.append("\"url\":\"").append(res.getUrl().replace("\"", "\\\"")).append("\",");
                        json.append("\"resourceType\":\"").append(res.getResourceType().replace("\"", "\\\"")).append("\"");
                        json.append("}");
                        isFirst = false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(">> [SkillGap API Lỗi]: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        
        json.append("]");
        System.out.println(">> [SkillGap API] Chuỗi dữ liệu xuất ra thành công: " + json.toString());
        return json.toString();
    }
    
    @RequestMapping(value = "/save-skill-progress", method = RequestMethod.POST)
    @ResponseBody
    public String saveSkillProgress(HttpServletRequest request) {
        // 1. Nhận danh sách các ID kỹ năng ĐÃ BIẾT từ Front-end gửi lên
        String[] completedIdsStr = request.getParameterValues("completedIds[]");
        
        // Chuyển mảng String sang Set<Long> để tối ưu hóa việc kiểm tra trong vòng lặp
        java.util.Set<Long> completedIdsSet = new java.util.HashSet<>();
        if (completedIdsStr != null) {
            for (String idStr : completedIdsStr) {
                completedIdsSet.add(Long.parseLong(idStr));
            }
        }

        HttpSession session = request.getSession();
        com.fe.pojo.User currentUser = (com.fe.pojo.User) session.getAttribute("userObj");
        
        if (currentUser == null) {
            return "error_auth";
        }

        javax.persistence.EntityManager em = emf.createEntityManager();

        try {
            // Lấy thông tin Profile thông qua Service bằng UserId
            StudentProfile student = studentprofile.getProfile(currentUser.getUserId());
            if (student == null) {
                return "Student profile not found.";
            }

            // 2. Lấy toàn bộ các SkillNode thuộc lộ trình mục tiêu (targetPath) của học viên
            java.util.List<SkillNode> flatList = new java.util.ArrayList<>();
            if (student.getTargetPath() != null) {
                String hqlNodes = "SELECT sn FROM SkillNode sn WHERE sn.path.pathId = :pathId";
                flatList = em.createQuery(hqlNodes, SkillNode.class)
                             .setParameter("pathId", student.getTargetPath().getPathId())
                             .getResultList();
            }

            // 3. Lấy toàn bộ tiến độ kỹ năng hiện tại đang lưu trong DB của học viên này
            // Chuỗi JPQL trỏ chính xác vào s.student.studentId (khớp với trường @Id trong lớp StudentProfile của bạn)
            String hqlProgress = "SELECT p FROM StudentSkillProgress p WHERE p.student.studentId = :studentId";
            java.util.List<StudentSkillProgress> currentProgressList = em.createQuery(hqlProgress, StudentSkillProgress.class)
                    .setParameter("studentId", student.getStudentId())
                    .getResultList();

            // Mở Transaction ghi dữ liệu an toàn
            em.getTransaction().begin();

            // Đồng bộ hóa thực thể student vào EntityManager hiện tại
            StudentProfile managedStudent = em.merge(student);

            // 4. Duyệt qua tất cả các kỹ năng thuộc lộ trình để thực hiện Upsert (Cập nhật thông minh)
            for (SkillNode node : flatList) {
                
                // Nếu nodeId nằm trong tập hợp gửi lên từ client -> COMPLETED, ngược lại -> NOT_STARTED
                String currentStatus = completedIdsSet.contains(node.getNodeId()) ? "COMPLETED" : "NOT_STARTED";

                // Kiểm tra xem nút kỹ năng này đã từng có bản ghi tiến độ nào trong DB chưa
                StudentSkillProgress progress = null;
                for (StudentSkillProgress p : currentProgressList) {
                    if (p.getNode() != null && p.getNode().getNodeId().equals(node.getNodeId())) {
                        progress = p;
                        break;
                    }
                }

                if (progress == null) {
                    // Trường hợp CHƯA CÓ: Tiến hành INSERT mới
                    progress = new StudentSkillProgress();
                    progress.setStudent(managedStudent);
                    progress.setNode(node);
                    progress.setStatus(currentStatus);
                    progress.setUpdatedAt(java.time.LocalDateTime.now());
                    
                    em.persist(progress);
                } else {
                    // Trường hợp ĐÃ CÓ: Tiến hành UPDATE trạng thái mới đánh giá lại
                    progress.setStatus(currentStatus);
                    progress.setUpdatedAt(java.time.LocalDateTime.now());
                    
                    em.merge(progress);
                }
            }

            // Khớp lệnh lưu tất cả các thay đổi xuống Database h
            em.getTransaction().commit();
            return "success";
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            em.close();
        }
    }
    
    @RequestMapping(value = "/load-not-started-skills", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String handleLoadNotStartedSkills(HttpServletRequest request) {
        HttpSession session = request.getSession();
        com.fe.pojo.User currentUser = (com.fe.pojo.User) session.getAttribute("userObj");
        StudentProfile profile = studentprofile.getProfile(currentUser.getUserId());
        if (profile == null) {
            System.out.println(">> [DEBUG ROADMAP] studentProfile trong Session bi NULL!");
            return "[]";
        }

        // 1. Kiểm tra ID của Student lấy ra từ Session
        System.out.println(">> [DEBUG ROADMAP] StudentId từ Session: " + profile.getStudentId());

        StringBuilder json = new StringBuilder();
        json.append("[");

        javax.persistence.EntityManager em = null;
        try {
            em = emf.createEntityManager(); 
            
            // 2. Thử truy vấn TOÀN BỘ tiến độ của Student này (không kèm điều kiện trạng thái) để check mapping
            String hqlAll = "SELECT sp FROM StudentSkillProgress sp WHERE sp.student.studentId = :studentId";
            java.util.List<com.fe.pojo.StudentSkillProgress> allProgress = em.createQuery(hqlAll, com.fe.pojo.StudentSkillProgress.class)
                    .setParameter("studentId", profile.getStudentId())
                    .getResultList();
            System.out.println(">> [DEBUG ROADMAP] TỔNG SỐ bản ghi tiến độ trong DB của Student này: " + allProgress.size());
            for (com.fe.pojo.StudentSkillProgress p : allProgress) {
                System.out.println("   + Bản ghi ID " + p.getId() + " có Status thực tế trong Entity: '" + p.getStatus() + "'");
            }

            // 3. Thực hiện truy vấn chính thức với điều kiện 'NOT_STARTED'
            String hql = "SELECT sp FROM StudentSkillProgress sp WHERE sp.student.studentId = :studentId AND sp.status = 'NOT_STARTED'";
            java.util.List<com.fe.pojo.StudentSkillProgress> progressList = em.createQuery(hql, com.fe.pojo.StudentSkillProgress.class)
                    .setParameter("studentId", profile.getStudentId())
                    .getResultList();

            System.out.println(">> [DEBUG ROADMAP] SỐ BẢN GHI thỏa mãn điều kiện 'NOT_STARTED': " + progressList.size());

            boolean isFirst = true;
            for (com.fe.pojo.StudentSkillProgress progress : progressList) {
                com.fe.pojo.SkillNode node = progress.getNode();
                if (node != null) {
                    System.out.println("   -> Nạp thành công Node: ID=" + node.getNodeId() + ", Name=" + node.getSkillName());
                    
                    if (!isFirst) json.append(",");
                    
                    json.append("{");
                    json.append("\"id\":").append(node.getNodeId()).append(",");
                    json.append("\"name\":\"").append(node.getSkillName().replace("\"", "\\\"")).append("\",");
                    
                    if (node.getParentNode() != null) {
                        json.append("\"parentId\":").append(node.getParentNode().getNodeId());
                    } else {
                        json.append("\"parentId\":null");
                    }
                    json.append("}");
                    
                    isFirst = false;
                } else {
                    System.out.println("   -> [CẢNH BÁO] Bản ghi tiến độ ID " + progress.getId() + " bị rỗng (null) Object SkillNode!");
                }
            }
        } catch (Exception e) {
            System.out.println(">> [DEBUG ROADMAP] LỖI HỆ THỐNG KHI TRUY VẤN:");
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }

        json.append("]");
        System.out.println(">> [DEBUG ROADMAP] Chuỗi JSON trả về cho AJAX: " + json.toString());
        return json.toString();
    }
    @RequestMapping(value = "/load-roadmap-resources-new", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String handleLoadRoadmapResourcesNew(@RequestParam(value = "nodeId", required = false) Long nodeId) {
        if (nodeId == null) {
            return "[]";
        }

        System.out.println(">> [Roadmap Resource API] Đang lấy tài liệu mới cho SkillNode ID: " + nodeId);
        StringBuilder json = new StringBuilder();
        json.append("[");

        javax.persistence.EntityManager em = null;
        try {
            em = emf.createEntityManager();
            
            // Tìm thực thể SkillNode từ DB để tận dụng lazy/eager loading của Hibernate tới danh sách resources
            com.fe.pojo.SkillNode node = em.find(com.fe.pojo.SkillNode.class, nodeId);
            
            if (node != null && node.getResources() != null) {
                java.util.List<com.fe.pojo.LearningResource> resources = node.getResources();
                System.out.println(">> [Roadmap Resource API] Tìm thấy " + resources.size() + " tài liệu gắn với node: " + node.getSkillName());
                
                boolean isFirst = true;
                for (com.fe.pojo.LearningResource res : resources) {
                    if (!isFirst) json.append(",");
                    
                    json.append("{");
                    json.append("\"title\":\"").append(res.getTitle().replace("\"", "\\\"")).append("\",");
                    json.append("\"url\":\"").append(res.getUrl().replace("\"", "\\\"")).append("\",");
                    // Lưu ý: Đổi getResourceType() hoặc getType() tùy thuộc vào thuộc tính trong POJO LearningResource của bạn
                    String rType = (res.getResourceType() != null) ? res.getResourceType() : "DOCUMENT";
                    json.append("\"type\":\"").append(rType.replace("\"", "\\\"")).append("\"");
                    json.append("}");
                    
                    isFirst = false;
                }
            } else {
                System.out.println(">> [Roadmap Resource API] Node ID " + nodeId + " không tồn tại hoặc không có tài liệu gán kèm.");
            }
        } catch (Exception e) {
            System.out.println(">> [Roadmap Resource API - LỖI]: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }

        json.append("]");
        System.out.println(">> [Roadmap Resource API] Kết quả chuỗi JSON xuất ra: " + json.toString());
        return json.toString();
    }
    @RequestMapping(value = "/submit-ai-chat", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String handleSubmitAIChat(@RequestParam("sessionId") Long sessionId, @RequestParam("text") String text, HttpServletRequest request) {
        // 1. Kiểm tra dữ liệu đầu vào
        if (text == null || text.trim().isEmpty()) {
            return "{\"error\":\"Nội dung câu hỏi không được để trống!\"}";
        }
        
        javax.persistence.EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            
            // 2. Tìm session
            com.fe.pojo.MentorSession session = em.find(com.fe.pojo.MentorSession.class, sessionId);
            if (session == null) {
                return "{\"error\":\"Phiên hội thoại không tồn tại.\"}";
            }
            
            // 3. LOGIC TỰ ĐỘNG CẬP NHẬT TIÊU ĐỀ
            boolean isTitleUpdated = false;
            String newTitle = session.getTitle();
            if ("Cuộc hội thoại mới".equals(session.getTitle())) {
                newTitle = (text.length() > 35) ? text.substring(0, 32) + "..." : text;
                session.setTitle(newTitle);
                isTitleUpdated = true;
            }
            
            // 4. Lưu tin nhắn của USER
            com.fe.pojo.ChatMessage userMsg = new com.fe.pojo.ChatMessage();
            userMsg.setSession(session);
            userMsg.setSenderType("USER");
            userMsg.setMessageText(text.trim());
            userMsg.setSentAt(java.time.LocalDateTime.now());
            em.persist(userMsg);
            
            // 5. Gọi AI Service (Lấy câu trả lời)
            String aiResult = aiServiceProvider.getAIResponse(new java.util.ArrayList<>(), text);
            
            // 6. Lưu tin nhắn của AI
            com.fe.pojo.ChatMessage aiMsg = new com.fe.pojo.ChatMessage();
            aiMsg.setSession(session);
            aiMsg.setSenderType("AI");
            aiMsg.setMessageText(aiResult);
            aiMsg.setSentAt(java.time.LocalDateTime.now());
            em.persist(aiMsg);
            
            em.getTransaction().commit();
            
            // 7. CHUẨN HÓA JSON: Thoát ký tự xuống dòng (\n) và dấu nháy kép (")
            // Điều này giúp trình duyệt không bị lỗi SyntaxError khi parse JSON
            String safeAiResult = aiResult.replace("\n", "\\n").replace("\"", "\\\"");
            String safeTitle = newTitle.replace("\"", "\\\"");
            
            return String.format("{\"aiResponse\":\"%s\", \"isTitleUpdated\":%b, \"newTitle\":\"%s\"}", 
                                 safeAiResult, isTitleUpdated, safeTitle);
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return "{\"error\":\"Lỗi hệ thống: " + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
    
    
    @RequestMapping(value = "/get-chat-sessions", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String handleGetChatSessions(HttpServletRequest request) {
        System.out.println(">> [AI API] Tải danh sách phiên chat (Chỉ lấy ID và Title).");
        
        Long currentStudentId = 1L; // ID giả định theo DB của bạn
        javax.persistence.EntityManager em = null;
        StringBuilder json = new StringBuilder();
        
        try {
            em = emf.createEntityManager();
            com.fe.dao.MentorSessionDAO sessionDAO = new com.fe.dao.MentorSessionDAO(em);
            List<com.fe.pojo.MentorSession> sessions = sessionDAO.findByStudentId(currentStudentId);
            
            json.append("[");
            boolean isFirst = true;
            for (com.fe.pojo.MentorSession session : sessions) {
                if (!isFirst) json.append(",");
                json.append("{");
                json.append("\"sessionId\":").append(session.getSessionId()).append(",");
                json.append("\"title\":\"").append(session.getTitle().replace("\"", "\\\"")).append("\"");
                json.append("}");
                isFirst = false;
            }
            json.append("]");
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
    @RequestMapping(value = "/get-chat-messages", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String handleGetChatMessages(@RequestParam("sessionId") Long sessionId, HttpServletRequest request) {
        System.out.println(">> [AI API] Tải chi tiết tin nhắn của phiên ID: " + sessionId + " bằng ChatMessageDAO.");
        
        javax.persistence.EntityManager em = null;
        StringBuilder json = new StringBuilder();
        
        try {
            em = emf.createEntityManager();
            
            // Sử dụng chính xác ChatMessageDAO mà bạn vừa cung cấp
            com.fe.dao.ChatMessageDAO messageDAO = new com.fe.dao.ChatMessageDAO(em);
            List<com.fe.pojo.ChatMessage> msgList = messageDAO.findBySessionId(sessionId);
            
            json.append("[");
            boolean isFirst = true;
            for (com.fe.pojo.ChatMessage msg : msgList) {
                if (!isFirst) json.append(",");
                
                // Lấy đúng trường senderType ('user' hoặc 'ai') và messageText từ Entity của bạn
                String sender = msg.getSenderType() != null ? msg.getSenderType() : "user";
                String text = msg.getMessageText() != null ? msg.getMessageText().replace("\"", "\\\"").replace("\n", "\\n") : "";
                
                json.append("{");
                json.append("\"sender\":\"").append(sender).append("\",");
                json.append("\"text\":\"").append(text).append("\"");
                json.append("}");
                
                isFirst = false;
            }
            json.append("]");
            return json.toString();
            
        } catch (Exception e) {
            System.out.println(">> [AI API - LỖI TẢI TIN NHẮN]: " + e.getMessage());
            e.printStackTrace();
            return "[]";
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    @RequestMapping(value = "/create-new-session", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String handleCreateNewSession(HttpServletRequest request) {
        // Giả định bạn đã lưu Student ID trong HTTP Session khi đăng nhập
        // Nếu bạn fix cứng hoặc lấy cách khác thì đổi lại nhé (Ví dụ: 1L hoặc lấy từ session)
        Long studentId = 1L; 
        
        javax.persistence.EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            
            // 1. Tìm đối tượng StudentProfile để liên kết relationship
            com.fe.pojo.StudentProfile student = em.find(com.fe.pojo.StudentProfile.class, studentId);
            
            // 2. Khởi tạo đối tượng phiên chat mới
            com.fe.pojo.MentorSession newSession = new com.fe.pojo.MentorSession();
            newSession.setStudent(student);
            newSession.setTitle("Cuộc hội thoại mới");
            newSession.setCreatedAt(java.time.LocalDateTime.now());
            
            // 3. Lưu xuống Database
            em.persist(newSession);
            em.getTransaction().commit();
            
            // 4. Trả về chuỗi JSON chứa thông tin phiên mới tạo cho Front-end
            return "{\"sessionId\":" + newSession.getSessionId() + ", \"title\":\"" + newSession.getTitle() + "\"}";
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    // =========================================================================
    // API PHẦN 6: LẤY DỮ LIỆU KHẢO SÁT THỊ TRƯỜNG (JOB TRENDS)
    // =========================================================================
    @RequestMapping(value = "/api/job-trends", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getJobTrends(@RequestParam(value = "portal", defaultValue = "TopCV") String portal) {
        System.out.println(">> [API JobTrend] Đang tải dữ liệu xu hướng cho nền tảng: " + portal);
        
        StringBuilder json = new StringBuilder();
        json.append("[");

        javax.persistence.EntityManager em = null;
        try {
            em = emf.createEntityManager();
            
            // Khởi tạo DAO và gọi hàm truy vấn dữ liệu đã được định nghĩa
            com.fe.dao.JobTrendDAO jobTrendDAO = new com.fe.dao.JobTrendDAO(em);
            List<com.fe.pojo.JobTrend> trends = jobTrendDAO.findByPortal(portal);

            boolean isFirst = true;
            for (com.fe.pojo.JobTrend trend : trends) {
                if (!isFirst) {
                    json.append(",");
                }

                json.append("{");
                json.append("\"trendId\":").append(trend.getTrendId()).append(",");
                json.append("\"skillName\":\"").append(trend.getSkillName().replace("\"", "\\\"")).append("\",");
                json.append("\"jobPortal\":\"").append(trend.getJobPortal().replace("\"", "\\\"")).append("\",");
                json.append("\"frequencyCount\":").append(trend.getFrequencyCount()).append(",");

                // Xử lý an toàn cho LocalDate
                if (trend.getRecordedDate() != null) {
                    json.append("\"recordedDate\":\"").append(trend.getRecordedDate().toString()).append("\"");
                } else {
                    json.append("\"recordedDate\":null");
                }
                
                json.append("}");
                isFirst = false;
            }
            
            System.out.println(">> [API JobTrend] Tải thành công " + trends.size() + " bản ghi.");
            
        } catch (Exception e) {
            System.err.println(">> [API JobTrend - LỖI]: " + e.getMessage());
            e.printStackTrace();
            // Trả về mảng rỗng nếu có lỗi để frontend xử lý fallback
            return "[]"; 
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        json.append("]");
        return json.toString();
    }

    
}
