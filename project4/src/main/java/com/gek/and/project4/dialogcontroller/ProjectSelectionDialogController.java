package com.gek.and.project4.dialogcontroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.listadapter.PeriodSummaryArrayAdapter;
import com.gek.and.project4.listadapter.ProjectSelectionArrayAdapter;
import com.gek.and.project4.model.ProjectSummary;
import com.gek.and.project4.types.PeriodType;
import com.gek.and.project4.util.DateUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by moo on 09.09.15.
 */
public class ProjectSelectionDialogController extends DefaultDialogController{
	private Long selectedProjectId;
	private ProjectSelectionDialogListener listener;

	public ProjectSelectionDialogController(Context context, Long projectId, ProjectSelectionDialogListener listener) {
		super(context);
		this.selectedProjectId = projectId;
		this.listener = listener;
	}

	public View buildView() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = loadViewFromXml(R.layout.project_selection);

		ListView listViewProjejectSelection = (ListView) contentView.findViewById(R.id.project_selection_list_view);

		ProjectSelectionArrayAdapter projectSelectionAdapter = new ProjectSelectionArrayAdapter(context, R.layout.project_selection_row);
		projectSelectionAdapter.addAll(Project4App.getApp(context).getProjectCardList());

		projectSelectionAdapter.setProjectSelectionListener(new ProjectSelectionArrayAdapter.ProjectSelectionListener() {
			@Override
			public void OnProjectSelected(Long projectId) {
				selectedProjectId = projectId;
				getDialog().dismiss();
				listener.onProjectSelected(projectId);
			}
		});

		listViewProjejectSelection.setAdapter(projectSelectionAdapter);

		return contentView;
	}

	public interface ProjectSelectionDialogListener {
		public void onProjectSelected(Long projectId);
	}

}