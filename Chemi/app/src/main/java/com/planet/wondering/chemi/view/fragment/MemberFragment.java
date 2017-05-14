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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.Common;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.model.archive.Content;
import com.planet.wondering.chemi.model.archive.Product;
import com.planet.wondering.chemi.model.archive.ReviewProduct;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.CategoryActivity;
import com.planet.wondering.chemi.view.activity.ContentActivity;
import com.planet.wondering.chemi.view.activity.ContentListActivity;
import com.planet.wondering.chemi.view.activity.MemberActivity;
import com.planet.wondering.chemi.view.activity.ProductActivity;
import com.planet.wondering.chemi.view.activity.ReviewActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class MemberFragment extends Fragment
        implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = MemberFragment.class.getSimpleName();

    private static final String ARG_CONFIG_USER = "config_user";

    public static MemberFragment newInstance() {

        Bundle args = new Bundle();

        MemberFragment fragment = new MemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIG_USER, user);

        MemberFragment fragment = new MemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AppBarLayout mMemberAppBarLayout;
    private CircleImageView mMemberProfileCircleImageView;
//    private ImageView mMemberConfigImageView;
    private TextView mMemberNameTextView;

    private LinearLayout mParentLayout;
    private TextView mParentAgeTextView;
    private TextView mParentDrySkinTextView;
    private TextView mParentOilySkinTextView;
    private TextView mParentDryOilySkinTextView;
    private TextView mParentAllergyTextView;

    private LinearLayout mChildLayout;
    private TextView mChildDrySkinTextView;
    private TextView mChildAllergyTextView;

    private Toolbar mMemberToolbar;
    private TextView mArchiveProductTitleTextView;
    private TextView mArchiveContentTitleTextView;
    private TextView mArchiveReviewProductTitleTextView;
    private TextView mArchiveProductMoreButtonTextView;
    private TextView mArchiveContentMoreButtonTextView;
    private TextView mArchiveReviewProductMoreButtonTextView;

    private RecyclerView mArchiveProductRecyclerView;
    private RecyclerView mArchiveContentRecyclerView;
    private RecyclerView mArchiveReviewProductRecyclerView;
    private ArchiveProductAdapter mArchiveProductAdapter;
    private ArchiveContentAdapter mArchiveContentAdapter;
    private ArchiveReviewProductAdapter mArchiveReviewProductAdapter;
    private ArrayList<Product> mArchiveProducts;
    private ArrayList<Content> mArchiveContents;
    private ArrayList<ReviewProduct> mArchiveReviewProducts;

    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mUser = (User) getArguments().getSerializable(ARG_CONFIG_USER);

        if (mUser.getArchiveProducts() == null) {
            mArchiveProducts = new ArrayList<>();
        } else {
            mArchiveProducts = mUser.getArchiveProducts();
        }

        if (mUser.getArchiveContents() == null) {
            mArchiveContents = new ArrayList<>();
        } else {
            mArchiveContents = mUser.getArchiveContents();
        }

        if (mUser.getReviewProducts() == null) {
            mArchiveReviewProducts = new ArrayList<>();
        } else {
            mArchiveReviewProducts = mUser.getReviewProducts();
        }
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
        mMemberProfileCircleImageView.setOnClickListener(this);
        mMemberNameTextView = (TextView) view.findViewById(R.id.member_profile_name_text_view);

        mMemberNameTextView.setOnClickListener(this);
