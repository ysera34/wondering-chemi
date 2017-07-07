package com.planet.wondering.chemi.util.helper;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.planet.wondering.chemi.model.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yoon on 2017. 1. 27..
 */

public class TagSharedPreferences {

    private static final int MAX_NUMBER_OF_TAGS = 10;
    private static final String PREF_TAGS = "com.planet.wondering.chemi.latest.tags";

    public static ArrayList<Tag> getStoredTags(Context context) {
        Gson gson = new Gson();
        String jsonTags = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_TAGS, null);
        if (jsonTags == null) {
            return null;
        }
        Tag[] tagArray = gson.fromJson(jsonTags, Tag[].class);
        List<Tag> tags = Arrays.asList(tagArray);
        tags = new ArrayList<>(tags);

        Iterator<Tag> iterator = tags.iterator();
        while (iterator.hasNext()) {
            Tag tag = iterator.next();
            if (tag.getName() == null) {
                iterator.remove();
            }
        }
        return (ArrayList<Tag>) tags;
    }

    public static void setStoreTags(Context context, List<Tag> tags) {
        Gson gson = new Gson();
        String jsonTags = gson.toJson(tags);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_TAGS, jsonTags)
                .apply();
    }

    public static void addStoreTag(Context context, Tag tag) {
        List<Tag> tags = getStoredTags(context);
        if (tags == null) {
            tags = new ArrayList<>();
        }
        for (Tag t : tags) {
            if (t.getName().equals(tag.getName())) {
                tags.remove(t);
                break;
            }
        }
        tags.add(tag);

        if (tags.size() > MAX_NUMBER_OF_TAGS) {
            tags.remove(0);
        }
        setStoreTags(context, tags);
    }

    public static void removeStoredTag(Context context, Tag tag) {
        ArrayList<Tag> tags = getStoredTags(context);
        if (tags != null) {
            tags.remove(findTagsIndex(context, tag));
            setStoreTags(context, tags);
        }
    }

    public static void removeAllStoredTag(Context context) {
        ArrayList<Tag> tags = getStoredTags(context);
        if (tags != null) {
            tags.clear();
            setStoreTags(context, tags);
        }
    }

    public static int findTagsIndex(Context context, Tag tag) {
        ArrayList<Tag> tags = getStoredTags(context);
        if (tags != null) {
            for (int i = 0; i < tags.size(); i++) {
                if (tags.get(i).getName().equals(tag.getName())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void arrangeStoredTags(List<Tag> tags) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (Tag tag : tags) {
            indices.add(tags.indexOf(tag));
        }
        Collections.sort(indices);
        for (int i : indices) {
            Log.i("arrangeStoredTags int", String.valueOf(i));
        }
    }
}
