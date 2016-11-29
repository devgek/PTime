package com.gek.and.project4.view;

/**
 * Created by moo on 29.11.16.
 */

public interface BackupRestoreViewMvc extends ViewMvc {
	interface BackupRestoreViewMvcListener {
		public void onBackupClick();
		public void onRestoreClick(boolean restoreLastBackup);
	}

	public void setListener(BackupRestoreViewMvcListener listener);
}
