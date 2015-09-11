package com.gek.and.project4.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.listadapter.ProjectSelectionArrayAdapter;
import com.gek.and.project4.listadapter.ProjectSelectionArrayAdapter.ProjectSelectionListener;
import com.gek.and.project4.util.ColorUtil;
import com.gek.and.project4.util.L;

public class ModalToolbarDialogFragment extends DialogFragment {

	private View view;
	private String title;
	private int positiveButtonId;
	private int negativeButtonId;
	private ModalToolbarDialogController dialogController;

	public void init(View view, String title, int positiveButtonId, int negativeButtonId, ModalToolbarDialogController dialogController) {
		this.view = view;
		this.title = title;
		this.positiveButtonId = positiveButtonId;
		this.negativeButtonId = negativeButtonId;
		this.dialogController = dialogController;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.modal_toolbar_dialog, null);
		mainView.addView(view);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		Toolbar toolbar = (Toolbar) mainView.findViewById(R.id.modal_toolbar_dialog_toolbar);
		toolbar.setTitle(title);
		builder.setView(mainView);

		if (positiveButtonId > -1 && dialogController != null) {
			builder.setPositiveButton(positiveButtonId, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					dialogController.onClickPositive();
				}
			});
		}
		if (negativeButtonId > -1 && dialogController != null) {
			builder.setNegativeButton(negativeButtonId, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					dialogController.onClickNegative();
				}
			});
		}

		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		
		return dialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (dialogController != null) {
			dialogController.onDialogClose(dialog);
		}
	}

	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);
		if (view == null) {
			throw new IllegalStateException("view must be set with call to init()");
		}
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//        	mActivator = (ModalToolbarDialogFragmentActivator) activity;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString()
//                    + " must implement ModalToolbarDialogFragmentActivator");
//        }
	}

	public interface ModalToolbarDialogController {
		public void onClickPositive();
		public void onClickNegative();
		public void onDialogClose(DialogInterface dialog);
	}
}
