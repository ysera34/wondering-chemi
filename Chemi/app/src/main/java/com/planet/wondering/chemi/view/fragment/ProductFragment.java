package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.storage.ProductStorage;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.ProductPagerActivity;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 18..
 */
public class ProductFragment extends Fragment {

    private static final String TAG = ProductFragment.class.getSimpleName();

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

    private int mProductId;
    private Product mProduct;

    private AppBarLayout mProductAppBarLayout;
    private Toolbar mProductToolbar;
    private ImageView mProductDetailImageView;
    private RatingBar mProductDetailReviewRatingBar;
    private TextView mProductDetailReviewRatingValueTextView;
    private TextView mProductDetailReviewRatingCountTextView;

    private TabLayout mProductDetailTabLayout;
    private ViewPager mProductDetailViewPager;
    private ArrayList<Fragment> mProductDetailListFragments;
    private ArrayList<String> mProductDetailListFragmentTitles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProductId = getArguments().getInt(ARG_PRODUCT_ID, 0);
        mProduct = ProductStorage.getStorage(getActivity()).getProduct(mProductId);

        mProductDetailListFragments = new ArrayList<>();
        mProductDetailListFragmentTitles = new ArrayList<>();

        addProductDetailFragment(ChemicalListFragment.newInstance(mProduct.getId()),
                getString(R.string.product_detail_tab_title1));
        addProductDetailFragment(ReviewListFragment.newInstance(mProduct.getId()),
                getString(R.string.product_detail_tab_title2));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        mProductAppBarLayout = (AppBarLayout) view.findViewById(R.id.product_detail_app_bar_layout);
        mProductAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                    ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
                } else {
                    ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
                }
            }
        });
        mProductToolbar = (Toolbar) view.findViewById(R.id.product_detail_toolbar);
        ((ProductPagerActivity) getActivity()).setSupportActionBar(mProductToolbar);

        mProductDetailImageView = (ImageView) view.findViewById(R.id.product_detail_image_view);
        mProductDetailReviewRatingBar = (RatingBar) view.findViewById(R.id.product_detail_review_rating_bar);
        mProductDetailReviewRatingValueTextView =
                (TextView) view.findViewById(R.id.product_detail_review_rating_value_text_view);
        mProductDetailReviewRatingCountTextView =
                (TextView) view.findViewById(R.id.product_detail_review_rating_count_text_view);

        mProductDetailTabLayout = (TabLayout) view.findViewById(R.id.product_detail_tab_layout);
        mProductDetailViewPager = (ViewPager) view.findViewById(R.id.product_detail_view_pager);

        FragmentManager fm = getChildFragmentManager();
        mProductDetailViewPager.setAdapter(new FragmentPagerAdapter(fm) {
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
        });
        mProductDetailTabLayout.setupWithViewPager(mProductDetailViewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void addProductDetailFragment(Fragment fragment, String title) {
        mProductDetailListFragments.add(fragment);
        mProductDetailListFragmentTitles.add(title);
    }

    @Override
    public void onResume() {
        super.onResume();

        bindProduct(mProduct);
    }

    private void bindProduct(Product product) {

        mProductToolbar.setTitle(mProduct.getName());
        mProductToolbar.setSubtitle(mProduct.getBrand());

//        mProductDetailImageView
        mProductDetailReviewRatingBar.setRating(product.getRatingValue());
        mProductDetailReviewRatingValueTextView.setText(String.valueOf(product.getRatingValue()));
        mProductDetailReviewRatingCountTextView.setText(
                getString(R.string.product_review_count, String.valueOf(product.getRatingCount())));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_product, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_faq:
                Toast.makeText(getActivity(), "action_faq", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_archive:
                Toast.makeText(getActivity(), "action_archive", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Toast.makeText(getActivity(), "action_share", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
