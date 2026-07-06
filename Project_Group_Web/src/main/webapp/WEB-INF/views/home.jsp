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
	    // Tự động kích hoạt hiển thị nghiệp vụ 1 khi vừa tải trang xong
	    window.onload = function() {
	        loadFeature('profile');
	    };
	
	    function loadFeature(featureName) {
	        // Đổi trạng thái active của các nút menu
	        let buttons = document.querySelectorAll('.tab-btn');
	        buttons.forEach(btn => btn.classList.remove('active'));
	        
	        if (event && event.target) {
	            event.target.classList.add('active');
	        } else {
	            // Tự động active nút đầu tiên lúc load trang
	            buttons[0].classList.add('active');
	        }
	        
	        let space = document.getElementById('view-space');
	        
	        // XỬ LÝ GIAO DIỆN CHO NGHIỆP VỤ 1 (GIAO DIỆN TĨNH)
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
	            space.innerHTML = "<h2>💬 Tương tác với Trợ lý ảo AI Virtual Mentor</h2><p>Khung chat AI và các hàm fetch gọi API AI xử lý sẽ đổ vào đây...</p>";
	        } else if (featureName === 'survey') {
	            space.innerHTML = "<h2>📊 Khảo sát thị trường tuyển dụng (Job Trend)</h2><p>Thẻ thả chọn Ngày quét từ DB và Bảng thống kê dữ liệu sẽ đổ vào đây...</p>";
	        } else if (featureName === 'target') {
	            // Lấy thông tin tên lộ trình hiện tại đã lưu trong Session (nếu có)
	            let sessionJobRole = "<%= session.getAttribute("JobRole") != null ? session.getAttribute("JobRole") : "Chưa cập nhật" %>";

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
	                                <option value="">⏳ Đang tải dữ liệu mục tiêu từ hệ thống...</option>
	                            </select>
	                        </div>

	                        <button onclick="submitJobTarget()" style="width: 100%; padding: 12px; background: #ff3b30; color: white; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 14px;">
	                            💾 Lưu cấu hình mục tiêu
	                        </button>
	                    </div>
	                </div>
	            `;

	            // ĐOẠN AJAX GỌI XUỐNG API ĐỂ ĐỔ DỮ LIỆU ĐỘNG VÀO BOX (ĐÃ BỎ DẤU CHÉO \)
	            fetch('${pageContext.request.contextPath}/api/profile/getTechPaths')
	                .then(res => {
	                    if (!res.ok) {
	                        throw new Error("Lỗi HTTP: " + res.status);
	                    }
	                    return res.json(); // Chuyển đổi dữ liệu nhận được thành mảng chuỗi JS
	                })
	                .then(data => {
	                    let selectBox = document.getElementById('sl-job-role');
	                    if (selectBox) {
	                        selectBox.innerHTML = ''; // Xóa dòng chữ đang tải cũ
	                        
	                        // Thêm tùy chọn mặc định ban đầu nếu chưa thiết lập mục tiêu
	                        if (sessionJobRole === "Chưa cập nhật" || sessionJobRole === "") {
	                            let defaultOpt = document.createElement('option');
	                            defaultOpt.value = "";
	                            defaultOpt.text = "-- Chọn một mục tiêu nghề nghiệp để bắt đầu --";
	                            selectBox.appendChild(defaultOpt);
	                        }

	                        // DUYỆT MẢNG CHUỖI TÊN (data là mảng các String mục tiêu nghề nghiệp)
	                        data.forEach(pathName => {
	                            let option = document.createElement('option');
	                            
	                            // Sử dụng trực tiếp biến pathName vì nó đã là một chuỗi văn bản thuần túy
	                            option.value = pathName; 
	                            option.text = pathName;
	                            
	                            // Tự động chọn nếu trùng với dữ liệu phiên làm việc hiện tại
	                            if (pathName === sessionJobRole) {
	                                option.selected = true;
	                            }
	                            selectBox.appendChild(option);
	                        });
	                    }
	                })
	                .catch(err => {
	                    console.error("Lỗi xử lý giao diện AJAX:", err);
	                    let selectBox = document.getElementById('sl-job-role');
	                    if (selectBox) {
	                        selectBox.innerHTML = '<option value="">❌ Không thể tải dữ liệu mục tiêu</option>';
	                    }
	                });
	        }
	        else {
	            space.innerHTML = "<h2>Nghiệp vụ: " + featureName + "</h2><p>Hệ thống đang sẵn sàng tích hợp logic từ Service cốt lõi của bạn.</p>";
	        }
	    }
	 	// Hàm xử lý gửi MSSV lên Server thông qua Ajax API
	    function submitMssv() {
	        let mssvValue = document.getElementById('txt-mssv').value.trim();
	        
	        // Kiểm tra tính hợp lệ dữ liệu nhập vào nhanh ở Front-end
	        if (!mssvValue) {
	            alert('Vui lòng nhập Mã số sinh viên trước khi bấm cập nhật!');
	            return;
	        }
	        
	        // Đóng gói dữ liệu dạng Form URL Encoded giống như submit Form truyền thống
	        let params = new URLSearchParams();
	        params.append('mssv', mssvValue);

	        // Bắn request dạng POST lên API vừa tạo ở Controller
	        fetch('${pageContext.request.contextPath}/api/profile/updateMssv', {
	            method: 'POST',
	            body: params
	        })
	        .then(res => res.text()) // Nhận phản hồi dạng chuỗi chữ thuần (success / error)
	        .then(status => {
	            if (status === 'success') {
	                alert('🎉 Cập nhật Mã số sinh viên thành công!');
	                
	                // Cách xử lý mượt mà: Cập nhật chữ trực tiếp trên giao diện không cần load lại trang
	                // 1. Đổi chữ ở thanh Sidebar lề trái
	                let sidebarMssv = document.querySelector('.profile-card p');
	                if (sidebarMssv) sidebarMssv.innerText = "MSSV: " + mssvValue;
	                
	                // 2. Đổi chữ ở khung đen hiển thị trạng thái hiện tại
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

	        // Bắn request dạng POST lên API sinh token vừa tạo ở Controller
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
	                
	                // Cập nhật lại đường dẫn URL trực tiếp trên khung thông tin đen lề giữa
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
	</script>
</body>
</html>

