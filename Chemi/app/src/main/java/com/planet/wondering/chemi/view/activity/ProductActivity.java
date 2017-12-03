package com.planet.wondering.chemi.view.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnDialogFinishedListener;
import com.planet.wondering.chemi.view.fragment.MemberCongratulationDialogFragment;
import com.planet.wondering.chemi.view.fragment.ProductFragment;
import com.planet.wondering.chemi.view.fragment.ProductImageDialogFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.planet.wondering.chemi.common.Common.IS_NOW_USED_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.LOGIN_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.PRODUCT_THUMBNAIL_WIDTH_HEIGHT_RATIO;
import static com.planet.wondering.chemi.common.Common.PROMOTE_EXTRA_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.REVIEW_CREATE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.REVIEW_READ_REQUEST_CODE;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.NetworkConfig.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;

/**
 * Created by yoon on 2017. 3. 15..
 */

public class ProductActivity extends AppBaseActivity
        implements OnDialogFinishedListener, View.OnClickListener {

    private static final String TAG = ProductActivity.class.getSimpleName();

    private static final String EXTRA_PRODUCT_ID = "com.planet.wondering.chemi.product_id";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ProductActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int productId) {
        Intent intent = new Intent(packageContext, ProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private int mProductId;
    private Product mProduct;

    private AppBarLayout mProductAppBarLayout;
    private CollapsingToolbarLayout mProductCollapsingToolbarLayout;
    private Toolbar mProductToolbar;

    private TextView mProductDetailToolbarTitleTextView;
    private TextView mProductDetailToolbarSubTitleTextView;

    private ImageView mProductDetailImageView;
    private RatingBar mProductDetailReviewRatingBar;
    private TextView mProductDetailReviewRatingValueTextView;
    private TextView mProductDetailReviewRatingCountTextView;

    private int mScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            mProductId = Integer.valueOf(uri.getQueryParameter("product_id"));
        } else {
            mProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);
        }

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.product_fragment_container);

        mProductAppBarLayout = (AppBarLayout) findViewById(R.id.product_detail_app_bar_layout);
//        mProductAppBarLayout.addOnOffsetChangedListener(new OnAppBarStateChangeListener() {
//            @Override
//            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                if (state.name().equals("COLLAPSED")) {
//                    showReviewCreateLayout();
//                } else if (state.name().equals("EXPANDED")) {
//                    hideReviewCreateLayout();
//                }
//            }
//        });
        mProductCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.product_detail_collapsing_toolbar_layout);

        mProductToolbar = (Toolbar) findViewById(R.id.product_detail_toolbar);
        setSupportActionBar(mProductToolbar);
        mProductToolbar.setTitle(null);

        mProductDetailToolbarTitleTextView = (TextView) findViewById(R.id.product_detail_toolbar_title_text_view);
        mProductDetailToolbarSubTitleTextView = (TextView) findViewById(R.id.product_detail_toolbar_sub_title_text_view);

        mProductDetailImageView = (ImageView) findViewById(R.id.product_detail_image_view);
        mProductDetailImageView.setOnClickListener(this);
        mProductDetailReviewRatingBar = (RatingBar) findViewById(R.id.product_detail_review_rating_bar);
        mProductDetailReviewRatingValueTextView =
                (TextView) findViewById(R.id.product_detail_review_rating_value_text_view);
        mProductDetailReviewRatingCountTextView =
                (TextView) findViewById(R.id.product_detail_review_rating_count_text_view);

        mScreenWidth = getScreenWidth();
        requestProduct();
    }

    private void bindProduct(Product product) {

        mProductDetailToolbarTitleTextView.setText(String.valueOf(mProduct.getBrand()));
        mProductDetailToolbarSubTitleTextView.setText(String.valueOf(mProduct.getName()));

        mProductDetailToolbarTitleTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProductDetailToolbarTitleTextView.setSelected(true);
            }
        }, 1500);
        mProductDetailToolbarSubTitleTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProductDetailToolbarSubTitleTextView.setSelected(true);
            }
        }, 1500);

        int thumbnailWidth = mScreenWidth;
        int thumbnailHeight = (int) (thumbnailWidth * PRODUCT_THUMBNAIL_WIDTH_HEIGHT_RATIO);

        Glide.with(getApplicationContext())
                .load(mProduct.getImagePath())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .override(thumbnailWidth, thumbnailHeight)
                .crossFade()
                .fitCenter()
                .into(mProductDetailImageView);
        mProductDetailReviewRatingBar.setRating(product.getRatingValue());
        mProductDetailReviewRatingValueTextView.setText(String.valueOf(product.getRatingValue()));
        mProductDetailReviewRatingCountTextView.setText(
                getString(R.string.product_review_count, String.valueOf(product.getRatingCount())));
        invalidateOptionsMenu();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_detail_image_view:
                ProductImageDialogFragment dialogFragment =
                        ProductImageDialogFragment.newInstance(mProduct.getImagePath());
                dialogFragment.show(getSupportFragmentManager(), "product_image_dialog_fragment");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(HomeActivity.newIntent(getApplicationContext()));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogFinished(boolean isChose, int requestCode) {
        if (isChose) {
            if (requestCode == LOGIN_DIALOG_REQUEST_CODE) {
                startActivityForResult(MemberStartActivity.newIntent(getApplicationContext(),
                        IS_NOW_USED_USER_REQUEST_CODE), IS_NOW_USED_USER_REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (requestCode == PROMOTE_EXTRA_DIALOG_REQUEST_CODE) {
                startActivityForResult(MemberActivity.newIntent(getApplicationContext(), 5),
                        PROMOTE_EXTRA_DIALOG_REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IS_NOW_USED_USER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data.getBooleanExtra("is_now_used_user", false)) {
                        MemberCongratulationDialogFragment dialogFragment =
                                MemberCongratulationDialogFragment.newInstance();
                        dialogFragment.show(getSupportFragmentManager(), "congratulation_dialog");
                    }
                }
                break;
            case PROMOTE_EXTRA_DIALOG_REQUEST_CODE:
//                Log.i(TAG, "PROMOTE_EXTRA_DIALOG_REQUEST_CODE : " + PROMOTE_EXTRA_DIALOG_REQUEST_CODE);
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case REVIEW_READ_REQUEST_CODE:
//                Log.i(TAG, "REVIEW_READ_REQUEST_CODE : " + REVIEW_READ_REQUEST_CODE);
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case REVIEW_CREATE_REQUEST_CODE:
//                Log.i(TAG, "REVIEW_CREATE_REQUEST_CODE : " + REVIEW_CREATE_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    private void requestProduct() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH + mProductId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProduct = Parser.parseProduct(response);
                        bindProduct(mProduct);
//                        progressDialog.dismiss();

                        if (mFragment == null) {
                            mFragment = ProductFragment.newInstance(mProduct);
                            mFragmentManager.beginTransaction()
                                    .add(R.id.product_fragment_container, mFragment)
                                    .commit();
                        }
//                        else {
//                            ((ProductFragment) mFragment).updateFragmentTitles(mProduct);
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        Log.e(TAG, error.toString());
                        Toast.makeText(getApplicationContext(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
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
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    @Override
    public void onBackPressed() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(10);
        ActivityManager.RunningTaskInfo taskInfo = runningTaskInfos.get(0);
        if (taskInfo.numActivities == 1) {
            startActivity(HomeActivity.newIntent(getApplicationContext()));
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
