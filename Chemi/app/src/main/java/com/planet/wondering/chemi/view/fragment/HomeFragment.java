package com.planet.wondering.chemi.view.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeTransform;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.home.RecommendProduct;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;

import java.util.ArrayList;

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
    private LinearLayout mHomeCategoryLayout;
    private TabLayout mHomeCategoryTabLayout;
    private LinearLayout mHomeRecommendProductLayout;
    private RecyclerView mHomeRecommendProductRecyclerView;
    private RecommendProductAdapter mRecommendProductAdapter;
    private ArrayList<RecommendProduct> mRecommendProducts;
    private int mScreenWidth;

    private RelativeLayout mSearchLayout;
    private Button mSearchButton;
    private ImageButton mSearchImageButton;

    private LayoutInflater mLayoutInflater;
    private RelativeLayout mAddSearchLayout;
    private LinearLayout mAddCategoryLayout;
    private boolean isAddedSearchLayout;
    private boolean isAddedCategoryLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScreenWidth = ((BottomNavigationActivity) getActivity()).getScreenWidth();
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
        mHomeCategoryLayout = (LinearLayout) view.findViewById(R.id.home_category_layout);
        mHomeCategoryTabLayout = (TabLayout) view.findViewById(R.id.home_category_tab_layout);
        mHomeRecommendProductLayout = (LinearLayout) view.findViewById(R.id.home_recommend_product_layout);
        mHomeRecommendProductRecyclerView = (RecyclerView) view.findViewById(R.id.home_recommend_product_recycler_view);
        mHomeRecommendProductRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHomeRecommendProductRecyclerView.setNestedScrollingEnabled(false);

        mSearchLayout = (RelativeLayout) view.findViewById(R.id.search_layout);
        mSearchImageButton = (ImageButton) view.findViewById(R.id.search_image_button);
        mSearchImageButton.setOnClickListener(this);
        mSearchButton = (Button) view.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments().getBoolean(ARG_IS_FIRST_USER, false)) {
            MemberCongratulationDialogFragment dialogFragment = MemberCongratulationDialogFragment.newInstance();
            dialogFragment.show(getFragmentManager(), "congratulation_dialog");
        }
        setupCategoryTabIcons();
        mAddSearchLayout = (RelativeLayout) mLayoutInflater.inflate(R.layout.layout_home_add_search_view, mHomeHeaderLayout, false);
        mAddCategoryLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.layout_home_add_category, mHomeHeaderLayout, false);

        updateUI();
        requestHome();
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
            case R.id.search_image_button:
            case R.id.search_button:
                SearchDetailFragment detailFragment = SearchDetailFragment.newInstance();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    detailFragment.setSharedElementEnterTransition(new ChangeTransform());
                    detailFragment.setEnterTransition(new ChangeTransform());
                    detailFragment.setExitTransition(new ChangeTransform());
                    detailFragment.setSharedElementReturnTransition(new ChangeTransform());
                }

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addSharedElement(mSearchLayout, getString(R.string.search_view))
                        .addSharedElement(mSearchButton, getString(R.string.search_edit_text))
                        .addSharedElement(mSearchImageButton, getString(R.string.search_image_button))
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                        .replace(R.id.main_fragment_container, detailFragment)
                        .add(R.id.main_fragment_container, detailFragment)
                        .addToBackStack("search_fragment")
                        .commit();
                break;
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        int currentYOffset = v.getScrollY();
        float searchLayoutPositionY = mSearchLayout.getY();

        int headerLayoutMeasuredHeight1;
        if (!isAddedSearchLayout) {
            headerLayoutMeasuredHeight1 = mHomeHeaderLayout.getMeasuredHeight();
        } else {
            headerLayoutMeasuredHeight1 = mHomeHeaderLayout.getMeasuredHeight() - mAddSearchLayout.getMeasuredHeight();
        }

        if (currentYOffset >= Math.abs(headerLayoutMeasuredHeight1 - (int) searchLayoutPositionY)) {
            if (!isAddedSearchLayout) {
                mHomeHeaderLayout.addView(mAddSearchLayout);
//                mSearchLayout.removeView();
                mSearchLayout.setVisibility(View.INVISIBLE);
                isAddedSearchLayout = true;
            } else {
                mHomeHeaderLayout.animate().translationY(-mHomeToolbarLayout.getMeasuredHeight())
                        .setInterpolator(new AccelerateInterpolator(2));
            }
        }

        if (currentYOffset < Math.abs(headerLayoutMeasuredHeight1 - (int) searchLayoutPositionY)
                || currentYOffset == 0) {
            if (isAddedSearchLayout) {
                mHomeHeaderLayout.removeView(mAddSearchLayout);
                mSearchLayout.setVisibility(View.VISIBLE);
                isAddedSearchLayout = false;
            } else {
                mHomeHeaderLayout.animate().translationY(0)
                        .setInterpolator(new AccelerateInterpolator(2));
            }
        }

        float categoryLayoutPositionY = mHomeCategoryLayout.getY();
        int headerLayoutMeasuredHeight2 = 0;

        if (isAddedSearchLayout) {
            if (!isAddedCategoryLayout) {
                headerLayoutMeasuredHeight2 = mHomeHeaderLayout.getMeasuredHeight()
                        - mHomeToolbarLayout.getMeasuredHeight();
            } else {
                headerLayoutMeasuredHeight2 = mHomeHeaderLayout.getMeasuredHeight()
                        - mHomeToolbarLayout.getMeasuredHeight() - mAddCategoryLayout.getMeasuredHeight();
            }
        }

        if (currentYOffset >= Math.abs(headerLayoutMeasuredHeight2 - (int) categoryLayoutPositionY)) {
            if (!isAddedCategoryLayout) {
                mHomeHeaderLayout.addView(mAddCategoryLayout);
                isAddedCategoryLayout = true;
            }
        }

        if (currentYOffset < Math.abs(headerLayoutMeasuredHeight2 - (int) categoryLayoutPositionY)) {
            if (isAddedCategoryLayout) {
                mHomeHeaderLayout.removeView(mAddCategoryLayout);
                isAddedCategoryLayout = false;
            }
        }
    }

    private void requestHome() {
        for (int i = 0; i < 4; i++) {
            RecommendProduct recommendProduct = new RecommendProduct();
            recommendProduct.setImagePath("https://s3.ap-northeast-2.amazonaws.com/chemistaticfiles02/images/products/312.jpg");
            recommendProduct.setBrand("더퓨어");
            recommendProduct.setName("아임키즈 2in1샴푸");
            recommendProduct.setDescription("우리아이 스스로 씻는 습관, 시원한 멘톨\n" +
                    "성분을 함유한 쿨링 샴푸와 함께 하세요.");
            mRecommendProducts.add(recommendProduct);
        }
        updateUI();
    }

    private void setupCategoryTabIcons() {

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
            Toast.makeText(getActivity(), mRecommendProduct.getName(), Toast.LENGTH_SHORT).show();
        }
    }

}
