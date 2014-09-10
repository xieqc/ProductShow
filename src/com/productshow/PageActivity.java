package com.productshow;

import com.productshow.bean.JavaScriptInterfaceBean;
import com.productshow.view.ExtendedWebView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@SuppressLint("NewApi")
public class PageActivity extends ActionBarActivity {
	public Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page);
		//设置屏幕方向
		SharedPreferences sharedPre=getSharedPreferences("config", Context.MODE_PRIVATE);
		String screenChange=sharedPre.getString("screenChange", "on");
		if(screenChange.equalsIgnoreCase("on"))  {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);	//按感应器
		} else {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	
		}
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Bundle bundle = this.getIntent().getExtras();
		String url = bundle.getString("url");
		
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.page_progressBar);
		progressBar.setMax(100);
		
		final WebView webView = (WebView)findViewById(R.id.page_webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		JavaScriptInterfaceBean jsb = new JavaScriptInterfaceBean(this, handler);
		webView.addJavascriptInterface(jsb, "bean");
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressBar.setProgress(newProgress);
				if(newProgress==100){
					progressBar.setVisibility(View.GONE);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				progressBar.setVisibility(View.VISIBLE);
				view.loadUrl(url);
				// 如果不需要其他对点击链接事件的处理返回true，否则返回false
				return true;
			}
		});
		webView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if(webView.canGoBack()){
							progressBar.setVisibility(View.VISIBLE);
							webView.goBack();   //后退
						} else {
							finish();	//退出Activity
						}
						return true;    //已处理
					}
				}
				return false;
			}
		});
		webView.loadUrl(url);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.page_back:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.page, menu);
		return true;
	}
}
