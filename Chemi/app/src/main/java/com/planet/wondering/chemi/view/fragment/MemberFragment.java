package com.planet.wondering.chemi.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.archive.Product;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.MemberActivity;

import java.util.ArrayList;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class MemberFragment extends Fragment {

    private static final String TAG = MemberFragment.class.getSimpleName();

    public static MemberFragment newInstance() {

        Bundle args = new Bundle();

        MemberFragment fragment = new MemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AppBarLayout mMemberAppBarLayout;
    private Toolbar mMemberToolbar;
    private RecyclerView mArchiveProductRecyclerView;
    private RecyclerView mArchiveContentRecyclerView;
    private RecyclerView mArchiveReviewProductRecyclerView;
    private ArchiveProductAdapter mArchiveProductAdapter;
    private ArchiveContentAdapter mArchiveContentAdapter;
    private ArchiveReviewProductAdapter mArchiveReviewProductAdapter;
    private ArrayList<Product> mArchiveProducts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArchiveProducts = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        mMemberToolbar = (Toolbar) view.findViewById(R.id.member_toolbar);
        ((MemberActivity) getActivity()).setSupportActionBar(mMemberToolbar);
        mMemberAppBarLayout = (AppBarLayout) view.findViewById(R.id.member_app_bar_layout);
        mMemberAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, final int verticalOffset) {
//                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
//                if (verticalOffset == -mMemberAppBarLayout.getHeight() + mMemberToolbar.getHeight()) {
                if (verticalOffset < -appBarLayout.getTotalScrollRange() / 2) {
                    ((MemberActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
                    ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
                } else {
                    ((MemberActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                    ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
                }
            }
        });
        mArchiveProductRecyclerView = (RecyclerView) view.findViewById(R.id.archive_product_recycler_view);
        mArchiveProductRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mArchiveProductRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL);
        mArchiveProductRecyclerView.addItemDecoration(mDividerItemDecoration);
        mArchiveProductRecyclerView.setNestedScrollingEnabled(false);
        mArchiveProductRecyclerView.setHasFixedSize(false);

        mArchiveContentRecyclerView = (RecyclerView) view.findViewById(R.id.archive_content_recycler_view);
        mArchiveReviewProductRecyclerView = (RecyclerView) view.findViewById(R.id.archive_review_product_recycler_view);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    private void updateUI() {

//        for (int i = 0; i < 10; i++) {
//            Product product = new Product();
//            product.setBrand("brand" + i);
//            product.setName("name" + i);
//            mArchiveProducts.add(product);
//        }

        if (mArchiveProductAdapter == null) {
            mArchiveProductAdapter = new ArchiveProductAdapter(mArchiveProducts);
            mArchiveProductRecyclerView.setAdapter(mArchiveProductAdapter);
        } else {
            mArchiveProductAdapter.notifyDataSetChanged();
        }

    }

    private class ArchiveProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Product> mArchiveProducts;

        public ArchiveProductAdapter(ArrayList<Product> archiveProducts) {
            mArchiveProducts = archiveProducts;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            if (viewType == ARCHIVE_PRODUCT_EMPTY_VIEW) {
                view = layoutInflater.inflate(R.layout.list_item_archive_product_empty, parent, false);
                return new ArchiveProductEmptyHolder(view);
            }
            view = layoutInflater.inflate(R.layout.list_item_archive_product, parent, false);
            return new ArchiveProductHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ArchiveProductHolder) {
                Product product = mArchiveProducts.get(position);
                ((ArchiveProductHolder) holder).bindProduct(product);
            }
        }

        @Override
        public int getItemCount() {
            return mArchiveProducts.size() > 0 ? mArchiveProducts.size() : 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (mArchiveProducts.size() == 0) {
                return ARCHIVE_PRODUCT_EMPTY_VIEW;
            }
            return super.getItemViewType(position);
        }
    }

    private static final int ARCHIVE_PRODUCT_EMPTY_VIEW = -1;

    private class ArchiveProductEmptyHolder extends RecyclerView.ViewHolder {

        public ArchiveProductEmptyHolder(View itemView) {
            super(itemView);
        }
    }

    private class ArchiveProductHolder extends RecyclerView.ViewHolder {

        private Product mProduct;

        private ImageView mArchiveProductImageView;
        private TextView mArchiveProductBrandTextView;
        private TextView mArchiveProductNameTextView;

        public ArchiveProductHolder(View itemView) {
            super(itemView);

            mArchiveProductImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_archive_product_image_view);
            mArchiveProductBrandTextView = (TextView)
                    itemView.findViewById(R.id.list_item_archive_product_brand_text_view);
            mArchiveProductNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_archive_product_name_text_view);
        }

        public void bindProduct(Product product) {
            mProduct = product;

//            mArchiveProductImageView
            mArchiveProductBrandTextView.setText(mProduct.getBrand());
            mArchiveProductNameTextView.setText(mProduct.getName());
        }
    }

    private class ArchiveContentAdapter {
    }

    private class ArchiveReviewProductAdapter {
    }


}
