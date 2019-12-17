package com.gek.and.project4.listadapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.gek.and.geklib.draganddroplist.DragNDropArrayAdapter;
import com.gek.and.project4.R;
import com.gek.and.project4.activity.ProjectManagementActivity;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.model.ProjectCard;
import com.gek.and.project4.service.ProjectService;
import com.gek.and.project4.view.ProjectView;

import java.util.List;

public class ProjectManagementArrayAdapter extends DragNDropArrayAdapter<ProjectCard> {
	static class ProjectViewHolder {
		ProjectView projectView;
		CheckBox activeCheckBox;
		ProjectCard card;
	}

	private ProjectManagementActivity parentActivity;
	
	public ProjectManagementArrayAdapter(ProjectManagementActivity parentActivity, List<ProjectCard> data, int dragHandler) {
		super(parentActivity.getApplicationContext(), data, dragHandler);
		this.parentActivity = parentActivity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProjectCard card = getItem(position);

		View row = convertView;
		ProjectViewHolder viewHolder;
		if (row == null) {
			/*
			 * Use LayoutInflater of activity, because otherwise the look of the checkboxes is not like Holo on Samsung Galaxy S
			 */
			LayoutInflater inflater = parentActivity.getLayoutInflater();
//			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.project_management_row, parent, false);
			
			viewHolder = new ProjectViewHolder();
			viewHolder.projectView = (ProjectView) row.findViewById(R.id.project_activation_project);
			viewHolder.activeCheckBox = (CheckBox) row.findViewById(R.id.project_activation_active);
			viewHolder.card = card;
			
			handleClick(viewHolder.activeCheckBox);
			handleClick(viewHolder.projectView);
			
			row.setTag(viewHolder);
		} else {
			viewHolder = (ProjectViewHolder) row.getTag();
		}
		

		viewHolder.projectView.setColor(Color.parseColor(card.getProject().getColor()));
		viewHolder.projectView.setCustomer(card.getProject().getCompany());
		viewHolder.projectView.setTitle(card.getProject().getTitle());
		viewHolder.projectView.setDimmed(!card.getProject().getActive().booleanValue());
		viewHolder.activeCheckBox.setChecked(card.getProject().getActive().booleanValue());
		viewHolder.activeCheckBox.setTag(viewHolder);
		viewHolder.projectView.setTag(card);

		return row;
	}

	private void handleClick(ProjectView projectView) {
		projectView.setOnClickListener(new View.OnClickListener(){
		    @Override
		    public void onClick(View v) {
		    	ProjectView pView = (ProjectView)v;
		    	
		    	ProjectCard card = (ProjectCard) pView.getTag();
		    	parentActivity.editProject(card.getProject().getId());
		    }
		});
	}
	
	private void handleClick(CheckBox checkBox) {
		checkBox.setOnClickListener(new View.OnClickListener(){
		    @Override
		    public void onClick(View v) {
		    	CheckBox checkBox = (CheckBox)v;
		    	ProjectViewHolder holder = (ProjectViewHolder) checkBox.getTag();
		    	ProjectCard card = holder.card;
		    	Project project = card.getProject();
		    	ProjectService projectService = Project4App.getApp(parentActivity).getProjectService();
		    	projectService.addOrUpdateProject(project.getId(), project.getCompany(), project.getTitle(), project.getSubTitle(), project.getColor(), project.getPriority(), checkBox.isChecked(), project.getDefaultNote(), project.getBillable());
		    	card.getProject().setActive(checkBox.isChecked());
		    	
		    	holder.projectView.setDimmed(!checkBox.isChecked());
		    }
		});
	}

	@Override
	public ProjectCard getItem(int position) {
		return super.getItem(position);
	}

	public interface ProjectSelectionListener {
		public void OnProjectSelected(Long projectId);
	}
}
