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

public class ModalToolbarDialogFragment extends DialogFragment {

	private ModalToolbarDialogFragmentActivator mActivator;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View customView = mActivator.getModalDialogView();
		Toolbar toolbar = (Toolbar) customView.findViewById(R.id.project_selection_toolbar);
		toolbar.setTitle(R.string.title_project_selection);
		builder.setView(customView);
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
        	mActivator = (ModalToolbarDialogFragmentActivator) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ModalToolbarDialogFragmentActivator");
        }
	}

	public interface ModalToolbarDialogFragmentActivator {
		public String getModalDialogTitle();
		public View getModalDialogView();
	}
}
