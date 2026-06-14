/**
 * 
 */
package com.fe.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "learning_resource")
public class LearningResource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long resourceId;

	private String title;
	private String url;
	private String resourceType;

	@ManyToOne
	@JoinColumn(name = "node_id")
	private SkillNode node;

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public SkillNode getNode() {
		return node;
	}

	public void setNode(SkillNode node) {
		this.node = node;
	}
}
