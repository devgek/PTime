package com.gek.and.project4.dialogcontroller;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.gek.and.project4.R;
import com.gek.and.project4.app.Project4App;
import com.gek.and.project4.fragment.ModalToolbarDialogFragment;
import com.gek.and.project4.util.ColorUtil;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

/**
 * Created by moo on 09.09.15.
 */
public class ColorPickerDialogController extends DefaultDialogController implements ModalToolbarDialogFragment.ModalToolbarDialogController{
	private ColorPicker picker;
	private SVBar svBar;
	private SaturationBar saturationBar;
	private OpacityBar opacityBar;
	private ValueBar valueBar;
	private Button buttonCancel;
	private Button buttonSelect;

	private String oldColorHex;

	public ColorPickerDialogController(Context context) {
		super(context);
	}

	public View buildView() {
		View mainView = loadViewFromXml(R.layout.color_picker);

		picker = (ColorPicker) mainView.findViewById(R.id.picker);
//		svBar = (SVBar) findViewById(R.id.svbar);
//		opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
		saturationBar = (SaturationBar) mainView.findViewById(R.id.saturationbar);
//		valueBar = (ValueBar) findViewById(R.id.valuebar);

		oldColorHex = Project4App.getApp(context).getEditProjectColorString();
		int oldColor = Color.parseColor(oldColorHex);

		picker.setColor(oldColor);
		picker.setOldCenterColor(oldColor);

//		picker.addSVBar(svBar);
//		picker.addValueBar(valueBar);
//		picker.addOpacityBar(opacityBar);
		picker.addSaturationBar(saturationBar);

//		picker.setOnColorChangedListener(this);
//		picker.setShowOldCenterColor(false);


		return mainView;
	}

	@Override
	public void onClickPositive() {
		float alpha = picker.getAlpha();
		String hexColor = ColorUtil.getHex(picker.getColor());
		Project4App.getApp(context).setEditProjectColorString(hexColor);
	}

	@Override
	public void onClickNegative() {
		Project4App.getApp(context).setEditProjectColorString(oldColorHex);
	}
}
