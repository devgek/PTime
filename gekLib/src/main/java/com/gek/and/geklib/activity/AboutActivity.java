package com.gek.and.geklib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.gek.and.geklib.R;

public abstract class AboutActivity extends Activity {
	private String appName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		populateParameter();
		
		setContentView(R.layout.gek_about);
		ViewGroup rootView = (ViewGroup) this.findViewById(android.R.id.content);
		
		View aboutContent = getLayoutInflater().inflate(getContentResourceId(), rootView, false);
		rootView.addView(aboutContent, 0);

		Button buttonOk = (Button) findViewById(R.id.gek_button_about_ok);
		buttonOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		prepareDataForContentView();
		
		setTitle(makeTitle());
	}

	protected abstract void prepareDataForContentView();

	protected abstract int getContentResourceId();
	
	protected String getAppName() {
		return this.appName;
	}

	protected void populateParameter() {
		this.appName = getIntent().getStringExtra("gek_about_appName");
	}

	private String makeTitle() {
		String about = getResources().getString(R.string.gek_action_about);
		return about + " " + appName;
	}

}
