package com.gek.and.project4.menu;

import android.app.Activity;
import android.content.Context;
import android.view.ActionProvider;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.model.ProjectCard;

import java.util.ArrayList;
import java.util.List;

public class ProjectActionProvider extends ActionProvider{
	private static final int SPINNER_WIDTH = 300;
	private Context context;
	private int currentSelection = -1;
	private ProjectActionProviderListener listener;
	
	public ProjectActionProvider(Context context) {
		super(context);
		this.context = context;
		if (context instanceof ProjectActionProviderListener) {
			this.listener = (ProjectActionProviderListener) context;
		}
	}
	

	@Override
	public View onCreateActionView() {
        Spinner spinner = new Spinner(this.context, Spinner.MODE_DROPDOWN);
		ArrayList<String> projectSelection = new ArrayList<String>();
		projectSelection.add("Alle");
		
		List<ProjectCard> projectCardList = Project4App.getApp((Activity)this.context).getProjectCardList();
		for (ProjectCard card : projectCardList) {
			projectSelection.add(card.getProject().getCompany());
		}
		
		ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, projectSelection);
        spinner.setAdapter(projectAdapter);
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != currentSelection) {
					if (listener != null) {
						listener.onProjectActionItemSelected(position);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
        spinner.setLayoutParams(new ViewGroup.LayoutParams(SPINNER_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
        return spinner;
	}

	public interface ProjectActionProviderListener {
		public void onProjectActionItemSelected(int position);
	}
}
