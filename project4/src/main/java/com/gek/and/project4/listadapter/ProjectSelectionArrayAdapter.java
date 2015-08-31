package com.gek.and.project4.listadapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gek.and.project4.R;
import com.gek.and.project4.model.ProjectCard;
import com.gek.and.project4.view.ProjectView;

public class ProjectSelectionArrayAdapter extends ArrayAdapter<ProjectCard> {
	static class ProjectViewHolder {
		ProjectView projectView;
		ProjectCard currentCard;
	}

	private ProjectSelectionListener projectSelectionListener;
	
	public ProjectSelectionArrayAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProjectCard card = getItem(position);

		View row = convertView;
		ProjectViewHolder viewHolder;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.project_selection_row, parent, false);
			
			viewHolder = new ProjectViewHolder();
			viewHolder.projectView = (ProjectView) row;
			
			handleClick(viewHolder.projectView);
			
			row.setTag(viewHolder);
		} else {
			viewHolder = (ProjectViewHolder) row.getTag();
		}
		

		viewHolder.projectView.setColor(Color.parseColor(card.getProject().getColor()));
		viewHolder.projectView.setCustomer(card.getProject().getCompany());
		viewHolder.projectView.setTitle(card.getProject().getTitle());
		viewHolder.currentCard = card;

		return row;
	}

	private void handleClick(ProjectView row) {
		row.setOnClickListener(new View.OnClickListener(){
		    @Override
		    public void onClick(View v) {
		    	ProjectViewHolder holder = (ProjectViewHolder) v.getTag();
		    	ProjectCard card = holder.currentCard;
		    	if (projectSelectionListener != null) {
		    		projectSelectionListener.OnProjectSelected(card.getProject().getId());
		    	}
		    }
		});
	}

	public ProjectSelectionListener getProjectSelectionListener() {
		return projectSelectionListener;
	}

	public void setProjectSelectionListener(ProjectSelectionListener projectSelectionListener) {
		this.projectSelectionListener = projectSelectionListener;
	}

	@Override
	public ProjectCard getItem(int position) {
		return super.getItem(position);
	}

	public interface ProjectSelectionListener {
		public void OnProjectSelected(Long projectId);
	}
}
