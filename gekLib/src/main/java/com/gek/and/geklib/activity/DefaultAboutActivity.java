package com.gek.and.geklib.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gek.and.geklib.R;

public class DefaultAboutActivity extends AboutActivity {
	private int iconResourceId;
	private String headerLine1;
	private String headerLine2;
	private String headerLine3;
	private String content;
	private int counter;
	private boolean c7 = false;

	@Override
	protected void onResume() {
		super.onResume();
		counter = 0;
		c7 = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void finish() {
		Intent back = this.getIntent();
		setResult(RESULT_OK, back);
		back.putExtra("c7", c7);
		super.finish();
	}

	@Override
	protected void populateParameter() {
		super.populateParameter();
		
		this.iconResourceId = getIntent().getIntExtra("gek_about_icon", -1);
		this.headerLine1 = getIntent().getStringExtra("gek_about_header_line1");
		this.headerLine2 = getIntent().getStringExtra("gek_about_header_line2");
		this.headerLine3 = getIntent().getStringExtra("gek_about_header_line3");
		this.content = getIntent().getStringExtra("gek_about_content");
	}

	@Override
	protected void prepareDataForContentView() {
		ImageView iView = (ImageView) findViewById(R.id.gek_about_default_icon);
		if (this.iconResourceId > -1) {
			iView.setImageResource(iconResourceId);
		}
		else {
			iView.setVisibility(View.INVISIBLE);
		}
		if (iView.getVisibility() == View.VISIBLE) {
			iView.setOnClickListener( new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (counter == 6) {
						c7 = true;
						counter = 0;
						finish();
					}
					else {
						counter++;
					};
				}
			});
		}
		
		TextView tvAppName = (TextView) findViewById(R.id.gek_about_default_header_appname);
		
		if (getAppName() != null) {
			tvAppName.setText(getAppName());
		}
		else {
			tvAppName.setVisibility(View.GONE);
		}
		
		TextView tvLine1 = (TextView) findViewById(R.id.gek_about_default_header_line1);
		
		if (this.headerLine1 != null) {
			tvLine1.setText(this.headerLine1);
		}
		else {
			tvLine1.setVisibility(View.GONE);
		}
		
		TextView tvLine2 = (TextView) findViewById(R.id.gek_about_default_header_line2);
		
		if (this.headerLine2 != null) {
			tvLine2.setText(this.headerLine2);
		}
		else {
			tvLine2.setVisibility(View.GONE);
		}
		
		TextView tvLine3 = (TextView) findViewById(R.id.gek_about_default_header_line3);
		
		if (this.headerLine3 != null) {
			tvLine3.setText(this.headerLine3);
		}
		else {
			tvLine3.setVisibility(View.GONE);
		}
		
		WebView wvContent = (WebView) findViewById(R.id.gek_about_default_content);
		wvContent.clearCache(true);
		wvContent.clearHistory();
		wvContent.loadData(this.content, "text/html", "UTF-8");
	}

	@Override
	protected int getContentResourceId() {
		return R.layout.gek_about_default;
	}
}
