package com.planet.wondering.chemi.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Config;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.view.activity.ReviewActivity;
import com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment;
import com.planet.wondering.chemi.view.custom.SwipeableViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.common.Common.LOGIN_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.PROMOTE_EXTRA_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.REVIEW_CREATE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.REVIEW_READ_REQUEST_CODE;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.NetworkConfig.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.EXTRA_DIALOG;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.LOGIN_DIALOG;

/**
 * Created by yoon on 2017. 1. 18..
 */
public class ProductFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ProductFragment.class.getSimpleName();

    private static final String ARG_PRODUCT = "product";
    private static final String ARG_PRODUCT_ID = "product_id";

    public static ProductFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductFragment newInstance(int productId) {

        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);

        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductFragment newInstance(Product product) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);

        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mProductId;
    private Product mProduct;

    private TabLayout mProductDetailTabLayout;
//    private ViewPager mProductDetailViewPager;
    private SwipeableViewPager mProductDetailViewPager;
    private ArrayList<Fragment> mProductDetailListFragments;
    private ArrayList<String> mProductDetailListFragmentTitles;

    private ProductFragmentPagerAdapter mFragmentPagerAdapter;

    private LinearLayout mReviewCreateLayout;
    private TextView mReviewCreateButtonTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getArguments().getInt(ARG_PRODUCT_ID, 0);
        mProduct = (Product) getArguments().getSerializable(ARG_PRODUCT);

        mProductDetailListFragments = new ArrayList<>();
        mProductDetailListFragmentTitles = new ArrayList<>();

        addProductDetailFragment(ReviewListFragment.newInstance(mProduct),
                getString(R.string.product_detail_tab_title2));
        addProductDetailFragment(ChemicalListFragment.newInstance(mProduct),
                "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        mProductDetailTabLayout = (TabLayout) view.findViewById(R.id.product_detail_tab_layout);
//        mProductDetailViewPager = (ViewPager) view.findViewById(R.id.product_detail_view_pager);
        mProductDetailViewPager = (SwipeableViewPager) view.findViewById(R.id.product_detail_view_pager);
        mReviewCreateLayout = (LinearLayout) view.findViewById(R.id.review_create_layout);
        mReviewCreateButtonTextView = (TextView) view.findViewById(R.id.review_create_button_text_view);
        mReviewCreateButtonTextView.setOnClickListener(this);

        mFragmentPagerAdapter = new ProductFragmentPagerAdapter(getChildFragmentManager());
        mProductDetailViewPager.setAdapter(mFragmentPagerAdapter);
        mProductDetailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    showReviewCreateLayout();
                } else if (position == 1) {
                    hideReviewCreateLayout();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mProductDetailTabLayout.setupWithViewPager(mProductDetailViewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mProduct.getProductType() == 1) {
            mProductDetailViewPager.setSwipeable(true);
            if (mProduct.isWholeChemicals()) {
                mFragmentPagerAdapter.setFragmentTitles(1, "전성분 목록");
            } else {
                mFragmentPagerAdapter.setFragmentTitles(1, "주성분 목록");
            }
            mFragmentPagerAdapter.notifyDataSetChanged();
        } else if (mProduct.getProductType() == 2) {
            LinearLayout chemicalTabLayout = ((LinearLayout) mProductDetailTabLayout.getChildAt(0));
            chemicalTabLayout.getChildAt(1).setEnabled(false);
            mProductDetailTabLayout.setSelectedTabIndicatorHeight(0);
//            mProductDetailTabLayout.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
//            mFragmentPagerAdapter.setFragmentTitles(1, "");
//            mFragmentPagerAdapter.notifyDataSetChanged();
        }
    }

    private void addProductDetailFragment(Fragment fragment, String title) {
        mProductDetailListFragments.add(fragment);
        mProductDetailListFragmentTitles.add(title);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PROMOTE_EXTRA_DIALOG_REQUEST_CODE:
//                Log.i(TAG, "PROMOTE_EXTRA_DIALOG_REQUEST_CODE : " + PROMOTE_EXTRA_DIALOG_REQUEST_CODE);
                requestMemberConfig(true);
                break;
            case REVIEW_READ_REQUEST_CODE:
//                Log.i(TAG, "REVIEW_READ_REQUEST_CODE : " + REVIEW_READ_REQUEST_CODE);
                for (Fragment fragment : getChildFragmentManager().getFragments()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case REVIEW_CREATE_REQUEST_CODE:
//                Log.i(TAG, "REVIEW_CREATE_REQUEST_CODE : " + REVIEW_CREATE_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_create_button_text_view:
                if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
                    requestMemberConfig(false);
                } else {
                    CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                            .newInstance(R.drawable.ic_login, R.string.login_info_message,
                                    R.string.login_button_title, LOGIN_DIALOG_REQUEST_CODE);
                    dialogFragment1.show(getChildFragmentManager(), LOGIN_DIALOG);
                }
                break;
        }
    }

    public void updateFragmentTitles(Product product) {
        mFragmentPagerAdapter.setFragmentTitles(1, getString(R.string.product_detail_tab_title2) +
                getString(R.string.product_detail_tab_title2_description, String.valueOf(product.getRatingCount())));
        mFragmentPagerAdapter.notifyDataSetChanged();
    }

    private class ProductFragmentPagerAdapter extends FragmentPagerAdapter {

        public ProductFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mProductDetailListFragments.get(position);
        }

        @Override
        public int getCount() {
            return mProductDetailListFragmentTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mProductDetailListFragmentTitles.get(position);
        }

        public void setFragmentTitles(int position, String title) {
            mProductDetailListFragmentTitles.set(position, title);
        }
    }

    public void showReviewCreateLayout() {
        mReviewCreateLayout.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(2));
        mProductDetailViewPager.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mProductDetailViewPager.getLayoutParams();
        params.height = 0;
        params.weight = 1;
        mProductDetailViewPager.setLayoutParams(params);
    }

    public void hideReviewCreateLayout() {

        mReviewCreateLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mReviewCreateLayout.animate().translationY(mReviewCreateLayout.getHeight())
                        .setInterpolator(new AccelerateInterpolator(2));
            }
        }, 200);

        mProductDetailViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProductDetailViewPager.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
        }, 500);
    }

    private void requestMemberConfig(final boolean isRetry) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + Config.User.PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        User user = Parser.parseMemberConfigUser(response);
                        if (!isRetry) {
                            if (user.isHasExtraInfo()) {
                                getActivity().startActivityForResult(ReviewActivity.newIntent(
                                        getActivity(), mProduct, REVIEW_CREATE_REQUEST_CODE),
                                        REVIEW_CREATE_REQUEST_CODE);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                                        .newInstance(R.drawable.ic_login, R.string.promote_extra_info_message,
                                                R.string.promote_extra_info_title, PROMOTE_EXTRA_DIALOG_REQUEST_CODE);
                                dialogFragment1.show(getChildFragmentManager(), EXTRA_DIALOG);
                            }
                        } else {
                            if (user.isHasExtraInfo()) {
                                getActivity().startActivityForResult(ReviewActivity.newIntent(
                                        getActivity(), mProduct, REVIEW_CREATE_REQUEST_CODE),
                                        REVIEW_CREATE_REQUEST_CODE);
                                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.promote_extra_info_message),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
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
}
