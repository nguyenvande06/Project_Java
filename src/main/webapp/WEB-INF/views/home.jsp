<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard - Roadmap Platform</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: Arial, sans-serif; }
        body { display: flex; height: 100vh; background: #f4f6f9; }
        
        /* Cột trái (Sidebar) */
        .sidebar { width: 300px; background: #1e1e24; color: white; padding: 20px; display: flex; flex-direction: column; }
        .profile-card { text-align: center; padding: 20px 0; border-bottom: 1px solid #3a3a45; margin-bottom: 20px; }
        .profile-card h3 { color: #ff3b30; margin-bottom: 5px; }
        .profile-card p { font-size: 13px; color: #aaa; }
        
        .menu-tabs { flex: 1; }
        .tab-btn { width: 100%; padding: 12px 15px; background: none; border: none; color: #ccc; text-align: left; font-size: 14px; cursor: pointer; border-radius: 4px; margin-bottom: 5px; }
        .tab-btn:hover, .tab-btn.active { background: #ff3b30; color: white; font-weight: bold; }
        
        .logout-btn { display: block; text-align: center; padding: 10px; background: #3a3a45; color: #ff4d4d; text-decoration: none; border-radius: 4px; font-weight: bold; margin-top: auto; }
        .logout-btn:hover { background: #ff4d4d; color: white; }

        /* Cột phải (Vùng hiển thị nội dung nghiệp vụ) */
        .main-content { flex: 1; padding: 40px; overflow-y: auto; }
        .content-box { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); min-height: 400px; }
        h2 { color: #333; margin-bottom: 20px; border-bottom: 2px solid #eee; padding-bottom: 10px; }
    </style>
</head>
<body>

    <div class="sidebar">
        <div class="profile-card">
            <h3><%= session.getAttribute("currentUser") %></h3>
            <p>MSSV: <%= session.getAttribute("Mssv") %></p>
        </div>
        
        <div class="menu-tabs">
            <button class="tab-btn active" onclick="loadFeature('profile')">1. Quản lý Hồ sơ cá nhân & E-Portfolio</button>
            <button class="tab-btn" onclick="loadFeature('target')">2. Cài đặt mục tiêu Nghề nghiệp</button>
            <button class="tab-btn" onclick="loadFeature('roadmap')">3. Xem Lộ trình cá nhân hóa</button>
            <button class="tab-btn" onclick="loadFeature('ai')">4. Tương tác với Trợ lý ảo AI</button>
            <button class="tab-btn" onclick="loadFeature('gap')">5. Phân tích khoảng cách kỹ năng</button>
            <button class="tab-btn" onclick="loadFeature('survey')">6. Khảo sát thị trường tuyển dụng</button>
        </div>
        
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">🚪 Đăng xuất hệ thống</a>
    </div>

    <div class="main-content">
        <div class="content-box" id="view-space">
            <h2>Chào mừng bạn quay trở lại!</h2>
            <p>Hãy chọn một nghiệp vụ ở menu bên trái để bắt đầu thực hiện định hướng và phân tích lộ trình học tập.</p>
        </div>
    </div>

	<script type="text/javascript">

	    window.onload = function() {
	        loadFeature('profile');
	    };
	
	    function loadFeature(featureName) {

	        let buttons = document.querySelectorAll('.tab-btn');
	        buttons.forEach(btn => btn.classList.remove('active'));
	        
	        if (event && event.target) {
	            event.target.classList.add('active');
	        } else {
	            // Tự động active nút đầu tiên lúc load trang
	            buttons[0].classList.add('active');
	        }
	        
	        let space = document.getElementById('view-space');

	        if (featureName === 'profile') {
	            space.innerHTML = `
	                <h2>💳 Quản lý Hồ sơ cá nhân & E-Portfolio</h2>
	                <p style="color:#666; margin-bottom: 20px;">Xem thông tin cá nhân hiện tại, cập nhật mã số sinh viên, liên kết tài khoản GitHub và chia sẻ Portfolio.</p>
	                
	                <div style="background: #1e1e24; color: #fff; padding: 20px; border-radius: 6px; margin-bottom: 25px;">
	                    <h4 style="color: #ff3b30; margin-bottom: 10px; border-bottom: 1px solid #3a3a45; padding-bottom: 5px;">[Thông tin hiện tại từ hệ thống]</h4>
	                    <p style="margin: 5px 0;">• Sinh viên: <strong style="color:#fff;"><%= session.getAttribute("currentUser") %></strong></p>
	                    <p style="margin: 5px 0;">• MSSV: <strong style="color:#fff;"><%= session.getAttribute("Mssv") %></strong></p>
	                    <p style="margin: 5px 0;">• GitHub Username: <strong style="color:#aaa;"><%= session.getAttribute("github") %></strong></p>
	                    <p style="margin: 5px 0;">• URL E-Portfolio: <a href="#" style="color:#007aff; text-decoration:none;"><%= session.getAttribute("Portfolio") %></a></p>
	                </div>
	
	                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
	                    
	                <div style="border: 1px solid #eee; padding: 20px; border-radius: 6px; background: #fafafa;">
	               	   <h3 style="font-size:16px; margin-bottom:15px; color:#333;">1. Cập nhật thông tin cá nhân</h3>
	             	   <div style="margin-bottom: 15px;">
	                	    <label style="display:block; font-size:13px; margin-bottom:5px; font-weight:bold;">Mã số sinh viên (MSSV):</label>
	                    
	                   		<input type="text" id="txt-mssv" value="<%= session.getAttribute("Mssv") != null && !session.getAttribute("Mssv").toString().equals("null") ? session.getAttribute("Mssv") : "" %>" placeholder="Nhập mã số sinh viên mới..." style="width:100%; padding:10px; border:1px solid #ccc; border-radius:4px;">
	               		 </div>
	               		 <button onclick="submitMssv()" style="padding: 10px 18px; background:#ff3b30; color:white; border:none; border-radius:4px; font-weight:bold; cursor:pointer;">Cập nhật thông tin</button>
	           		 </div>
	
	                    <div style="border: 1px solid #eee; padding: 20px; border-radius: 6px; background: #fafafa;">
	                        <h3 style="font-size:16px; margin-bottom:15px; color:#333;">2. Liên kết tài khoản & Tạo mã chia sẻ</h3>
	                        
	                        <div style="margin-bottom: 15px;">
	                            <label style="display:block; font-size:13px; margin-bottom:5px; font-weight:bold;">Tên tài khoản GitHub:</label>
	                            <input type="text" id="txt-github" placeholder="Nhập GitHub username..." style="width:100%; padding:10px; border:1px solid #ccc; border-radius:4px; margin-bottom:10px;">
	                            <button onclick="submitGithub()" style="width:100%; padding: 10px; background:#24292e; color:white; border:none; border-radius:4px; font-weight:bold; cursor:pointer;">🔗 Kết nối GitHub</button>
	                        </div>
	                        
	                        <div style="border-top: 1px dashed #ccc; padding-top: 15px;">
	                       		<button onclick="generateToken()" style="width:100%; padding: 10px; background:#007aff; color:white; border:none; border-radius:4px; font-weight:bold; cursor:pointer;">✨ Sinh mã token E-Portfolio</button>
	                        </div>
	                    </div>
	
	                </div>
	            `;
	        } else if (featureName === 'ai') {
	            // Khởi tạo không gian giao diện Trợ lý AI (Chỉ giữ khung thô, đã xóa hết dữ liệu mẫu & nút xóa phiên)
	            space.innerHTML = `
	                <h2>🤖 Tương Tác Với Trợ Lý Ảo AI</h2>
	                <p style="color: #666; margin-bottom: 20px;">Hỏi đáp kiến thức, giải thích mã nguồn và hỗ trợ học tập cá nhân hóa dựa trên lộ trình của bạn.</p>
	                
	                <div id="ai-chat-layout-wrapper" style="display: flex; height: 550px; background: #ffffff; border: 1px solid #eee; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;">
	                    
	                    <div id="ai-chat-sidebar" style="width: 240px; background: #1e1e24; color: #ffffff; display: flex; flex-direction: column; border-right: 1px solid #2d2d35; flex-shrink: 0;">
	                        
	                        <div style="padding: 15px;">
	                            <button onclick="handleCreateNewChatSession()" style="width: 100%; background: #ff3b30; color: #ffffff; border: none; padding: 10px; border-radius: 4px; font-size: 13px; font-weight: bold; cursor: pointer; transition: background 0.2s;" onmouseover="this.style.background='#e02e24';" onmouseout="this.style.background='#ff3b30';">
	                                ➕ Tạo phiên chat mới
	                            </button>
	                        </div>

	                        <div style="font-size: 11px; color: #666; font-weight: bold; text-transform: uppercase; padding: 0 15px 8px 15px; text-align: left; letter-spacing: 0.5px;">
	                            Lịch sử hội thoại
	                        </div>

	                        <div id="ai-sessions-list-box" style="flex: 1; overflow-y: auto; padding: 0 6px 15px 6px; display: flex; flex-direction: column; gap: 4px;">
	                        </div>

	                        <div style="padding: 10px 15px; border-top: 1px solid #2d2d35; background: #15151a; font-size: 11px; color: #555; text-align: left;">
	                            Trạng thái: Sẵn sàng
	                        </div>
	                    </div>

	                    <div id="ai-chat-main-content" style="flex: 1; display: flex; flex-direction: column; background: #ffffff;">
	                        
	                        <div style="padding: 14px 20px; background: #fafafa; border-bottom: 1px solid #eee; display: flex; align-items: center; justify-content: space-between;">
	                            <div style="text-align: left;">
	                                <h3 id="current-session-title" style="margin: 0; font-size: 14px; font-weight: 600; color: #333;">Chưa có phiên hội thoại</h3>
	                            </div>
	                        </div>

	                        <div id="ai-chat-messages-box" style="flex: 1; padding: 20px; overflow-y: auto; background: #fdfdfd; display: flex; flex-direction: column; gap: 14px;">
	                        </div>

	                        <div style="padding: 15px 20px; background: #ffffff; border-top: 1px solid #eee;">
	                            <div style="display: flex; gap: 10px; background: #f5f5f7; border: 1px solid #eee; padding: 5px 6px 5px 12px; border-radius: 6px; align-items: center;">
	                                <input type="text" id="ai-chat-user-input" placeholder="Hỏi trợ lý ảo câu hỏi của bạn..." style="flex: 1; border: none; background: transparent; outline: none; font-size: 13px; color: #333;" onkeypress="if(event.key === 'Enter') handleSendChatMessage()" />
	                                <button onclick="handleSendChatMessage()" style="background: #ff3b30; color: #fff; border: none; padding: 6px 14px; border-radius: 4px; font-size: 12px; font-weight: bold; cursor: pointer;" onmouseover="this.style.background='#e02e24';" onmouseout="this.style.background='#ff3b30';">
	                                    Gửi 🚀
	                                </button>
	                            </div>
	                        </div>
	                    </div>
	                </div>

	                <style>
	                    /* Định dạng chung cho mỗi item phiên chat */
	                    .session-item {
	                        transition: background 0.2s, color 0.2s;
	                    }
	                    /* Ép chữ bên trong hiển thị màu xám sáng (Không bị dính màu đen toàn trang) */
	                    .session-item div {
	                        color: #cccccc !important;
	                    }
	                    /* Khi hover chuột vào phiên chat */
	                    .session-item:hover { 
	                        background: rgba(255, 255, 255, 0.05) !important; 
	                    }
	                    .session-item:hover div {
	                        color: #ffffff !important;
	                    }
	                    /* Khi phiên chat đang được click chọn hoạt động */
	                    .session-item.active { 
	                        background: rgba(255, 255, 255, 0.15) !important; 
	                    }
	                    .session-item.active div {
	                        color: #ffffff !important;
	                        font-weight: 600;
	                    }
               		</style>
	            `;

	            // Gọi hàm nạp dữ liệu ban đầu từ kho lưu trữ lên UI
	            if (typeof initChatState === 'function') {
	                initChatState();
	            }
	        } else if (featureName === 'survey') {
	            // Khởi tạo không gian giao diện Khảo sát thị trường tuyển dụng
	            space.innerHTML = `
	                <div style="background: white; padding: 24px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); max-width: 1000px; margin: 0 auto;">
	                    <h2 style="font-size: 20px; font-weight: bold; margin-bottom: 8px; display: flex; align-items: center; gap: 8px; color: #333;">
	                        📈 Phân Tích & Khảo Sát Thị Trường Tuyển Dụng
	                    </h2>
	                    <p style="font-size: 14px; color: #666; margin-bottom: 24px;">
	                        Cập nhật theo thời gian thực xu hướng từ khóa công nghệ và kỹ năng đang được các nhà tuyển dụng săn đón nhiều nhất.
	                    </p>

	                    <!-- Filter Area -->
	                    <div style="background: #f8f9fa; padding: 16px; border: 1px solid #e9ecef; border-radius: 6px; margin-bottom: 24px; display: flex; align-items: center; justify-content: space-between;">
	                        <div style="display: flex; align-items: center; gap: 12px;">
	                            <label for="portal-filter" style="font-weight: 600; color: #495057; font-size: 14px;">🏢 Nền tảng phân tích:</label>
	                            <select id="portal-filter" onchange="window.handlePortalChange(this.value)" style="padding: 8px 16px; border: 1px solid #ced4da; border-radius: 4px; font-size: 14px; color: #495057; background-color: #fff; outline: none; cursor: pointer; min-width: 180px; box-shadow: 0 1px 2px rgba(0,0,0,0.05);">
	                                <option value="TopCV">TopCV</option>
	                                <option value="VietnamWorks">VietnamWorks</option>
	                                <option value="ITVieC">ITviec</option>
	                                <option value="Linkedin">LinkedIn</option>
	                            </select>
	                        </div>
	                        <div id="survey-status-badge" style="padding: 6px 12px; background: #e6f4ea; color: #1e8e3e; border-radius: 20px; font-size: 12px; font-weight: 600; display: flex; align-items: center; gap: 6px;">
	                            <span style="display: inline-block; width: 8px; height: 8px; background: #1e8e3e; border-radius: 50%;"></span>
	                            Sẵn sàng
	                        </div>
	                    </div>

	                    <!-- Table Area -->
	                    <div style="border: 1px solid #e9ecef; border-radius: 6px; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.02);">
	                        <table style="width: 100%; border-collapse: collapse; background: #fff;">
	                            <thead>
	                                <tr style="background: #1e222b; color: #fff; font-size: 13px; text-transform: uppercase; letter-spacing: 0.5px;">
	                                    <th style="padding: 14px 16px; text-align: center; width: 80px;">Top</th>
	                                    <th style="padding: 14px 16px; text-align: left;">Kỹ năng / Công nghệ</th>
	                                    <th style="padding: 14px 16px; text-align: right; width: 180px;">Tần suất xuất hiện</th>
	                                    <th style="padding: 14px 16px; text-align: left; width: 250px;">Biểu đồ tương quan</th>
	                                    <th style="padding: 14px 16px; text-align: center; width: 120px;">Cập nhật</th>
	                                </tr>
	                            </thead>
	                            <tbody id="job-trend-table-body">
	                                <tr>
	                                    <td colspan="5" style="text-align: center; padding: 30px; color: #6c757d; font-size: 14px;">
	                                        🔄 Đang tải dữ liệu khảo sát...
	                                    </td>
	                                </tr>
	                            </tbody>
	                        </table>
	                    </div>
	                </div>
	            `;
	            
	            // Chờ DOM render xong mới gọi API cho nền tảng mặc định
	            setTimeout(() => {
	                if(typeof window.handlePortalChange === 'function') {
	                    window.handlePortalChange('TopCV');
	                }
	            }, 50);
	    
	        } else if (featureName === 'target') {
	        	
	            let sessionJobRole = "<%= session.getAttribute("JobRole")%>";
	          
	            let sessionRawStr = '<%= session.getAttribute("lstTechPaths") != null ? session.getAttribute("lstTechPaths").toString() : "[]" %>';
	            
	           
	            let cleanStr = sessionRawStr.trim();
	            if (cleanStr.startsWith('[') && cleanStr.endsWith(']')) {
	                cleanStr = cleanStr.substring(1, cleanStr.length - 1);
	            }

	            let techPathsList = [];
	            if (cleanStr.trim() !== "") {
	                techPathsList = cleanStr.split(',').map(item => item.trim());
	            }

	            let space = document.getElementById('view-space');
	            space.innerHTML = `
	                <div style="background: white; padding: 24px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); max-width: 900px; margin: 0 auto;">
	                    <h2 style="font-size: 20px; font-weight: bold; margin-bottom: 8px; display: flex; align-items: center; gap: 8px; color: #333;">
	                        🎯 Thiết lập Mục tiêu Nghề nghiệp
	                    </h2>
	                    <p style="font-size: 14px; color: #666; margin-bottom: 20px;">
	                        Lựa chọn định hướng công nghệ mục tiêu từ hệ thống để làm cơ sở phân tích lộ trình học tập.
	                    </p>

	                    <div style="background: #1e222b; color: #a9b2c3; padding: 20px; border-radius: 6px; margin-bottom: 24px; font-family: monospace; font-size: 14px; line-height: 1.6;">
	                        <span style="color: #ff3b30; font-weight: bold; display: block; margin-bottom: 10px;">[Định hướng nghề nghiệp hiện tại]</span>
	                        <p style="margin: 5px 0;">• Mục tiêu nghề nghiệp: <strong style="color: #fff;" id="lbl-job-role">\${sessionJobRole}</strong></p>
	                    </div>

	                    <div style="border: 1px solid #eee; padding: 20px; border-radius: 6px; background: #fafafa;">
	                        <h3 style="font-size: 16px; margin-bottom: 15px; color: #333; font-weight: bold;">Cập nhật định hướng mới</h3>
	                        
	                        <div style="margin-bottom: 20px;">
	                            <label style="display: block; font-size: 13px; margin-bottom: 5px; font-weight: bold; color: #555;">Chọn mục tiêu nghề nghiệp (Dữ liệu hệ thống):</label>
	                            <select id="sl-job-role" style="width: 100%; padding: 12px; border: 1px solid #ccc; border-radius: 4px; background: white; font-size: 13px;">
	                                <option value="">⏳ Đang xử lý dữ liệu...</option>
	                            </select>
	                        </div>

	                        <button onclick="submitJobTarget()" style="width: 100%; padding: 12px; background: #ff3b30; color: white; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 14px;">
	                            💾 Lưu cấu hình mục tiêu
	                        </button>
	                    </div>
	                </div>
	            `;

	            let selectBox = document.getElementById('sl-job-role');
	            if (selectBox) {
	                selectBox.innerHTML = ''; 
	                
	                if (sessionJobRole === "Chưa cập nhật" || sessionJobRole === "") {
	                    let defaultOpt = document.createElement('option');
	                    defaultOpt.value = "";
	                    defaultOpt.text = "-- Chọn một mục tiêu nghề nghiệp để bắt đầu --";
	                    selectBox.appendChild(defaultOpt);
	                }

	                techPathsList.forEach(pathName => {
	                    let option = document.createElement('option');
	                    option.value = pathName; 
	                    option.text = pathName;

	                    if (pathName === sessionJobRole) {
	                        option.selected = true;
	                    }
	                    selectBox.appendChild(option);
	                });
	            }
	            
	        } else if (featureName === 'gap') {
	            // 1. LẤY TOÀN BỘ SKILLNODE CON TỪ SESSION LÊN ĐỂ CHUẨN BỊ KHẢO SÁT
	            let realSkillNodes = [];
	            
	            <% 
	            	java.util.List<com.fe.pojo.SkillNode> sessionNodes = (java.util.List<com.fe.pojo.SkillNode>) session.getAttribute("leafSkillNodes");
	                if (sessionNodes != null && !sessionNodes.isEmpty()) {
	                    for (com.fe.pojo.SkillNode node : sessionNodes) {
	            %>
	                        realSkillNodes.push({
	                            id: <%= node.getNodeId() %>,
	                            name: "<%= node.getSkillName().replace("\"", "\\\"") %>",
	                            priority: <%= node.getPriorityLevel() != null ? node.getPriorityLevel() : 3 %>
	                        });
	            <% 
	                    }
	                }
	            %>

	            if (realSkillNodes.length === 0) {
	                let space = document.getElementById('view-space');
	                space.innerHTML = `
	                    <div style="background: white; padding: 24px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); max-width: 800px; margin: 0 auto; text-align: center;">
	                        <h2 style="font-size: 20px; color: #ff3b30; margin-bottom: 12px;">⚠️ Chưa thể phân tích khoảng cách kỹ năng</h2>
	                        <p style="color: #666;">Bạn vui lòng chọn Mục tiêu nghề nghiệp trước khi thực hiện chức năng này!</p>
	                    </div>
	                `;
	                return;
	            }

	            // Thiết lập các biến tạm quản lý trạng thái khảo sát trên RAM
	            window.surveyNodes = realSkillNodes;
	            window.userSurveyAnswers = {}; // Lưu đáp án tạm thời { nodeId: 'COMPLETED' hoặc 'NOT_STARTED' }
	            window.currentSkillIndex = 0;

	            let space = document.getElementById('view-space');
	            space.innerHTML = `
	                <div style="background: white; padding: 24px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); max-width: 800px; margin: 0 auto;">
	                    <h2 style="font-size: 20px; font-weight: bold; margin-bottom: 8px; display: flex; align-items: center; gap: 8px; color: #333;">
	                        📊 Phân tích Khoảng cách Kỹ năng (Skill Gap Analysis)
	                    </h2>
	                    <p style="font-size: 14px; color: #666; margin-bottom: 20px;">
	                        Vui lòng tự đánh giá trung thực để hệ thống kết xuất lộ trình tài liệu bổ sung phù hợp nhất.
	                    </p>

	                    <div id="survey-wizard-container" style="background: #1e222b; color: #fff; padding: 30px; border-radius: 8px; margin-bottom: 24px;">
	                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
	                            <span id="survey-progress-text" style="font-family: monospace; font-size: 13px; color: #ff3b30;">TIẾN ĐỘ: 1/1</span>
	                            <div style="width: 70%; background: #3a3a45; height: 6px; border-radius: 3px; overflow: hidden;">
	                                <div id="survey-progress-bar" style="width: 0%; background: #ff3b30; height: 100%; transition: width 0.3s ease;"></div>
	                            </div>
	                        </div>

	                        <div id="survey-question-box" style="min-height: 120px; display: flex; flex-direction: column; justify-content: center;">
	                            <span id="lbl-skill-priority" style="color: white; padding: 3px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; width: fit-content; margin-bottom: 12px;">-</span>
	                            <h3 id="lbl-skill-name" style="font-size: 18px; line-height: 1.5; margin: 0; color: #fff;">Đang tải...</h3>
	                        </div>

	                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-top: 25px;">
	                            <button onclick="handleSurveySelection('COMPLETED')" style="padding: 14px; background: #34c759; color: white; border: none; border-radius: 6px; font-weight: bold; font-size: 15px; cursor: pointer;">
	                                👍 Tôi ĐÃ BIẾT kỹ năng này
	                            </button>
	                            <button onclick="handleSurveySelection('NOT_STARTED')" style="padding: 14px; background: #ff3b30; color: white; border: none; border-radius: 6px; font-weight: bold; font-size: 15px; cursor: pointer;">
	                                👎 Tôi CHƯA BIẾT kỹ năng này
	                            </button>
	                        </div>
	                    </div>

	                    <div id="survey-result-container" style="display: none; border-top: 2px dashed #eee; padding-top: 20px;">
	                        <div style="background: #15181f; color: #fff; padding: 20px; border-radius: 6px; margin-bottom: 20px; text-align: center;">
	                            <h3 style="margin: 0 0 10px 0; color: #34c759; font-size: 22px;">🎉 Đã hoàn thành đánh giá!</h3>
	                            <p style="font-size: 16px; margin: 0;">Độ tương thích năng lực hiện tại: <strong id="lbl-match-percent" style="color: #ff3b30; font-size: 24px;">0%</strong></p>
	                        </div>

	                        <h4 style="font-size: 16px; font-weight: bold; color: #333; margin-bottom: 15px;">📋 Danh sách các kỹ năng cần bổ sung & tài liệu đề xuất:</h4>
	                        <div id="survey-gap-report" style="display: flex; flex-direction: column; gap: 15px;">
	                             </div>
	                    </div>
	                </div>
	            `;

	            // Hàm render nội dung câu hỏi
	            window.renderSurveyQuestion = function() {
	                if (window.currentSkillIndex >= window.surveyNodes.length) {
	                    // Khi làm xong hết câu hỏi -> Ẩn hộp câu hỏi, hiện hộp kết quả
	                    document.getElementById('survey-wizard-container').style.display = 'none';
	                    document.getElementById('survey-result-container').style.display = 'block';
	                    
	                    // Gọi hàm xử lý kết quả để lấy tài liệu thực tế từ HomeController lên
	                    fetchMissingResourcesAndShow();
	                    return;
	                }

	                let currentSkill = window.surveyNodes[window.currentSkillIndex];
	                let progressPercent = ((window.currentSkillIndex + 1) / window.surveyNodes.length) * 100;
	                
	                document.getElementById('survey-progress-text').innerText = `TIẾN ĐỘ: ${window.currentSkillIndex + 1}/${window.surveyNodes.length}`;
	                document.getElementById('survey-progress-bar').style.width = `${progressPercent}%`;

	                let pLabel = document.getElementById('lbl-skill-priority');
	                if (currentSkill.priority == 1) {
	                    pLabel.innerText = "🔥 MỨC ĐỘ: BẮT BUỘC";
	                    pLabel.style.background = "#ff3b30";
	                } else if (currentSkill.priority == 2) {
	                    pLabel.innerText = "⏱️ MỨC ĐỘ: NÊN BIẾT";
	                    pLabel.style.background = "#ff9500";
	                } else {
	                    pLabel.innerText = "🚀 MỨC ĐỘ: NÂNG CAO";
	                    pLabel.style.background = "#007aff";
	                }

	                document.getElementById('lbl-skill-name').innerText = currentSkill.name;
	            };

	            window.handleSurveySelection = function(status) {
	                let currentSkill = window.surveyNodes[window.currentSkillIndex];
	                window.userSurveyAnswers[currentSkill.id] = status; // Lưu trạng thái
	                window.currentSkillIndex++;
	                window.renderSurveyQuestion();
	            };

	         	// 🚀 ĐIỀU CHỈNH CHÍNH TẠI ĐÂY: GỌI AJAX GỬI BIẾN TẠM THIẾU VỀ HOME CONTROLLER
	            function fetchMissingResourcesAndShow() {
	                let total = window.surveyNodes.length;
	                let completedCount = 0;
	                let missingIds = [];
	                let completedIds = [];

	                // Duyệt tìm các id mà user chọn Chưa biết (NOT_STARTED)
	                window.surveyNodes.forEach(skill => {
	                    let status = window.userSurveyAnswers[skill.id];
	                    if (status === 'COMPLETED') {
	                        completedCount++;
	                        completedIds.push(skill.id);
	                    } else {
	                        missingIds.push(skill.id); // Lưu vào danh sách thiếu gửi lên server
	                    }
	                });

	                // Tính toán và hiển thị % tương thích
	                let matchPercent = Math.round((completedCount / total) * 100);
	                document.getElementById('lbl-match-percent').innerText = matchPercent + "%";
	                
	                //
	                if (completedIds.length > 0) {
	                    let progressData = new URLSearchParams();
	                    completedIds.forEach(id => progressData.append('completedIds[]', id));

	                    fetch('${pageContext.request.contextPath}/save-skill-progress', {
	                        method: 'POST',
	                        headers: {
	                            'Content-Type': 'application/x-www-form-urlencoded'
	                        },
	                        body: progressData
	                    })
	                    .then(res => res.text())
	                    .then(msg => console.log(">> [DB Progress]:", msg))
	                    .catch(err => console.error("Lỗi lưu tiến độ kỹ năng:", err));
	                }
	                
	                // lấy tài liệu kỹ năng thiếu
	                let reportBox = document.getElementById('survey-gap-report');
	                if (missingIds.length === 0) {
	                    reportBox.innerHTML = `<div style="text-align:center; color:#34c759; font-weight:bold; padding:20px; font-size:16px;">🎉 Tuyệt vời! Bạn tự đánh giá đã nắm được 100% nền tảng của lộ trình này.</div>`;
	                    return;
	                }

	                reportBox.innerHTML = `<div style="text-align:center; color:#666; padding:20px;">🔄 Hệ thống đang truy vấn tài liệu tương thích từ cơ sở dữ liệu...</div>`;

	                // Chuẩn bị dữ liệu gửi (dạng x-www-form-urlencoded chuẩn Spring MVC nhận @RequestParam)
	                let formData = new URLSearchParams();
	                missingIds.forEach(id => formData.append('missingIds[]', id));

	                // Gọi API ngầm lên Controller
	                fetch('${pageContext.request.contextPath}/load-gap-resources', {
	                    method: 'POST',
	                    headers: {
	                        'Content-Type': 'application/x-www-form-urlencoded'
	                    },
	                    body: formData
	                })
	                // === CHỈNH SỬA TẠI ĐÂY ===
					.then(response => response.text())
	                .then(rawText => {
	                    console.log("Dữ liệu thô nhận từ Server:", rawText);
	                    
	                    let resources;
	                    try {
	                        resources = JSON.parse(rawText);
	                    } catch(e) {
	                        reportBox.innerHTML = `<div style="color:red; padding:10px;">❌ Lỗi cú pháp JSON!</div>`;
	                        return;
	                    }

	                    if (!resources || resources.length === 0) {
	                        reportBox.innerHTML = `<div style="color:#ff9500; padding:10px;">⚠️ Mảng tài liệu từ Server bị rỗng!</div>`;
	                        return;
	                    }
	                    
	                    let gapHtml = "";
	                    let matchedCount = 0;

	                    // Duyệt qua toàn bộ node kỹ năng có trên giao diện
	                    window.surveyNodes.forEach(skill => {
	                        // 🎯 Giải pháp: Chuyển cả 2 vế về chuỗi thô, cắt khoảng trắng để so sánh chính xác nhất
	                        let nodeResources = resources.filter(r => String(r.nodeId).trim() === String(skill.id).trim());
	                        
	                        if (nodeResources.length > 0) {
	                            matchedCount += nodeResources.length;
	                            let resourceHtml = "";
	                            
	                            nodeResources.forEach(res => {
	                                resourceHtml += `
	                                    <div style="font-size:13px; color:#007aff; margin-top:6px; display:flex; align-items:center; gap:6px;">
	                                        🔹 <strong>[\${res.resourceType}]</strong> \${res.title} 
	                                        <a href="${res.url}" target="_blank" style="color:#ff3b30; text-decoration:underline; font-weight:bold; margin-left:auto;">Học ngay →</a>
	                                    </div>`;
	                            });

	                            let pText = skill.priority == 1 ? "Bắt buộc" : (skill.priority == 2 ? "Nên biết" : "Nâng cao");
	                            let pColor = skill.priority == 1 ? "#ff3b30" : (skill.priority == 2 ? "#ff9500" : "#007aff");

	                            gapHtml += `
	                                <div style="border: 1px solid #eee; padding: 15px; border-radius: 6px; background: #fafafa; box-shadow: 0 2px 4px rgba(0,0,0,0.02); margin-bottom: 10px;">
	                                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:6px; border-bottom:1px solid #f0f0f0; padding-bottom:6px;">
	                                   		<strong style="color:#333; font-size:15px;">📌 \${skill.name || 'Kỹ năng số ' + skill.id} (ID: \${skill.id})</strong>
	                                        <span style="font-size:11px; font-weight:bold; color:white; background:${pColor}; padding:2px 6px; border-radius:3px;">${pText}</span>
	                                    </div>
	                                    \${resourceHtml}
	                                </div>
	                            `;
	                        }
	                    });

	                    if (matchedCount === 0) {
	                        reportBox.innerHTML = `
	                            <div style="background:#fff3cd; color:#856404; padding:15px; border-radius:6px; font-size:13px;">
	                                ⚠️ Không có tài liệu nào trùng khớp với ID kỹ năng hiện tại trên giao diện.
	                            </div>`;
	                    } else {
	                        reportBox.innerHTML = gapHtml;
	                    }
	                })
	                //
	                .catch(err => {
	                    console.error("Lỗi fetch tài liệu:", err);
	                    reportBox.innerHTML = `<div style="text-align:center; color:#ff3b30; font-weight:bold; padding:20px;">❌ Đã có lỗi xảy ra khi nạp tài nguyên. Vui lòng thử lại!</div>`;
	                });
	            }

	            // Chạy câu hỏi đầu tiên luôn
	            window.renderSurveyQuestion();
	        } else if (featureName === 'roadmap') {
	            // Khởi tạo khung giao diện trống (Giữ nguyên cấu trúc giao diện mẫu của bạn)
	            space.innerHTML = `
	                <h2>🗺️ Lộ Trình Học Tập Cá Nhân Hóa</h2>
	                <p style="color: #666; margin-bottom: 20px;">Dựa trên kết quả đánh giá kỹ năng, hệ thống đã tối ưu lộ trình học tập riêng cho bạn.</p>
	                
	                <div style="display: flex; gap: 20px; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;">
	                    
	                    <div style="flex: 6; background: #ffffff; border: 1px solid #eee; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.02);">
	                        <h3 style="margin-top: 0; color: #333; border-bottom: 2px solid #fafafa; padding-bottom: 10px;">Cấu Trúc Lộ Trình</h3>
	                        <div id="roadmap-tree-container" class="roadmap-tree" style="display: flex; flex-direction: column; gap: 25px; margin-top: 15px;">
	                            <div style="text-align: center; padding: 20px; color: #666;">
	                                <div style="display: inline-block; width: 20px; height: 20px; border: 2px solid rgba(0,122,255,0.15); border-radius: 50%; border-top-color: #007aff; animation: spin 0.75s linear infinite; margin-right: 8px; vertical-align: middle;"></div>
	                                Đang tải lộ trình học tập cá nhân...
	                            </div>
	                        </div>
	                    </div>

	                    <div style="flex: 4; background: #ffffff; border: 1px solid #eee; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.02); height: fit-content; position: sticky; top: 20px;">
	                        <h3 style="margin-top: 0; color: #333; border-bottom: 2px solid #fafafa; padding-bottom: 10px;">📖 Tài Liệu Bài Học</h3>
	                        
	                        <div id="roadmap-resource-panel" style="min-height: 160px; display: flex; flex-direction: column; justify-content: center; align-items: center; color: #999; font-size: 13.5px; text-align: center; border: 2px dashed #ddd; border-radius: 6px; padding: 15px;">
	                            <div style="font-size: 28px; margin-bottom: 6px;">💡</div>
	                            Chọn một nút kỹ năng bất kỳ trong sơ đồ lộ trình để hiển thị kho tài liệu tương ứng.
	                        </div>
	                    </div>

	                </div>
	                <style>@keyframes spin { to { transform: rotate(360deg); } }</style>
	            `;

	            // TỰ ĐỘNG KÍCH HOẠT: Gọi API lấy các kỹ năng NOT_STARTED ngay khi mở tab Roadmap
	            if (typeof window.loadRoadmapTreeData === 'function') {
	                window.loadRoadmapTreeData();
	            }
	        }
	       
	        
	        else {
	            space.innerHTML = "<h2>Nghiệp vụ: " + featureName + "</h2><p>Hệ thống đang sẵn sàng tích hợp logic từ Service cốt lõi của bạn.</p>";
	        }
	        
	    }
	    function submitJobTarget() {
	        let selectBox = document.getElementById('sl-job-role');
	        if (!selectBox) return;
	        
	        let selectedTarget = selectBox.value;
	        
	        if (selectedTarget === "") {
	            alert("⚠️ Vui lòng chọn một mục tiêu nghề nghiệp cụ thể từ danh sách!");
	            return;
	        }
	        
	        let params = new URLSearchParams();
	        params.append('selectedTarget', selectedTarget);
	        
	        // Gọi đúng đường dẫn API mới đồng bộ cấu trúc
	        fetch('${pageContext.request.contextPath}/api/profile/updateTarget', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/x-www-form-urlencoded'
	            },
	            body: params.toString()
	        })
	        .then(res => {
	            if (!res.ok) {
	                throw new Error("Lỗi HTTP: " + res.status);
	            }
	            return res.text(); // Đọc dữ liệu chữ thô trả về từ @ResponseBody
	        })
	        .then(result => {
	            // So khớp các chuỗi trả về chuẩn kiểu chữ thường của dự án bạn
	            if (result === "success") {
	                alert("🎉 Cập nhật mục tiêu nghề nghiệp thành công!");
	                
	                // Cập nhật text hiển thị trực tiếp trên giao diện box đen
	                let lblJobRole = document.getElementById('lbl-job-role');
	                if (lblJobRole) {
	                    lblJobRole.innerText = selectedTarget;
	                }
	            } else if (result === "error_auth") {
	                alert("⚠️ Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại!");
	                window.location.href = "${pageContext.request.contextPath}/login";
	            } else {
	                alert("❌ Cập nhật thất bại. Vui lòng thử lại sau!");
	            }
	        })
	        .catch(err => {
	            console.error("Lỗi xử lý AJAX cập nhật mục tiêu:", err);
	            alert("❌ Đã xảy ra lỗi kết nối đến hệ thống!");
	        });
	    }
	    function submitMssv() {
	        let mssvValue = document.getElementById('txt-mssv').value.trim();
	        
	        if (!mssvValue) {
	            alert('Vui lòng nhập Mã số sinh viên trước khi bấm cập nhật!');
	            return;
	        }
	        
	
	        let params = new URLSearchParams();
	        params.append('mssv', mssvValue);

	       
	        fetch('${pageContext.request.contextPath}/api/profile/updateMssv', {
	            method: 'POST',
	            body: params
	        })
	        .then(res => res.text()) 
	        .then(status => {
	            if (status === 'success') {
	                alert('🎉 Cập nhật Mã số sinh viên thành công!');
	                
	              
	                let sidebarMssv = document.querySelector('.profile-card p');
	                if (sidebarMssv) sidebarMssv.innerText = "MSSV: " + mssvValue;
	                
	               
	                let infoBoxMssv = document.querySelector('#view-space div strong:nth-of-type(2)');
	                if (infoBoxMssv) infoBoxMssv.innerText = mssvValue;
	                
	            } else if (status === 'error_auth') {
	                alert('Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại!');
	                window.location.href = '${pageContext.request.contextPath}/login';
	            } else {
	                alert('❌ Có lỗi xảy ra trong quá trình lưu dữ liệu dưới DB!');
	            }
	        })
	        .catch(err => {
	            console.error("Lỗi kết nối API:", err);
	            alert('Không thể kết nối đến máy chủ.');
	        });
	    }
	    function generateToken() {
	        if (!confirm('Đã sinh mã token')) {
	            return;
	        }

	        fetch('${pageContext.request.contextPath}/api/profile/generateToken', {
	            method: 'POST'
	        })
	        .then(res => res.text())
	        .then(result => {
	            if (result === 'error_auth') {
	                alert('Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại!');
	                window.location.href = '${pageContext.request.contextPath}/login';
	            } else if (result === 'error') {
	                alert('❌ Có lỗi xảy ra trong quá trình sinh mã dữ liệu dưới DB!');
	            } else {
	                alert('🎉 Sinh mã Token E-Portfolio thành công!');
	                
	              
	                let portfolioLink = document.querySelector('#view-space div a');
	                if (portfolioLink) {
	                    portfolioLink.innerText = "http://smart-career.vn/portfolio/" + result;
	                    portfolioLink.href = "http://smart-career.vn/portfolio/" + result; // Cập nhật cả link thẻ a
	                }
	            }
	        })
	        .catch(err => {
	            console.error("Lỗi kết nối API:", err);
	            alert('Không thể kết nối đến máy chủ.');
	        });
	    }
	    
	    //
	    function submitGithub() {
        let githubValue = document.getElementById('txt-github').value.trim();
        
        if (!githubValue) {
            alert('Vui lòng nhập tên tài khoản GitHub trước!');
            return;
        }
        
        let params = new URLSearchParams();
        params.append('github', githubValue);

        fetch('${pageContext.request.contextPath}/api/profile/linkGithub', {
            method: 'POST',
            body: params
        })
        .then(res => res.text())
        .then(status => {
            if (status === 'success') {
                alert('🎉 Kết nối tài khoản GitHub và đồng bộ danh sách dự án thành công!');
                
                // Cập nhật text hiển thị trực tiếp lên khung đen lề giữa
                let infoBoxGithub = document.querySelector('#view-space div strong:nth-of-type(3)');
                if (infoBoxGithub) infoBoxGithub.innerText = githubValue;
                
            } else if (status === 'error_auth') {
                alert('Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại!');
                window.location.href = '${pageContext.request.contextPath}/login';
            } else {
                alert('❌ Có lỗi xảy ra trong quá trình lưu dữ liệu dưới DB!');
            }
        })
        .catch(err => {
            console.error("Lỗi kết nối API:", err);
            alert('Không thể kết nối đến máy chủ.');
        });
    }
	    window.loadRoadmapTreeData = function() {
	        const treeContainer = document.getElementById('roadmap-tree-container');
	        if (!treeContainer) return;

	        fetch('${pageContext.request.contextPath}/load-not-started-skills', {
	            method: 'POST',
	            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
	        })
	        .then(res => res.json())
	        .then(allNodesFromDb => {
	            // =================================================================
	            // 🔎 ĐOẠN IN RA CONSOLE MÀN HÌNH THEO YÊU CẦU CỦA BẠN:
	            console.log("=== [ROADMAP DATA DƯỚI CONTROLLER TRẢ VỀ] ===");
	            console.log(allNodesFromDb);
	            console.log("=============================================");
	            // =================================================================

	            if (!allNodesFromDb || allNodesFromDb.length === 0) {
	                treeContainer.innerHTML = `<p style="color: #999; padding: 20px; text-align: center;">🎉 Tuyệt vời! Bạn không có kỹ năng nào ở trạng thái cần học tập.</p>`;
	                return;
	            }

	            // THUẬT TOÁN HIỂN THỊ CẢI TIẾN:
	            // Bước 1: Tìm các nút độc lập hoặc nút cấp cao nhất trong danh sách nhận được
	            // Nếu nút đó có parentId nhưng nút cha của nó không nằm trong danh sách NOT_STARTED, ta coi nó là nút gốc tạm thời để vẽ giao diện.
	            const nodeIds = allNodesFromDb.map(n => n.id);
	            const rootDisplayNodes = allNodesFromDb.filter(n => n.parentId === null || n.parentId === 0 || !nodeIds.includes(n.parentId));
	            
	            let treeHtml = '';

	            rootDisplayNodes.forEach((parent, index) => {
	                // Tìm các nút con (nếu có) phụ thuộc vào nút hiển thị này
	                const children = allNodesFromDb.filter(n => n.parentId === parent.id);
	                const themeColor = index % 2 === 0 ? '#ff3b30' : '#007aff';
	                const boxColor = index % 2 === 0 ? 'rgba(255,59,48,0.2)' : 'rgba(0,122,255,0.2)';

	                treeHtml += `
	                    <div style="border-left: 2px dashed \${themeColor}; padding-left: 15px; margin-left: 10px;">
	                        <div onclick="previewRoadmapResource(\${parent.id}, \`\${parent.name}\`)" 
	                             style="font-size: 15px; font-weight: bold; color: #fff; background: \${themeColor}; padding: 12px 16px; border-radius: 6px; cursor: pointer; display: inline-block; box-shadow: 0 2px 4px \${boxColor}; transition: all 0.2s;">
	                            📌 \${index + 1}. \${parent.name}
	                        </div>
	                `;

	                if (children.length > 0) {
	                    treeHtml += `
	                        <div style="display: flex; flex-wrap: wrap; gap: 10px; margin-top: 12px; padding-left: 15px;">
	                    `;
	                    
	                    children.forEach((child, cIndex) => {
	                        treeHtml += `
	                            <div onclick="previewRoadmapResource(\${child.id}, \`\${child.name}\`)" 
	                                 style="font-size: 13px; font-weight: 500; color: #333; background: #fafafa; border: 1px solid #ddd; padding: 8px 12px; border-radius: 4px; cursor: pointer; transition: all 0.2s;">
	                                🔹 \${index + 1}.\${cIndex + 1} \${child.name}
	                            </div>
	                        `;
	                    });

	                    treeHtml += `
	                        </div>
	                    `;
	                }

	                treeHtml += `
	                    </div>
	                `;
	            });

	            treeContainer.innerHTML = treeHtml;
	        })
	        .catch(err => {
	            console.error("Lỗi kết nối API:", err);
	            treeContainer.innerHTML = `<p style="color: #ff3b30; text-align: center; padding: 20px;">❌ Lỗi kết nối không thể tải cấu trúc lộ trình.</p>`;
	        });
	    };

	    // =========================================================================
	    // HÀM B: GỌI API LẤY TÀI LIỆU BÀI HỌC KHI CLICK VÀO NÚT KỸ NĂNG (BÊN PHẢI)
	    window.previewRoadmapResource = function(nodeId, nodeName) {
	        const panel = document.getElementById('roadmap-resource-panel');
	        if (!panel) return;
	
	        // Tạo hiệu ứng Loading mượt mà
	        panel.style.border = "1px solid #eee";
	        panel.style.justifyContent = "flex-start";
	        panel.style.alignItems = "stretch";
	        panel.innerHTML = `
	            <div style="text-align: center; padding: 25px 10px; color: #007aff;">
	                <div style="display: inline-block; width: 22px; height: 22px; border: 2.5px solid rgba(0,122,255,0.15); border-radius: 50%; border-top-color: #007aff; animation: roadmapSpin 0.75s linear infinite; margin-bottom: 8px;"></div>
	                <div style="font-weight: 500; font-size: 13px; color: #666;">Đang tải kho tài liệu...</div>
	                <div style="font-weight: bold; color: #222; margin-top: 5px; font-size: 14px;">\${nodeName}</div>
	            </div>
	            <style>@keyframes roadmapSpin { to { transform: rotate(360deg); } }</style>
	        `;
	
	        // Truyền tham số nodeId đơn lẻ lên Server (Khớp chuẩn RequestParam Backend)
	        const params = new URLSearchParams();
	        params.append('nodeId', nodeId);
	
	        // Gọi trực tiếp đến API mới tạo
			fetch('${pageContext.request.contextPath}/load-roadmap-resources-new', {
			    method: 'POST',
			    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
			    body: params
			})
	        .then(res => res.json())
	        .then(resources => {
	            let resourcesHtml = '';
	
	            if (!resources || resources.length === 0) {
	                resourcesHtml = `
	                    <div style="text-align: center; padding: 20px; color: #999; font-style: italic; border: 1px dashed #eee; border-radius: 4px;">
	                        📭 Hiện tại chưa có link tài liệu được gán trực tiếp cho mục này.
	                    </div>`;
	            } else {
	                resources.forEach(r => {
	                    const icon = (r.type === 'VIDEO' || r.type === 'video') ? '🎥' : '📚';
	                    resourcesHtml += `
	                        <div style="font-size: 13px; background: #fafafa; border: 1px solid #eee; padding: 10px; border-radius: 4px; display: flex; align-items: center; justify-content: space-between; margin-bottom: 8px;">
	                            <span style="text-align: left; padding-right: 10px;">\${icon} <strong>[\${r.type}]</strong> \${r.title}</span>
	                            <a href="\${r.url}" target="_blank" style="color: #007aff; font-weight: bold; text-decoration: none; white-space: nowrap;">Học ngay →</a>
	                        </div>
	                    `;
	                });
	            }
	
	            panel.innerHTML = `
	                <div style="margin-bottom: 15px; padding-bottom: 8px; border-bottom: 1px solid #eee; text-align: left;">
	                    <span style="font-size: 11px; font-weight: bold; color: #fff; background: #ff9500; padding: 2px 6px; border-radius: 3px; margin-right: 6px;">Cần học tập</span>
	                    <strong style="color: #111; font-size: 14px;">\${nodeName}</strong>
	                </div>
	                <div style="display: flex; flex-direction: column;">
	                    \${resourcesHtml}
	                </div>
	            `;
	        })
	        .catch(err => {
	            console.error("Lỗi nạp tài liệu từ API mới:", err);
	            panel.innerHTML = `<div style="color: #ff3b30; font-size: 13px; padding: 20px; text-align: center;">❌ Không thể kết nối để lấy tài liệu học tập.</div>`;
	        });
	    };
	    let aiChatSessions = []; 
		let activeSessionId = null;
		
		function initChatState() {
		    const sidebarBox = document.getElementById('ai-sessions-list-box');
		    if (!sidebarBox) return;

		    sidebarBox.innerHTML = `<div style="text-align:center; color:#666; font-size:12px; margin-top:20px;">🔄 Đang tải lịch sử...</div>`;

		    // Gọi API 1: Chỉ lấy danh sách Session danh mục bên trái
		    fetch('/Project_Group_Web/get-chat-sessions', {
		        method: 'GET'
		    })
		    .then(res => res.json())
		    .then(data => {
		        aiChatSessions = data; // Lưu danh sách các session {sessionId, title} vào biến toàn cục

		        // Vẽ danh sách phiên ra cột bên trái (Hàm render này ta sẽ viết ở bước kế tiếp)
		        if (typeof renderChatSessions === 'function') {
		            renderChatSessions();
		        }

		        // Nếu có danh sách phiên, tự động chọn và kích hoạt tải tin nhắn của phiên đầu tiên
		        if (aiChatSessions.length > 0) {
		            switchChatSession(aiChatSessions[0].sessionId);
		        } else {
		            // Nếu trống lịch sử hoàn toàn
		            document.getElementById('current-session-title').innerText = "Chưa có phiên hội thoại";
		            document.getElementById('ai-chat-messages-box').innerHTML = `<div style="text-align:center;color:#999;margin-top:40px;font-size:13px;font-style:italic;">👋 Hãy bấm nút phía trên để tạo phiên chat mới nhé!</div>`;
		        }
		    })
		    .catch(err => {
		        console.error("Lỗi khi lấy danh sách phiên:", err);
		        sidebarBox.innerHTML = `<div style="text-align:center; color:#ff3b30; font-size:11px; margin-top:20px;">❌ Lỗi tải lịch sử</div>`;
		    });
		}
		// Hàm 1: Vẽ danh sách các phiên chat lên cột bên trái (Sidebar)
		// Thay thế đoạn renderChatSessions() cũ bằng đoạn an toàn này:
		function renderChatSessions() {
		    const sidebarBox = document.getElementById('ai-sessions-list-box');
		    if (!sidebarBox) return;
		
		    let html = '';
		    
		    // In thử ra console trình duyệt (F12) để bạn kiểm tra cấu trúc dữ liệu thực tế
		    console.log(">> Dữ liệu các phiên nhận được tại Front-end:", aiChatSessions);
		
		    aiChatSessions.forEach(session => {
		        // So sánh ép kiểu an toàn
		        const isActive = (session.sessionId == activeSessionId);
		        
		        // Thiết lập class active để nhận CSS từ thẻ <style>
		        const activeClass = isActive ? 'session-item active' : 'session-item';
		
		        // Khắc phục việc Backend trả về thuộc tính title hoặc viết hoa chữ đầu nếu có
		        let displayTitle = session.title || session.Title;
		        
		        if (!displayTitle) {
		            displayTitle = "Phiên số #" + session.sessionId; // Nếu title trống, hiển thị ID tạm thời để kiểm tra
		        }
		
		        html += `
		            <div id="session-\${session.sessionId}" 
		                 onclick="switchChatSession(\${session.sessionId})" 
		                 class="\${activeClass}" 
		                 style="padding: 12px; margin-bottom: 4px; border-radius: 6px; cursor: pointer; display: flex; align-items: center; gap: 10px; width: 100%; box-sizing: border-box;">
		                
		                <span style="font-size: 14px; flex-shrink: 0; user-select: none;">💬</span>
		                
		                <span class="session-title-text" style="flex: 1; text-align: left; font-size: 13px; font-weight: 500; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; min-width: 0;">
		                    \${displayTitle}
		                </span>
		            </div>
		        `;
		    });
		
		    sidebarBox.innerHTML = html;
		}

		// Hàm 2: Khi user bấm click chọn một phiên chat cụ thể
		function switchChatSession(sessionId) {
		    if (!sessionId) return;
		    activeSessionId = sessionId;
		    
		    // Cập nhật class active cho sidebar
		    renderChatSessions();
		
		    // Sửa so sánh an toàn bằng dấu == để tránh lệch kiểu dữ liệu chuỗi/số
		    const currentSession = aiChatSessions.find(s => s.sessionId == sessionId);
		    const titleHeader = document.getElementById('current-session-title');
		    if (titleHeader) {
		        titleHeader.innerText = currentSession ? currentSession.title : "Nội dung cuộc hội thoại";
		    }
		
		    const msgBox = document.getElementById('ai-chat-messages-box');
		    if (!msgBox) return;
		
		    msgBox.innerHTML = `<div style="text-align:center; color:#666; font-size:12px; margin-top:30px;">🔄 Đang tải tin nhắn...</div>`;
		
		    // Gọi API lấy tin nhắn chi tiết
		    fetch('/Project_Group_Web/get-chat-messages?sessionId=' + sessionId, {
		        method: 'GET'
		    })
		    .then(res => {
		        if (!res.ok) throw new Error("HTTP error " + res.status);
		        return res.json();
		    })
		    .then(messages => {
		        let html = '';
		        
		        if (!messages || messages.length === 0) {
		            html = `<div style="text-align:center;color:#999;margin-top:40px;font-size:12px;font-style:italic;">Chưa có tin nhắn nào trong phiên này. Hãy nhập câu hỏi bên dưới nhé!</div>`;
		        } else {
		        	messages.forEach(msg => {
		        	    let rawText = msg.text || "";
		        	    
		        	    // Ép toàn bộ chuỗi về chữ thường (Ví dụ: 'AI' -> 'ai', 'USER' -> 'user')
		        	    let senderType = (msg.sender || "user").toLowerCase().trim();
		        	    
		        	    let safeText = rawText.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
		        	    let formattedText = safeText.replace(/\*\*(.*?)\*\*/g, '<strong>\$1</strong>').replace(/\n/g, '<br/>');

		        	    // Lúc này so sánh chắc chắn sẽ trúng 100%
		        	    if (senderType === 'ai') {
		        	        // Hiển thị khung chat của AI nằm bên TRÁI
		        	        html += `
		        	            <div style="display: flex; gap: 10px; max-width: 85%; align-self: flex-start;">
		        	                <div style="width: 30px; height: 30px; background: #1e1e24; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 14px; flex-shrink: 0; user-select:none;">🤖</div>
		        	                <div style="background: #ffffff; border: 1px solid #eee; padding: 10px 12px; border-radius: 4px; border-top-left-radius: 0; color: #333; font-size: 13px; line-height: 1.5; text-align: left; box-shadow: 0 1px 2px rgba(0,0,0,0.03);">
		        	                    \${formattedText}
		        	                </div>
		        	            </div>
		        	        `;
		        	    } else {
		        	        // Hiển thị khung chat của USER nằm bên PHẢI
		        	        html += `
		        	            <div style="display: flex; gap: 10px; max-width: 85%; align-self: flex-end; flex-direction: row-reverse;">
		        	                <div style="width: 30px; height: 30px; background: #ff3b30; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 12px; font-weight: bold; color: #fff; flex-shrink: 0; user-select:none;">A</div>
		        	                <div style="background: #ff3b30; color: #ffffff; padding: 10px 12px; border-radius: 4px; border-top-right-radius: 0; font-size: 13px; line-height: 1.5; text-align: left; box-shadow: 0 1px 2px rgba(0,0,0,0.05);">
		        	                    \${formattedText}
		        	                </div>
		        	            </div>
		        	        `;
		        	    }
		        	});
		        }
		        msgBox.innerHTML = html;
		        msgBox.scrollTop = msgBox.scrollHeight; // Cuộn mượt xuống tin nhắn cuối cùng
		    })
		    .catch(err => {
		        console.error("Lỗi render tin nhắn:", err);
		        msgBox.innerHTML = `<div style="text-align:center; color:#ff3b30; font-size:11px; margin-top:20px;">❌ Lỗi tải nội dung tin nhắn</div>`;
		    });
		}
		function handleSendChatMessage() {
		    const inputEl = document.getElementById('ai-chat-user-input');
		    if (!inputEl) return;
		    
		    const text = inputEl.value.trim();
		    if (!text) return; // Không gửi nếu chuỗi rỗng
		    
		    if (!activeSessionId) {
		        alert("Vui lòng chọn hoặc tạo một phiên chat mới ở thanh bên trái trước khi đặt câu hỏi!");
		        return;
		    }
		    
		    const msgBox = document.getElementById('ai-chat-messages-box');
		    if (!msgBox) return;
		    
		    // Xóa chữ "Chưa có tin nhắn nào..." nếu đây là tin nhắn đầu tiên
		    if (msgBox.innerHTML.includes("Chưa có tin nhắn nào")) {
		        msgBox.innerHTML = '';
		    }
		    
		    // --- ĐÈ TIN NHẮN CỦA NGƯỜI DÙNG LÊN GIAO DIỆN NGAY LẬP TỨC ---
		    let safeUserText = text.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
		    let formattedUserText = safeUserText.replace(/\n/g, '<br/>');
		    
		    msgBox.innerHTML += `
		        <div style="display: flex; gap: 10px; max-width: 85%; align-self: flex-end; flex-direction: row-reverse;">
		            <div style="width: 30px; height: 30px; background: #ff3b30; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 12px; font-weight: bold; color: #fff; flex-shrink: 0; user-select:none;">A</div>
		            <div style="background: #ff3b30; color: #ffffff; padding: 10px 12px; border-radius: 4px; border-top-right-radius: 0; font-size: 13px; line-height: 1.5; text-align: left; box-shadow: 0 1px 2px rgba(0,0,0,0.05);">
		                \${formattedUserText}
		            </div>
		        </div>
		    `;
		    
		    // Xóa nội dung ô input sau khi gửi
		    inputEl.value = '';
		    msgBox.scrollTop = msgBox.scrollHeight;
		    
		    // --- TẠO HIỆU ỨNG AI ĐANG SUY NGHĨ TẠM THỜI ---
		    const loadingId = "ai-loading-temp";
		    msgBox.innerHTML += `
		        <div id="\${loadingId}" style="display: flex; gap: 10px; max-width: 85%; align-self: flex-start;">
		            <div style="width: 30px; height: 30px; background: #1e1e24; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 14px; flex-shrink: 0; user-select:none;">🤖</div>
		            <div style="background: #ffffff; border: 1px solid #eee; padding: 10px 12px; border-radius: 4px; border-top-left-radius: 0; color: #777; font-size: 13px; font-style: italic; line-height: 1.5; text-align: left;">
		                🔄 Trợ lý ảo đang suy nghĩ...
		            </div>
		        </div>
		    `;
		    msgBox.scrollTop = msgBox.scrollHeight;
		    
		    // --- GỌI API SUBMIT LÊN SERVER ---
		    const params = new URLSearchParams();
		    params.append('sessionId', activeSessionId);
		    params.append('text', text);
		    
		    fetch('/Project_Group_Web/submit-ai-chat', {
		        method: 'POST',
		        headers: {
		            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
		        },
		        body: params
		    })
		    .then(res => {
		        if (!res.ok) throw new Error("Lỗi kết nối server");
		        return res.json(); // Nhận dữ liệu dạng JSON
		    })
		    .then(data => {
		        // Xóa bỏ khung loading tạm thời
		        const tempLoading = document.getElementById(loadingId);
		        if (tempLoading) tempLoading.remove();
		        
		        if (data.error) {
		            msgBox.innerHTML += `<div style="text-align:center; color:#ff3b30; font-size:11px;">❌ ${data.error}</div>`;
		            return;
		        }

		        // =========================================================================
		        // 🚀 LOGIC MỚI: CẬP NHẬT TIÊU ĐỀ SIDEBAR & HEADER CHAT LẬP TỨC
		        // =========================================================================
		        if (data.isTitleUpdated && data.newTitle) {
		            // 1. Cập nhật lại trong mảng dữ liệu local JavaScript
		            const currentSession = aiChatSessions.find(s => s.sessionId == activeSessionId);
		            if (currentSession) {
		                currentSession.title = data.newTitle;
		            }
		            
		            // 2. Thay đổi tiêu đề ở Header khung chat bên phải luôn
		            const titleHeader = document.getElementById('current-session-title');
		            if (titleHeader) {
		                titleHeader.innerText = data.newTitle;
		            }
		            
		            // 3. Render lại sidebar bên trái để cập nhật chữ hiển thị mới
		            renderChatSessions();
		        }
			    // Thay đoạn cũ của bạn bằng đoạn này:
			    // 1. Xử lý các ký tự HTML đặc biệt để tránh lỗi render
			    let safeAiText = data.aiResponse.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
	
			    // 2. GIẢI MÃ: Đổi ký tự xuống dòng (\\n) thành <br/> và xử lý in đậm
			     // Lưu ý dấu \\n trong regex này để khớp với dữ liệu đã thoát từ Java
			    let formattedAiText = safeAiText.replace(/\\n/g, '<br/>')
			                                     .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
	
			     // 3. Render câu trả lời thật của AI
		        msgBox.innerHTML += `
		            <div style="display: flex; gap: 10px; max-width: 85%; align-self: flex-start;">
		                <div style="width: 30px; height: 30px; background: #1e1e24; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 14px; flex-shrink: 0; user-select:none;">🤖</div>
		                <div style="background: #ffffff; border: 1px solid #eee; padding: 10px 12px; border-radius: 4px; border-top-left-radius: 0; color: #333; font-size: 13px; line-height: 1.5; text-align: left; box-shadow: 0 1px 2px rgba(0,0,0,0.03);">
		                    \${formattedAiText}
		                </div>
		            </div>
		        `;
		        msgBox.scrollTop = msgBox.scrollHeight;
		    })
		    .catch(err => {
		        console.error("Lỗi khi chat:", err);
		        const tempLoading = document.getElementById(loadingId);
		        if (tempLoading) tempLoading.remove();
		        
		        msgBox.innerHTML += `<div style="text-align:center; color:#ff3b30; font-size:11px;">❌ Không thể kết nối tới máy chủ.</div>`;
		    });
		}
		function handleCreateNewChatSession() {
		    fetch('/Project_Group_Web/create-new-session', {
		        method: 'POST'
		    })
		    .then(res => {
		        if (!res.ok) throw new Error("Không thể tạo phiên chat mới");
		        return res.json();
		    })
		    .then(newSession => {
		        if (newSession.error) {
		            alert("Lỗi: " + newSession.error);
		            return;
		        }

		        // 1. Thêm phiên chat mới vừa tạo vào ĐẦU mảng danh sách (để nó hiện lên trên cùng sidebar)
		        aiChatSessions.unshift(newSession);

		        // 2. Kích hoạt chọn luôn phiên chat mới này
		        activeSessionId = newSession.sessionId;

		        // 3. Render lại danh sách sidebar bên trái để cập nhật giao diện hiện tại
		        renderChatSessions();

		        // 4. Đổi tiêu đề khung chat bên phải
		        const titleHeader = document.getElementById('current-session-title');
		        if (titleHeader) {
		            titleHeader.innerText = newSession.title;
		        }

		        // 5. Làm trống khung chat bên phải để chuẩn bị cho hội thoại mới
		        const msgBox = document.getElementById('ai-chat-messages-box');
		        if (msgBox) {
		            msgBox.innerHTML = `<div style="text-align:center;color:#999;margin-top:40px;font-size:12px;font-style:italic;">Phiên chat mới đã sẵn sàng. Hãy nhập câu hỏi đầu tiên của bạn bên dưới!</div>`;
		        }
		    })
		    .catch(err => {
		        console.error("Lỗi tạo session:", err);
		        alert("Có lỗi xảy ra khi tạo cuộc hội thoại mới.");
		    });
		}
		
		// =========================================================================
        // HÀM XỬ LÝ API CHO PHẦN 6: KHẢO SÁT THỊ TRƯỜNG (JOB TREND)
        // =========================================================================
        window.handlePortalChange = function(portalName) {
            const tableBody = document.getElementById('job-trend-table-body');
            const statusBadge = document.getElementById('survey-status-badge');
            
            if(!tableBody) return;

            // Hiển thị trạng thái đang tải
            statusBadge.innerHTML = `<span style="display: inline-block; width: 8px; height: 8px; background: #f5b041; border-radius: 50%;"></span> Đang phân tích...`;
            statusBadge.style.background = "#fef5e7";
            statusBadge.style.color = "#d68910";

            // Thay đổi đường dẫn /api/job-trends cho khớp với Controller của bạn nếu cần
            fetch('${pageContext.request.contextPath}/api/job-trends?portal=' + portalName)
            .then(res => {
                if (!res.ok) throw new Error("HTTP " + res.status);
                return res.json();
            })
            .then(data => {
                tableBody.innerHTML = '';
                
                // Trường hợp API trả về mảng rỗng
                if (!data || data.length === 0) {
                    tableBody.innerHTML = `<tr><td colspan="5" style="text-align: center; padding: 30px; color: #dc3545; font-size: 14px; background: #f8d7da;">⚠️ Không có dữ liệu khảo sát cho nền tảng <b>\${portalName}</b>.</td></tr>`;
                    statusBadge.innerHTML = `<span style="display: inline-block; width: 8px; height: 8px; background: #dc3545; border-radius: 50%;"></span> Trống`;
                    statusBadge.style.background = "#f8d7da";
                    statusBadge.style.color = "#dc3545";
                    return;
                }

                // Sắp xếp giảm dần theo tần suất (đề phòng API chưa sort)
                data.sort((a, b) => b.frequencyCount - a.frequencyCount);
                
                // Lấy giá trị lớn nhất làm mốc 100% cho biểu đồ
                const maxCount = data[0].frequencyCount; 

                let htmlContent = '';
                data.forEach((item, index) => {
                    const percentage = maxCount > 0 ? (item.frequencyCount / maxCount) * 100 : 0;
                    
                    // Xử lý huy hiệu Top 1, 2, 3 chuyên nghiệp
                    let rankHtml = '';
                    let barColor = '#007aff'; // Màu mặc định cho biểu đồ
                    if (index === 0) {
                        rankHtml = `<div style="background: #ffe0e0; color: #dc3545; font-weight: bold; width: 30px; height: 30px; line-height: 30px; border-radius: 50%; margin: 0 auto; box-shadow: 0 2px 4px rgba(220,53,69,0.2);">1</div>`;
                        barColor = '#dc3545';
                    } else if (index === 1) {
                        rankHtml = `<div style="background: #fff3cd; color: #ff9500; font-weight: bold; width: 30px; height: 30px; line-height: 30px; border-radius: 50%; margin: 0 auto; box-shadow: 0 2px 4px rgba(255,149,0,0.2);">2</div>`;
                        barColor = '#ff9500';
                    } else if (index === 2) {
                        rankHtml = `<div style="background: #d1e7dd; color: #28a745; font-weight: bold; width: 30px; height: 30px; line-height: 30px; border-radius: 50%; margin: 0 auto; box-shadow: 0 2px 4px rgba(40,167,69,0.2);">3</div>`;
                        barColor = '#28a745';
                    } else {
                        rankHtml = `<div style="color: #6c757d; font-weight: 600;">\${index + 1}</div>`;
                    }

                    // Xử lý an toàn định dạng LocalDate của Java khi trả về JSON (có thể là String hoặc mảng [YYYY, MM, DD])
                    let dateStr = item.recordedDate;
                    if (Array.isArray(item.recordedDate)) {
                        dateStr = `\${item.recordedDate[0]}-\${String(item.recordedDate[1]).padStart(2, '0')}-\${String(item.recordedDate[2]).padStart(2, '0')}`;
                    }

                    htmlContent += `
                        <tr style="border-bottom: 1px solid #e9ecef; transition: background 0.2s;" onmouseover="this.style.background='#f8f9fa'" onmouseout="this.style.background='transparent'">
                            <td style="padding: 16px; text-align: center;">\${rankHtml}</td>
                            <td style="padding: 16px; font-weight: 600; color: #212529; font-size: 15px;">\${item.skillName}</td>
                            <td style="padding: 16px; text-align: right; font-weight: 500; color: #495057;">
                                \${item.frequencyCount.toLocaleString()} <span style="font-size: 12px; color: #adb5bd;">lượt</span>
                            </td>
                            <td style="padding: 16px;">
                                <div style="display: flex; align-items: center; gap: 10px;">
                                    <div style="flex: 1; background: #e9ecef; border-radius: 10px; height: 8px; overflow: hidden;">
                                        <div style="background: \${barColor}; height: 100%; width: \${percentage}%; border-radius: 10px; transition: width 0.5s ease;"></div>
                                    </div>
                                    <span style="font-size: 12px; font-weight: 600; color: \${barColor}; width: 35px; text-align: right;">\${Math.round(percentage)}%</span>
                                </div>
                            </td>
                            <td style="padding: 16px; text-align: center; color: #6c757d; font-size: 13px;">\${dateStr || '-'}</td>
                        </tr>
                    `;
                });
                
                tableBody.innerHTML = htmlContent;

                // Cập nhật trạng thái thành công
                statusBadge.innerHTML = `<span style="display: inline-block; width: 8px; height: 8px; background: #1e8e3e; border-radius: 50%;"></span> Cập nhật thành công`;
                statusBadge.style.background = "#e6f4ea";
                statusBadge.style.color = "#1e8e3e";
            })
            .catch(err => {
                console.error("Lỗi tải xu hướng thị trường:", err);
                tableBody.innerHTML = `<tr><td colspan="5" style="text-align: center; padding: 30px; color: #dc3545; font-size: 14px;">❌ Đã xảy ra lỗi khi tải dữ liệu từ máy chủ. Vui lòng kiểm tra lại kết nối hoặc API.</td></tr>`;
                statusBadge.innerHTML = `<span style="display: inline-block; width: 8px; height: 8px; background: #dc3545; border-radius: 50%;"></span> Lỗi kết nối`;
                statusBadge.style.background = "#f8d7da";
                statusBadge.style.color = "#dc3545";
            });
        };
		
	</script>
</body>
</html>

