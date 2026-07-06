package com.fe.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "student_profile")
public class StudentProfile {
	@Id
    private long studentId; 
    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private User user;

	private String mssv;
	private String githubUsername;

	@Column(columnDefinition = "TEXT")
	private String transcriptSummary;

	private String portfolioShareToken;

	@ManyToOne
	@JoinColumn(name = "target_path_id")
	private TechPath targetPath;

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getMssv() {
		return mssv;
	}

	public void setMssv(String mssv) {
		this.mssv = mssv;
	}

	public String getGithubUsername() {
		return githubUsername;
	}

	public void setGithubUsername(String githubUsername) {
		this.githubUsername = githubUsername;
	}

	public String getTranscriptSummary() {
		return transcriptSummary;
	}

	public void setTranscriptSummary(String transcriptSummary) {
		this.transcriptSummary = transcriptSummary;
	}

	public String getPortfolioShareToken() {
		return portfolioShareToken;
	}

	public void setPortfolioShareToken(String portfolioShareToken) {
		this.portfolioShareToken = portfolioShareToken;
	}

	public TechPath getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(TechPath targetPath) {
		this.targetPath = targetPath;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}