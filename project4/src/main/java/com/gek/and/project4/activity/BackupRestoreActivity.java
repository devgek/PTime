package com.gek.and.project4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import com.gek.and.geklib.draganddroplist.DragNDropListView;
import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.listadapter.ProjectManagementArrayAdapter;
import com.gek.and.project4.model.ProjectCard;
import com.gek.and.project4.service.ProjectService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class BackupRestoreActivity extends AppCompatActivity {
	private CheckBox mRestoreLastBackup;
    private static final String DB_NAME = "project4-db";
    private static final String DB_BACKUP_NAME = "ptime-backup.db";
    private static final String DB_RESTORE_NAME = "ptime-restore.db";

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
		
		setContentView(R.layout.backup_restore);
		
		Button backupButton = (Button) findViewById(R.id.backup_restore_button_backup);
		backupButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBackup();
            }
        });
        
		Button restoreButton = (Button) findViewById(R.id.backup_restore_button_restore);
        restoreButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRestore();
            }
        });

        mRestoreLastBackup = (CheckBox) findViewById(R.id.backup_restore_checkbox_restore);

		Toolbar toolbar = (Toolbar) findViewById(R.id.backup_restore_toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle(R.string.title_backup_restore);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

    private void doRestore() {
        try {
            Project4App.getApp(this).closeDatabase();

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            File currentDB = getApplicationContext().getDatabasePath(DB_NAME);

            String backupDBPath = DB_RESTORE_NAME;
            if (mRestoreLastBackup.isChecked()) {
                backupDBPath = DB_BACKUP_NAME;
            }
            File backupDB = new File(sd, backupDBPath);

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

    private void doBackup() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                File currentDB = getApplicationContext().getDatabasePath(DB_NAME);

                String backupDBPath = DB_BACKUP_NAME;
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getBaseContext(), "Backup: " + backupDB.toString(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
	public void finish() {
		Intent back = this.getIntent();
		setResult(RESULT_OK, back);
        back.putExtra("reloadSummary", true);
        super.finish();
	}


}
