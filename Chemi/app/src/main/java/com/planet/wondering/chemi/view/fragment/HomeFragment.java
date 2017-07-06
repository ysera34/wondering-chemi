package com.planet.wondering.chemi.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.Common;
import com.planet.wondering.chemi.model.home.BestReview;
import com.planet.wondering.chemi.model.home.PromoteContent;
import com.planet.wondering.chemi.model.home.RecommendProduct;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.view.activity.AppBaseActivity;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.ContentActivity;
import com.planet.wondering.chemi.view.activity.ImageActivity;
import com.planet.wondering.chemi.view.activity.MemberStartActivity;
import com.planet.wondering.chemi.view.activity.ProductActivity;
import com.planet.wondering.chemi.view.activity.ProductListActivity;
import com.planet.wondering.chemi.view.activity.ReviewActivity;
import com.planet.wondering.chemi.view.activity.SearchActivity;
import com.planet.wondering.chemi.view.custom.RotateViewPager;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.planet.wondering.chemi.network.Config.Content.PROMOTE_CONTENT_PATH;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.Product.RECOMMEND_PATH;
import static com.planet.wondering.chemi.network.Config.Review.BEST_REVIEW_PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 6. 21..
 */

public class HomeFragment extends Fragment
        implements View.OnClickListener, NestedScrollView.OnScrollChangeListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private static final String ARG_IS_FIRST_USER = "is_first_user";

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance(boolean isFirstUser) {

        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_FIRST_USER, isFirstUser);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CoordinatorLayout mHomeCoordinatorLayout;
    private LinearLayout mHomeHeaderLayout;
    private LinearLayout mHomeToolbarLayout;
    private NestedScrollView mHomeNestedScrollView;
    private LinearLayout mHomeScrollLayout;
    private RotateViewPager mHomeContentViewPager;
    private LinearLayout mHomeCategoryLayout;
    private LinearLayout mHomeRecommendProductLayout;
    private RecyclerView mHomeRecommendProductRecyclerView;
    private RecommendProductAdapter mRecommendProductAdapter;
    private ArrayList<RecommendProduct> mRecommendProducts;
    private RotateViewPager mHomeReviewViewPager;
    private int mScreenWidth;

    private RelativeLayout mHomeSearchLayout;
    private Button mHomeSearchButton;
    private ImageButton mHomeSearchImageButton;
    private Button mHomeAddSearchButton;
    private ImageButton mHomeAddSearchImageButton;

    private LayoutInflater mLayoutInflater;
    private RelativeLayout mAddSearchLayout;
    private LinearLayout mHomeAddCategoryLayout;
    private boolean isAddedSearchLayout;
    private boolean isAddedCategoryLayout;
    private boolean isBottomNavigationVisible = true;

    private LinearLayout mPromoteSignInLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScreenWidth = ((AppBaseActivity) getActivity()).getScreenWidth();
        mLayoutInflater = LayoutInflater.from(getContext());
        isAddedSearchLayout = false;
        isAddedCategoryLayout = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mHomeCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.home_coordinator_layout);
        mHomeHeaderLayout = (LinearLayout) view.findViewById(R.id.home_header_layout);
        mHomeToolbarLayout = (LinearLayout) view.findViewById(R.id.home_toolbar_layout);
        mHomeNestedScrollView = (NestedScrollView) view.findViewById(R.id.home_scroll_view);
        mHomeNestedScrollView.setOnScrollChangeListener(this);
        mHomeScrollLayout = (LinearLayout) view.findViewById(R.id.home_scroll_layout);

        mHomeContentViewPager = (RotateViewPager) view.findViewById(R.id.home_promote_content_rotate_view_pager);
        mHomeCategoryLayout = (LinearLayout) view.findViewById(R.id.home_category_layout);
        mHomeCategoryLayout.setOnClickListener(this);
        mHomeRecommendProductLayout = (LinearLayout) view.findViewById(R.id.home_recommend_product_layout);
        mHomeRecommendProductRecyclerView = (RecyclerView) view.findViewById(R.id.home_recommend_product_recycler_view);
        mHomeRecommendProductRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHomeRecommendProductRecyclerView.setNestedScrollingEnabled(false);
        mHomeReviewViewPager = (RotateViewPager) view.findViewById(R.id.home_best_review_rotate_view_pager);

        mHomeSearchLayout = (RelativeLayout) view.findViewById(R.id.home_search_layout);
        mHomeSearchButton = (Button) view.findViewById(R.id.home_search_text_button);
        mHomeSearchButton.setOnClickListener(this);
        mHomeSearchImageButton = (ImageButton) view.findViewById(R.id.home_search_image_button);
        mHomeSearchImageButton.setOnClickListener(this);

        mPromoteSignInLayout = (LinearLayout) view.findViewById(R.id.promote_sign_in_layout);
        view.findViewById(R.id.promote_sign_in_clear_image_view).setOnClickListener(this);
        view.findViewById(R.id.promote_sign_in_text_view).setOnClickListener(this);
        view.findViewById(R.id.expert_group_layout).setOnClickListener(this);
        view.findViewById(R.id.expert_group_detail_text_view).setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments().getBoolean(ARG_IS_FIRST_USER, false)) {
            MemberCongratulationDialogFragment dialogFragment = MemberCongratulationDialogFragment.newInstance();
            dialogFragment.show(getFragmentManager(), "congratulation_dialog");
        }

        mAddSearchLayout = (RelativeLayout) mLayoutInflater.inflate(R.layout.layout_home_add_search_view, mHomeHeaderLayout, false);
        mHomeAddSearchButton = (Button) mAddSearchLayout.findViewById(R.id.home_add_search_text_button);
        mHomeAddSearchButton.setOnClickListener(this);
        mHomeAddSearchImageButton = (ImageButton) mAddSearchLayout.findViewById(R.id.home_add_search_image_button);
        mHomeAddSearchImageButton.setOnClickListener(this);
        mHomeAddCategoryLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.layout_home_add_category, mHomeHeaderLayout, false);
        mHomeAddCategoryLayout.setOnClickListener(this);

        setCategoryViewClickListener();
        updateUI();
        requestHome();

        if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
            mPromoteSignInLayout.setVisibility(View.GONE);
        }
    }

    private void setCategoryViewClickListener() {
        for (int i = 0; i < mHomeCategoryLayout.getChildCount(); i++) {
            mHomeCategoryLayout.getChildAt(i).setOnClickListener(this);
        }
        for (int i = 0; i < mHomeAddCategoryLayout.getChildCount(); i++) {
            mHomeAddCategoryLayout.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mHomeContentViewPager.addOnPageChangeListener();
        mHomeContentViewPager.startRotateViewPager();
        mHomeReviewViewPager.addOnPageChangeListener();
        mHomeReviewViewPager.startRotateViewPager();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHomeContentViewPager.removeOnPageChangeListener();
        mHomeContentViewPager.stopRotateViewPager();
        mHomeReviewViewPager.removeOnPageChangeListener();
        mHomeReviewViewPager.stopRotateViewPager();
    }

    private void updateUI() {
        if (mRecommendProductAdapter == null) {
            mRecommendProducts = new ArrayList<>();
            mRecommendProductAdapter = new RecommendProductAdapter(mRecommendProducts);
            mHomeRecommendProductRecyclerView.setAdapter(mRecommendProductAdapter);
        } else {
            mRecommendProductAdapter.setRecommendProducts(mRecommendProducts);
            mRecommendProductAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search_text_button:
            case R.id.home_add_search_text_button:
                startActivity(SearchActivity.newIntent(getActivity()));
                break;
            case R.id.home_search_image_button:
            case R.id.home_add_search_image_button:
                startActivity(ProductListActivity.newIntent(getActivity(), mHomeSearchButton.getText().toString()));
                break;
            case R.id.home_category_layout:
            case R.id.home_add_category_layout:
                break;
            case R.id.category_group_view_1:
            case R.id.add_category_group_view_1:
                startActivity(SearchActivity.newIntent(getActivity(), 0));
                break;
            case R.id.category_group_view_2:
            case R.id.add_category_group_view_2:
                startActivity(SearchActivity.newIntent(getActivity(), 1));
                break;
            case R.id.category_group_view_3:
            case R.id.add_category_group_view_3:
                startActivity(SearchActivity.newIntent(getActivity(), 2));
                break;
            case R.id.category_group_view_4:
            case R.id.add_category_group_view_4:
                startActivity(SearchActivity.newIntent(getActivity(), 3));
                break;

            case R.id.promote_sign_in_clear_image_view:
                mPromoteSignInLayout.setVisibility(View.GONE);
                break;
            case R.id.promote_sign_in_text_view:
                startActivity(MemberStartActivity.newIntent(getActivity()));
                break;
            case R.id.expert_group_layout:
            case R.id.expert_group_detail_text_view:
                startActivity(ImageActivity.newIntent(getActivity(),
                        "케미전문가단", "https://s3.ap-northeast-2.amazonaws.com/chemistaticfiles02/images/others/expert_group_image.jpg"));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        float searchLayoutPositionY = mHomeToolbarLayout.getMeasuredHeight() + mHomeSearchLayout.getY();
        if (scrollY >= searchLayoutPositionY) {
            if (!isAddedSearchLayout) {
                mHomeHeaderLayout.addView(mAddSearchLayout);
                mHomeSearchLayout.setVisibility(View.INVISIBLE);
                isAddedSearchLayout = true;
            }
        } else {
            if (isAddedSearchLayout) {
                mHomeHeaderLayout.removeView(mAddSearchLayout);
                mHomeSearchLayout.setVisibility(View.VISIBLE);
                isAddedSearchLayout = false;
            }
        }

        float categoryLayoutPositionY = mHomeCategoryLayout.getY();
        if (scrollY >= categoryLayoutPositionY - mHomeSearchLayout.getMeasuredHeight()) {
            if (!isAddedCategoryLayout) {
                mHomeHeaderLayout.addView(mHomeAddCategoryLayout);
                isAddedCategoryLayout = true;
            }
        } else {
            if (isAddedCategoryLayout) {
                mHomeHeaderLayout.removeView(mHomeAddCategoryLayout);
                isAddedCategoryLayout = false;
            }
        }

        /* Bottom Navigation Show Hide */
//        Log.i(TAG, "scrollY - oldScrollY: " + (scrollY - oldScrollY));
        int currentOffsetY = scrollY - oldScrollY;

        // up : + ; down : -
        if (currentOffsetY > 70) {
            if (isBottomNavigationVisible) {
                ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
                isBottomNavigationVisible = false;
            }
        }
        if (currentOffsetY < -70) {
            if (!isBottomNavigationVisible) {
                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
                isBottomNavigationVisible = true;
            }
        }

        if (v.getScrollY() == 0) {
//            Log.i(TAG, "NestedScrollView: Top has been reached" );
            if (!isBottomNavigationVisible) {
                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
                isBottomNavigationVisible = true;
            }
        }
        if (mHomeScrollLayout.getMeasuredHeight() == v.getScrollY() + v.getHeight()) {
//            Log.i(TAG, "NestedScrollView: Bottom has been reached" );
            if (!isBottomNavigationVisible) {
                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
                isBottomNavigationVisible = true;
            }
        }
    }

    private void requestHome() {
        requestPromoteContents();
        requestRecommendProducts();
        requestBestReviews();
    }

    private class RecommendProductAdapter extends RecyclerView.Adapter<RecommendProductHolder>{

        private ArrayList<RecommendProduct> mRecommendProducts;

        public RecommendProductAdapter(ArrayList<RecommendProduct> recommendProducts) {
            mRecommendProducts = recommendProducts;
        }

        public void setRecommendProducts(ArrayList<RecommendProduct> recommendProducts) {
            mRecommendProducts = recommendProducts;
        }

        @Override
        public RecommendProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_product_recommend, parent, false);
            return new RecommendProductHolder(view);
        }

        @Override
        public void onBindViewHolder(RecommendProductHolder holder, int position) {
            holder.bindRecommendProduct(mRecommendProducts.get(position));
        }

        @Override
        public int getItemCount() {
            return mRecommendProducts.size();
        }
    }

    private class RecommendProductHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private RecommendProduct mRecommendProduct;

        private ImageView mRecommendProductImageView;
        private TextView mRecommendProductBrandTextView;
        private TextView mRecommendProductNameTextView;
        private TextView mRecommendDescriptionTextView;

        public RecommendProductHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mRecommendProductImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_recommend_product_image_view);
            mRecommendProductBrandTextView = (TextView)
                    itemView.findViewById(R.id.list_item_recommend_product_brand_text_view);
            mRecommendProductNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_recommend_product_name_text_view);
            mRecommendDescriptionTextView = (TextView)
                    itemView.findViewById(R.id.list_item_recommend_description);
        }

        public void bindRecommendProduct(RecommendProduct recommendProduct) {
            mRecommendProduct = recommendProduct;

            int thumbnailWidth = (int) (mScreenWidth * 0.32f);
            int thumbnailHeight = (int) (thumbnailWidth * 0.74f);

            Glide.with(getActivity())
                    .load(mRecommendProduct.getImagePath())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .override(thumbnailWidth, thumbnailHeight)
                    .centerCrop()
                    .crossFade()
                    .into(mRecommendProductImageView);
            mRecommendProductBrandTextView.setText(String.valueOf(mRecommendProduct.getBrand()));
            mRecommendProductNameTextView.setText(String.valueOf(mRecommendProduct.getName()));
            mRecommendDescriptionTextView.setText(String.valueOf(mRecommendProduct.getDescription()));
        }

        @Override
        public void onClick(View v) {
            startActivity(ProductActivity.newIntent(getActivity(), mRecommendProduct.getId()));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void requestPromoteContents() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PROMOTE_CONTENT_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<PromoteContent> promoteContents = Parser.parsePromoteContents(response);
                        mHomeContentViewPager.setRotateViewPagerAdapter(promoteContents);
                        mHomeContentViewPager.setItemClickListener(new RotateViewPager.OnItemClickListener() {
                            @Override
                            public void onItemClick(int itemId) {
                                startActivity(ContentActivity.newIntent(getActivity(), itemId));
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestRecommendProducts() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + RECOMMEND_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mRecommendProducts = Parser.parseRecommendProducts(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestBestReviews() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + BEST_REVIEW_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<BestReview> bestReviews = Parser.parseBestReviews(response);
                        mHomeReviewViewPager.setRotateViewPagerAdapter(bestReviews);
                        mHomeReviewViewPager.setItemClickListener(new RotateViewPager.OnItemClickListener() {
                            @Override
                            public void onItemClick(int itemId) {
                                startActivity(ReviewActivity.newIntent(
                                        getActivity(), itemId, Common.REVIEW_READ_REQUEST_CODE));
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }
}
