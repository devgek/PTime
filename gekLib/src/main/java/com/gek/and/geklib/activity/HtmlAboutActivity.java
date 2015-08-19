package com.gek.and.geklib.activity;

import android.webkit.WebView;

import com.gek.and.geklib.R;
/**
 * HtmlAboutActivity loads the content of the about activity screen from an html file which has to be located in folder
 * 'asset'. This html content is shown via a WebView.
 * @author moo
 *
 */
public class HtmlAboutActivity extends AboutActivity {
	private String htmlTemplate;

	@Override
	protected void populateParameter() {
		super.populateParameter();
		
		String extraHtml = getIntent().getStringExtra("gek_about_html");
		this.htmlTemplate = extraHtml != null ? extraHtml : null;
	}

	@Override
	protected void prepareDataForContentView() {
		WebView webView = (WebView) findViewById(R.id.gek_about_html);
		webView.clearCache(true);
		webView.clearHistory();
//		webView.loadData(this.content, "text/html", "UTF-8");
		webView.loadUrl("file:///android_asset/" + htmlTemplate);
	}

	@Override
	protected int getContentResourceId() {
		return R.layout.gek_about_webview;
	}

}
