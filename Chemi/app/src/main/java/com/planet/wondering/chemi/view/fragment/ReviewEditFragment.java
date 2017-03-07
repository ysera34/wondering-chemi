package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnReviewEditListener;
import com.planet.wondering.chemi.view.activity.ReviewActivity;

/**
 * Created by yoon on 2017. 2. 28..
 */

public class ReviewEditFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ReviewEditFragment.class.getSimpleName();

    private static final String ARG_REVIEW_CONTENT = "review_content";

    public static ReviewEditFragment newInstance() {

        Bundle args = new Bundle();

        ReviewEditFragment fragment = new ReviewEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewEditFragment newInstance(String reviewContent) {

        Bundle args = new Bundle();
        args.putString(ARG_REVIEW_CONTENT, reviewContent);

        ReviewEditFragment fragment = new ReviewEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String mReviewContent;

    private Toolbar mReviewEditToolbar;
    private InputMethodManager mInputMethodManager;
    private EditText mReviewEditReviewEditText;
    private Button mReviewEditReviewEditCompleteButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mReviewContent = getArguments().getString(ARG_REVIEW_CONTENT, "");
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_edit, container, false);

        mReviewEditToolbar = (Toolbar) view.findViewById(R.id.review_edit_toolbar);
        ((ReviewActivity) getActivity()).setSupportActionBar(mReviewEditToolbar);
        ((ReviewActivity) getActivity()).getSupportActionBar().setTitle("리뷰 작성");

        mReviewEditReviewEditText = (EditText) view.findViewById(R.id.review_edit_review_edit_text);
        if (!mReviewContent.equals("")) {
            mReviewEditReviewEditText.setText(mReviewContent);
        }
//        mInputMethodManager.showSoftInput(mReviewEditReviewEditText, InputMethodManager.SHOW_IMPLICIT);
        mReviewEditReviewEditCompleteButton = (Button)
                view.findViewById(R.id.review_edit_review_edit_complete_button);
        mReviewEditReviewEditCompleteButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_review_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_review_confirm:
//                Toast.makeText(getActivity(), "action_review_confirm", Toast.LENGTH_SHORT).show();
                mReviewEditListener.onReviewEdit(mReviewEditReviewEditText.getText().toString(), false);
                mInputMethodManager.hideSoftInputFromWindow(mReviewEditReviewEditText.getWindowToken(), 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_edit_review_edit_complete_button:
                mReviewEditListener.onReviewEdit(mReviewEditReviewEditText.getText().toString(), false);
                mInputMethodManager.hideSoftInputFromWindow(mReviewEditReviewEditText.getWindowToken(), 0);
                break;
        }
    }

    OnReviewEditListener mReviewEditListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mReviewEditListener = (OnReviewEditListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implements OnReviewEditListener");
        }
    }


}