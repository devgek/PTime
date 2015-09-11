package com.gek.and.project4.dialogcontroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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

/**
 * Created by moo on 09.09.15.
 */
public class PeriodSummaryDialogController extends DefaultDialogController{
	private int periodCode;

	public PeriodSummaryDialogController(Context context, int periodCode) {
		super(context);
		this.periodCode = periodCode;
	}

	public View buildView() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.period_summary, null);

		TextView headText = (TextView) contentView.findViewById(R.id.period_summary_head_title);
		headText.setText(getHeadText(periodCode));

		TextView headMinutes = (TextView) contentView.findViewById(R.id.period_summary_head_hm);
		headMinutes.setText(DateUtil.getFormattedHM(getTotalMinutes()));

		ListView periodProjectList = (ListView) contentView.findViewById(R.id.period_summary_list_view);

		PeriodSummaryArrayAdapter periodSummaryAdapter = new PeriodSummaryArrayAdapter(context, R.layout.period_summary_row);
		periodSummaryAdapter.addAll(Project4App.getApp(context).getPeriodSummaryList());

		periodProjectList.setAdapter(periodSummaryAdapter);

		return contentView;
	}

	private int getTotalMinutes() {
		int totalMinutes = 0;

		List<ProjectSummary> summaryList = Project4App.getApp(this.context).getPeriodSummaryList();
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