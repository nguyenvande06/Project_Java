/**
 * 
 */
package com.fe.pojo;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "skill_node")
public class SkillNode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nodeId;

	private String skillName;
	private Integer priorityLevel;

	@ManyToOne
	@JoinColumn(name = "path_id")
	private TechPath path;

	@ManyToOne
	@JoinColumn(name = "parent_node_id")
	private SkillNode parentNode;

	@OneToMany(mappedBy = "parentNode")
	private List<SkillNode> children;

	@OneToMany(mappedBy = "node")
	private List<LearningResource> resources;

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public Integer getPriorityLevel() {
		return priorityLevel;
	}

	public void setPriorityLevel(Integer priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	public TechPath getPath() {
		return path;
	}

	public void setPath(TechPath path) {
		this.path = path;
	}

	public SkillNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(SkillNode parentNode) {
		this.parentNode = parentNode;
	}

	public List<SkillNode> getChildren() {
		return children;
	}

	public void setChildren(List<SkillNode> children) {
		this.children = children;
	}

	public List<LearningResource> getResources() {
		return resources;
	}

	public void setResources(List<LearningResource> resources) {
		this.resources = resources;
	}
}