//        mMemberConfigImageView = (ImageView) view.findViewById(R.id.member_config_image_view);
//        mMemberConfigImageView.setOnTouchListener(this);

        mParentLayout = (LinearLayout) view.findViewById(R.id.member_profile_parent_layout);
        mParentAgeTextView = (TextView) view.findViewById(R.id.member_profile_parent_age_text_view);
        mParentDrySkinTextView = (TextView) view.findViewById(R.id.member_profile_parent_dry_skin_text_view);
        mParentOilySkinTextView = (TextView) view.findViewById(R.id.member_profile_parent_oily_skin_text_view);
        mParentDryOilySkinTextView = (TextView) view.findViewById(R.id.member_profile_parent_dry_oily_skin_text_view);
        mParentAllergyTextView = (TextView) view.findViewById(R.id.member_profile_parent_allergy_text_view);

        mChildLayout = (LinearLayout) view.findViewById(R.id.member_profile_child_layout);
        mChildDrySkinTextView = (TextView) view.findViewById(R.id.member_profile_child_dry_skin_text_view);
        mChildAllergyTextView = (TextView) view.findViewById(R.id.member_profile_child_allergy_text_view);

        mArchiveProductTitleTextView = (TextView) view.findViewById(R.id.archive_product_title_text_view);
        mArchiveContentTitleTextView = (TextView) view.findViewById(R.id.archive_content_title_text_view);
        mArchiveReviewProductTitleTextView = (TextView) view.findViewById(R.id.archive_review_product_title_text_view);

        mArchiveProductMoreButtonTextView = (TextView) view.findViewById(R.id.archive_product_more_button_text_view);
        mArchiveProductMoreButtonTextView.setOnClickListener(this);
        mArchiveContentMoreButtonTextView = (TextView) view.findViewById(R.id.archive_content_more_button_text_view);
        mArchiveContentMoreButtonTextView.setOnClickListener(this);
        mArchiveReviewProductMoreButtonTextView = (TextView) view.findViewById(R.id.archive_review_product_more_button_text_view);
        mArchiveReviewProductMoreButtonTextView.setOnClickListener(this);


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

        if (mUser.getImagePath() == null || mUser.getImagePath().equals("null")) {
            if (mUser.isHasExtraInfo()) {
                if (mUser.isGender()) {
                    mMemberProfileCircleImageView.setImageResource(R.drawable.ic_user_profile_mommy);
                } else {
                    mMemberProfileCircleImageView.setImageResource(R.drawable.ic_user_profile_daddy);
                }
            } else {
                mMemberProfileCircleImageView.setImageResource(R.drawable.ic_user);
            }
        } else {
            Glide.with(getActivity())
                    .load(mUser.getImagePath())
                    .crossFade()
                    .into(mMemberProfileCircleImageView);
        }

        mMemberNameTextView.setText(mUser.getName());

        if (mUser.isHasExtraInfo()) {
//            promote member survey

        } else {
//            display member info
        }

        mParentAgeTextView.setText(mUser.getAge());

        if (mUser.isHasDrySkin() && mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.VISIBLE);
        } else if (mUser.isHasDrySkin() && !mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.VISIBLE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        } else if (!mUser.isHasDrySkin() && mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.VISIBLE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        } else if (!mUser.isHasDrySkin() && !mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        }

        if (mUser.isHasAllergy()) {
            mParentAllergyTextView.setVisibility(View.VISIBLE);
        } else {
            mParentAllergyTextView.setVisibility(View.GONE);
        }

        if (mUser.isHasChild()) {
            mChildLayout.setVisibility(View.VISIBLE);

            if (mUser.isChildHasDrySkin()) {
                mChildDrySkinTextView.setVisibility(View.VISIBLE);
            } else {
                mChildDrySkinTextView.setVisibility(View.GONE);
            }

            if (mUser.isChildHasAllergy()) {
                mChildAllergyTextView.setVisibility(View.VISIBLE);
            } else {
                mChildAllergyTextView.setVisibility(View.GONE);
            }

        } else {
            mChildLayout.setVisibility(View.GONE);
        }

        mArchiveProductTitleTextView.setText(getString(
                R.string.member_profile_archive_product_title, mUser.getName()));
        mArchiveContentTitleTextView.setText(
                getString(R.string.member_profile_archive_content_title, mUser.getName()));
        mArchiveReviewProductTitleTextView.setText(
                getString(R.string.member_profile_archive_review_product_title, mUser.getName()));

//        if (mArchiveProducts.size() <= 0) {
            mArchiveProductMoreButtonTextView.setVisibility(View.GONE);
//        }
//        if (mArchiveContents.size() <= 0) {
            mArchiveContentMoreButtonTextView.setVisibility(View.GONE);
