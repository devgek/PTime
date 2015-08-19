package com.gek.and.project4.comparator;

import java.util.Comparator;

import com.gek.and.project4.types.ProjectType;


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
