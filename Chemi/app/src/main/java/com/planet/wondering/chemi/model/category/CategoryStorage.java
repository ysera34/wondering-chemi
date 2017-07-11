package com.planet.wondering.chemi.model.category;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 7. 3..
 */

public class CategoryStorage {

    private static CategoryStorage sCategoryStorage;
    private Context mContext;

    public static CategoryStorage getCategoryStorage(Context context) {
        if (sCategoryStorage == null) {
            sCategoryStorage = new CategoryStorage(context);
        }
        return sCategoryStorage;
    }

    private ArrayList<CategoryGroup> mCategoryGroups;

    private CategoryStorage(Context context) {
        mContext = context;
        mCategoryGroups = new ArrayList<>();
    }

    public ArrayList<CategoryGroup> getCategoryGroups() {
        return mCategoryGroups;
    }

    public CategoryGroup getCategoryGroup(int categoryGroupId) {
        return new CategoryGroup(mContext, categoryGroupId);
    }
}
