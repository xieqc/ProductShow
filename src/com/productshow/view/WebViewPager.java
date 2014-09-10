package com.productshow.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.productshow.view.ExtendedWebView;

public class WebViewPager extends ViewPager {
    public WebViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
    	//在canScroll方法中检测child views是否还可以滑动，如果不可以才将滑动事件交给viewPager处理
        if (v instanceof ExtendedWebView) {
//            return ((ExtendedWebView) v).canScrollHor(-dx, x, y);
        	return true;
        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }
}