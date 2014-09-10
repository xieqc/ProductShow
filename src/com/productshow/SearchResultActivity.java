package com.productshow;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.widget.TextView;

public class SearchResultActivity extends Activity {
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchresult);
		tv = (TextView)findViewById(R.id.textView1);
		doSearchQuery();
	}
	
	public void doSearchQuery(){
        final Intent intent = getIntent();
        //�����������ֵ
        String query=intent.getStringExtra(SearchManager.QUERY);
        //����������¼
        SearchRecentSuggestions suggestions=new SearchRecentSuggestions(this, SearchSuggestionSampleProvider.AUTHORITY, SearchSuggestionSampleProvider.MODE);
        suggestions.saveRecentQuery(query, null);
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            //��ȡ���ݵ�����
            Bundle bundled=intent.getBundleExtra(SearchManager.APP_DATA);
            if(bundled!=null){
                String ttdata=bundled.getString("data");
                tv.setText(ttdata);
                Log.v("test","================================="+ttdata);
            }else{
            	tv.setText("no data");
                Log.v("test","=================================no data");
            }
        }
    }
}