//        }
//        if (mArchiveReviewProducts.size() <= 0) {
            mArchiveReviewProductMoreButtonTextView.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_profile_circle_image_view:
            case R.id.member_profile_name_text_view:
                mMenuSelectedListener.onMenuSelected(1);
                break;
            case R.id.archive_product_more_button_text_view:
                break;
            case R.id.archive_content_more_button_text_view:
                break;
            case R.id.archive_review_product_more_button_text_view:
                break;
        }
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
                mMenuSelectedListener.onMenuSelected(-1);
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

        if (mArchiveProductAdapter == null) {
            mArchiveProductAdapter = new ArchiveProductAdapter(mArchiveProducts);
            mArchiveProductRecyclerView.setAdapter(mArchiveProductAdapter);
        } else {
            mArchiveProductAdapter.notifyDataSetChanged();
        }

        if (mArchiveContentAdapter == null) {
            mArchiveContentAdapter = new ArchiveContentAdapter(mArchiveContents);
            mArchiveContentRecyclerView.setAdapter(mArchiveContentAdapter);
        } else {
            mArchiveContentAdapter.notifyDataSetChanged();
        }

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

    private class ArchiveProductEmptyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mArchiveProductEmptyPromoteButtonTextView;

        public ArchiveProductEmptyHolder(View itemView) {
            super(itemView);
            mArchiveProductEmptyPromoteButtonTextView = (TextView)
                    itemView.findViewById(R.id.archive_product_empty_promote_button_text_view);
            mArchiveProductEmptyPromoteButtonTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.archive_product_empty_promote_button_text_view:
                    startActivity(CategoryActivity.newIntent(getActivity()));
                    break;
            }
        }
    }

    private class ArchiveProductHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Product mProduct;

        private ImageView mArchiveProductImageView;
        private TextView mArchiveProductBrandTextView;
        private TextView mArchiveProductNameTextView;

        public ArchiveProductHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mArchiveProductImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_archive_product_image_view);
            mArchiveProductBrandTextView = (TextView)
                    itemView.findViewById(R.id.list_item_archive_product_brand_text_view);
            mArchiveProductNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_archive_product_name_text_view);
        }

        public void bindProduct(Product product) {
            mProduct = product;

            Glide.with(getActivity())
                    .load(mProduct.getImagePath())
                    .crossFade()
                    .into(mArchiveProductImageView);
            mArchiveProductBrandTextView.setText(mProduct.getBrand());
            mArchiveProductNameTextView.setText(mProduct.getName());
        }

        @Override
        public void onClick(View v) {
            startActivity(ProductActivity.newIntent(getActivity(), mProduct.getProductId(), (byte) 0));
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

    private class ArchiveContentEmptyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mArchiveContentEmptyPromoteButtonTextView;

        public ArchiveContentEmptyHolder(View itemView) {
            super(itemView);
            mArchiveContentEmptyPromoteButtonTextView = (TextView)
                    itemView.findViewById(R.id.archive_content_empty_promote_button_text_view);
            mArchiveContentEmptyPromoteButtonTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.archive_content_empty_promote_button_text_view:
                    startActivity(ContentListActivity.newIntent(getActivity()));
                    break;
            }
        }
    }

    private class ArchiveContentHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Content mContent;

        private ImageView mArchiveContentImageView;
        private TextView mArchiveContentTitleTextView;
//        private TextView mArchiveContentSubTitleTextView;

        public ArchiveContentHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mArchiveContentImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_archive_content_image_view);
            mArchiveContentTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_archive_content_title_text_view);
//            mArchiveContentSubTitleTextView = (TextView)
//                    itemView.findViewById(R.id.list_item_archive_content_sub_title_text_view);
        }

        public void bindContent(Content content) {
            mContent = content;
            Glide.with(getActivity())
                    .load(mContent.getImagePath())
                    .crossFade()
                    .into(mArchiveContentImageView);
            mArchiveContentTitleTextView.setText(mContent.getTitle());
//            mArchiveContentSubTitleTextView.setText(mContent.getSubTitle());
        }

        @Override
        public void onClick(View v) {
            startActivity(ContentActivity.newIntent(getActivity(), mContent.getContentId()));
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

    private class ArchiveReviewProductEmptyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mArchiveReviewEmptyPromoteButtonTextView;

        public ArchiveReviewProductEmptyHolder(View itemView) {
            super(itemView);
            mArchiveReviewEmptyPromoteButtonTextView = (TextView)
                    itemView.findViewById(R.id.archive_review_empty_promote_button_text_view);
            mArchiveReviewEmptyPromoteButtonTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.archive_review_empty_promote_button_text_view:
                    startActivity(CategoryActivity.newIntent(getActivity()));
                    break;
            }
        }
    }

    private class ArchiveReviewProductHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ReviewProduct mReviewProduct;

        private ImageView mArchiveReviewProductImageView;
        private TextView mArchiveReviewProductNameTextView;
        private RatingBar mArchiveReviewProductRatingBar;
        private TextView mArchiveReviewProductDateTextView;

        public ArchiveReviewProductHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

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

            Glide.with(getActivity())
                    .load(mReviewProduct.getProductImagePath())
                    .crossFade()
                    .into(mArchiveReviewProductImageView);
            mArchiveReviewProductNameTextView.setText(mReviewProduct.getProductName());
            mArchiveReviewProductRatingBar.setRating(mReviewProduct.getRatingValue());
            mArchiveReviewProductDateTextView.setText(mReviewProduct.getCreateDate());
//            mArchiveReviewProductDateTextView.setText(String.valueOf(mReviewProduct.getWriteDate()));
        }

        @Override
        public void onClick(View v) {
            startActivity(ReviewActivity.newIntent(
                    getActivity(), mReviewProduct.getReviewId(), Common.REVIEW_READ_REQUEST_CODE));
        }
    }

}
