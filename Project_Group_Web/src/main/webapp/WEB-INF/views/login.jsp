<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập hệ thống</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f6f9; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .card { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); width: 350px; }
        h2 { text-align: center; color: #333; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; color: #666; }
        input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background-color: #db2828; color: white; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; }
        button:hover { background-color: #b91c1c; }
        .error { color: #db2828; font-size: 14px; text-align: center; margin-bottom: 15px; }
        .success { color: #21ba45; font-size: 14px; text-align: center; margin-bottom: 15px; }
        .switch { text-align: center; margin-top: 15px; font-size: 14px; }
        .switch a { color: #db2828; text-decoration: none; font-weight: bold; }
    </style>
</head>
<body>
    <div class="card">
        <h2>HỆ THỐNG ĐĂNG NHẬP</h2>
        
        <div class="error">${error}</div>
        <div class="success">${message}</div>

        <form action="${pageContext.request.contextPath}/login" method="POST">
            <div class="form-group">
                <label>Email đăng nhập:</label>
                <input type="email" name="email" required placeholder="Nhập email của bạn...">
            </div>
            <div class="form-group">
                <label>Mật khẩu:</label>
                <input type="password" name="password" required placeholder="Nhập mật khẩu...">
            </div>
            <button type="submit">Đăng nhập</button>
        </form>
        
        <div class="switch">
            Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
        </div>
    </div>
</body>
</html>