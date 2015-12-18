package com.gek.and.project4.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by moo on 28.09.15.
 */
public class MenuUtil {
	public static void colorMenu(Menu menu, final int color) {
		for (int i = 0, size = menu.size(); i < size; i++) {
			MenuItem item = menu.getItem(i);
			colorMenuItem(item, color);
		}
	}

	public static void colorMenuItem(final MenuItem menuItem, final int color) {
		final Drawable drawable = menuItem.getIcon();
		if (drawable != null) {
			// If we don't mutate the drawable, then all drawable's with this id will have a color
			// filter applied to it.
//			drawable.mutate();
			drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
		}
	}

}
