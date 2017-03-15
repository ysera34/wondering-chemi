package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.view.fragment.ProductFragment;

import org.json.JSONObject;

import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 3. 15..
 */

public class ProductActivity extends BottomNavigationActivity {

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
    private Toolbar mProductToolbar;
    private ImageView mProductDetailImageView;
    private RatingBar mProductDetailReviewRatingBar;
    private TextView mProductDetailReviewRatingValueTextView;
    private TextView mProductDetailReviewRatingCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product);

        mProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

//        if (mFragment == null) {
//            mFragment = ProductFragment.newInstance();
//            mFragmentManager.beginTransaction()
//                    .add(R.id.fragment_container, mFragment)
//                    .commit();
//        }

//        mProductAppBarLayout = (AppBarLayout) findViewById(R.id.product_detail_app_bar_layout);
//        mProductAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
//                    hideBottomNavigationView();
//                } else {
//                    showBottomNavigationView();
//                }
//            }
//        });

//        mProductToolbar = (Toolbar) findViewById(R.id.product_detail_toolbar);
//        setSupportActionBar(mProductToolbar);
//
//        mProductDetailImageView = (ImageView) findViewById(R.id.product_detail_image_view);
//        mProductDetailReviewRatingBar = (RatingBar) findViewById(R.id.product_detail_review_rating_bar);
//        mProductDetailReviewRatingValueTextView =
//                (TextView) findViewById(R.id.product_detail_review_rating_value_text_view);
//        mProductDetailReviewRatingCountTextView =
//                (TextView) findViewById(R.id.product_detail_review_rating_count_text_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestProduct();
    }

    private void bindProduct(Product product) {

        setTitle(mProduct.getName());
        mProductToolbar.setSubtitle(mProduct.getBrand());

//        mProductDetailImageView
        mProductDetailReviewRatingBar.setRating(product.getRatingValue());
        mProductDetailReviewRatingValueTextView.setText(String.valueOf(product.getRatingValue()));
        mProductDetailReviewRatingCountTextView.setText(
                getString(R.string.product_review_count, String.valueOf(product.getRatingCount())));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_toolbar_product, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_faq:
//                Toast.makeText(getApplicationContext(), "action_faq", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.action_archive:
//                Toast.makeText(getApplicationContext(), "action_archive", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.action_share:
//                Toast.makeText(getApplicationContext(), "action_share", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
//                        bindProduct(mProduct);
//                        progressDialog.dismiss();

                        if (mFragment == null) {
                            mFragment = ProductFragment.newInstance(mProduct);
                            mFragmentManager.beginTransaction()
                                    .add(R.id.fragment_container, mFragment)
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
}
