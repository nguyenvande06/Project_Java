package com.fe.pojo;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "job_trend")
public class JobTrend {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trend_id")
	private Long trendId;

	@Column(name = "skill_name", length = 100)
	private String skillName;

	@Column(name = "job_portal", length = 50)
	private String jobPortal;

	@Column(name = "frequency_count")
	private int frequencyCount;

	@Column(name = "recorded_date")
	private LocalDate recordedDate = LocalDate.now();


	public Long getTrendId() {
		return trendId;
	}

	public void setTrendId(Long trendId) {
		this.trendId = trendId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getJobPortal() {
		return jobPortal;
	}

	public void setJobPortal(String jobPortal) {
		this.jobPortal = jobPortal;
	}

	public int getFrequencyCount() {
		return frequencyCount;
	}

	public void setFrequencyCount(int frequencyCount) {
		this.frequencyCount = frequencyCount;
	}

	public LocalDate getRecordedDate() {
		return recordedDate;
	}

	public void setRecordedDate(LocalDate recordedDate) {
		this.recordedDate = recordedDate;
	}
}