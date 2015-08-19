package com.gek.and.geklib.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

public class ColorBarView extends ImageView {
	final float scale = getResources().getDisplayMetrics().density;
	private int BAR_NULL_WIDTH = 4;
	private int BAR_MIN_WIDTH = 8;
	private int barWidth = 0;
	private int barHeight = 0;
	private int paddingTop = 0;
	private List<ColorBar> colorBars;
	private Paint paint;
	private Rect rect;
	private List<ColorBarInternal> internalColorBars;
	
	public ColorBarView(Context context) {
		super(context);
		init();
	}

	public ColorBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ColorBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		rect = new Rect();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (barWidth < 1) {
			barWidth = MeasureSpec.getSize(widthMeasureSpec);
		}
		if (barHeight < 1) {
			barHeight = MeasureSpec.getSize(heightMeasureSpec);
		}
		
		setMeasuredDimension(barWidth, barHeight + paddingTop);
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (colorBars != null && !colorBars.isEmpty()) {
			if (internalColorBars == null) {
				prepareInternalColorBars();
			}
			
			int fromX = 0;
			int fromY = paddingTop == 0 ? 0 : -1 + paddingTop;
			int toX = -1;
			int toY = -1 + paddingTop + barHeight;
			int sumDrawn = 0;
			
			for (ColorBarInternal bar : internalColorBars) {
				int partWidth = bar.getPixel();
				
				if (sumDrawn + partWidth > barWidth) {
					partWidth = barWidth - sumDrawn;
				}
				
				// draw the bar
				toX += partWidth;
				rect.set(fromX, fromY, toX, toY);
				paint.setColor(bar.getColorBar().getColor());
				canvas.drawRect(rect, paint);
				
				sumDrawn += partWidth;
				fromX = sumDrawn;
			}
			
			canvas.save();
			canvas.restore();
		}
		
	}

	public List<ColorBar> getColorBars() {
		return colorBars;
	}

	public void setColorBars(List<ColorBar> colorBars) {
		this.colorBars = colorBars;
		this.internalColorBars = null;
	}
	
	public int getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}

	public int getBarHeight() {
		return barHeight;
	}

	public void setBarHeight(int barHeight) {
		this.barHeight = (int) (barHeight * scale + 0.5f);
	}

	public int getPaddingTop() {
		return paddingTop;
	}

	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	private void prepareInternalColorBars() {
		int sumBars = 0;
		for (ColorBar bar : colorBars) {
			sumBars += bar.getValue();
		}
		
		float pixelPerMinute = 0;
		BarWidthMode mode;
		if (sumBars > 0) {
			pixelPerMinute = (float)barWidth / (float)sumBars;
			mode = BarWidthMode.ABSOLUTE;
		}
		else {
			pixelPerMinute = barWidth / colorBars.size();
			mode = BarWidthMode.PROPORTIONAL;
		}
		
		int sumColorBarWidth = 0;
		
		internalColorBars = new ArrayList<ColorBarInternal>();
		for (ColorBar bar : colorBars) {
			int colorBarWidth = computeBarWidth(bar, mode, pixelPerMinute);
			if (colorBarWidth < BAR_MIN_WIDTH) {
				if (bar.getValue() == 0) {
					colorBarWidth = BAR_NULL_WIDTH;
				}
				else {
					colorBarWidth = BAR_MIN_WIDTH;
				}
			}
			
			ColorBarInternal internalBar = new ColorBarInternal(bar, colorBarWidth);
			internalColorBars.add(internalBar);
			
			sumColorBarWidth += colorBarWidth;
		}
		
		if (sumColorBarWidth > this.barWidth) {
			handleBarLonger(internalColorBars, sumColorBarWidth - this.barWidth);
		}
		else {
			if (sumColorBarWidth < this.barWidth) {
				handleBarShorter(internalColorBars, this.barWidth - sumColorBarWidth);
			}
		}
	}
	
	private int computeBarWidth(ColorBar bar, BarWidthMode mode, float pixelPerMinute) {
		if (BarWidthMode.ABSOLUTE == mode) {
			return Math.round(pixelPerMinute * bar.getValue());
		}
		else {
			return Math.round(pixelPerMinute * 1);
		}
	}
	
	private void changeFirstBar(List<ColorBarInternal> internalColorBars, int difference) {
		ColorBarInternal firstBar = internalColorBars.get(0);
		firstBar.setPixel(firstBar.getPixel() + difference);
	}
	
	private void handleBarShorter(List<ColorBarInternal> internalColorBars, int difference) {
		changeFirstBar(internalColorBars, difference);
	}

	private void handleBarLonger(List<ColorBarInternal> internalColorBars,	int difference) {
		if (difference <= BAR_MIN_WIDTH) {
			changeFirstBar(internalColorBars, difference *(-1));
		}
		else {
			int sumLonger = difference;
			
			while (sumLonger > 0) {
				for (ColorBarInternal bar : internalColorBars) {
					if (sumLonger > 0 && (bar.getPixel() > BAR_MIN_WIDTH)) {
						bar.setPixel(bar.getPixel() - 1);
						sumLonger--;
					}
				}
			}
			
		}
	}

	private class ColorBarInternal implements Comparable<ColorBarInternal>{
		ColorBar colorBar;
		Integer pixel;
		
		public ColorBarInternal(ColorBar colorBar, Integer pixel) {
			this.colorBar = colorBar;
			this.pixel = pixel;
		}

		@Override
		public int compareTo(ColorBarInternal another) {
			return another.getPixel().compareTo(this.getPixel());
		}
		
		public Integer getPixel() {
			return this.pixel;
		}
		
		public void setPixel(int pixel) {
			this.pixel = pixel;
		}

		public ColorBar getColorBar() {
			return this.colorBar;
		}
	}
	
	private enum BarWidthMode {
		ABSOLUTE, PROPORTIONAL
	}
}
