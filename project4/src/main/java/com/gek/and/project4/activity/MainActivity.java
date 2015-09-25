package com.gek.and.project4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;

public class MainActivity extends AppCompatActivity {
	private static final String STATE_SELECTED_MENU_POSITION = "STATE_SELECTED_MENU_POSITION";
	private Toolbar mToolbar;
	private DrawerLayout mDrawerLayout;
	private NavigationView mNavigationView;
	private int mCurrentSelectedMenuPosition;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_MENU_POSITION, mCurrentSelectedMenuPosition);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		mCurrentSelectedMenuPosition = savedInstanceState.getInt(STATE_SELECTED_MENU_POSITION, 0);
		Menu menu = mNavigationView.getMenu();
		menu.getItem(mCurrentSelectedMenuPosition).setChecked(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu items for use in the action bar
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_list_bookings) {
			listBookings();
			return true;
		} else if (itemId == R.id.action_manage_projects) {
			manageProjects();
			return true;
		} else if (itemId == R.id.action_settings) {
			changeSettings();
			return true;
		} else if (itemId == R.id.action_about) {
			showAbout();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (savedInstanceState != null) {
			mCurrentSelectedMenuPosition = savedInstanceState.getInt(STATE_SELECTED_MENU_POSITION);
		}

		mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle(R.string.title_project_dashboard);

		mDrawerLayout = (DrawerLayout)findViewById(R.id.main_drawer_layout);
		setUpNavDrawer();

		mNavigationView = (NavigationView) findViewById(R.id.main_navigation_view);
		handleNavigation();
//
//		WorkaroundActionOverflow.execute(this);
	}

	private void handleNavigation() {
		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				int itemId = menuItem.getItemId();
				if (itemId == R.id.action_dashboard) {
					mCurrentSelectedMenuPosition = 0;
				} else if (itemId == R.id.action_list_bookings) {
					listBookings();
					mCurrentSelectedMenuPosition = 1;
				} else if (itemId == R.id.action_manage_projects) {
					manageProjects();
					mCurrentSelectedMenuPosition = 2;
				} else if (itemId == R.id.action_settings) {
					changeSettings();
					mCurrentSelectedMenuPosition = 3;
				} else if (itemId == R.id.action_about) {
					showAbout();
					mCurrentSelectedMenuPosition = 4;
				}

				mDrawerLayout.closeDrawer(mNavigationView);
				return true;
			}
		});
	}

	private void setUpNavDrawer() {
		if (mToolbar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			mToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
			mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mDrawerLayout.openDrawer(GravityCompat.START);
				}
			});
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	private void showAbout() {
		Intent about = new Intent(this, com.gek.and.geklib.activity.DefaultAboutActivity.class);
		
		Project4App theApp = Project4App.getApp(this);
		
		about.putExtra("gek_about_appName", getResources().getString(R.string.app_name));
		about.putExtra("gek_about_icon", R.drawable.ic_launcher_clock);
		about.putExtra("gek_about_header_line1", theApp.getVersion());
		about.putExtra("gek_about_header_line2", theApp.getCopyright());
		about.putExtra("gek_about_header_line3", theApp.getDeveloper());
		about.putExtra("gek_about_content", theApp.loadHtmlAboutContent());
		
		startActivity(about);
	}
	private void listBookings() {
		startActivityNewTask(BookingListActivity.class);
	}

	private void changeSettings() {
		startActivityNewTask(SettingsActivity.class);
	}

	private void manageProjects() {
		Intent intent = new Intent(this, ProjectManagementActivity.class);
		startActivityForResult(intent, 3000);
	}
	
	private void startActivityNewTask(Class<?> theActivityClass) {
		Intent intent = new Intent(this, theActivityClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
