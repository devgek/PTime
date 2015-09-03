package com.gek.and.project4.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.listadapter.ProjectSelectionArrayAdapter;
import com.gek.and.project4.listadapter.ProjectSelectionArrayAdapter.ProjectSelectionListener;

public class ProjectSelectionFragment extends DialogFragment {

	private ProjectSelectionDialogListener mProjectSelectionDialogListener;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View mainView = inflater.inflate(R.layout.project_selection, null);
		ListView listViewProjejectSelection = (ListView) mainView.findViewById(R.id.project_selection_list_view);

		ProjectSelectionArrayAdapter projectSelectionAdapter = new ProjectSelectionArrayAdapter(getActivity().getApplicationContext(), R.layout.project_selection_row);
		projectSelectionAdapter.addAll(Project4App.getApp(getActivity()).getProjectCardList());
		projectSelectionAdapter.setProjectSelectionListener(new ProjectSelectionListener() {
			@Override
			public void OnProjectSelected(Long projectId) {
				mProjectSelectionDialogListener.onProjectSelected(projectId);
				getDialog().dismiss();
			}
		});

		listViewProjejectSelection.setAdapter(projectSelectionAdapter);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		Toolbar toolbar = (Toolbar) mainView.findViewById(R.id.project_selection_toolbar);
		toolbar.setTitle(R.string.title_project_selection);
		builder.setView(mainView);
//		builder.setTitle(R.string.title_project_selection);

		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		
		return dialog;
	}

	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
        	mProjectSelectionDialogListener = (ProjectSelectionDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ProjectSelectionDialogListener");
        }
	}

	public interface ProjectSelectionDialogListener {
		public void onProjectSelected(Long projectId);
	}
}
