package com.gek.and.project4.dialogcontroller;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.gek.and.project4.R;
import com.gek.and.project4.fragment.ModalToolbarDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moo on 09.09.15.
 */
public class DefaultDialogController implements ModalToolbarDialogFragment.ModalToolbarDialogController{
	protected Context context;
	protected List<DialogControllerListener> listeners = new ArrayList<DialogControllerListener>();
	protected DialogInterface dialog;

	public DefaultDialogController(Context context) {
		this.context = context;
	}

	public View loadViewFromXml(int viewResource) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(viewResource, null);

		return view;
	}

	public void addListener(DialogControllerListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void onClickPositive() {

	}

	@Override
	public void onClickNegative() {

	}

	@Override
	public void onDialogClose(DialogInterface dialog) {
		for (DialogControllerListener listener : listeners) {
			listener.onDialogClose(dialog);
		}
	}

	@Override
	public void connectDialog(DialogInterface dialog) {
		this.dialog = dialog;
	}

	public DialogInterface getDialog() {
		return this.dialog;
	}

	public interface DialogControllerListener {
		public void onDialogClose(DialogInterface dialog);
	}
}
