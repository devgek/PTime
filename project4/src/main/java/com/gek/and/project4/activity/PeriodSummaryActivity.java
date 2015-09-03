package com.gek.and.project4.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.listadapter.PeriodSummaryArrayAdapter;
import com.gek.and.project4.model.ProjectSummary;
import com.gek.and.project4.types.PeriodType;
import com.gek.and.project4.util.DateUtil;

import java.util.Calendar;
import java.util.List;

public class PeriodSummaryActivity extends AppCompatActivity {
//	private TextView headLine;
	private TextView headText;
	private TextView headMinutes;
	private ListView periodProjectList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.period_summary);

		Intent intent = getIntent();
		int periodCode = intent.getIntExtra("periodCode", PeriodType.TODAY.getCode());
		
//		headLine = (TextView) findViewById(R.id.period_summary_head);
//		headLine.setText(getHeadeLine(periodCode));
		headText = (TextView) findViewById(R.id.period_summary_head_title);
		headText.setText(getHeadText(periodCode));
		
		headMinutes = (TextView) findViewById(R.id.period_summary_head_hm);
		headMinutes.setText(DateUtil.getFormattedHM(getTotalMinutes()));
		
		periodProjectList = (ListView) findViewById(R.id.period_summary_list_view);
		
		PeriodSummaryArrayAdapter periodSummaryAdapter = new PeriodSummaryArrayAdapter(this, R.layout.period_summary_row);
		periodSummaryAdapter.addAll(Project4App.getApp(this).getPeriodSummaryList());
		
		periodProjectList.setAdapter(periodSummaryAdapter);

		setTitle(R.string.title_period_summary);
	}

	private int getTotalMinutes() {
		int totalMinutes = 0;
		
		List<ProjectSummary> summaryList = Project4App.getApp(this).getPeriodSummaryList();
		for (ProjectSummary ps : summaryList) {
			totalMinutes += ps.getSummaryMinutes();
		}
		
		return totalMinutes;
	}

	private String getHeadText(int periodCode) {
		PeriodType periodType = PeriodType.fromInt(periodCode);
		
		Calendar now = Calendar.getInstance();
		switch (periodType) {
		case TODAY: return DateUtil.getFormattedDayFull(now.getTime());
		case WEEK: return DateUtil.getFormattedWeek(now);
		case MONTH: return DateUtil.getFormattedMonth(now);
		default: return "";
		}
	}
}
