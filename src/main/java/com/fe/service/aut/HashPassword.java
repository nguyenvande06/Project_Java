package com.fe.service.aut;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword implements IHashPassword {

    @Override
    public String hash(String password) {
        if (password == null) {
            return null;
        }
        try {
            // thuật toán SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            byte[] encodedHash = digest.digest(password.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString(); 
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi: Không tìm thấy thuật toán băm SHA-256", e);
        }
    }
}