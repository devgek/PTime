package com.gek.and.geklib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.gek.and.geklib.R;

public abstract class AboutActivity extends AppCompatActivity {
	private String appName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		populateParameter();
		
		setContentView(R.layout.gek_about_toolbar);
		ViewGroup rootView = (ViewGroup) this.findViewById(R.id.gek_about_root);
		
		View aboutContent = getLayoutInflater().inflate(getContentResourceId(), rootView, false);
		rootView.addView(aboutContent);

//		Button buttonOk = (Button) findViewById(R.id.gek_button_about_ok);
//		buttonOk.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});

		prepareDataForContentView();

		Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle(R.string.gek_action_about);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	protected abstract void prepareDataForContentView();

	protected abstract int getContentResourceId();
	
	protected String getAppName() {
		return this.appName;
	}

	protected void populateParameter() {
		this.appName = getIntent().getStringExtra("gek_about_appName");
	}


}
