<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản</title>
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
        .switch { text-align: center; margin-top: 15px; font-size: 14px; }
        .switch a { color: #db2828; text-decoration: none; font-weight: bold; }
    </style>
</head>
<body>
    <div class="card">
        <h2>HỆ THỐNG ĐĂNG KÝ</h2>
        
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="POST">
            <div class="form-group">
                <label>Họ và tên:</label>
                <input type="text" name="fullName" required placeholder="Nhập họ và tên...">
            </div>
            <div class="form-group">
                <label>Email đăng ký:</label>
                <input type="email" name="email" required placeholder="Nhập email của bạn...">
            </div>
            <div class="form-group">
                <label>Mật khẩu:</label>
                <input type="password" name="password" required placeholder="Nhập mật khẩu (ít nhất 6 ký tự)...">
            </div>
            <div class="form-group">
                <label>Nhập lại mật khẩu:</label>
                <input type="password" name="confirmPassword" required placeholder="Nhập lại mật khẩu...">
            </div>
            <button type="submit">Đăng ký tài khoản</button>
        </form>
        
        <div class="switch">
            Đã có tài khoản? <a href="${pageContext.request.contextPath}/">Đăng nhập ngay</a>
        </div>
    </div>
</body>
</html>