package com.planet.wondering.chemi.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.Common;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.util.listener.OnReviewEditListener;
import com.planet.wondering.chemi.view.fragment.ReviewCreateFragment;
import com.planet.wondering.chemi.view.fragment.ReviewEditFragment;
import com.planet.wondering.chemi.view.fragment.ReviewReadFragment;
import com.planet.wondering.chemi.view.fragment.ReviewUpdateFragment;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.network.Config.Review.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;

/**
 * Created by yoon on 2017. 2. 23..
 */

public class ReviewActivity extends BottomNavigationActivity
        implements OnReviewEditListener, OnMenuSelectedListener {

    private static final String TAG = ReviewActivity.class.getSimpleName();

    private static final String EXTRA_PRODUCT = "com.planet.wondering.chemi.product";
    private static final String EXTRA_REVIEW = "com.planet.wondering.chemi.review";
    private static final String EXTRA_REVIEW_ID = "com.planet.wondering.chemi.review_id";
    private static final String EXTRA_REQUEST_ID = "com.planet.wondering.chemi.request_id";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ReviewActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int reviewId, int requestId) {
        Intent intent = new Intent(packageContext, ReviewActivity.class);
        intent.putExtra(EXTRA_REVIEW_ID, reviewId);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, Product product, int requestId) {
        Intent intent = new Intent(packageContext, ReviewActivity.class);
        intent.putExtra(EXTRA_PRODUCT, product);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, Product product, Review review, int requestId) {
        Intent intent = new Intent(packageContext, ReviewActivity.class);
        intent.putExtra(EXTRA_PRODUCT, product);
        intent.putExtra(EXTRA_REVIEW, review);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private int mReviewId;
    private Product mProduct;
    private Review mReview;
    private int mRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBottomNavigationLayout.setVisibility(View.GONE);

        mReviewId = getIntent().getIntExtra(EXTRA_REVIEW_ID, -1);
        mProduct = (Product) getIntent().getSerializableExtra(EXTRA_PRODUCT);
        mReview = (Review) getIntent().getSerializableExtra(EXTRA_REVIEW);
        mRequestId = getIntent().getIntExtra(EXTRA_REQUEST_ID, -1);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);

        if (mRequestId == Common.REVIEW_CREATE_REQUEST_CODE) {
            mFragment = ReviewCreateFragment.newInstance(mProduct);
            mFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, mFragment)
                    .commit();
        } else if (mRequestId == Common.REVIEW_READ_REQUEST_CODE) {
            if (mReviewId == -1) {
//                mFragment = ReviewReadFragment.newInstance(mProduct, mReview);
//                mFragmentManager.beginTransaction()
//                        .add(R.id.main_fragment_container, mFragment)
//                        .commit();
                requestReview(mReviewId);
            } else {
//                mFragment = ReviewReadFragment.newInstance(mReviewId);
//                mFragmentManager.beginTransaction()
//                        .add(R.id.main_fragment_container, mFragment)
//                        .commit();
                requestReview(mReviewId);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setupBottomNavigation(0);
    }

    private String mReviewContent;

    @Override
    public void onReviewEdit(String reviewContent, boolean isEdit, int requestId) {
        if (isEdit) {
            mReviewContent = reviewContent;
            mFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, ReviewEditFragment.newInstance(reviewContent))
                    .add(R.id.main_fragment_container, ReviewEditFragment.newInstance(reviewContent, requestId))
                    .addToBackStack(null)
                    .commit();
        } else {
            mReviewContent = reviewContent;
//            mFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, ReviewCreateFragment.newInstance(reviewContent))

//                    .replace(R.id.fragment_container, ReviewCreateFragment.newInstance(mProduct, reviewContent))
//                    .commit();

            mFragmentManager.popBackStackImmediate();
            Fragment fragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);
            if (requestId == 1) {
                ((ReviewCreateFragment) fragment).updateContentTextView(mReviewContent);
            } else if (requestId == 2){
                ((ReviewUpdateFragment) fragment).updateContentTextView(mReviewContent);
            }
//            mFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, ReviewCreateFragment.newInstance(reviewContent))
//                    .addToBackStack(null)
//                    .commit();
        }
    }

    @Override
    public void onMenuSelected(int layoutIndex) {
        switch (layoutIndex) {
            case 4:
                mFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, ReviewUpdateFragment.newInstance(mReview))
                        .commit();
                break;
        }
    }

    public void requestReview(int reviewId) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH + File.separator + reviewId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mReview = Parser.parseReview(response);
                        mFragment = ReviewReadFragment.newInstance(mReview);
                        mFragmentManager.beginTransaction()
                                .add(R.id.main_fragment_container, mFragment)
                                .commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getApplicationContext(),
                                "리뷰를 가져오는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getApplicationContext()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);
//        MemberConfigFragment memberConfigFragment = MemberConfigFragment.newInstance();
        if (fragment instanceof ReviewEditFragment) {
//            mFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, ReviewCreateFragment.newInstance(mProduct, mReviewContent))
//                    .commit();

            mFragmentManager.popBackStackImmediate();

        } else {
            setResult(Activity.RESULT_OK, new Intent());
            super.onBackPressed();
        }
    }
}
