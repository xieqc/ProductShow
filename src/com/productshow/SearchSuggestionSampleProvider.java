package com.productshow;

import android.content.SearchRecentSuggestionsProvider;

public class SearchSuggestionSampleProvider extends SearchRecentSuggestionsProvider {

    final static String AUTHORITY="com.android.cbin.SearchSuggestionSampleProvider";
    final static int MODE=DATABASE_MODE_QUERIES;
    
    public SearchSuggestionSampleProvider(){
        super();
        setupSuggestions(AUTHORITY, MODE);
    }
}