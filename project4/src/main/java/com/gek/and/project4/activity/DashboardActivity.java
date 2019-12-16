package com.gek.and.project4.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gek.and.geklib.GekConstants;
import com.gek.and.geklib.type.AppType;
import com.gek.and.geklib.view.ColorBar;
import com.gek.and.geklib.view.ColorBarView;
import com.gek.and.project4.AppConstants;
import com.gek.and.project4.R;
import com.gek.and.project4.Summary;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.async.SummaryLoader;
import com.gek.and.project4.async.SummaryLoader.SummaryLoaderTarget;
import com.gek.and.project4.async.TimeBooker;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.fragment.ModalToolbarDialogFragment;
import com.gek.and.project4.listadapter.ProjectCardArrayAdapter;
import com.gek.and.project4.model.BookedValues;
import com.gek.and.project4.model.ProjectCard;
import com.gek.and.project4.model.ProjectSummary;
import com.gek.and.project4.service.ProjectService;
import com.gek.and.project4.types.PeriodType;
import com.gek.and.project4.util.BookingUtil;
import com.gek.and.project4.util.DateUtil;
import com.gek.and.project4.util.L;
import com.gek.and.project4.dialogcontroller.PeriodSummaryDialogController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DashboardActivity extends MainActivity implements SummaryLoaderTarget{
	private static final String TAG = "DashboardActivity::";
	private final int BAR_HEIGHT = 40;
	private final int BAR_PADDING = 0;
	
	private ProjectService projectService;
	private ProjectCardArrayAdapter projectCardAdapter;
	private ListView projectCardListView;
	private View mainView;
	private View headerView;
	private TextView tvHeaderSummaryToday;
	private TextView tvSummaryToday;
	private TextView tvHeaderSummaryWeek;
	private TextView tvSummaryWeek;
	private TextView tvHeaderSummaryMonth;
	private TextView tvSummaryMonth;
	private TextView tvHeaderCustomer;
	private TextView tvHeaderProject;
	private TextView tvHeaderRunning;
	private ColorBarView colorBarViewToday;
	private ColorBarView colorBarViewWeek;
	private ColorBarView colorBarViewMonth;
	private LinearLayout llDashboardSummary;


	private Thread tickerThread;
	private boolean tickerThreadFlag = false;

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.tickerThread != null) {
			this.tickerThreadFlag = false;
			this.tickerThread = null;
		}
	}

	@Override
	protected void onResume() {
		updateHeader();
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startSummaryLoader();

		final Summary summary = Project4App.getApp(this).getSummary();
		
		mainView = this.findViewById(android.R.id.content);

		llDashboardSummary = (LinearLayout) findViewById(R.id.layout_dashboard_summary);

		headerView = this.findViewById(R.id.dashboard_header_view);
		tvHeaderSummaryToday = (TextView) findViewById(R.id.dashboard_header_title_today);
		tvSummaryToday = (TextView) findViewById(R.id.dashboard_header_today);
		tvHeaderSummaryWeek = (TextView) findViewById(R.id.dashboard_header_title_week);
		tvSummaryWeek = (TextView) findViewById(R.id.dashboard_header_week);
		tvHeaderSummaryMonth = (TextView) findViewById(R.id.dashboard_header_title_month);
		tvSummaryMonth = (TextView) findViewById(R.id.dashboard_header_month);
		tvHeaderCustomer = (TextView) findViewById(R.id.dashboard_header_customer);
		tvHeaderProject = (TextView) findViewById(R.id.dashboard_header_project);
		tvHeaderRunning = (TextView) findViewById(R.id.dashboard_header_running);

		headerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Project p = Project4App.getApp(DashboardActivity.this).getRunningProject();
				if (p != null) {
					bookProject(p.getId());
				}
			}
		});
		/*
		textViewToday = (TextView) findViewById(R.id.dashboard_header_today);
		textViewWeek = (TextView) findViewById(R.id.summary_title_week);
		textViewMonth = (TextView) findViewById(R.id.summary_title_month);

		colorBarViewToday = (ColorBarView) findViewById(R.id.colorBarToday);
		colorBarViewToday.setBarHeight(BAR_HEIGHT);
		colorBarViewToday.setPaddingTop(BAR_PADDING);
		
		FrameLayout colorBarViewTodayFrame = (FrameLayout) findViewById(R.id.colorBarTodayFrame);
		colorBarViewTodayFrame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSummaryDialog(PeriodType.TODAY, getProjectSummary(summary.getProjectsToday()));
			}
		});
		
		colorBarViewWeek = (ColorBarView) findViewById(R.id.colorBarWeek);
		colorBarViewWeek.setBarHeight(BAR_HEIGHT);
		colorBarViewWeek.setPaddingTop(BAR_PADDING);

		FrameLayout colorBarViewWeekFrame = (FrameLayout) findViewById(R.id.colorBarWeekFrame);
		colorBarViewWeekFrame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSummaryDialog(PeriodType.WEEK, getProjectSummary(summary.getProjectsWeek()));
			}
		});

		colorBarViewMonth = (ColorBarView) findViewById(R.id.colorBarMonth);
		colorBarViewMonth.setBarHeight(BAR_HEIGHT);
		colorBarViewMonth.setPaddingTop(BAR_PADDING);

		FrameLayout colorBarViewMonthFrame = (FrameLayout) findViewById(R.id.colorBarMonthFrame);
		colorBarViewMonthFrame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSummaryDialog(PeriodType.MONTH, getProjectSummary(summary.getProjectsMonth()));
			}
		});
		*/

		projectCardListView = (ListView) findViewById(R.id.project_list_view);

		projectService = Project4App.getApp(this).getProjectService();

		projectCardAdapter = new ProjectCardArrayAdapter(getApplicationContext(), R.layout.project_card);
		projectCardAdapter.setProjectCardActivity(this);

		Project4App.getApp(this).setProjectCardList(projectService.getActiveProjects(null));
		projectCardAdapter.addAll(Project4App.getApp(this).getProjectCardList());

