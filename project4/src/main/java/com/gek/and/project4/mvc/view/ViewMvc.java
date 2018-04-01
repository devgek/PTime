package com.gek.and.project4.mvc.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by moo on 29.11.16.
 */

public interface ViewMvc {
	public View getRootView();
	public Bundle getViewState();
	public boolean hasToolbar();
	public Toolbar getToolbar();

}
