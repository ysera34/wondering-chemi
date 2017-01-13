package com.planet.wondering.chemi.model.storage;

import android.content.Context;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.SearchWord;
import com.planet.wondering.chemi.util.helper.SearchPreferences;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 13..
 */

public class SearchLatestStorage {

    private static SearchLatestStorage sSearchLatestStorage;
    private Context mContext;

    private ArrayList<SearchWord> mSearchWords;

    private SearchLatestStorage(Context context) {
        mContext = context;
        mSearchWords = new ArrayList<>();
        String[] searchWordArr = mContext.getResources()
                .getStringArray(R.array.search_latest_word_array);

        for (int i = 0; i < 3; i++) {
            SearchWord searchWord = new SearchWord();
            searchWord.setSearchWord(searchWordArr[i]);
            searchWord.setSearchWordIndex(i);
            SearchPreferences.setStoredSearchWordIndex(mContext, searchWord.getSearchWordIndex());
            SearchPreferences.setStoredSearchWordObject(
                    mContext, searchWord, SearchPreferences.getStoredSearchWordIndex(mContext));
//            mSearchWords.add(searchWord);
        }
    }

    public static SearchLatestStorage getStorage(Context context) {
        if (sSearchLatestStorage == null) {
            sSearchLatestStorage = new SearchLatestStorage(context.getApplicationContext());
        }
        return sSearchLatestStorage;
    }

    public ArrayList<SearchWord> getSearchWords() {
        int index = SearchPreferences.getStoredSearchWordIndex(mContext);
        if (index > 0) {
            int size = SearchPreferences.getStoredSearchWordIndex(mContext) + 1;
            for (int i = 0; i < size; i++) {
                mSearchWords.add(SearchPreferences.getStoredSearchWordObject(mContext, i));
            }
        }
        return mSearchWords;
    }

    public void arrangePreferences() {

    }
}