//		updateCardAdapter();

		projectCardListView.setAdapter(projectCardAdapter);

		FloatingActionButton addProjectButton = findViewById(R.id.button_add_project);
		addProjectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addProject();
			}
		});
		
		if (this.tickerThread == null && !this.tickerThreadFlag) {
			startRunningBookingTickerThread();
		}
	}
	
	private void showSummaryDialog(PeriodType periodType, List<ProjectSummary> periodSummaryList) {
		Project4App.getApp(this).setPeriodSummaryList(periodSummaryList);

		PeriodSummaryDialogController dialogController = new PeriodSummaryDialogController(this, periodType.getCode());
		View dialogView = dialogController.buildView();

		ModalToolbarDialogFragment dialogFragment = new ModalToolbarDialogFragment();
		dialogFragment.init(dialogView, getResources().getString(R.string.title_period_summary), -1, -1, null);
		dialogFragment.show(getFragmentManager(), "periodSummary");
	}

	private void startSummaryLoader() {
		SummaryLoader summaryLoader = new SummaryLoader();
		summaryLoader.execute(new Object[]{this, this});
	}

	protected void addProject() {
		if (!Project4App.getApp(this).isPro() && isProjectLimitReached()) {
			Toast.makeText(this, "Mehr als " + AppConstants.PROJECT_LIMIT_FREE + " Projekte sind nur in der Pro-Version möglich.", Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(this, ProjectDetailActivity.class);
		startActivityForResult(intent, AppConstants.RC_PROJECT__DETAIL_NEW);
	}

	public void editProject(Long projectId) {
		Intent intent = new Intent(this, ProjectDetailActivity.class);
		intent.putExtra("projectId", projectId);
		startActivityForResult(intent, AppConstants.RC_PROJECT_DETAIL_EDIT);
	}

	public void bookProject(Long projectId) {
		TimeBooker timeBooker = new TimeBooker();
		timeBooker.execute(new Object[]{this, projectId});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == AppConstants.RC_PROJECT__DETAIL_NEW || requestCode == AppConstants.RC_PROJECT_DETAIL_EDIT ||
					requestCode == AppConstants.RC_PROJECT_MANAGEMENT || requestCode == AppConstants.RC_BACKUP_RESTORE) {
				boolean reloadSummary = data.getBooleanExtra("reloadSummary", false);
				if (reloadSummary) {
					startSummaryLoader();
				}
				else {
					updateDashboardHeader();
					updateCardAdapter();
					invalidateMainView();
				}
			}
			if (requestCode == GekConstants.RC_ABOUT) {
				boolean c7 = data.getBooleanExtra("c7", false);
				if (c7) {
					Project4App.getApp(this).setAppType(AppType.PRO);
				}
			}
		}
	}

	private void updateCardAdapter() {
		projectCardAdapter.clear();
		projectCardAdapter.addAll(Project4App.getApp(this).getUpdatedProjectCardList());
	}

	public void onPostSummaryLoad() {
		updateDashboardHeader();

		Summary summary = Project4App.getApp(this).getSummary();
		Long runningProjectId = summary.getRunningNow() != null ? summary.getRunningNow().getProjectId() : null;
		
		updateRunningProject(runningProjectId);
		updateCardAdapter();

		invalidateMainView();
	}

	public void onPostTimeBooking(Long projectId, boolean bookedStart) {
		deleteNotifications();
		
		Long startedProjectId = -1L;
		if (bookedStart) {
			startedProjectId = projectId;
			sendProjectStartedNotification(projectId);
		}
		updateRunningProject(startedProjectId);
		updateCardAdapter();
		updateDashboardHeader();
		updateHeader();
		invalidateMainView();
	}

	private void updateHeader() {
		int headerColor = 0;
		int statusColor = 0;
		Project p = Project4App.getApp(this).getRunningProject();
		if (p != null) {
			headerColor = Color.parseColor(p.getColor());
		}
		else {
			headerColor = getResources().getColor(R.color.primary);
		}

		headerView.setBackgroundColor(headerColor);
		getAppBar().setBackgroundColor(headerColor);

		if (p != null) {
			headerView.setVisibility(View.VISIBLE);
		}
		else {
			headerView.setVisibility(View.GONE);
		}

//		statusColor = getResources().getColor(R.color.primary_dark);

//		if (Build.VERSION.SDK_INT >= 21) {
//			getWindow().setStatusBarColor(statusColor);
//		}
		if (isShowDashboarSummary()) {
			llDashboardSummary.setVisibility(View.VISIBLE);
		}
		else {
			llDashboardSummary.setVisibility(View.GONE);
		}

	}

	private void sendProjectStartedNotification(Long projectId) {
		Project project = Project4App.getApp(this).getProjectService()
				.getProject(projectId);
		// Prepare intent which is triggered if the
		// notification is selected
		Intent intent = new Intent(this, DashboardActivity.class);
//		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack
		stackBuilder.addParentStack(DashboardActivity.class);
		// Adds the Intent to the top of the stack
		stackBuilder.addNextIntent(intent);
		// Gets a PendingIntent containing the entire back stack
		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		// Build notification
		// Actions are just fake
		Notification noti = new Notification.Builder(this)
				.setContentTitle(project.getCompany())
				.setContentText(project.getTitle())
				.setSmallIcon(R.drawable.gek_notify_clock)
				.setContentIntent(pendingIntent).build();
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti);
	}
	
	private void deleteNotifications() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}

	private void updateRunningProject(Long projectId) {
		List<ProjectCard> cardList = Project4App.getApp(this).getProjectCardList();
		for (ProjectCard card : cardList) {
			if (card.getProject().getId().equals(projectId)) {
				card.setRunningNow(true);
			} else {
				card.setRunningNow(false);
			}
		}
	}

	private void invalidateMainView() {
		if (mainView != null) {
//			colorBarViewToday.invalidate();
//			colorBarViewWeek.invalidate();
//			colorBarViewMonth.invalidate();
			projectCardListView.invalidate();
			mainView.invalidate();
		}
	}

	private void updateDashboardHeader() {
		Summary summary = Project4App.getApp(this).getSummary();

		tvHeaderSummaryToday.setText(DateUtil.getFormattedDay(summary.getInitDate()));
		tvHeaderSummaryWeek.setText(DateUtil.getFormattedWeek(summary.getInitDate()));
		tvHeaderSummaryMonth.setText(DateUtil.getFormattedMonth(summary.getInitDate()));
		tvSummaryToday.setText(summary.getFormattedDay());
		tvSummaryWeek.setText(summary.getFormattedWeek());
		tvSummaryMonth.setText(summary.getFormattedMonth());
		/*
		colorBarViewToday.setColorBars(getColorBars(summary.getProjectsToday()));
		colorBarViewWeek.setColorBars(getColorBars(summary.getProjectsWeek()));
		colorBarViewMonth.setColorBars(getColorBars(summary.getProjectsMonth()));
		*/
		Booking b = Project4App.getApp(this).getRunningBooking();
		if (b != null) {
			Project p = Project4App.getApp(this).getRunningProject();
			tvHeaderCustomer.setText(p.getCompany());
			tvHeaderProject.setText(p.getTitle());
			tvHeaderRunning.setText(DateUtil.getFormattedHM(BookingUtil.getRunningMinutes(b)));
		}
		else {
			tvHeaderCustomer.setText("");
			tvHeaderProject.setText("");
			tvHeaderRunning.setText("");
		}
	}

	private List<ProjectSummary> getProjectSummary(Map<Long, BookedValues> projects) {
		List<ProjectSummary> projectSummaries = new ArrayList<ProjectSummary>();

		Set<Map.Entry<Long, BookedValues>> entries = projects.entrySet();
		Iterator<Map.Entry<Long, BookedValues>> it = entries.iterator();
		while (it.hasNext()) {
			Map.Entry<Long, BookedValues> entry = (Map.Entry<Long, BookedValues>) it.next();
			Long projectId = entry.getKey();
			
			Project project = Project4App.getApp(this).getProjectService().getProject(projectId);
			int minutes = entry.getValue().getComplete();
			
			ProjectSummary ps = new ProjectSummary();
			ps.setColorHex(project.getColor());
			ps.setCustomer(project.getCompany());
			ps.setProject(project.getTitle());
			ps.setSummaryMinutes(minutes);
			
			projectSummaries.add(ps);
		}
		
		Collections.sort(projectSummaries);
		return projectSummaries;
	}
	
	private List<ColorBar> getColorBars(Map<Long, BookedValues> projects) {
		List<ColorBar> colorBars = new ArrayList<ColorBar>();

		Set<Map.Entry<Long, BookedValues>> entries = projects.entrySet();
		Iterator<Map.Entry<Long, BookedValues>> it = entries.iterator();
		while (it.hasNext()) {
			Map.Entry<Long, BookedValues> entry = (Map.Entry<Long, BookedValues>) it.next();
			Long projectId = entry.getKey();
			Project project = Project4App.getApp(this).getProjectService().getProject(projectId);
			int projectColor;
			if (project == null) {
				projectColor = getResources().getColor(R.color.no_project_color);
			} else {
				projectColor = Color.parseColor(project.getColor());
			}

			ColorBar bar = new ColorBar(entry.getValue().getComplete(), projectColor);
			colorBars.add(bar);
		}
		
		Collections.sort(colorBars);
		return colorBars;
	}
	
	private void startRunningBookingTickerThread() {
		final Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					updateDashboardHeader();
					invalidateMainView();
				}
			}
			
		};
		
		tickerThread = new Thread() {

			@Override
			public void run() {
				while (tickerThreadFlag) {
					try {
						Thread.sleep(60 * 1000);
						
						Summary summary = Project4App.getApp(DashboardActivity.this).getSummary();
						synchronized(summary) {
							if (summary.isLoaded() && summary.getRunningNow() != null) {
								summary.addBooking(summary.getRunningNow());
								handler.sendEmptyMessage(1);
							}
						}
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					}
				}
				L.d(TAG, "bookingTicker::run finished " + Thread.currentThread().getId());
			}
			
		};
		
		tickerThreadFlag = true;
		tickerThread.setName("bookingTicker");
		L.d(TAG, "bookingTicker::start " + tickerThread.getId());
		tickerThread.start();
	}
	
	private boolean isProjectLimitReached() {
		return Project4App.getApp(this).getProjectCardList().size() >= AppConstants.PROJECT_LIMIT_FREE;
	}

	private boolean isShowDashboarSummary() {
		return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("setting_dashboard_showSummary", true);
	}


}
