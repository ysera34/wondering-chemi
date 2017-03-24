package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.AppBaseActivity;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.BottomNavigationViewHelper;
import com.planet.wondering.chemi.util.listener.OnAppBarStateChangeListener;
import com.planet.wondering.chemi.view.fragment.ProductFragment;

import org.json.JSONObject;

import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 3. 15..
 */

public class ProductActivity extends AppBaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

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
    private ImageView mProductDetailImageView;
    private RatingBar mProductDetailReviewRatingBar;
    private TextView mProductDetailReviewRatingValueTextView;
    private TextView mProductDetailReviewRatingCountTextView;

    protected BottomNavigationView mBottomNavigationView;
    public RelativeLayout mBottomNavigationLayout;

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
        mProductAppBarLayout.addOnOffsetChangedListener(new OnAppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state.name().equals("COLLAPSED")) {
                    hideBottomNavigationView();
                } else if (state.name().equals("EXPANDED")) {
                    showBottomNavigationView();
                }
            }
        });
        mProductCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.product_detail_collapsing_toolbar_layout);

        mProductToolbar = (Toolbar) findViewById(R.id.product_detail_toolbar);
        setSupportActionBar(mProductToolbar);

        mProductDetailImageView = (ImageView) findViewById(R.id.product_detail_image_view);
        mProductDetailReviewRatingBar = (RatingBar) findViewById(R.id.product_detail_review_rating_bar);
        mProductDetailReviewRatingValueTextView =
                (TextView) findViewById(R.id.product_detail_review_rating_value_text_view);
        mProductDetailReviewRatingCountTextView =
                (TextView) findViewById(R.id.product_detail_review_rating_count_text_view);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mBottomNavigationLayout = (RelativeLayout) findViewById(R.id.bottom_navigation_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNavigation(mSearchType);
        requestProduct();
    }

    private void bindProduct(Product product) {

        setTitle(mProduct.getName());
        mProductToolbar.setSubtitle(mProduct.getBrand());

        Glide.with(getApplicationContext())
                .load(mProduct.getImagePath())
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                .crossFade()
//                .override(700, 460)
                .into(mProductDetailImageView);
        mProductDetailReviewRatingBar.setRating(product.getRatingValue());
        mProductDetailReviewRatingValueTextView.setText(String.valueOf(product.getRatingValue()));
        mProductDetailReviewRatingCountTextView.setText(
                getString(R.string.product_review_count, String.valueOf(product.getRatingCount())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_faq:
                Toast.makeText(getApplicationContext(), "action_faq", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_archive:
                Toast.makeText(getApplicationContext(), "action_archive", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Toast.makeText(getApplicationContext(), "action_share", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        Log.e(TAG, error.getMessage());
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(SearchActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_category:
                startActivity(CategoryActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_content:
                startActivity(ContentActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_dictionary:
                startActivity(DictionaryActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_member:
                startActivity(MemberActivity.newIntent(getApplicationContext()));
                break;
        }
        return true;
    }

    public void setupBottomNavigation(int menuIndex) {
        mBottomNavigationView.getMenu().getItem(menuIndex).setChecked(true);
        mBottomNavigationView.getMenu().getItem(menuIndex).setEnabled(false);
    }

    public void showBottomNavigationView() {
        mBottomNavigationLayout.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(2));
    }

    public void hideBottomNavigationView() {
        mBottomNavigationLayout.animate().translationY(mBottomNavigationLayout.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
    }
}
