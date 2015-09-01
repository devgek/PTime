package com.gek.and.project4.menu;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.gek.and.project4.R;

public class PeriodActionProvider extends ActionProvider {
	private Context context;
	private PeriodActionProviderListener listener;

	public PeriodActionProvider(Context context) {
		super(context);
		this.context = context;
		if (context instanceof PeriodActionProviderListener) {
			listener = (PeriodActionProviderListener) context;
		}
	}

	@Override
	public View onCreateActionView() {
        Spinner spinner = new Spinner(this.context, Spinner.MODE_DROPDOWN);
        SpinnerAdapter periodAdapter = ArrayAdapter.createFromResource(this.context, R.array.period, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(periodAdapter);
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (listener != null) {
					listener.onPeriodActionItemSelected(position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
        
        return spinner;
	}
	
	public interface PeriodActionProviderListener {
		public void onPeriodActionItemSelected(int position);
	}

}
