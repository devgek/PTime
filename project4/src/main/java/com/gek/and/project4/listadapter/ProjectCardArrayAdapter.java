package com.gek.and.project4.listadapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gek.and.project4.R;
import com.gek.and.project4.activity.DashboardActivity;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.model.ProjectCard;

public class ProjectCardArrayAdapter extends ArrayAdapter<ProjectCard> {
	static class CardViewHolder {
		TextView line1;
		TextView line2;
		TextView runningNow;
		TextView totalMonth;
		ImageView colorBar;
		ImageButton editProject;
		ProjectCard currentCard;
	}

	private DashboardActivity projectCardActivity;
	
	public ProjectCardArrayAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProjectCard card = getItem(position);

		View row = convertView;
		CardViewHolder viewHolder;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.project_card, parent, false);
			
			viewHolder = new CardViewHolder();
			viewHolder.line1 = (TextView) row.findViewById(R.id.project_card_line1);
			viewHolder.line2 = (TextView) row.findViewById(R.id.project_card_line2);
			viewHolder.runningNow = (TextView) row.findViewById(R.id.project_card_running_today);
			viewHolder.colorBar = (ImageView) row.findViewById(R.id.project_card_color_bar);
			viewHolder.editProject = (ImageButton) row.findViewById(R.id.button_edit_project);
			
			handleEditProject(viewHolder.editProject);
			handleClick(row);
			
			row.setTag(viewHolder);
		} else {
			viewHolder = (CardViewHolder) row.getTag();
		}
		

		viewHolder.line1.setText(card.getProject().getCompany());
		viewHolder.line2.setText(card.getProject().getTitle());
		viewHolder.runningNow.setText(card.getRunningNowString());
		viewHolder.colorBar.setBackgroundColor(Color.parseColor(card.getProject().getColor()));
		viewHolder.editProject.setTag(card.getProject());
		viewHolder.currentCard = card;

		return row;
	}

	private void handleClick(View row) {
		row.setOnClickListener(new View.OnClickListener(){
		    @Override
		    public void onClick(View v) {
		    	CardViewHolder holder = (CardViewHolder) v.getTag();
		    	ProjectCard card = holder.currentCard;
		    	projectCardActivity.bookProject(card.getProject().getId());
		    }
		});
	}

	private void handleEditProject(ImageButton editProject) {
		editProject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageButton button = (ImageButton) v;
				Project project = (Project) button.getTag();
				projectCardActivity.editProject(project.getId());
			}
		});
	}

	
	@Override
	public ProjectCard getItem(int position) {
		return super.getItem(position);
	}

	public DashboardActivity getProjectCardActivity() {
		return projectCardActivity;
	}

	public void setProjectCardActivity(DashboardActivity projectCardActivity) {
		this.projectCardActivity = projectCardActivity;
	}

}
