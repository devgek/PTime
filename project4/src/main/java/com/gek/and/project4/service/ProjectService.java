package com.gek.and.project4.service;

import android.database.sqlite.SQLiteDatabase;

import com.gek.and.project4.dao.BookingDao;
import com.gek.and.project4.dao.DaoSession;
import com.gek.and.project4.dao.ProjectDao;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.model.ProjectCard;

import java.util.LinkedList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class ProjectService {
	ProjectDao projectDao;
	DaoSession daoSession;
	
	public ProjectService(DaoSession daoSession) {
		this.daoSession = daoSession;
		projectDao = daoSession.getProjectDao();
	}
	
	public List<ProjectCard> getAllProjects(Long runningProjectId) {
		return getProjects(true, runningProjectId);
	}
	
	public List<ProjectCard> getActiveProjects(Long runningProjectId) {
		return getProjects(false, runningProjectId);
	}

	public Project addOrUpdateProject(long projectId, String customer, String title, String subTitle, String color, int priority, boolean active, String defaultNote, boolean billable) {
		if (projectId < 0) {
			return addProject(customer, title, subTitle, color, priority, active, defaultNote, billable);
		}
		
		Project p = getProject(projectId);
		p.setCompany(customer);
		p.setTitle(title);
		p.setSubTitle(subTitle);
		p.setColor(color);
		p.setPriority(priority);
		p.setActive(Boolean.valueOf(active));
		p.setDefaultNote(defaultNote);
		p.setBillable(Boolean.valueOf(billable));

		this.projectDao.update(p);
		
		return p;
	}

	public Project updatePriority(Project project, int newPriority) {
		project.setPriority(newPriority);
		this.projectDao.update(project);

		return project;
	}

	public Project addProject(String customer, String title, String subTitle, String color, int priority, boolean active, String defaultNote, boolean billable) {
		Project p = new Project(null, title, subTitle, customer, color, priority, Boolean.valueOf(active), defaultNote, Boolean.valueOf(billable));
		long id = this.projectDao.insert(p);
		p.setId(id);
		
		return p;
	}
	
	public Project getProject(long projectId) {
		return this.projectDao.loadByRowId(projectId);
	}
	
	public boolean deleteProject(long projectId) {
		SQLiteDatabase db = this.daoSession.getDatabase();
		boolean ok = true;
		
		db.beginTransaction();
		try {
			deleteAllBookingsForProject(projectId);
			this.projectDao.deleteByKey(projectId);
			db.setTransactionSuccessful();
		}
		catch(Exception e) {
			ok = false;
		}
		finally {
			db.endTransaction();
		}
		
		return ok;
	}

	public ProjectCard toCard(Project project, Long runningProjectId) {
		ProjectCard card = new ProjectCard(project);
		card.setRunningNow(runningProjectId != null && runningProjectId.longValue() == project.getId().longValue());

		return card;
	}

	private void deleteAllBookingsForProject(long projectId) {
		SQLiteDatabase db = this.daoSession.getDatabase();
		BookingDao bookingDao = this.daoSession.getBookingDao();

		String sql = "delete from " + bookingDao.getTablename() + " where " + BookingDao.Properties.ProjectId.columnName + " = " + projectId;
		db.execSQL(sql);
	}

	/**
	 * getProjects returns a LinkedList of ProjectCard
	 * @param all
	 * @param runningProjectId
	 * @return a LinkedList of ProjectCard
	 */
	private List<ProjectCard> getProjects(boolean all, Long runningProjectId) {
		List<ProjectCard> projectCards = new LinkedList<ProjectCard>();

		List<Project> projectEntities = getAllWithPriority();
		for (Project projectEntity : projectEntities) {
			if (all || projectEntity.getActive().equals(Boolean.TRUE)) {
				projectCards.add(toCard(projectEntity, runningProjectId));
			}
		}

		return projectCards;
	}

	private List<Project> getAllWithPriority() {
		QueryBuilder<Project> qb = this.projectDao.queryBuilder();
		qb.orderAsc(ProjectDao.Properties.Priority);
		return qb.list();
	}
}
