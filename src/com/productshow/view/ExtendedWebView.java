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
    	//ƫ��
        final int offset = computeHorizontalScrollOffset();
        //��Χ
        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
//        Log.v("test", "ƫ�ƣ�"+offset+" ��Χ��"+range+" ����"+direction+" x��"+x+" y��"+y+" voffset"+this.computeVerticalScrollOffset());
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
        
    }
}
