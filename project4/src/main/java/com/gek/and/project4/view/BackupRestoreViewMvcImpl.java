package com.gek.and.project4.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.gek.and.project4.R;

/**
 * Created by moo on 29.11.16.
 */

public class BackupRestoreViewMvcImpl implements BackupRestoreViewMvc {
	private BackupRestoreViewMvcListener listener;
	private View rootView;
	private CheckBox mRestoreLastBackup;
	Toolbar toolbar;

	public BackupRestoreViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
		rootView = inflater.inflate(R.layout.backup_restore, container, false);

		init();
	}

	private void init() {
		mRestoreLastBackup = (CheckBox) rootView.findViewById(R.id.backup_restore_checkbox_restore);
		toolbar = (Toolbar) rootView.findViewById(R.id.backup_restore_toolbar);

		Button backupButton = (Button) rootView.findViewById(R.id.backup_restore_button_backup);
		backupButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onBackupClick();
			}
		});

		Button restoreButton = (Button) rootView.findViewById(R.id.backup_restore_button_restore);
		restoreButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onRestoreClick(mRestoreLastBackup.isChecked());
			}
		});
	}


	@Override
	public void setListener(BackupRestoreViewMvcListener listener) {
		this.listener = listener;
	}

	@Override
	public View getRootView() {
		return rootView;
	}

	@Override
	public Bundle getViewState() {
		return null;
	}

	@Override
	public boolean hasToolbar() {
		return true;
	}

	@Override
	public Toolbar getToolbar() {
		return toolbar;
	}
}
