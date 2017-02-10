package com.planet.wondering.chemi.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.archive.Content;
import com.planet.wondering.chemi.model.archive.Product;
import com.planet.wondering.chemi.model.archive.ReviewProduct;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.MemberActivity;

import java.text.DateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class MemberFragment extends Fragment
        implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = MemberFragment.class.getSimpleName();

    public static MemberFragment newInstance() {

        Bundle args = new Bundle();

        MemberFragment fragment = new MemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AppBarLayout mMemberAppBarLayout;
    private CircleImageView mMemberProfileCircleImageView;
    private TextView mMemberNameTextView;
    private TextView mMemberInfoTextView;
    private TextView mMemberSkinTypeTextView;
//    private ImageView mMemberConfigImageView;
    private Toolbar mMemberToolbar;
    private RecyclerView mArchiveProductRecyclerView;
    private RecyclerView mArchiveContentRecyclerView;
    private RecyclerView mArchiveReviewProductRecyclerView;
    private ArchiveProductAdapter mArchiveProductAdapter;
    private ArchiveContentAdapter mArchiveContentAdapter;
    private ArchiveReviewProductAdapter mArchiveReviewProductAdapter;
    private ArrayList<Product> mArchiveProducts;
    private ArrayList<Content> mArchiveContents;
    private ArrayList<ReviewProduct> mArchiveReviewProducts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mArchiveProducts = new ArrayList<>();
        mArchiveContents = new ArrayList<>();
        mArchiveReviewProducts = new ArrayList<>();
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

        mMemberProfileCircleImageView = (CircleImageView) view.findViewById(R.id.member_profile_circle_image_view);
        mMemberNameTextView = (TextView) view.findViewById(R.id.member_name_text_view);
        mMemberNameTextView.setText("안녕하세요");
        mMemberInfoTextView = (TextView) view.findViewById(R.id.member_info_text_view);
        mMemberSkinTypeTextView = (TextView) view.findViewById(R.id.member_skin_type_text_view);
//        mMemberConfigImageView = (ImageView) view.findViewById(R.id.member_config_image_view);
//        mMemberConfigImageView.setOnTouchListener(this);

        mArchiveProductRecyclerView = (RecyclerView) view.findViewById(R.id.archive_product_recycler_view);
        mArchiveProductRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
//                mArchiveProductRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL);
//        mArchiveProductRecyclerView.addItemDecoration(mDividerItemDecoration);
        mArchiveProductRecyclerView.setNestedScrollingEnabled(false);
        mArchiveProductRecyclerView.setHasFixedSize(false);

        mArchiveContentRecyclerView = (RecyclerView) view.findViewById(R.id.archive_content_recycler_view);
        mArchiveContentRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mArchiveContentRecyclerView.setNestedScrollingEnabled(false);
        mArchiveContentRecyclerView.setHasFixedSize(false);

        mArchiveReviewProductRecyclerView = (RecyclerView) view.findViewById(R.id.archive_review_product_recycler_view);
        mArchiveReviewProductRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mArchiveReviewProductRecyclerView.setNestedScrollingEnabled(false);
        mArchiveReviewProductRecyclerView.setHasFixedSize(false);

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

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.member_config_image_view:
//                Toast.makeText(getActivity(), "member_config_image_view", Toast.LENGTH_SHORT).show();
//                mMenuSelectedListener.onMenuSelected();
//                break;
//        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        switch (v.getId()) {
//            case R.id.member_config_image_view:
//                Toast.makeText(getActivity(), "member_config_image_view", Toast.LENGTH_SHORT).show();
//                mMenuSelectedListener.onMenuSelected();
//                break;
//        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_member, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_config:
//                Toast.makeText(getActivity(), "action_config", Toast.LENGTH_SHORT).show();
                mMenuSelectedListener.onMenuSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    OnMenuSelectedListener mMenuSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mMenuSelectedListener = (OnMenuSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMenuSelectedListener");
        }
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

//        for (int i = 0; i < 10; i++) {
//            Content content = new Content();
//            content.setTitle("title" + i + "title title title title title title title title title title");
//            mArchiveContents.add(content);
//        }

        if (mArchiveContentAdapter == null) {
            mArchiveContentAdapter = new ArchiveContentAdapter(mArchiveContents);
            mArchiveContentRecyclerView.setAdapter(mArchiveContentAdapter);
        } else {
            mArchiveContentAdapter.notifyDataSetChanged();
        }

//        for (int i = 0; i < 10; i++) {
//            ReviewProduct reviewProduct = new ReviewProduct();
//            reviewProduct.setProductName("product " + i + " name  product name product name");
//            reviewProduct.setRatingValue(i % 5);
//            reviewProduct.setWriteDate(new Date());
//            mArchiveReviewProducts.add(reviewProduct);
//        }

        if (mArchiveReviewProductAdapter == null) {
            mArchiveReviewProductAdapter = new ArchiveReviewProductAdapter(mArchiveReviewProducts);
            mArchiveReviewProductRecyclerView.setAdapter(mArchiveReviewProductAdapter);
        } else {
            mArchiveReviewProductAdapter.notifyDataSetChanged();
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

    private class ArchiveContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Content> mArchiveContents;

        public ArchiveContentAdapter(ArrayList<Content> archiveContents) {
            mArchiveContents = archiveContents;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            if (viewType == ARCHIVE_CONTENT_EMPTY_VIEW) {
                view = layoutInflater.inflate(R.layout.list_item_archive_content_empty, parent, false);
                return new ArchiveContentEmptyHolder(view);
            }
            view = layoutInflater.inflate(R.layout.list_item_archive_content, parent, false);
            return new ArchiveContentHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ArchiveContentHolder) {
                Content content = mArchiveContents.get(position);
                ((ArchiveContentHolder) holder).bindContent(content);
            }
        }

        @Override
        public int getItemCount() {
            return mArchiveContents.size() > 0 ? mArchiveContents.size() : 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (mArchiveContents.size() == 0) {
                return ARCHIVE_CONTENT_EMPTY_VIEW;
            }
            return super.getItemViewType(position);
        }
    }

    private static final int ARCHIVE_CONTENT_EMPTY_VIEW = -1;

    private class ArchiveContentEmptyHolder extends RecyclerView.ViewHolder {

        public ArchiveContentEmptyHolder(View itemView) {
            super(itemView);
        }
    }

    private class ArchiveContentHolder extends RecyclerView.ViewHolder {

        private Content mContent;

        private ImageView mArchiveContentImageView;
        private TextView mArchiveContentTitleTextView;

        public ArchiveContentHolder(View itemView) {
            super(itemView);

            mArchiveContentImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_archive_content_image_view);
            mArchiveContentTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_archive_content_title_text_view);
        }

        public void bindContent(Content content) {
            mContent = content;

            mArchiveContentTitleTextView.setText(mContent.getTitle());
        }
    }

    private class ArchiveReviewProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<ReviewProduct> mArchiveReviewProducts;

        public ArchiveReviewProductAdapter(ArrayList<ReviewProduct> archiveReviewProducts) {
            mArchiveReviewProducts = archiveReviewProducts;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            if (viewType == ARCHIVE_REVIEW_PRODUCT_EMPTY_VIEW) {
                view = layoutInflater.inflate(R.layout.list_item_archive_review_empty, parent, false);
                return new ArchiveReviewProductEmptyHolder(view);
            }
            view = layoutInflater.inflate(R.layout.list_item_archive_review, parent, false);
            return new ArchiveReviewProductHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ArchiveReviewProductHolder) {
                ReviewProduct reviewProduct = mArchiveReviewProducts.get(position);
                ((ArchiveReviewProductHolder) holder).bindReviewProduct(reviewProduct);
            }
        }

        @Override
        public int getItemCount() {
            return mArchiveReviewProducts.size() > 0 ? mArchiveReviewProducts.size() : 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (mArchiveReviewProducts.size() == 0) {
                return ARCHIVE_REVIEW_PRODUCT_EMPTY_VIEW;
            }
            return super.getItemViewType(position);
        }
    }

    private static final int ARCHIVE_REVIEW_PRODUCT_EMPTY_VIEW = -1;

    private class ArchiveReviewProductEmptyHolder extends RecyclerView.ViewHolder {

        public ArchiveReviewProductEmptyHolder(View itemView) {
            super(itemView);
        }
    }

    private class ArchiveReviewProductHolder extends RecyclerView.ViewHolder {

        private ReviewProduct mReviewProduct;

        private ImageView mArchiveReviewProductImageView;
        private TextView mArchiveReviewProductNameTextView;
        private RatingBar mArchiveReviewProductRatingBar;
        private TextView mArchiveReviewProductDateTextView;

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

        public ArchiveReviewProductHolder(View itemView) {
            super(itemView);

            mArchiveReviewProductImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_archive_review_product_image_view);
            mArchiveReviewProductNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_archive_review_product_name_text_view);
            mArchiveReviewProductRatingBar = (RatingBar)
                    itemView.findViewById(R.id.list_item_archive_review_product_rating_bar);
            mArchiveReviewProductDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_archive_review_product_date_text_view);
        }

        public void bindReviewProduct(ReviewProduct reviewProduct) {
            mReviewProduct = reviewProduct;

//            mArchiveReviewProductImageView
            mArchiveReviewProductNameTextView.setText(mReviewProduct.getProductName());
            mArchiveReviewProductRatingBar.setRating(mReviewProduct.getRatingValue());
            mArchiveReviewProductDateTextView.setText(format.format(mReviewProduct.getWriteDate()));
        }
    }

}
