package com.productshow;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		//N秒后自动跳转至MainActivity
		final Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		};
		timer.schedule(task, 1000 * 3);
	}
}
