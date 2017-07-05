package com.planet.wondering.chemi.model.category;

import android.content.Context;

import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 7. 5..
 */

public class CategoryGroup {

    private int mId;
    private int mNameResId;
    private int mImageResId;
    private Context mContext;
    private ArrayList<CategoryPart> mCategoryParts;
    private String[] group0PartNameArray;
    private String[] group1PartNameArray;
    private String[] group2PartNameArray;
    private String[] group3PartNameArray;


    public CategoryGroup(Context context, int id) {
        mContext = context;
        mCategoryParts = new ArrayList<>();
        setCategoryGroup(id);
    }

    public CategoryGroup(int nameResId, int imageResId) {
        mNameResId = nameResId;
        mImageResId = imageResId;
    }

    private void setCategoryGroup(int id) {
        group0PartNameArray = mContext.getResources().getStringArray(R.array.category_group_0_part_name_array);
        group1PartNameArray = mContext.getResources().getStringArray(R.array.category_group_1_part_name_array);
        group2PartNameArray = mContext.getResources().getStringArray(R.array.category_group_2_part_name_array);
        group3PartNameArray = mContext.getResources().getStringArray(R.array.category_group_3_part_name_array);

        switch (id) {
            case 0:
                for (int i = 0; i < group0PartNameArray.length; i++) {
                    mCategoryParts.add(new CategoryPart(group0PartNameArray[i], group0PartIconSelectorResIdArray[i]));
                }
                break;
            case 1:
                for (int i = 0; i < group1PartNameArray.length; i++) {
                    mCategoryParts.add(new CategoryPart(group1PartNameArray[i], group1PartIconSelectorResIdArray[i]));
                }
                break;
            case 2:
                for (int i = 0; i < group2PartNameArray.length; i++) {
                    mCategoryParts.add(new CategoryPart(group2PartNameArray[i], group2PartIconSelectorResIdArray[i]));
                }
                break;
            case 3:
                for (int i = 0; i < group3PartNameArray.length; i++) {
                    mCategoryParts.add(new CategoryPart(group3PartNameArray[i], group3PartIconSelectorResIdArray[i]));
                }
                break;
        }
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getNameResId() {
        return mNameResId;
    }

    public void setNameResId(int nameResId) {
        mNameResId = nameResId;
    }

    public int getImageResId() {
        return mImageResId;
    }

    public void setImageResId(int imageResId) {
        mImageResId = imageResId;
    }

    public ArrayList<CategoryPart> getCategoryParts() {
        return mCategoryParts;
    }

    public void setCategoryParts(ArrayList<CategoryPart> categoryParts) {
        mCategoryParts = categoryParts;
    }

//    private String[] group0PartNameArray = mContext.getResources().getStringArray(R.array.category_group_0_part_name_array);
//    private String[] group1PartNameArray = mContext.getResources().getStringArray(R.array.category_group_1_part_name_array);
//    private String[] group2PartNameArray = mContext.getResources().getStringArray(R.array.category_group_2_part_name_array);
//    private String[] group3PartNameArray = mContext.getResources().getStringArray(R.array.category_group_3_part_name_array);

    private int[] group0PartIconSelectorResIdArray = {
            R.drawable.selector_category_baby_nappy, R.drawable.selector_category_wipes,
            R.drawable.selector_category_skincare, R.drawable.selector_category_skincare,
            R.drawable.selector_category_suncare, R.drawable.selector_category_haricare,
            R.drawable.selector_category_etc,};

    private int[] group1PartIconSelectorResIdArray = {
            R.drawable.selector_category_female_product, R.drawable.selector_category_momcare,
            R.drawable.selector_category_etc,};

    private int[] group2PartIconSelectorResIdArray = {
            R.drawable.selector_category_skincare, R.drawable.selector_category_haricare,
            R.drawable.selector_category_dentalcare, R.drawable.selector_category_etc,};

    private int[] group3PartIconSelectorResIdArray = {
            R.drawable.selector_category_wipes, R.drawable.selector_category_living,
            R.drawable.selector_category_etc,};
}
