package com.planet.wondering.chemi.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;

/**
 * Created by yoon on 2017. 6. 28..
 */

public class CategoryBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = CategoryBottomSheetDialogFragment.class.getSimpleName();

    private static final String ARG_CATEGORY_GROUP = "category_group";

    public static CategoryBottomSheetDialogFragment newInstance() {

        Bundle args = new Bundle();

        CategoryBottomSheetDialogFragment fragment = new CategoryBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CategoryBottomSheetDialogFragment newInstance(int categoryGroup) {

        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_GROUP, categoryGroup);

        CategoryBottomSheetDialogFragment fragment = new CategoryBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }



}
