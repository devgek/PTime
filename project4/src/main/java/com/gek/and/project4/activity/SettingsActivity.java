package com.gek.and.project4.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gek.and.project4.R;
import com.gek.and.project4.settings.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings);

		Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle(R.string.title_settings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		getFragmentManager().beginTransaction().replace(R.id.settings_frame, new SettingsFragment()).commit();
	}
}
