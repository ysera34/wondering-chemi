package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnReviewEditListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.Review.Key.DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.Review.Key.RATING;
import static com.planet.wondering.chemi.network.Config.Review.REVIEW_PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;

/**
 * Created by yoon on 2017. 4. 17..
 */

public class ReviewUpdateFragment extends Fragment
        implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private static final String TAG = ReviewUpdateFragment.class.getSimpleName();

    private static final String ARG_REVIEW = "review";

    public static ReviewUpdateFragment newInstance() {

        Bundle args = new Bundle();

        ReviewUpdateFragment fragment = new ReviewUpdateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewUpdateFragment newInstance(Review review) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_REVIEW, review);

        ReviewUpdateFragment fragment = new ReviewUpdateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RelativeLayout mReviewUpdateConfirmLayout;
    private ImageView mUpdateProductImageView;
    private TextView mUpdateProductBrandTextView;
    private TextView mUpdateProductNameTextView;
    private TextView mUpdateRatingMessageTextView;
    private RatingBar mUpdateReviewRatingBar;
    private TextView mUpdateReviewLengthTextView;
    private TextView mUpdateReviewTextView;
    private ImageView mUpdateReviewImage1ImageView;
    private ImageView mUpdateReviewImage2ImageView;
    private ImageView mUpdateReviewImage3ImageView;

    private String mReviewHint;
    private Review mReview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReviewHint = getString(R.string.review_create_review_hint);
        mReview = (Review) getArguments().getSerializable(ARG_REVIEW);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_update, container, false);

        mReviewUpdateConfirmLayout = (RelativeLayout) view.findViewById(R.id.review_update_confirm_layout);
        mReviewUpdateConfirmLayout.setOnClickListener(this);
        mUpdateProductImageView = (ImageView) view.findViewById(R.id.review_update_product_image_view);
        mUpdateProductBrandTextView = (TextView) view.findViewById(R.id.review_update_product_brand_text_view);
        mUpdateProductNameTextView = (TextView) view.findViewById(R.id.review_update_product_name_text_view);
        mUpdateRatingMessageTextView = (TextView) view.findViewById(R.id.review_update_message_text_view);
        mUpdateReviewRatingBar = (RatingBar) view.findViewById(R.id.review_update_rating_value_rating_bar);
        mUpdateReviewRatingBar.setOnRatingBarChangeListener(this);
        mUpdateReviewLengthTextView = (TextView) view.findViewById(R.id.review_update_review_text_length_text_view);
        mUpdateReviewTextView = (TextView) view.findViewById(R.id.review_update_review_text_view);
        mUpdateReviewTextView.setOnClickListener(this);
        mUpdateReviewImage1ImageView = (ImageView) view.findViewById(R.id.review_update_review_image1_image_view);
        mUpdateReviewImage1ImageView.setOnClickListener(this);
        mUpdateReviewImage2ImageView = (ImageView) view.findViewById(R.id.review_update_review_image2_image_view);
        mUpdateReviewImage2ImageView.setOnClickListener(this);
        mUpdateReviewImage3ImageView = (ImageView) view.findViewById(R.id.review_update_review_image3_image_view);
        mUpdateReviewImage3ImageView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindProduct();
        bindReview();
    }

    private void bindProduct() {
        Glide.with(getActivity())
                .load(mReview.getProductImagePath())
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                .crossFade()
                .override(300, 200)
                .into(mUpdateProductImageView);
        mUpdateProductBrandTextView.setText(mReview.getProductBrand());
        mUpdateProductNameTextView.setText(mReview.getProductName());
    }

    private void bindReview() {
        mUpdateReviewRatingBar.setRating(mReview.getRatingValue());
        String[] messageArray = getResources().getStringArray(R.array.review_rating_message_array);
        for (int i = 0; i < messageArray.length; i++) {
            if (mUpdateReviewRatingBar.getRating() == 0.5f * i) {
                mUpdateRatingMessageTextView.setText(messageArray[i]);
                break;
            }
        }
        mUpdateReviewLengthTextView.setText(
                getString(R.string.review_create_review_length_format,
                        String.valueOf(mReview.getContent().length())));
        mUpdateReviewTextView.setText(mReview.getContent());

        switch (mReview.getImagePaths().size()) {

            case 1:
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage1ImageView);
                mUpdateReviewImage2ImageView.setVisibility(View.GONE);
                mUpdateReviewImage3ImageView.setVisibility(View.GONE);
                break;
            case 2:
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage1ImageView);
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(1))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage2ImageView);
                mUpdateReviewImage3ImageView.setVisibility(View.GONE);
                break;
            case 3:
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage1ImageView);
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(1))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage2ImageView);
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(2))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage3ImageView);
                break;
        }
    }

    private boolean isRatingBarChanged = false;
    private boolean isWriteContent = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_update_confirm_layout:
                if (!isRatingBarChanged) {
                    Toast.makeText(getActivity(), "제품의 별점을 매겨보세요", Toast.LENGTH_SHORT).show();
                } else if (!isWriteContent) {
                    Toast.makeText(getActivity(), "제품의 리뷰를 작성해보세요", Toast.LENGTH_SHORT).show();
                } else {
                    requestUpdateReview();
                    mReviewUpdateConfirmLayout.setEnabled(false);
                }
                break;
            case R.id.review_update_review_text_view:
                mReviewEditListener.onReviewEdit(mUpdateReviewTextView.getText().toString(), true, 2);
                break;
//            case R.id.review_update_review_image1_image_view:
//                if (!isHasUpdateImage1) {
//                    checkStoragePermission(1);
//                } else {
//                    createEditImageMenuBottomSheetDialog(1);
//                }
//                break;
//            case R.id.review_update_review_image2_image_view:
//                if (!isHasUpdateImage2) {
//                    checkStoragePermission(2);
//                } else {
//                    createEditImageMenuBottomSheetDialog(2);
//                }
//                break;
//            case R.id.review_update_review_image3_image_view:
//                if (!isHasUpdateImage3) {
//                    checkStoragePermission(3);
//                } else {
//                    createEditImageMenuBottomSheetDialog(3);
//                }
//                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        String[] messageArray = getResources().getStringArray(R.array.review_rating_message_array);

        for (int i = 0; i < messageArray.length; i++) {
            if (rating == 0.5f * i) {
                mUpdateRatingMessageTextView.setText(messageArray[i]);
                break;
            }
        }
        isRatingBarChanged = true;
    }

    public void updateContentTextView(String reviewContent) {
        if (reviewContent == null || reviewContent.equals("")) {
            mUpdateReviewTextView.setText(mReviewHint);
        } else {
            mUpdateReviewTextView.setText(reviewContent);
            isWriteContent = true;
        }
        mUpdateReviewLengthTextView.setText(
                getString(R.string.review_create_review_length_format,
                        String.valueOf(mUpdateReviewTextView.getText().length())));
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

    private void requestUpdateReview() {

        Map<String, String> params = new HashMap<>();
        params.put(RATING, String.valueOf(mUpdateReviewRatingBar.getRating()));
        params.put(DESCRIPTION, mUpdateReviewTextView.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, URL_HOST + PATH + mReview.getProductId() + REVIEW_PATH + mReview.getId(),
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(),
                                "작성하신 리뷰가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                "리뷰 수정 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
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
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);

    }
}
