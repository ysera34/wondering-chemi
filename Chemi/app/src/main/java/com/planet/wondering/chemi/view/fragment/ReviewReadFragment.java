package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 4. 12..
 */

public class ReviewReadFragment extends Fragment
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = ReviewReadFragment.class.getSimpleName();

    public static ReviewReadFragment newInstance() {

        Bundle args = new Bundle();

        ReviewReadFragment fragment = new ReviewReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private InputMethodManager mInputMethodManager;
    private RelativeLayout mReviewReadMoreMenuLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_read, container, false);
        mReviewReadMoreMenuLayout = (RelativeLayout) view.findViewById(R.id.review_read_more_menu_layout);
        mReviewReadMoreMenuLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_read_more_menu_layout:
                showPopupMenu(mReviewReadMoreMenuLayout);
//                displayPopupWindow(mReviewReadMoreMenuLayout);
//                initiatePopupWindow(mReviewReadMoreMenuLayout);
                break;
            case R.id.popup_menu_action_update:
                popupWindow.dismiss();
                Toast.makeText(getActivity(), "popup_menu_action_update", Toast.LENGTH_SHORT).show();
                break;
            case R.id.popup_menu_action_delete:
                popupWindow.dismiss();
                Toast.makeText(getActivity(), "popup_menu_action_delete", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_more_review_read, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                Toast.makeText(getActivity(), "action_update", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete:
                Toast.makeText(getActivity(), "action_delete", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private void displayPopupWindow(View view) {
        PopupWindow popupWindow = new PopupWindow(getActivity());
        View layout = getActivity().getLayoutInflater().inflate(R.layout.layout_popup_menu, null);
        popupWindow.setContentView(layout);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(view);
    }

    PopupWindow popupWindow;

    private void initiatePopupWindow(View anchorView) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_popup_menu, null);
        TextView menuItem1 = (TextView) view.findViewById(R.id.popup_menu_action_update);
        menuItem1.setOnClickListener(this);
        TextView menuItem2 = (TextView) view.findViewById(R.id.popup_menu_action_delete);
        menuItem2.setOnClickListener(this);

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        popupWindow = new PopupWindow(view, FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, true);
        Drawable backgroundDrawable = getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame);
        popupWindow.setBackgroundDrawable(backgroundDrawable);
        popupWindow.showAsDropDown(anchorView, 5, 5);

    }
}
