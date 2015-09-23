package com.gek.and.project4.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.dialogcontroller.ColorPickerDialogController;
import com.gek.and.project4.dialogcontroller.DefaultDialogController;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.fragment.ModalToolbarDialogFragment;
import com.gek.and.project4.model.ProjectCard;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends AppCompatActivity implements DefaultDialogController.DialogControllerListener{
	private EditText editTextCustomer;
	private EditText editTextProject;
	private ImageButton buttonProjectColor;
	private CheckedTextView switchProjectActive;
	
	private long projectId;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    if (isModeNew()) {
		    inflater.inflate(R.menu.project_new, menu);
	    }
	    else {
		    inflater.inflate(R.menu.project_edit, menu);
	    }
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_discard) {
			confirmDeleteProject();
			return true;
		} else if (itemId == R.id.action_save) {
			saveProject();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.project_detail);
		
		Intent callingIntent = getIntent();
		this.projectId = callingIntent.getLongExtra("projectId", -1);
		
		editTextCustomer = (EditText) findViewById(R.id.projectDetailCustomerText);
		editTextProject = (EditText) findViewById(R.id.projectDetailProjectText);
		buttonProjectColor = (ImageButton) findViewById(R.id.projectDetailProjectColor);
		
		buttonProjectColor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseProjectColor();
			}
		});
		
		switchProjectActive = (CheckedTextView) findViewById(R.id.projectDetailActiveSwitch);
		switchProjectActive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (switchProjectActive.isChecked()) {
					switchProjectActive.setChecked(false);
				} else {
					switchProjectActive.setChecked(true);
				}

				setStateText();
			}
		});
		
		prepareData();

		Toolbar toolbar = (Toolbar) findViewById(R.id.project_detail_toolbar);
		setSupportActionBar(toolbar);
		if (isModeNew()) {
			getSupportActionBar().setTitle(R.string.title_project_add);
		}
		else {
			getSupportActionBar().setTitle(R.string.title_project_change);
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}


	private void chooseProjectColor() {
//		Intent colorChooser = new Intent(getApplicationContext(), ColorPickerActivity.class);
//		startActivityForResult(colorChooser, 1);
		ColorPickerDialogController dialogController = new ColorPickerDialogController(this);
		dialogController.addListener(this);
		View contentView = dialogController.buildView();

		ModalToolbarDialogFragment dialogFragment = new ModalToolbarDialogFragment();
		dialogFragment.init(contentView, getResources().getString(R.string.title_project_color_picker), R.string.buttonColorSelect, R.string.buttonColorCancel, dialogController);
		dialogFragment.show(getFragmentManager(), "colorChooser");
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				setProjectColorButton();
			}
		}
	}

	private boolean isModeNew() {
		return projectId < 0;
	}
	
	private void prepareData() {
		if (isModeNew()) {
			editTextCustomer.setText("");
			editTextProject.setText("");
			setProjectColor((String) getResources().getString(R.color.project_color_preselect));
			switchProjectActive.setChecked(true);
		}
		else {
			Project editProject = Project4App.getApp(this).getProjectService().getProject(projectId);
			editTextCustomer.setText(editProject.getCompany());
			editTextProject.setText(editProject.getTitle());
			setProjectColor(editProject.getColor());
			switchProjectActive.setChecked(editProject.getActive() == null || editProject.getActive().equals(Boolean.TRUE));
		}
		
		setStateText();
	}
	
	private void setStateText() {
		if (switchProjectActive.isChecked()) {
			switchProjectActive.setText(R.string.project_detail_active_text_on);
		}
		else {
			switchProjectActive.setText(R.string.project_detail_active_text_off);
		}
	}

	private void setProjectColor(String colorString) {
		Project4App.getApp(this).setEditProjectColorString(colorString);
		setProjectColorButton();
	}

	private void setProjectColorButton() {
		String colorString = Project4App.getApp(this).getEditProjectColorString();
		GradientDrawable d = (GradientDrawable) buttonProjectColor.getBackground();
		d.setColor(Color.parseColor(colorString));
		buttonProjectColor.invalidate();
	}
	
	private String getProjectColor() {
//		GradientDrawable d = (GradientDrawable) imageButtonColor.getBackground();
//		int intColor = 0;
//		return String.format("#%06X", 0xFFFFFF & intColor);
		//workaround, because you can't get a solid color out of GradientDrawable
		return Project4App.getApp(this).getEditProjectColorString();
	}

	private void saveProject() {
		String customer = editTextCustomer.getText().toString();
		String title = editTextProject.getText().toString();
		
		if (customer.trim().equals("") || title.trim().equals("")) {
			Toast.makeText(getApplicationContext(), "Kunde und Projekt müssen angegeben werden!", Toast.LENGTH_SHORT).show();
			return;
		}

		String projectColor = getProjectColor();
		
		boolean active = switchProjectActive.isChecked();
		
		Project p = Project4App.getApp(this).getProjectService().addOrUpdateProject(projectId, customer, title, "", projectColor, 0, active);
		if (isModeNew()) {
			ProjectCard pCard = Project4App.getApp(this).getProjectService().toCard(p, null);
			List<ProjectCard> projectCardList = Project4App.getApp(this).getProjectCardList();
			projectCardList.add(pCard);
		}
		
		goBackWithResult(RESULT_OK, false);
	}
	
	private void deleteProject() {
		boolean deleted = Project4App.getApp(this).getProjectService().deleteProject(this.projectId);
		if (!deleted) {
			Toast.makeText(this, "Projekt konnte nicht gelöscht werden.", Toast.LENGTH_SHORT).show();
		}
		else {
			List<ProjectCard> projectCardListOld = Project4App.getApp(this).getProjectCardList();
			List<ProjectCard> projectCardList = new ArrayList<ProjectCard>();
			for (ProjectCard card : projectCardListOld) {
				if (card.getProject().getId().equals(this.projectId)) {
					continue;
				}
				projectCardList.add(card);
			}
			
			Project4App.getApp(this).setProjectCardList(projectCardList);
			goBackWithResult(RESULT_OK, true);
		}
	}
	
	private boolean isRunningProject() {
		Booking runningBooking = Project4App.getApp(this).getSummary().getRunningNow();
		return runningBooking != null && runningBooking.getProjectId().equals(this.projectId);
	}

	private void goBackWithResult(int result, boolean reloadSummary) {
		Intent back = new Intent(getApplicationContext(), DashboardActivity.class);
		back.putExtra("reloadSummary", reloadSummary);
		setResult(result, back);
		finish();
	}
	
	private void cancel() {
		finish();
	}
	
	public void confirmDeleteProject() {
		if (isRunningProject()) {
			Toast.makeText(this, "Das gerade laufende Projekt kann nicht gelöscht werden.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Projekt löschen");
		ad.setMessage("Das Projekt und alle seine Zeitbuchungen werden gelöscht.");
		ad.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deleteProject();
						arg0.cancel();

					}
				});
		ad.setNegativeButton("Abbrechen",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.cancel();
					}
				});
		AlertDialog ad_echt = ad.create();
		ad_echt.show();
	}

	@Override
	public void onDialogClose(DialogInterface dialog) {
		setProjectColorButton();
	}
}
