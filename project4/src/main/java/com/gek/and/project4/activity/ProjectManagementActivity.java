package com.gek.and.project4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.gek.and.geklib.draganddroplist.DragNDropListView;
import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.listadapter.ProjectManagementArrayAdapter;
import com.gek.and.project4.model.ProjectCard;
import com.gek.and.project4.service.ProjectService;

import java.util.List;

public class ProjectManagementActivity extends AppCompatActivity {
	private ProjectManagementArrayAdapter adapter;
	private List<ProjectCard> projects;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
//	        NavUtils.navigateUpFromSameTask(this);
	    	finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.project_management);
		
		DragNDropListView listView = (DragNDropListView) findViewById(R.id.project_management_list_view);
		
		ProjectService projectService = Project4App.getApp(this).getProjectService();
		this.projects = projectService.getAllProjects(null);

		this.adapter = new ProjectManagementArrayAdapter(this, projects, R.id.draghandler);
		listView.setDragNDropAdapter(this.adapter);

		Toolbar toolbar = (Toolbar) findViewById(R.id.project_management_toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle(R.string.title_project_management);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void editProject(Long projectId) {
		Intent intent = new Intent(this, ProjectDetailActivity.class);
		intent.putExtra("projectId", projectId);
		startActivityForResult(intent, 2000);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 2000) {
				this.adapter.clear();
				ProjectService projectService = Project4App.getApp(this).getProjectService();
				this.adapter.addAll(projectService.getAllProjects(null));
			}
		}
	}

	@Override
	public void finish() {
		Intent back = this.getIntent();
		setResult(RESULT_OK, back);
		if (adapter.isPositionChanged() || isNoPositionOrdering()) {
			reorderProjects();
		}
		super.finish();
	}

	private boolean isNoPositionOrdering() {
		int last = projects.size() - 1;
		return (last != projects.get(last).getProject().getPriority());
	}

	private void reorderProjects() {
		int[] positions = adapter.getPositions();

		if (positions.length != projects.size()) {
			Log.e("PTime-Error", "illegal array size");
			return;
		}

		ProjectService projectService = Project4App.getApp(this).getProjectService();

		for (int i = 0; i < projects.size(); i++) {
			int oldPos = positions[i];
			projectService.updatePriority(projects.get(oldPos).getProject(), i);
		}
	}

}
