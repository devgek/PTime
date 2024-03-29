package com.gek.and.project4.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;

import com.gek.and.geklib.type.AppBarType;
import com.gek.and.geklib.type.AppType;
import com.gek.and.geklib.util.AboutUtil;
import com.gek.and.geklib.util.PackageInfoUtil;
import com.gek.and.project4.Summary;
import com.gek.and.project4.dagger.DaggerServiceComponent;
import com.gek.and.project4.dagger.ServiceComponent;
import com.gek.and.project4.dagger.ServiceModule;
import com.gek.and.project4.dao.BookingDao;
import com.gek.and.project4.dao.DaoMaster;
import com.gek.and.project4.dao.DaoSession;
import com.gek.and.project4.dao.ProjectDao;
import com.gek.and.project4.dao.SelectiveUpdateOpenHelper;
import com.gek.and.project4.entity.Booking;
import com.gek.and.project4.entity.Project;
import com.gek.and.project4.model.ProjectCard;
import com.gek.and.project4.model.ProjectSummary;
import com.gek.and.project4.service.BookingService;
import com.gek.and.project4.service.ProjectService;

import java.util.List;

public abstract class Project4App extends Application {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ProjectDao projectDao;
    private BookingDao bookingDao;
    private ProjectService projectService;
    private BookingService bookingService;
    private List<ProjectCard> projectCardList;
    private Summary summary = new Summary();
    private String editProjectColorString;
    private List<Booking> lastBookingList;
    private Booking editBooking;
    protected AppType appType;
	protected AppBarType appBarType;
    private List<ProjectSummary> periodSummaryList;

    private ServiceComponent serviceComponent;
    private List<Booking> importList;

	@Override
	public void onCreate() {
		/* Workaround for bug on create intent to store csv since android 8.0, see
		https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed
		*/
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		// end of workaround

		super.onCreate();
		
		setAppType();
		this.appBarType = AppBarType.LITE;

		initDatabase();

		serviceComponent = createServiceComponent();
	}

	private ServiceComponent createServiceComponent() {
		return DaggerServiceComponent
				.builder()
				.serviceModule(new ServiceModule())
				.build();
	}

	public void initDatabase() {
//        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "project4-db", null);
		SelectiveUpdateOpenHelper helper = new SelectiveUpdateOpenHelper(this, "project4-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		projectDao = daoSession.getProjectDao();
		bookingDao = daoSession.getBookingDao();
		projectService = new ProjectService(daoSession);
		bookingService = new BookingService(daoSession);
	}

	public void closeDatabase() {
		if (db.isOpen()) {
			db.close();
		}
	}

	protected abstract void setAppType();

	public void setAppType(AppType appType) {
		this.appType = appType;
	}
	
	public String getVersion() {
		return "Version " + PackageInfoUtil.getVersionName(this) + (isPro() ? " P" : "");
	}
	
	public String getCopyright() {
		return "Copyright (c) 2021";
	}
	
	public String getDeveloper() {
		return "ptime.app[at]gmail.com";
	}
	
	public String getAboutContentAssetName() {
		return "about_content.html";
	}

	public String loadHtmlAboutContent() {
		String content = AboutUtil.readContentFromAsset(getAssets(), getAboutContentAssetName());
		return content;
	}
	
	public boolean isPro() {
		return AppType.PRO.equals(this.appType);
	}

	public boolean isAppBarDark() {
		return AppBarType.DARK.equals(this.appBarType);
	}
	
	public ProjectDao getProjectDao() {
		return projectDao;
	}

	public BookingDao getBookingDao() {
		return bookingDao;
	}

	public ProjectService getProjectService() {
		return projectService;
	}
	
	public List<ProjectCard> getProjectCardList() {
		return projectCardList;
	}
	
	public List<ProjectCard> getUpdatedProjectCardList() {
		Long runningProjectId = summary.getRunningNow() != null ? summary.getRunningNow().getProjectId() : null;
		this.projectCardList = projectService.getActiveProjects(runningProjectId);
		return projectCardList;
	}
	
	public BookingService getBookingService() {
		return bookingService;
	}

	public void setProjectCardList(List<ProjectCard> projectCardList) {
		this.projectCardList = projectCardList;
	}

	public Summary getSummary() {
		return summary;
	}

	public void setSummary(Summary summary) {
		this.summary = summary;
	}

	public String getEditProjectColorString() {
		return editProjectColorString;
	}

	public void setEditProjectColorString(String editProjectColorString) {
		this.editProjectColorString = editProjectColorString;
	}

	public List<Booking> getLastBookingList() {
		return lastBookingList;
	}

	public void setLastBookingList(List<Booking> lastBookingList) {
		this.lastBookingList = lastBookingList;
	}

	public Booking getEditBooking() {
		return editBooking;
	}

	public void setEditBooking(Booking editBooking) {
		this.editBooking = editBooking;
	}

	public List<ProjectSummary> getPeriodSummaryList() {
		return periodSummaryList;
	}

	public void setPeriodSummaryList(List<ProjectSummary> periodSummaryList) {
		this.periodSummaryList = periodSummaryList;
	}

	public boolean isRunning() {
		return getSummary().getRunningNow() != null;
	}

	public Booking getRunningBooking() {
		if (isRunning()) {
			return getSummary().getRunningNow();
		}
		else {
			return null;
		}
	}

	public Project getRunningProject() {
		if (isRunning()) {
			Booking b = getRunningBooking();
			return projectService.getProject(b.getProjectId());
		}
		else {
			return null;
		}
	}

	public static Project4App getApp(Activity activity) {
		Project4App app = (Project4App) activity.getApplication();
		return app;
	}

	public static Project4App getApp(Context context) {
		Project4App app = (Project4App) context.getApplicationContext();
		return app;
	}

	public ServiceComponent getServiceComponent() {
		return serviceComponent;
	}

	public List<Booking> getImportList() {
		return importList;
	}

	public void setImportList(List<Booking> importList) {
		this.importList = importList;
	}
}
