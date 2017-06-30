package com.planet.wondering.chemi.view.fragment;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 6. 28..
 */

public class CategoryBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = CategoryBottomSheetDialogFragment.class.getSimpleName();

    private static final String ARG_CATEGORY_GROUP_ID = "category_group_id";

    public static CategoryBottomSheetDialogFragment newInstance() {

        Bundle args = new Bundle();

        CategoryBottomSheetDialogFragment fragment = new CategoryBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CategoryBottomSheetDialogFragment newInstance(int categoryGroupId) {

        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_GROUP_ID, categoryGroupId);

        CategoryBottomSheetDialogFragment fragment = new CategoryBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mCategoryGroupId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryGroupId = getArguments().getInt(ARG_CATEGORY_GROUP_ID, -1);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.fragment_category_bottom_sheet, null);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        dialog.setContentView(view);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetCallback);
        }

        View parent = (View) view.getParent();
        parent.setFitsSystemWindows(true);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        view.measure(0, 0);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;
        bottomSheetBehavior.setPeekHeight(screenHeight);

        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;

        int searchLayoutHeight = (int) getResources().getDimension(R.dimen.search_editText_height);

        if (params.getBehavior() instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) params.getBehavior()).setBottomSheetCallback(mBottomSheetCallback);
        }

        params.height = screenHeight - statusBarHeight - searchLayoutHeight;
        parent.setLayoutParams(params);
    }



}
