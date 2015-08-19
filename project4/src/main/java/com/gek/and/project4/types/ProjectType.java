package com.gek.and.project4.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.gek.and.project4.comparator.ProjectTypeComparator;

public enum ProjectType {
	AVENUM("VVO", 3, "#99CC00"),
	PIPE("Line", 1, "#33B5E5"),
	KAHRER_SOFTWARE("Android Stuff", 2, "#FF4444"),
	MIPO_CONSULT("Atlassian Tools", 4, "#FF4444");
	
	private String code;
	private int priority;
	private String color;
	
	ProjectType(String code, int priority, String color) {
		this.code = code;
		this.priority = priority;
		this.color = color;
	}

	public String getCode() {
		return this.code;
	}

	public int getPriority() {
		return this.priority;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public static List<ProjectType> getPriorityList() {
		List<ProjectType> projectTypeList = Arrays.asList(ProjectType.values());
		Collections.sort(projectTypeList, new ProjectTypeComparator());
		return projectTypeList;
	}
}
