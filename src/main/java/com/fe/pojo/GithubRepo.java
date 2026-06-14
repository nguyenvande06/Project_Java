package com.fe.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "github_repo")
public class GithubRepo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "repo_id")
	private Long repoId;

	@Column(name = "repo_name", length = 255)
	private String repoName;

	@Column(name = "repo_url", length = 500)
	private String repoUrl;

	@Column(name = "ai_project_story", columnDefinition = "TEXT")
	private String aiProjectStory;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private StudentProfile student;

	public Long getRepoId() {
		return repoId;
	}

	public void setRepoId(Long repoId) {
		this.repoId = repoId;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getRepoUrl() {
		return repoUrl;
	}

	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	public String getAiProjectStory() {
		return aiProjectStory;
	}

	public void setAiProjectStory(String aiProjectStory) {
		this.aiProjectStory = aiProjectStory;
	}

	public StudentProfile getStudent() {
		return student;
	}

	public void setStudent(StudentProfile student) {
		this.student = student;
	}
}