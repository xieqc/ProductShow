package com.productshow.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

public class ExtendedWebView extends WebView {
    public ExtendedWebView(Context context) {
        super(context);
    }

    public ExtendedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean canScrollHor(int direction, int x, int y) {
    	//Æ«ÒÆ
        final int offset = computeHorizontalScrollOffset();
        //·¶Î§
        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
//        Log.v("test", "Æ«ÒÆ£º"+offset+" ·¶Î§£º"+range+" ·½Ïò£º"+direction+" x£º"+x+" y£º"+y+" voffset"+this.computeVerticalScrollOffset());
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
        
    }
}
