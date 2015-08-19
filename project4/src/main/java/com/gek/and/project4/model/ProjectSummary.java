package com.gek.and.project4.model;


public class ProjectSummary implements Comparable<ProjectSummary>{
	private String colorHex;
	private String customer;
	private String project;
	private Integer summaryMinutes;
	
	public String getColorHex() {
		return colorHex;
	}

	public void setColorHex(String colorHex) {
		this.colorHex = colorHex;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Integer getSummaryMinutes() {
		return summaryMinutes;
	}

	public void setSummaryMinutes(Integer summaryMinutes) {
		this.summaryMinutes = summaryMinutes;
	}

	@Override
	public int compareTo(ProjectSummary another) {
		return another.getSummaryMinutes().compareTo(this.getSummaryMinutes());
	}
	
}
