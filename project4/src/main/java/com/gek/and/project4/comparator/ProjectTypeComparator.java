package com.gek.and.project4.comparator;

import com.gek.and.project4.types.ProjectType;

import java.util.Comparator;


public class ProjectTypeComparator implements Comparator<ProjectType> {

	@Override
	public int compare(ProjectType lhs, ProjectType rhs) {
		if(lhs.getPriority() < rhs.getPriority()) {
			return -1;
		}
		else
		if (lhs.getPriority() > rhs.getPriority()) {
			return +1;
		}
		else {
			return 0;
		}
	}


}
