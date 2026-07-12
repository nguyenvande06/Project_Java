package com.fe.pojo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tech_path")
// Lộ trình học
public class TechPath {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pathId;

	private String pathName;
	private String description;
	private Boolean isActive;

	@OneToMany(mappedBy = "path")
	private List<SkillNode> skillNodes;

	public Long getPathId() {
		return pathId;
	}

	public void setPathId(Long pathId) {
		this.pathId = pathId;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<SkillNode> getSkillNodes() {
		return skillNodes;
	}

	public void setSkillNodes(List<SkillNode> skillNodes) {
		this.skillNodes = skillNodes;
	}
}
