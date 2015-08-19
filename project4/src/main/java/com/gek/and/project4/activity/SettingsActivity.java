package com.gek.and.project4.activity;

import android.app.Activity;
import android.os.Bundle;

import com.gek.and.project4.settings.SettingsFragment;

public class SettingsActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}
}
