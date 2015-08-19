package com.gek.and.project4.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

public class DefaultSharedPreferenceChangeListener implements
		OnSharedPreferenceChangeListener {

	private PreferenceFragment parentFragment;

	public DefaultSharedPreferenceChangeListener(
			PreferenceFragment parentFragment) {
		this.parentFragment = parentFragment;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Preference changedPref = parentFragment.findPreference(key);

		setSummary(changedPref);
	}

	public void initSummaries(PreferenceGroup pg) {
	    for (int i = 0; i < pg.getPreferenceCount(); ++i) {
		    Preference p = pg.getPreference(i);
		    if (p instanceof PreferenceGroup) {
			      this.initSummaries((PreferenceGroup) p); // recursion
		    }
		    else {
			      this.setSummary(p);
		    }
	    }
	}

	private void setSummary(Preference pref) {
		if (pref instanceof EditTextPreference) {
			pref.setSummary(((EditTextPreference) pref).getText());
		}
	}

}
