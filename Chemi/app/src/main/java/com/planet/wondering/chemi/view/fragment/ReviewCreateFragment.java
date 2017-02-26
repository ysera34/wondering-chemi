package com.planet.wondering.chemi.view.fragment;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.view.activity.ReviewActivity;

/**
 * Created by yoon on 2017. 2. 23..
 */

public class ReviewCreateFragment extends Fragment {

    public static ReviewCreateFragment newInstance() {

        Bundle args = new Bundle();

        ReviewCreateFragment fragment = new ReviewCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Toolbar mReviewCreateToolbar;
    private LinearLayout mReviewCreateRatingBarLayout;
    private TextView mReviewCreateMessageTextView;
    private RatingBar mReviewCreateRatingValueRatingBar;
    private EditText mReviewCreateReviewEditText;
    private ImageButton mReviewCreateImage1ImageButton;
    private ImageButton mReviewCreateImage2ImageButton;
    private ImageButton mReviewCreateImage3ImageButton;

    private Review mReview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        setRetainInstance(true);
    }

//    public void setReview(Review review) {
//        mReview = review;
//    }
//
//    public Review getReview() {
//        return mReview;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_create, container, false);

        mReviewCreateToolbar = (Toolbar) view.findViewById(R.id.review_create_toolbar);
        ((ReviewActivity) getActivity()).setSupportActionBar(mReviewCreateToolbar);
//        mReviewCreateToolbar.setTitle("리뷰 작성");
        ((ReviewActivity) getActivity()).getSupportActionBar().setTitle("리뷰 작성");
        mReviewCreateRatingBarLayout = (LinearLayout) view.findViewById(R.id.review_create_rating_bar_layout);
        mReviewCreateMessageTextView = (TextView) view.findViewById(R.id.review_create_message_text_view);
        mReviewCreateRatingValueRatingBar = (RatingBar) view.findViewById(R.id.review_create_rating_value_rating_bar);
        mReviewCreateReviewEditText = (EditText) view.findViewById(R.id.review_create_review_edit_text);
        mReviewCreateImage1ImageButton = (ImageButton) view.findViewById(R.id.review_create_review_image1_image_button);
        mReviewCreateImage2ImageButton = (ImageButton) view.findViewById(R.id.review_create_review_image2_image_button);
        mReviewCreateImage3ImageButton = (ImageButton) view.findViewById(R.id.review_create_review_image3_image_button);
       return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_review_create, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_review_confirm:
                Toast.makeText(getActivity(), "action_review_confirm", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
//            Toast.makeText(getActivity(), "keyboard visible", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
//            Toast.makeText(getActivity(), "keyboard invisible", Toast.LENGTH_SHORT).show();
//        }
//    }
}
