package com.gek.and.project4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.mvc.view.BackupRestoreViewMvc;
import com.gek.and.project4.mvc.view.BackupRestoreViewMvcImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class BackupRestoreActivity extends AppCompatActivity implements BackupRestoreViewMvc.BackupRestoreViewMvcListener{
    private static final String DB_NAME = "project4-db";
    private static final String DB_BACKUP_NAME = "ptime-backup.db";
    private static final String DB_RESTORE_NAME = "ptime-restore.db";
	private BackupRestoreViewMvc backupRestoreView;

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

	@Nullable

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		backupRestoreView = new BackupRestoreViewMvcImpl(getLayoutInflater(), getMainWindow());
		backupRestoreView.setListener(this);
		setContentView(backupRestoreView.getRootView());

		if (backupRestoreView.hasToolbar()) {
			setSupportActionBar(backupRestoreView.getToolbar());
		}

		getSupportActionBar().setTitle(R.string.title_backup_restore);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private ViewGroup getMainWindow() {
		return (ViewGroup) this.findViewById(android.R.id.content);
	}

	@Override
	public void finish() {
		Intent back = this.getIntent();
		setResult(RESULT_OK, back);
        back.putExtra("reloadSummary", true);
        super.finish();
	}


	@Override
	public void onBackupClick() {
		try {
			//File sd = Environment.getExternalStorageDirectory();
			File bDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

			if (bDir.canWrite()) {
				File currentDB = getApplicationContext().getDatabasePath(DB_NAME);

				String backupDBPath = DB_BACKUP_NAME;
				File backupDB = new File(bDir, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(getBaseContext(), "Backup: " + backupDB.toString(), Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(getBaseContext(), "Zugriff auf " + bDir.getAbsolutePath() + " nicht möglich.", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onRestoreClick(boolean restoreLastBackup) {
		try {
			Project4App.getApp(this).closeDatabase();

			//File sd = Environment.getExternalStorageDirectory();
			File bDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

			File currentDB = getApplicationContext().getDatabasePath(DB_NAME);

			String backupDBPath = DB_RESTORE_NAME;
			if (restoreLastBackup) {
				backupDBPath = DB_BACKUP_NAME;
			}
			File backupDB = new File(bDir, backupDBPath);

			FileChannel src = new FileInputStream(backupDB).getChannel();
			FileChannel dst = new FileOutputStream(currentDB).getChannel();
			dst.transferFrom(src, 0, src.size());
			src.close();
			dst.close();
			Toast.makeText(getBaseContext(), "Restore: " + backupDB.toString(), Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		finally {
			Project4App.getApp(this).initDatabase();
		}
	}
}
