package com.fe.service.survey;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import com.fe.pojo.JobTrend;

public class ServiceSurvey implements IServiceSurvey {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAs");

    @Override
    public List<LocalDate> getAvailableScanDates() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT DISTINCT j.recordedDate FROM JobTrend j ORDER BY j.recordedDate DESC", LocalDate.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<JobTrend> getTrendsByDate(LocalDate date) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT j FROM JobTrend j WHERE j.recordedDate = :date", JobTrend.class)
                     .setParameter("date", date)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void displayMarketSurveyReport(LocalDate selectedDate, LocalDate compareDate) {
        List<JobTrend> currentTrends = getTrendsByDate(selectedDate);
        List<JobTrend> pastTrends = getTrendsByDate(compareDate);

        if (currentTrends.isEmpty()) {
            System.out.println("❌ Không có dữ liệu khảo sát cho ngày " + selectedDate);
            return;
        }

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(" KẾT QUẢ PHÂN TÍCH TẦN SUẤT TỪ KHÓA TRONG NGÀY " + selectedDate);
        System.out.println("--------------------------------------------------------------------------------");

        // 1. Phân nhóm dữ liệu theo từng cổng tuyển dụng
        Map<String, List<JobTrend>> groupedByPortal = currentTrends.stream()
                .collect(Collectors.groupingBy(JobTrend::getJobPortal));

        for (Map.Entry<String, List<JobTrend>> entry : groupedByPortal.entrySet()) {
            String portalName = entry.getKey();
            List<JobTrend> trends = entry.getValue();

            int totalPortalCount = trends.stream().mapToInt(JobTrend::getFrequencyCount).sum();

            System.out.println("\n🌐 CỔNG TUYỂN DỤNG: " + portalName);
            for (JobTrend trend : trends) {
                double percentage = totalPortalCount > 0 ? ((double) trend.getFrequencyCount() / totalPortalCount) * 100 : 0.0;
                System.out.printf("  - %-15s : %d lượt xuất hiện (Chiếm %.1f%%)\n", 
                        trend.getSkillName(), trend.getFrequencyCount(), percentage);
            }
        }

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(" XU HƯỚNG TƯƠNG TÁC THỊ TRƯỜNG (So với ngày " + compareDate + ")");
        System.out.println("--------------------------------------------------------------------------------\n");


        int totalCurrentAll = currentTrends.stream().mapToInt(JobTrend::getFrequencyCount).sum();
        int totalPastAll = pastTrends.stream().mapToInt(JobTrend::getFrequencyCount).sum();

        Map<String, Integer> currentSkillMap = currentTrends.stream()
                .collect(Collectors.groupingBy(JobTrend::getSkillName, Collectors.summingInt(JobTrend::getFrequencyCount)));
        
        Map<String, Integer> pastSkillMap = pastTrends.stream()
                .collect(Collectors.groupingBy(JobTrend::getSkillName, Collectors.summingInt(JobTrend::getFrequencyCount)));

        for (String skillName : currentSkillMap.keySet()) {
            double currentRate = totalCurrentAll > 0 ? ((double) currentSkillMap.get(skillName) / totalCurrentAll) * 100 : 0.0;
            double pastRate = pastSkillMap.containsKey(skillName) && totalPastAll > 0 
                    ? ((double) pastSkillMap.get(skillName) / totalPastAll) * 100 : 0.0;

            double diff = currentRate - pastRate;

            if (diff > 0) {
                System.out.printf("📈 %-15s ---> [TĂNG: +%.1f%%]\n", skillName, diff);
            } else if (diff < 0) {
                System.out.printf("📉 %-15s ---> [GIẢM: %.1f%%]\n", skillName, diff);
            } else if (pastSkillMap.containsKey(skillName)) {
                System.out.printf("➖ %-15s ---> [ỔN ĐỊNH: 0.0%%]\n", skillName);
            }
        }
        System.out.println("\n================================================================================");
    }
}