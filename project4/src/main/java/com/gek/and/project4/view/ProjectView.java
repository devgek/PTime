package com.gek.and.project4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gek.and.project4.R;

public class ProjectView extends LinearLayout {

	private ImageView mColorBar;
	private TextView mLine1;
	private TextView mLine2;
	private boolean dimmed = false;

	public ProjectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		setPadding(10, 10, 10, 10);
		
		if (isFocusable()) {
			setBackground(getResources().getDrawable(R.drawable.apptheme_edit_text_holo_light));
		}

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.compound_view_project, this, true);

		mColorBar = (ImageView) findViewById(R.id.combound_viev_project_color_bar);
		mLine1 = (TextView) findViewById(R.id.combound_viev_project_line1);
		mLine2 = (TextView) findViewById(R.id.combound_viev_project_line2);
	}

	public ProjectView(Context context) {
		super(context);
	}
	
	public void setCustomer(String customer) {
		mLine1.setText(customer);
	}
	
	public void setTitle(String title) {
		mLine2.setText(title);
	}
	
	public void setColor(int color) {
		mColorBar.setBackgroundColor(color);
	}
	
	public boolean isDimmed() {
		return dimmed;
	}

	public void setDimmed(boolean dimmed) {
		this.dimmed = dimmed;
		if (dimmed) {
			setTranslucent();
		}
		else {
			setOpaque();
		}
	}

	private void setTranslucent() {
		this.setAlpha((float) 0.5);
	}
	
	private void setOpaque() {
		this.setAlpha((float) 1);
	}
}
