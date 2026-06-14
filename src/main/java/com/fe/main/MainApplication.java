package com.fe.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MainApplication {
    public static void main(String[] args) {
        try {
            System.out.println("--- ĐANG KHỞI TẠO VÀ ÁNH XẠ XUỐNG DATABASE ---");
            
         
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAs");
            EntityManager em = emf.createEntityManager();
            
            System.out.println("--- ÁNH XẠ THÀNH CÔNG! KHÔNG CÓ LỖI XẢY RA ---");
            
            em.close();
            emf.close();
            
        } catch (Exception e) {
            System.err.println("--- THẤT BẠI! CÓ LỖI CẤU HÌNH HOẶC ÁNH XẠ ---");
            e.printStackTrace();
        }
    }
}