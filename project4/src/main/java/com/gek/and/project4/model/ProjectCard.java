package com.gek.and.project4.model;

import com.gek.and.project4.entity.Project;

public class ProjectCard {
	private Project project;
	private boolean runningNow;
	
	public ProjectCard(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return project;
	}

	public String getLine1() {
		return project.getTitle();
	}
	
	public String getLine2() {
		return project.getCompany();
	}
	
	public void setRunningNow(boolean runningNow) {
		this.runningNow = runningNow;
	}

	public boolean isRunningNow() {
		return runningNow;
	}

	public String getRunningNowString() {
		return (runningNow ? "läuft ..." : "");
	}
}
