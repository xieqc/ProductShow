package com.productshow;

import java.util.Locale;

import com.productshow.bean.JavaScriptInterfaceBean;
import com.productshow.view.ExtendedWebView;
import com.productshow.view.WebViewPager;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	        if((System.currentTimeMillis()-exitTime) > 2000){
	            Toast.makeText(getApplicationContext(), getString(R.string.exit_verify), Toast.LENGTH_SHORT).show();
	            exitTime = System.currentTimeMillis();
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	WebViewPager mViewPager;

    @Override
    public boolean onSearchRequested(){
        Bundle bundle=new Bundle();
        //打开浮动搜索框（第一个参数默认添加到搜索框的值）
        //bundle为传递的数据
        startSearch("", false, bundle, false);
        //这个地方一定要返回真 如果只是super.onSearchRequested方法不但onSearchRequested（搜索框默认值）无法添加到搜索框中,bundle也无法传递出去
        return true;
    }
    
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		this.screenChange();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.screenChange();	
		this.doSearchQuery();
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (WebViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id) {
			case R.id.action_serach:
				Toast.makeText(this, "action_serach", Toast.LENGTH_SHORT).show();
				onSearchRequested();
				return true;
			case R.id.action_settings:
				Intent intent = new Intent();
				intent.setClass(this,PageActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("url", "file:///android_asset/html/setting.html");
				intent.putExtras(bundle);
				startActivity(intent);
				return true;
			case R.id.action_exit:
				finish();
				System.exit(0);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		public Handler handler = new Handler();
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			final ExtendedWebView webView = (ExtendedWebView) rootView.findViewById(R.id.main_webView);
			final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.main_progressBar);
			progressBar.setMax(100);
			/** 
	         * 在android 2.3以后进行了较为严格的限制 
	         * 该类可以用来帮助开发者改进他们编写的应用。 
	         * 并且提供了各种的策略， 
	         * 这些策略能随时检查和报告开发者开发应用中存在的问题 
	         
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
			*/
				WebSettings webSettings = webView.getSettings();
				webSettings.setJavaScriptEnabled(true);
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
						//view.loadUrl(url);
						Intent intent = new Intent();
						intent.setClass(PlaceholderFragment.this.getActivity(),PageActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("url", url);
						intent.putExtras(bundle);
						startActivity(intent);
						// 如果不需要其他对点击链接事件的处理返回true，否则返回false
						return true;
					}
				});
				webView.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (event.getAction() == KeyEvent.ACTION_DOWN) {
							if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
								progressBar.setVisibility(View.VISIBLE);
								webView.goBack();   //后退
								return true;    //已处理
							}
						}
						return false;
					}
				});
				JavaScriptInterfaceBean jsb = new JavaScriptInterfaceBean(this.getActivity(), handler);
				webView.addJavascriptInterface(jsb, "bean");
				
			switch(getArguments().getInt(ARG_SECTION_NUMBER)) {
				case 1:
					webView.loadUrl("http://www.tmall.com/go/market/brand/mbrandstreet.php");//file:///android_asset/html/index.html
					break;
				case 2:
					webView.loadUrl("http://m.tmall.com/tmallCate.htm");//file:///android_asset/html/class.html
					break;
				case 3:
					webView.loadUrl("http://hf.m.tmall.com");
					break;
			}
			return rootView;
		}
	}
	
	
	//设置屏幕方向
	private void screenChange() {
		SharedPreferences sharedPre=getSharedPreferences("config", Context.MODE_PRIVATE);
		String screenChange=sharedPre.getString("screenChange", "on");
		if(screenChange.equalsIgnoreCase("on"))  {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);	//按感应器
		} else {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	
		}
	}
	
	public void doSearchQuery(){
        final Intent intent = getIntent();
        //获得搜索框里值
        String query=intent.getStringExtra(SearchManager.QUERY);
        //保存搜索记录
        SearchRecentSuggestions suggestions=new SearchRecentSuggestions(this, SearchSuggestionSampleProvider.AUTHORITY, SearchSuggestionSampleProvider.MODE);
        suggestions.saveRecentQuery(query, null);
        Log.v("test","================================="+query);
        /*
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            //获取传递的数据
            Bundle bundled=intent.getBundleExtra(SearchManager.APP_DATA);
            if(bundled!=null){
                String ttdata=bundled.getString("data");
                Log.v("test","================================="+ttdata);
            }else{
                Log.v("test","=================================no data");
            }
        }*/
    }
}
