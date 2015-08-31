package com.gek.and.project4.service;

import com.gek.and.project4.entity.Project;
import com.gek.and.project4.model.ProjectCard;

import java.util.List;

/**
 * Created by moo on 27.08.15.
 */
public class ProjectCardService {
	ProjectService projectService;

	public ProjectCardService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public List<ProjectCard> getActiveCards(Long runningProjectId) {
		return projectService.getActiveProjects(runningProjectId);
	}

	public List<ProjectCard> insertLast(List<ProjectCard> oldList, Project project) {
		return oldList;
	}
}
