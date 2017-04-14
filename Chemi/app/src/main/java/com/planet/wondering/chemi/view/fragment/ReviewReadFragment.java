package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yoon on 2017. 4. 12..
 */

public class ReviewReadFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ReviewReadFragment.class.getSimpleName();

    private static final String ARG_PRODUCT = "product";
    private static final String ARG_REVIEW = "review";
    private static final String ARG_REVIEW_ID = "review_id";

    public static ReviewReadFragment newInstance() {

        Bundle args = new Bundle();

        ReviewReadFragment fragment = new ReviewReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewReadFragment newInstance(int reviewId) {

        Bundle args = new Bundle();
        args.putInt(ARG_REVIEW_ID, reviewId);

        ReviewReadFragment fragment = new ReviewReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewReadFragment newInstance(Review review) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_REVIEW, review);

        ReviewReadFragment fragment = new ReviewReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewReadFragment newInstance(Product product, Review review) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        args.putSerializable(ARG_REVIEW, review);

        ReviewReadFragment fragment = new ReviewReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mReviewId;
    private Product mProduct;
    private Review mReview;

    private InputMethodManager mInputMethodManager;
    private RelativeLayout mReviewReadMoreMenuLayout;
    private PopupWindow mPopupWindow;

    private ImageView mReviewReadProductImageView;
    private TextView mReviewReadProductBrandTextView;
    private TextView mReviewReadProductNameTextView;

    private TextView mUserNameTextView;
    private CircleImageView mUserProfileImageView;
    private TextView mRatingMessageTextView;
    private RatingBar mRatingBar;
    private TextView mDateTextView;

    private TextView mParentIconTextView;
    private TextView mParentAgeTextView;
    private TextView mParentDrySkinTextView;
    private TextView mParentOilySkinTextView;
    private TextView mParentDryOilySkinTextView;
    private TextView mParentAllergyTextView;

    private LinearLayout mChildLayout;
    private TextView mChildIconTextView;
    private TextView mChildDrySkinTextView;
    private TextView mChildAllergyTextView;

    private TextView mContentTextView;

    private LinearLayout mImagesLayout;
    private ImageView mImage1ImageView;
    private ImageView mImage2ImageView;
    private ImageView mImage3ImageView;

    private RecyclerView mReviewReadCommentRecyclerView;
    private CommentAdapter mCommentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mReviewId = getArguments().getInt(ARG_REVIEW_ID, -1);
        mProduct = (Product) getArguments().getSerializable(ARG_PRODUCT);
        mReview = (Review) getArguments().getSerializable(ARG_REVIEW);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_read, container, false);
        mReviewReadMoreMenuLayout = (RelativeLayout) view.findViewById(R.id.review_read_more_menu_layout);
        mReviewReadMoreMenuLayout.setOnClickListener(this);

        mReviewReadProductImageView = (ImageView) view.findViewById(R.id.review_read_product_image_view);
        mReviewReadProductBrandTextView = (TextView) view.findViewById(R.id.review_read_product_brand_text_view);
        mReviewReadProductNameTextView = (TextView) view.findViewById(R.id.review_read_product_name_text_view);

        mUserNameTextView = (TextView) view.findViewById(R.id.review_read_user_name_text_view);
        mUserProfileImageView = (CircleImageView) view.findViewById(R.id.review_read_user_image_view);
        mRatingMessageTextView = (TextView) view.findViewById(R.id.review_read_rating_message_text_view);
        mRatingBar = (RatingBar) view.findViewById(R.id.review_read_rating_bar);
        mDateTextView = (TextView) view.findViewById(R.id.review_read_date_text_view);

        mParentIconTextView = (TextView) view.findViewById(R.id.review_parent_icon_text_view);
        mParentAgeTextView = (TextView) view.findViewById(R.id.review_parent_age_text_view);
        mParentDrySkinTextView = (TextView) view.findViewById(R.id.review_parent_dry_skin_text_view);
        mParentOilySkinTextView = (TextView) view.findViewById(R.id.review_parent_oily_skin_text_view);
        mParentDryOilySkinTextView = (TextView) view.findViewById(R.id.review_parent_dry_oily_skin_text_view);
        mParentAllergyTextView = (TextView) view.findViewById(R.id.review_parent_allergy_text_view);

        mChildLayout = (LinearLayout) view.findViewById(R.id.review_child_layout);
        mChildIconTextView = (TextView) view.findViewById(R.id.review_child_icon_text_view);
        mChildDrySkinTextView = (TextView) view.findViewById(R.id.review_child_dry_skin_text_view);
        mChildAllergyTextView = (TextView) view.findViewById(R.id.review_child_allergy_text_view);

        mContentTextView = (TextView) view.findViewById(R.id.review_read_content_text_view);

        mImagesLayout = (LinearLayout) view.findViewById(R.id.review_read_images_layout);
        mImage1ImageView = (ImageView) view.findViewById(R.id.review_read_review_image1_image_view);
        mImage2ImageView = (ImageView) view.findViewById(R.id.review_read_review_image2_image_view);
        mImage3ImageView = (ImageView) view.findViewById(R.id.review_read_review_image3_image_view);

        mReviewReadCommentRecyclerView = (RecyclerView) view.findViewById(R.id.review_read_comment_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mReview.isAuthor()) {
            mReviewReadMoreMenuLayout.setVisibility(View.VISIBLE);
        }
        if (mReview != null) {
            bindProduct();
            bindReview();
        } else {
            Toast.makeText(getActivity(), "mReview null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_read_more_menu_layout:
                displayPopupWindow(mReviewReadMoreMenuLayout);
                break;
            case R.id.popup_menu_action_update:
                mPopupWindow.dismiss();
                Toast.makeText(getActivity(), "popup_menu_action_update", Toast.LENGTH_SHORT).show();
                break;
            case R.id.popup_menu_action_delete:
                mPopupWindow.dismiss();
                Toast.makeText(getActivity(), "popup_menu_action_delete", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void displayPopupWindow(View anchorView) {
        mPopupWindow = new PopupWindow(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_popup_menu, null);
        TextView menuItem1 = (TextView) view.findViewById(R.id.popup_menu_action_update);
        menuItem1.setOnClickListener(this);
        TextView menuItem2 = (TextView) view.findViewById(R.id.popup_menu_action_delete);
        menuItem2.setOnClickListener(this);
        mPopupWindow.setContentView(view);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPopupWindow.setElevation(16.0f);
        }
        mPopupWindow.showAsDropDown(anchorView);
    }

    private void bindProduct() {
        Glide.with(getActivity())
                .load(mReview.getProductImagePath())
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                .crossFade()
                .override(300, 200)
                .into(mReviewReadProductImageView);
        mReviewReadProductBrandTextView.setText(mReview.getProductBrand());
        mReviewReadProductNameTextView.setText(mReview.getProductName());
    }

    private void bindReview() {
        mUserNameTextView.setText(mReview.getUser().getName());

        if (mReview.getUser().getImagePath() == null || mReview.getUser().getImagePath().equals("null")) {
            if (mReview.getUser().isGender()) {
                mUserProfileImageView.setImageResource(R.drawable.ic_user_profile_mommy);
            } else {
                mUserProfileImageView.setImageResource(R.drawable.ic_user_profile_daddy);
            }
        } else {
            Glide.with(getActivity())
                    .load(mReview.getUser().getImagePath())
                    .crossFade()
                    .into(mUserProfileImageView);
        }

        if (mReview.getUser().isGender()) {
            mParentIconTextView.setBackgroundResource(R.drawable.ic_mommy_gray);
        } else {
            mParentIconTextView.setBackgroundResource(R.drawable.ic_daddy_gray);
        }

        mParentAgeTextView.setText(mReview.getUser().getAge());

        if (mReview.getUser().isHasDrySkin() && mReview.getUser().isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.VISIBLE);
        } else if (mReview.getUser().isHasDrySkin() && !mReview.getUser().isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.VISIBLE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        } else if (!mReview.getUser().isHasDrySkin() && mReview.getUser().isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.VISIBLE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        } else if (!mReview.getUser().isHasDrySkin() && !mReview.getUser().isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        }

        if (mReview.getUser().isHasAllergy()) {
            mParentAllergyTextView.setVisibility(View.VISIBLE);
        } else {
            mParentAllergyTextView.setVisibility(View.GONE);
        }

        if (mReview.getUser().isHasChild()) {
            mChildLayout.setVisibility(View.VISIBLE);

            if (mReview.getUser().isChildHasDrySkin()) {
                mChildDrySkinTextView.setVisibility(View.VISIBLE);
            } else {
                mChildDrySkinTextView.setVisibility(View.GONE);
            }

            if (mReview.getUser().isChildHasAllergy()) {
                mChildAllergyTextView.setVisibility(View.VISIBLE);
            } else {
                mChildAllergyTextView.setVisibility(View.GONE);
            }

        } else {
            mChildLayout.setVisibility(View.GONE);
        }

        mRatingBar.setRating(mReview.getRatingValue());
        mDateTextView.setText(mReview.getDate());

        String[] messageArray = getResources().getStringArray(R.array.review_rating_message_array);
        for (int i = 0; i < messageArray.length; i++) {
            if (mRatingBar.getRating() == 0.5f * i) {
                mRatingMessageTextView.setText(messageArray[i]);
                break;
            }
        }

        mContentTextView.setText(mReview.getContent());

        if (mReview.getImagePaths().size() == 0) {
            mImagesLayout.setVisibility(View.GONE);
        } else {
            mImagesLayout.setVisibility(View.VISIBLE);

            switch (mReview.getImagePaths().size()) {

                case 1:
                    Glide.with(getActivity())
                            .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                            .crossFade()
                            .override(210, 210)
//                                .centerCrop()
                            .into(mImage1ImageView);
                    mImage2ImageView.setVisibility(View.GONE);
                    mImage3ImageView.setVisibility(View.GONE);
                    break;
                case 2:
                    Glide.with(getActivity())
                            .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                            .crossFade()
                            .override(210, 210)
//                                .centerCrop()
                            .into(mImage1ImageView);
                    Glide.with(getActivity())
                            .load(mReview.getImagePaths().get(1))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                            .crossFade()
                            .override(210, 210)
//                                .centerCrop()
                            .into(mImage2ImageView);
                    mImage3ImageView.setVisibility(View.GONE);
                    break;
                case 3:
                    Glide.with(getActivity())
                            .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                            .crossFade()
                            .override(210, 210)
//                                .centerCrop()
                            .into(mImage1ImageView);
                    Glide.with(getActivity())
                            .load(mReview.getImagePaths().get(1))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                            .crossFade()
                            .override(210, 210)
//                                .centerCrop()
                            .into(mImage2ImageView);
                    Glide.with(getActivity())
                            .load(mReview.getImagePaths().get(2))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                            .crossFade()
                            .override(210, 210)
//                                .centerCrop()
                            .into(mImage3ImageView);
                    break;
            }
        }
    }

    private class CommentAdapter {
    }
}
