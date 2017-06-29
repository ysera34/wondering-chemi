package com.planet.wondering.chemi.view.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnDialogFinishedListener;
import com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment;
import com.planet.wondering.chemi.view.fragment.MemberCongratulationDialogFragment;
import com.planet.wondering.chemi.view.fragment.ProductFragment;
import com.planet.wondering.chemi.view.fragment.ProductImageDialogFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kakao.util.exception.KakaoException.ErrorType.KAKAOTALK_NOT_INSTALLED;
import static com.planet.wondering.chemi.common.Common.IS_NOW_USED_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.LOGIN_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.PRODUCT_SHARE_TEMPLATE_CODE;
import static com.planet.wondering.chemi.common.Common.PRODUCT_THUMBNAIL_WIDTH_HEIGHT_RATIO;
import static com.planet.wondering.chemi.common.Common.PROMOTE_EXTRA_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.Product.KEEP_PATH;
import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.LOGIN_DIALOG;

/**
 * Created by yoon on 2017. 3. 15..
 */

public class ProductActivity extends AppBaseActivity
        implements OnDialogFinishedListener, View.OnClickListener {

    private static final String TAG = ProductActivity.class.getSimpleName();

    private static final String EXTRA_PRODUCT_ID = "com.planet.wondering.chemi.product_id";
    private static final String EXTRA_SEARCH_TYPE = "com.planet.wondering.chemi.search_type";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ProductActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int productId) {
        Intent intent = new Intent(packageContext, ProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int productId, byte searchType) {
        Intent intent = new Intent(packageContext, ProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        intent.putExtra(EXTRA_SEARCH_TYPE, searchType);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private byte mSearchType;
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

    protected BottomNavigationView mBottomNavigationView;
    public RelativeLayout mBottomNavigationLayout;

    private int mScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);
        mSearchType = getIntent().getByteExtra(EXTRA_SEARCH_TYPE, (byte) -1);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.product_fragment_container);

//        if (mFragment == null) {
//            mFragment = ProductFragment.newInstance();
//            mFragmentManager.beginTransaction()
//                    .add(R.id.fragment_container, mFragment)
//                    .commit();
//        }

        mProductAppBarLayout = (AppBarLayout) findViewById(R.id.product_detail_app_bar_layout);
//        mProductAppBarLayout.addOnOffsetChangedListener(new OnAppBarStateChangeListener() {
//            @Override
//            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                if (state.name().equals("COLLAPSED")) {
//                    hideBottomNavigationView();
//                } else if (state.name().equals("EXPANDED")) {
//                    showBottomNavigationView();
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

//        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
//        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
//        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
//        mBottomNavigationLayout = (RelativeLayout) findViewById(R.id.bottom_navigation_layout);

        mScreenWidth = getScreenWidth();
        requestProduct();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        setupBottomNavigation(mSearchType);
//    }

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
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .override(700, 460)
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
        if (mProduct != null) {
            if (!mProduct.isArchive()) {
                getMenuInflater().inflate(R.menu.menu_toolbar_product_archive_false, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_toolbar_product_archive_true, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_product_faq:
//                Toast.makeText(getApplicationContext(), "action_faq", Toast.LENGTH_SHORT).show();
                startActivity(MemberActivity.newIntent(getApplicationContext(), 4));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.action_product_archive:
                if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
//                    Toast.makeText(getApplicationContext(), "action_archive", Toast.LENGTH_SHORT).show();
                    requestArchiveProduct(mProduct.isArchive());
                } else {
                    CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                            .newInstance(R.drawable.ic_login, R.string.login_info_message,
                                    R.string.login_button_title, LOGIN_DIALOG_REQUEST_CODE);
                    dialogFragment1.show(getSupportFragmentManager(), LOGIN_DIALOG);
                }
                break;
            case R.id.action_product_share:
                requestShareProductToKakao();
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
            default:
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (data != null) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
                break;
        }
    }

    private void requestProduct() {

//        Product product = new Product();
//        product.setName("product" + mProductId);
//        product.setBrand("brand" + mProductId);
//
//        bindProduct(product);
//        final ProgressDialog progressDialog =
//                ProgressDialog.show(getApplicationContext(), "상품의 정보를 가져오고 있습니다.",
//                        "잠시만 기다려 주세요.", false, false);

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
                        } else {
                            ((ProductFragment) mFragment).updateFragmentTitles(mProduct);
                        }
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

    private void requestArchiveProduct(final boolean isArchive) {

        int requestMethodId;
        if (!isArchive) {
            requestMethodId = Request.Method.POST;
        } else {
            requestMethodId = Request.Method.DELETE;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                requestMethodId, URL_HOST + PATH + mProductId + KEEP_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {

                            if (!isArchive) {
                                Toast.makeText(getApplicationContext(), "보관함에 보관되었어요.", Toast.LENGTH_SHORT).show();
                                mProduct.setArchive(true);
                            } else {
                                Toast.makeText(getApplicationContext(), "보관함에세 삭제되었어요.", Toast.LENGTH_SHORT).show();
                                mProduct.setArchive(false);
                            }
                            invalidateOptionsMenu();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
//                        Toast.makeText(getApplicationContext(),
//                                "상품을 보관하는 중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestShareProductToKakao() {

        String prefixTitle = "";
        if (UserSharedPreferences.getStoredUserName(getApplicationContext()) != null) {
            prefixTitle = getString(R.string.share_user_name_format,
                    UserSharedPreferences.getStoredUserName(getApplicationContext())) + " ";
        }

        Map<String, String> templateArgs = new HashMap<>();
        templateArgs.put("${imagePath}", mProduct.getImagePath());
        templateArgs.put("${title}", prefixTitle + getString(R.string.share_product_title_format,
                mProduct.getBrand(), mProduct.getName()));
        templateArgs.put("${description}", getString(R.string.share_product_description_format,
                mProduct.getBrand(), mProduct.getName()));
        templateArgs.put("${product_id}", String.valueOf(mProduct.getId()));

        KakaoLinkService.getInstance().sendCustom(this, PRODUCT_SHARE_TEMPLATE_CODE,
                templateArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
//                Logger.e(errorResult.toString());
//                Toast.makeText(getApplicationContext(), errorResult.toString(), Toast.LENGTH_LONG).show();

                if (errorResult.getException().toString().split(":")[0].equals(KAKAOTALK_NOT_INSTALLED.toString())) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductActivity.this);
                    builder1.setMessage(getString(com.kakao.kakaolink.R.string.com_kakao_alert_install_kakaotalk));
                    builder1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(KakaoTalkLinkProtocol.TALK_MARKET_URL_PREFIX)));
                        }
                    });
                    builder1.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog1 = builder1.create();
                    dialog1.show();
                }
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });

    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                startActivity(HomeActivity.newIntent(getApplicationContext()));
