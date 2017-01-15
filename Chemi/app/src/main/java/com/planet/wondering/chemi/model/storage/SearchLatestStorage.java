package com.planet.wondering.chemi.model.storage;

import android.content.Context;
import android.util.Log;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.SearchWord;
import com.planet.wondering.chemi.util.helper.SearchPreferences;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 13..
 */

public class SearchLatestStorage {

    private static final String TAG = SearchLatestStorage.class.getSimpleName();

    private static SearchLatestStorage sSearchLatestStorage;
    private Context mContext;

    private ArrayList<SearchWord> mSearchWords;

    private int mSharedPreferenceIndex;
    private boolean mRemoveSearchWord;
    private ArrayList<Integer> mRemoveIndices;

    private SearchLatestStorage(Context context) {
        mContext = context;
        mSearchWords = new ArrayList<>();
        if (SearchPreferences.getStoredSearchWordIndex(mContext) != 0) {
            mSharedPreferenceIndex = SearchPreferences.getStoredSearchWordIndex(mContext);
        }
        mRemoveIndices = new ArrayList<>();
        Log.d(TAG + " mSharedPreferenceIndex", String.valueOf(mSharedPreferenceIndex));

        String[] searchWordArr = mContext.getResources()
                .getStringArray(R.array.search_latest_word_array);

        for (int i = 0; i < 16; i++) {
            SearchWord searchWord = new SearchWord();
            searchWord.setSearchWord(searchWordArr[i]);
            searchWord.setSearchWordIndex(i);

            SearchPreferences.setStoredSearchWordIndex(mContext, searchWord.getSearchWordIndex());
            SearchPreferences.setStoredSearchWordObject(
                    mContext, searchWord, SearchPreferences.getStoredSearchWordIndex(mContext));
//            mSearchWords.add(searchWord);
        }
        mSharedPreferenceIndex = SearchPreferences.getStoredSearchWordIndex(mContext);
        Log.d(TAG + " after spIndex", String.valueOf(mSharedPreferenceIndex)); // 15

    }

    public static SearchLatestStorage getStorage(Context context) {
        if (sSearchLatestStorage == null) {
            sSearchLatestStorage = new SearchLatestStorage(context.getApplicationContext());
        }
        return sSearchLatestStorage;
    }

    public ArrayList<SearchWord> getSearchWords() {
        if (mSharedPreferenceIndex > 0) {
            mSearchWords.clear();
            for (int i = 0; i <= mSharedPreferenceIndex; i++) {
                mSearchWords.add(SearchPreferences.getStoredSearchWordObject(mContext, i));
            }
        }
        return mSearchWords;
    }

    /**
     *
     * @param searchWord
     * index from 1 to 10
     */
    public void addSearchWord(SearchWord searchWord) {
//        SearchPreferences.setStoredSearchWordIndex(mContext, searchWord.getSearchWordIndex());
//        SearchPreferences.setStoredSearchWordObject(
//                mContext, searchWord, SearchPreferences.getStoredSearchWordIndex(mContext));
        Log.d(TAG + " before index", String.valueOf(mSharedPreferenceIndex));
        SearchPreferences.setStoredSearchWordIndex(mContext, mSharedPreferenceIndex + 1);
        mSharedPreferenceIndex = SearchPreferences.getStoredSearchWordIndex(mContext);
        Log.d(TAG + " after index", String.valueOf(mSharedPreferenceIndex));
        SearchPreferences.setStoredSearchWordObject(mContext, searchWord, mSharedPreferenceIndex);
    }

    /**
     *
     * @param position : adapter position + 1 = preference index
     * @return result array after remove
     */
    public ArrayList<SearchWord> removeSearchWord(int position) {
        Log.d(TAG + "position", String.valueOf(position));
        int index = position;
        SearchPreferences.removeStoredSearchWordObject(mContext, index);
        mSearchWords.remove(index);
        mRemoveIndices.add(index);
        return mSearchWords;
    }

    public void removeAllSearchWords() {
        for (int i = 1; i <= mSharedPreferenceIndex; i++) {
            SearchPreferences.removeStoredSearchWordObject(mContext, i);
        }
        SearchPreferences.setStoredSearchWordIndex(mContext, 0);
        mSharedPreferenceIndex = SearchPreferences.getStoredSearchWordIndex(mContext);
    }

    public void arrangePreferences() {
        if (mRemoveIndices.size() > 0) {
            // arrange index
//            mSharedPreferenceIndex -= mRemoveIndices.size();
//            SearchPreferences.setStoredSearchWordIndex(mContext, mSharedPreferenceIndex);
//            mRemoveIndices.clear();

            // arrange searchWord object
            mSharedPreferenceIndex = 0;
            for (SearchWord searchWord : mSearchWords) {
            }

        }
    }
}
