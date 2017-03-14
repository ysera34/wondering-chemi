package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.util.listener.OnRecyclerViewScrollListener;
import com.planet.wondering.chemi.view.activity.ProductPagerActivity;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 19..
 */

public class ReviewListFragment extends Fragment {

    private static final String TAG = ReviewListFragment.class.getSimpleName();

    private static final String ARG_PRODUCT = "product";
    private static final String ARG_PRODUCT_ID = "product_id";

    public static ReviewListFragment newInstance() {

        Bundle args = new Bundle();

        ReviewListFragment fragment = new ReviewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewListFragment newInstance(int productId) {

        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);

        ReviewListFragment fragment = new ReviewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewListFragment newInstance(Product product) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);

        ReviewListFragment fragment = new ReviewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mProductId;
    private Product mProduct;
    private Review mReview;
    private ArrayList<Review> mReviews;

    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getArguments().getInt(ARG_PRODUCT_ID, -1);
        mProduct = (Product) getArguments().getSerializable(ARG_PRODUCT);

        mReviews = new ArrayList<>();
        mReviews.add(new Review());
        mReviews.add(new Review());
        mReviews.add(new Review());
        mReviews.add(new Review());
        mReviews.add(new Review());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);

        mReviewRecyclerView = (RecyclerView) view.findViewById(R.id.review_recycler_view);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        SeparatorDecoration decoration =
//                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
//        mReviewRecyclerView.addItemDecoration(decoration);
        mReviewRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onShowView() {
                ((ProductPagerActivity) getActivity()).showBottomNavigationView();
            }

            @Override
            public void onHideView() {
                ((ProductPagerActivity) getActivity()).hideBottomNavigationView();
            }
        });
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

        if (mReviewAdapter == null) {
            mReviewAdapter = new ReviewAdapter(mReviews);
            mReviewRecyclerView.setAdapter(mReviewAdapter);
        } else {
            mReviewAdapter.notifyDataSetChanged();
        }
    }

    private class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Review> mReviews = new ArrayList<>();

        public ReviewAdapter(ArrayList<Review> reviews) {
            mReviews = reviews;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            switch (viewType) {
                case VIEW_TYPE_EMPTY:
                    view = layoutInflater.inflate(R.layout.list_item_review_empty, parent, false);
                    return new EmptyViewHolder(view);
                case VIEW_TYPE_HEADER:
                    view = layoutInflater.inflate(R.layout.list_item_review_header, parent, false);
                    return new ReviewHeaderHolder(view);
                case VIEW_TYPE_ITEM:
                    view = layoutInflater.inflate(R.layout.list_item_review, parent, false);
                    return new ReviewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ReviewHolder) {
                Review review = mReviews.get(position - 1);
                ((ReviewHolder) holder).bindReview(review);
            }
        }

        @Override
        public int getItemCount() {
            if (mReviews == null || mReviews.size() == 0) {
                return 2;
            }
            if (mReviews.size() > 0) {
                return mReviews.size() + 1;
            }
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return VIEW_TYPE_HEADER;
            }
            if ((mReviews == null || mReviews.size() == 0) && position == 1) {
                return VIEW_TYPE_EMPTY;
            }
            return VIEW_TYPE_ITEM;
        }
    }

    private static final int VIEW_TYPE_HEADER = -1;
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private class ReviewHeaderHolder extends RecyclerView.ViewHolder {

        public ReviewHeaderHolder(View itemView) {
            super(itemView);
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ReviewHolder extends RecyclerView.ViewHolder {

        private Review mReview;

        public ReviewHolder(View itemView) {
            super(itemView);
        }

        public void bindReview(Review review) {

        }
    }

}
