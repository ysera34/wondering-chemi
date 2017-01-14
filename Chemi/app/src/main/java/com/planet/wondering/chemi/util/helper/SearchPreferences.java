package com.planet.wondering.chemi.util.helper;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.planet.wondering.chemi.model.SearchWord;

/**
 * Created by yoon on 2017. 1. 13..
 */

public class SearchPreferences {

    private static final int MAX_NUMBER_OF_SEARCH_WORDS = 10;
    private static final String PREF_SEARCH_WORD = "searchWord";
    private static final String PREF_SEARCH_WORD_INDEX = "searchWordIndex";

    public static String getStoredSearchWord(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_WORD, null);
    }

    public static void setStoredSearchWord(Context context, String searchWord) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_WORD, searchWord)
                .apply();
    }

    public static int getStoredSearchWordIndex(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_SEARCH_WORD_INDEX, 0);
    }

    public static void setStoredSearchWordIndex(Context context, int searchWordIndex) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_SEARCH_WORD_INDEX, searchWordIndex)
                .apply();
    }

    public static void removeStoredSearchWordIndex(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_SEARCH_WORD_INDEX)
                .apply();
    }

    public static SearchWord getStoredSearchWordObject(Context context, int searchWordIndex) {
        Gson gson = new Gson();
        String searchWordJson = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_WORD + searchWordIndex, null);
        return gson.fromJson(searchWordJson, SearchWord.class);
    }

    public static void setStoredSearchWordObject(Context context, SearchWord searchWord, int searchWordIndex) {
        Gson gson = new Gson();
        String searchWordJson = gson.toJson(searchWord);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_WORD + searchWordIndex, searchWordJson)
                .apply();
    }

    public static void removeStoredSearchWordObject(Context context, int searchWordIndex) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_SEARCH_WORD + searchWordIndex)
                .apply();
    }
}