//                break;
////            case R.id.action_category:
////                startActivity(CategoryActivity.newIntent(getApplicationContext()));
////                break;
//            case R.id.action_content:
//                startActivity(ContentListActivity.newIntent(getApplicationContext()));
//                break;
//            case R.id.action_dictionary:
//                startActivity(DictionaryActivity.newIntent(getApplicationContext()));
//                break;
//            case R.id.action_member:
//                startActivity(MemberActivity.newIntent(getApplicationContext()));
//                break;
//        }
//        return true;
//    }

//    public void setupBottomNavigation(int menuIndex) {
//        mBottomNavigationView.getMenu().getItem(menuIndex).setChecked(true);
//        mBottomNavigationView.getMenu().getItem(menuIndex).setEnabled(false);
//    }
//
//    public void showBottomNavigationView() {
//        mBottomNavigationLayout.animate().translationY(0)
//                .setInterpolator(new DecelerateInterpolator(2));
//    }
//
//    public void hideBottomNavigationView() {
//        mBottomNavigationLayout.animate().translationY(mBottomNavigationLayout.getHeight())
//                .setInterpolator(new AccelerateInterpolator(2));
//    }

    @Override
    public void onBackPressed() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(10);
        ActivityManager.RunningTaskInfo taskInfo = runningTaskInfos.get(0);
        if (taskInfo.numActivities == 1) {
            startActivity(HomeActivity.newIntent(getApplicationContext()));
        }
        super.onBackPressed();
    }
}
