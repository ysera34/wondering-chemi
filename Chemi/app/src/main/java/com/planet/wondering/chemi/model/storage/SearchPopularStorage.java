package com.planet.wondering.chemi.model.storage;

import android.content.Context;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.SearchWord;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class SearchPopularStorage {

    private static SearchPopularStorage sSearchPopularStorage;
    private Context mContext;

    private ArrayList<SearchWord> mSearchWords;

    private SearchPopularStorage(Context context) {
        mContext = context;
        mSearchWords = new ArrayList<>();
        String[] searchWordArr = mContext.getResources()
                .getStringArray(R.array.search_popular_word_array);

        for (int i = 0; i < 8; i++) {
            SearchWord searchWord = new SearchWord();
            searchWord.setRatingNumber(i + 1);
            searchWord.setSearchWord(searchWordArr[i]);
            searchWord.setVariationValue(new Random().nextInt(9) + 1);
            searchWord.setVariationState(i%2 == 0);
            mSearchWords.add(searchWord);
        }
    }

    public static SearchPopularStorage getStorage(Context context) {
        if (sSearchPopularStorage == null) {
            sSearchPopularStorage = new SearchPopularStorage(context.getApplicationContext());
        }
        return sSearchPopularStorage;
    }

    public ArrayList<SearchWord> getSearchWords() {
        return mSearchWords;
    }

}
