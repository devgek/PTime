package com.gek.and.project4.listadapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gek.and.project4.R;
import com.gek.and.project4.model.ProjectSummary;
import com.gek.and.project4.util.DateUtil;

public class PeriodSummaryArrayAdapter extends ArrayAdapter<ProjectSummary> {
	static class ListViewHolder {
		ImageView colorBar;
		TextView line1;
		TextView line2Left;
		TextView projectTotal;
	}

	public PeriodSummaryArrayAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProjectSummary projectSummary = getItem(position);

		View row = convertView;
		ListViewHolder viewHolder;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.period_summary_row, parent, false);
			
			viewHolder = new ListViewHolder();
			viewHolder.colorBar = (ImageView) row.findViewById(R.id.period_summary_row_color_bar);
			viewHolder.line1 = (TextView) row.findViewById(R.id.period_summary_row_line1);
			viewHolder.line2Left = (TextView) row.findViewById(R.id.period_summary_row_line2_left);
			viewHolder.projectTotal = (TextView) row.findViewById(R.id.period_summary_row_line2_right);
			
			row.setTag(viewHolder);
		} else {
			viewHolder = (ListViewHolder) row.getTag();
		}
		

		viewHolder.colorBar.setBackgroundColor(Color.parseColor(projectSummary.getColorHex()));
		viewHolder.line1.setText(projectSummary.getCustomer());
		viewHolder.line2Left.setText(projectSummary.getProject());
		viewHolder.projectTotal.setText(DateUtil.getFormattedHM(projectSummary.getSummaryMinutes()));
		
		return row;
	}

}
