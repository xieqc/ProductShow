package com.productshow.bean;

import org.json.JSONObject;

import com.productshow.PageActivity;
import com.productshow.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaScriptInterfaceBean {
	private FragmentActivity fragmentActivity;
	private String title;
	
	public JavaScriptInterfaceBean(FragmentActivity fragmentActivity, Handler handler) {
		this.fragmentActivity = fragmentActivity;
	}
	
	@JavascriptInterface
	public void toPage(String url) {
		final Intent intent = new Intent();
		intent.setClass(fragmentActivity, PageActivity.class);
		/*将Bundle对象assign给Intent*/
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		intent.putExtras(bundle);
		fragmentActivity.startActivity(intent);
	}
	
	@JavascriptInterface
	public void checkVersion() {
		Toast.makeText(fragmentActivity.getApplicationContext(), fragmentActivity.getString(R.string.checkVersion), Toast.LENGTH_SHORT).show();
	}
	
	@JavascriptInterface
	public String setInit() {
		JSONObject jsonObj = new JSONObject();
		SharedPreferences sharedPre=fragmentActivity.getSharedPreferences("config", Context.MODE_PRIVATE);
		String screenChange=sharedPre.getString("screenChange", "on");
		try {
			jsonObj.put("screenChange",screenChange);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return jsonObj.toString();
	}
	
	@JavascriptInterface
	public void screenChange(String status) {
		SharedPreferences sharedPre=fragmentActivity.getSharedPreferences("config", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();
		editor.putString("screenChange",status);
		editor.commit();
		if(status.equals("on")) {
			fragmentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}else {
			fragmentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	
		}
	}

	@JavascriptInterface
	public String getTitle() {
		return title;
	}

	@JavascriptInterface
	public void setTitle(String title) {
		this.title = title;
	}
}
