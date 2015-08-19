package com.gek.and.project4.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ListView;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.listadapter.ProjectManagementArrayAdapter;
import com.gek.and.project4.service.ProjectService;

public class ProjectManagementActivity extends Activity {
	private ProjectManagementArrayAdapter adapter;
	
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
		
		ListView listView = (ListView) findViewById(R.id.project_management_list_view);
		
		this.adapter = new ProjectManagementArrayAdapter(0, this);
		
		ProjectService projectService = Project4App.getApp(this).getProjectService();

		this.adapter.addAll(projectService.getAllProjects(null));
		listView.setAdapter(this.adapter);
		
		getActionBar().setTitle(R.string.title_project_management);
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
		super.finish();
	}

}
