package com.planet.wondering.chemi.model.storage;

import android.content.Context;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.SearchWord;

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
            mSearchWords.add(searchWord);
        }
    }

    public static SearchLatestStorage getStorage(Context context) {
        if (sSearchLatestStorage == null) {
            sSearchLatestStorage = new SearchLatestStorage(context.getApplicationContext());
        }
        return sSearchLatestStorage;
    }

    public ArrayList<SearchWord> getSearchWords() {
        return mSearchWords;
    }
}
