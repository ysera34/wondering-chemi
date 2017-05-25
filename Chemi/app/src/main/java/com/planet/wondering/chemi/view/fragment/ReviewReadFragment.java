package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Comment;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.ProductActivity;
import com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.planet.wondering.chemi.common.Common.LOGIN_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.PRODUCT_LIST_ITEM_THUMBNAIL_SCREEN_WIDTH_RATIO;
import static com.planet.wondering.chemi.common.Common.PRODUCT_LIST_ITEM_THUMBNAIL_WIDTH_HEIGHT_RATIO;
import static com.planet.wondering.chemi.common.Common.REVIEW_COMMENT_TYPE;
import static com.planet.wondering.chemi.network.Config.Comment.COMMENT_PATH;
import static com.planet.wondering.chemi.network.Config.Comment.Key.DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.Review.REVIEW_PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.LOGIN_DIALOG;

/**
 * Created by yoon on 2017. 4. 12..
 */

public class ReviewReadFragment extends Fragment
        implements View.OnClickListener, View.OnFocusChangeListener {

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
    private LinearLayout mReviewReadBackLayout;
    private RelativeLayout mReviewReadMoreMenuLayout;
    private PopupWindow mPopupWindow;

    private NestedScrollView mReviewReadNestedScrollView;

    private LinearLayout mReviewReadProductLayout;
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

    private FragmentManager mChildFragmentManager;
    private Fragment mCommentFragment;

    private EditText mCommentCreateEditText;
    private TextView mCommentSubmitTextView;

    private int mScreenWidth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mReviewId = getArguments().getInt(ARG_REVIEW_ID, -1);
        mProduct = (Product) getArguments().getSerializable(ARG_PRODUCT);
        mReview = (Review) getArguments().getSerializable(ARG_REVIEW);

        mScreenWidth = ((BottomNavigationActivity) getActivity()).getScreenWidth();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_read, container, false);
        mReviewReadBackLayout = (LinearLayout) view.findViewById(R.id.review_read_back_layout);
        mReviewReadBackLayout.setOnClickListener(this);
        mReviewReadMoreMenuLayout = (RelativeLayout) view.findViewById(R.id.review_read_more_menu_layout);
        mReviewReadMoreMenuLayout.setOnClickListener(this);

        mReviewReadNestedScrollView = (NestedScrollView) view.findViewById(R.id.review_read_nested_scroll_view);

        mReviewReadProductLayout = (LinearLayout) view.findViewById(R.id.review_read_product_layout);
        mReviewReadProductLayout.setOnClickListener(this);
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
        mImage1ImageView.setOnClickListener(this);
        mImage2ImageView = (ImageView) view.findViewById(R.id.review_read_review_image2_image_view);
        mImage2ImageView.setOnClickListener(this);
        mImage3ImageView = (ImageView) view.findViewById(R.id.review_read_review_image3_image_view);
        mImage3ImageView.setOnClickListener(this);

        mChildFragmentManager = getChildFragmentManager();
        mCommentFragment = mChildFragmentManager.findFragmentById(R.id.review_comment_fragment_container);

        if (mCommentFragment == null) {
            mCommentFragment = CommentFragment.newInstance(mReview.getId(), REVIEW_COMMENT_TYPE);
            mChildFragmentManager.beginTransaction()
                    .add(R.id.review_comment_fragment_container, mCommentFragment)
                    .commit();
        }

        mCommentCreateEditText = (EditText) view.findViewById(R.id.review_read_comment_edit_text);
        mCommentCreateEditText.setOnFocusChangeListener(this);
        mCommentCreateEditText.setOnClickListener(this);
        mCommentSubmitTextView = (TextView) view.findViewById(R.id.review_read_comment_submit_text_view);
        mCommentSubmitTextView.setOnClickListener(this);

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
        validationEditText();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.review_read_comment_edit_text:
                if (hasFocus) {
                    if (UserSharedPreferences.getStoredToken(getActivity()) != null) {

                    } else {
                        CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                                .newInstance(R.drawable.ic_login, R.string.login_info_message,
                                        R.string.login_button_title, LOGIN_DIALOG_REQUEST_CODE);
                        dialogFragment1.show(getChildFragmentManager(), LOGIN_DIALOG);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_read_back_layout:
                getActivity().onBackPressed();
                break;
            case R.id.review_read_more_menu_layout:
                mInputMethodManager.hideSoftInputFromWindow(mCommentCreateEditText.getWindowToken(), 0);
                displayPopupWindow(mReviewReadMoreMenuLayout);
                break;
            case R.id.popup_menu_action_update:
                mPopupWindow.dismiss();
                mMenuSelectedListener.onMenuSelected(4);
                break;
            case R.id.popup_menu_action_delete:
                mPopupWindow.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("리뷰를 정말 지우시겠어요?");
                builder.setPositiveButton("지우기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestDeleteReview(mReview);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.review_read_product_layout:
                startActivity(ProductActivity.newIntent(getActivity(), mReview.getProductId(), (byte) 0));
                break;
            case R.id.review_read_comment_edit_text:
                if (UserSharedPreferences.getStoredToken(getActivity()) != null) {

                } else {
                    CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                            .newInstance(R.drawable.ic_login, R.string.login_info_message,
                                    R.string.login_button_title, LOGIN_DIALOG_REQUEST_CODE);
                    dialogFragment1.show(getChildFragmentManager(), LOGIN_DIALOG);
                }
                break;
            case R.id.review_read_comment_submit_text_view:
                if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
                    if (isValidatedCreateComment) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("댓글을 등록하시겠어요?");
                        builder1.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mInputMethodManager.hideSoftInputFromWindow(mCommentCreateEditText.getWindowToken(), 0);
                                requestCreateReviewComment(mReview);
                            }
                        });
                        builder1.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog1 = builder1.create();
                        dialog1.show();
                    } else {
                        Toast.makeText(getActivity(), "댓글을 입력해보세요!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                            .newInstance(R.drawable.ic_login, R.string.login_info_message,
                                    R.string.login_button_title, LOGIN_DIALOG_REQUEST_CODE);
                    dialogFragment1.show(getChildFragmentManager(), LOGIN_DIALOG);
                }
                break;
            case R.id.review_read_review_image1_image_view:
                ReviewImageDialogFragment dialogFragment1 = ReviewImageDialogFragment.newInstance(mReview.getImagePaths(), 1);
                dialogFragment1.show(getFragmentManager(), "review_image_fragment");
                break;
            case R.id.review_read_review_image2_image_view:
                ReviewImageDialogFragment dialogFragment2 = ReviewImageDialogFragment.newInstance(mReview.getImagePaths(), 2);
                dialogFragment2.show(getFragmentManager(), "review_image_fragment");
                break;
            case R.id.review_read_review_image3_image_view:
                ReviewImageDialogFragment dialogFragment3 = ReviewImageDialogFragment.newInstance(mReview.getImagePaths(), 3);
                dialogFragment3.show(getFragmentManager(), "review_image_fragment");
                break;
        }
    }

    private boolean isValidatedCreateComment = false;

    private void validationEditText() {
        mCommentCreateEditText.addTextChangedListener(new TextValidator(mCommentCreateEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.length() > 0) {
                    mCommentSubmitTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                    mCommentSubmitTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                    isValidatedCreateComment = true;
                } else {
                    mCommentSubmitTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                    mCommentSubmitTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                    isValidatedCreateComment = false;
                }
            }
        });
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

        int thumbnailWidth = (int) (mScreenWidth * PRODUCT_LIST_ITEM_THUMBNAIL_SCREEN_WIDTH_RATIO);
        int thumbnailHeight = (int) (thumbnailWidth * PRODUCT_LIST_ITEM_THUMBNAIL_WIDTH_HEIGHT_RATIO);

        Glide.with(getActivity())
                .load(mReview.getProductImagePath())
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
//                .override(260, 200)
                .override(thumbnailWidth, thumbnailHeight)
                .centerCrop()
                .crossFade()
                .into(mReviewReadProductImageView);
        mReviewReadProductBrandTextView.setText(mReview.getProductBrand());
        mReviewReadProductNameTextView.setText(mReview.getProductName());
        mReviewReadProductNameTextView.setSelected(true);
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

    private void requestDeleteReview(Review review) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE, URL_HOST + PATH + review.getProductId() + REVIEW_PATH + review.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(),
                                "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestCreateReviewComment(Review review) {

        Map<String, String> params = new HashMap<>();
        params.put(DESCRIPTION, mCommentCreateEditText.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + REVIEW_PATH + review.getId() + COMMENT_PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "댓글이 등록되었어요", Toast.LENGTH_SHORT).show();

                        mCommentCreateEditText.getText().clear();
                        mCommentCreateEditText.clearFocus();
                        mCommentSubmitTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                        mCommentSubmitTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                        isValidatedCreateComment = false;
                        Fragment fragment = mChildFragmentManager.findFragmentById(R.id.review_comment_fragment_container);
                        if (fragment instanceof CommentFragment) {
                            ((CommentFragment) fragment).updateCommentList(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    public void commentSelected(Comment comment) {

    }

    public void hideSoftKeyboard() {
        mInputMethodManager.hideSoftInputFromWindow(mCommentCreateEditText.getWindowToken(), 0);
    }

    public void commentNestedScroll() {
        mReviewReadNestedScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mReviewReadNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
            }
        }, 200);
//        final Handler handler = new Handler();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {Thread.sleep(100);} catch (InterruptedException e) {Log.e(TAG, e.getMessage());}
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mReviewReadNestedScrollView.fullScroll(View.FOCUS_DOWN);
//                    }
//                });
//            }
//        }).start();
    }

    public void commentEditDialogFinished(String description) {
        Fragment fragment = mChildFragmentManager.findFragmentById(R.id.review_comment_fragment_container);
        if (fragment instanceof CommentFragment) {
            ((CommentFragment) fragment).commentEditDialogFinished(description);
        }
    }

    OnMenuSelectedListener mMenuSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mMenuSelectedListener = (OnMenuSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implements OnReviewEditListener");
        }
    }
}
