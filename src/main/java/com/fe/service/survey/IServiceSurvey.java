package com.fe.service.survey;

import java.time.LocalDate;
import java.util.List;
import com.fe.pojo.JobTrend;

public interface IServiceSurvey {
    
    List<LocalDate> getAvailableScanDates();
    
    List<JobTrend> getTrendsByDate(LocalDate date);

    void displayMarketSurveyReport(LocalDate selectedDate, LocalDate compareDate);
}