package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Config;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.listener.OnRecyclerViewScrollListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.ReviewActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

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
                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
            }

            @Override
            public void onHideView() {
                ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        updateUI();
        requestReviewList();
    }

    private void updateUI() {

        if (mReviewAdapter == null) {
            mReviewAdapter = new ReviewAdapter(mReviews);
            mReviewRecyclerView.setAdapter(mReviewAdapter);
        } else {
            mReviewAdapter.setReviews(mReviews);
            mReviewAdapter.notifyDataSetChanged();
        }
    }

    public void requestReviewList() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH + mProduct.getId() + Config.Review.PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, response.toString());
                        mReviews = Parser.parseReviewList(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                R.string.progress_dialog_message_error, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Review> mReviews;

        public ReviewAdapter(ArrayList<Review> reviews) {
            mReviews = reviews;
        }

        public void setReviews(ArrayList<Review> reviews) {
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
            if (holder instanceof ReviewHeaderHolder) {
                ((ReviewHeaderHolder) holder).bindReviewHeader(mReviews.size());
            }
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

        private TextView mReviewCountTextView;

        public ReviewHeaderHolder(View itemView) {
            super(itemView);

            mReviewCountTextView = (TextView)
                    itemView.findViewById(R.id.list_item_review_header_count_text_view);
        }

        public void bindReviewHeader(int reviewCount) {
            mReviewCountTextView.setText(getString(R.string.review_count_format, String.valueOf(reviewCount)));
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageButton mReviewCreateImageButton;

        EmptyViewHolder(View itemView) {
            super(itemView);

            mReviewCreateImageButton = (ImageButton)
                    itemView.findViewById(R.id.list_item_review_empty_review_create_image_button);
            mReviewCreateImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.list_item_review_empty_review_create_image_button:
                    startActivity(ReviewActivity.newIntent(getActivity(), mProduct));
                    break;
            }
        }
    }

    private class ReviewHolder extends RecyclerView.ViewHolder {

        private Review mReview;

        private TextView mUserNameTextView;
        private CircleImageView mUserProfileImageView;
        private RatingBar mRatingBar;
        private TextView mDateTextView;

        private TextView mParentIconTextView;
        private TextView mParentAgeTextView;
        private TextView mParentDrySkinTextView;
        private TextView mParentOilySkinTextView;
        private TextView mParentDryOilySkinTextView;
        private TextView mParentAllergyTextView;

        private LinearLayout mChildLayout;
        private TextView mChildIconTextView;
        private TextView mChildDrySkinTextView;
        private TextView mChildAllergyTextView;

        private TextView mContentTextView;

        private LinearLayout mImagesLayout;
        private ImageView mImage1ImageView;
        private ImageView mImage2ImageView;
        private ImageView mImage3ImageView;

        public ReviewHolder(View itemView) {
            super(itemView);

            mUserNameTextView = (TextView) itemView.findViewById(R.id.list_item_review_user_name_text_view);
            mUserProfileImageView = (CircleImageView) itemView.findViewById(R.id.list_item_review_user_profile_image_view);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.list_item_review_rating_bar);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_review_date_text_view);

            mParentIconTextView = (TextView) itemView.findViewById(R.id.list_item_review_parent_icon_text_view);
            mParentAgeTextView = (TextView) itemView.findViewById(R.id.list_item_review_parent_age_text_view);
            mParentDrySkinTextView = (TextView) itemView.findViewById(R.id.list_item_review_parent_dry_skin_text_view);
            mParentOilySkinTextView = (TextView) itemView.findViewById(R.id.list_item_review_parent_oily_skin_text_view);
            mParentDryOilySkinTextView = (TextView) itemView.findViewById(R.id.list_item_review_parent_dry_oily_skin_text_view);
            mParentAllergyTextView = (TextView) itemView.findViewById(R.id.list_item_review_parent_allergy_text_view);

            mChildLayout = (LinearLayout) itemView.findViewById(R.id.list_item_review_child_layout);
            mChildIconTextView = (TextView) itemView.findViewById(R.id.list_item_review_child_icon_text_view);
            mChildDrySkinTextView = (TextView) itemView.findViewById(R.id.list_item_review_child_dry_skin_text_view);
            mChildAllergyTextView = (TextView) itemView.findViewById(R.id.list_item_review_child_allergy_text_view);

            mContentTextView = (TextView) itemView.findViewById(R.id.list_item_review_content_text_view);

            mImagesLayout = (LinearLayout) itemView.findViewById(R.id.list_item_review_images_layout);
            mImage1ImageView = (ImageView) itemView.findViewById(R.id.list_item_review_image1_image_view);
            mImage2ImageView = (ImageView) itemView.findViewById(R.id.list_item_review_image2_image_view);
            mImage3ImageView = (ImageView) itemView.findViewById(R.id.list_item_review_image3_image_view);
        }

        public void bindReview(Review review) {
            mReview = review;

            mUserNameTextView.setText(mReview.getUser().getName());

            mRatingBar.setRating(mReview.getRatingValue());
            mDateTextView.setText(mReview.getDate());

            if (mReview.getUser().isGender()) {
                mParentIconTextView.setBackgroundResource(R.drawable.ic_mommy_gray);
            } else {
                mParentIconTextView.setBackgroundResource(R.drawable.ic_daddy_gray);
            }

            mParentAgeTextView.setText(mReview.getUser().getAge());

            if (mReview.getUser().isHasDrySkin() && mReview.getUser().isHasOilySkin()) {
                mParentDrySkinTextView.setVisibility(View.GONE);
                mParentOilySkinTextView.setVisibility(View.GONE);
                mParentDryOilySkinTextView.setVisibility(View.VISIBLE);
            } else if (mReview.getUser().isHasDrySkin() && !mReview.getUser().isHasOilySkin()) {
                mParentDrySkinTextView.setVisibility(View.VISIBLE);
                mParentOilySkinTextView.setVisibility(View.GONE);
                mParentDryOilySkinTextView.setVisibility(View.GONE);
            } else if (!mReview.getUser().isHasDrySkin() && mReview.getUser().isHasOilySkin()) {
                mParentDrySkinTextView.setVisibility(View.GONE);
                mParentOilySkinTextView.setVisibility(View.VISIBLE);
                mParentDryOilySkinTextView.setVisibility(View.GONE);
            } else if (!mReview.getUser().isHasDrySkin() && !mReview.getUser().isHasOilySkin()) {
                mParentDrySkinTextView.setVisibility(View.GONE);
                mParentOilySkinTextView.setVisibility(View.GONE);
                mParentDryOilySkinTextView.setVisibility(View.GONE);
            }

            if (mReview.getUser().isHasAllergy()) {
                mParentAllergyTextView.setVisibility(View.VISIBLE);
            } else {
                mParentAllergyTextView.setVisibility(View.GONE);
            }

            if (mReview.getUser().isHasChild()) {
                mChildLayout.setVisibility(View.VISIBLE);

                if (mReview.getUser().isChildHasDrySkin()) {
                    mChildDrySkinTextView.setVisibility(View.VISIBLE);
                } else {
                    mChildDrySkinTextView.setVisibility(View.GONE);
                }

                if (mReview.getUser().isChildHasAllergy()) {
                    mChildAllergyTextView.setVisibility(View.VISIBLE);
                } else {
                    mChildAllergyTextView.setVisibility(View.GONE);
                }

            } else {
                mChildLayout.setVisibility(View.GONE);
            }

            mContentTextView.setText(mReview.getContent());

            if (mReview.getImagePaths().size() == 0) {
                mImagesLayout.setVisibility(View.GONE);
            } else {
                mImagesLayout.setVisibility(View.VISIBLE);

                switch (mReview.getImagePaths().size()) {
                    case 3:
                        Glide.with(getActivity())
                                .load(mReview.getImagePaths().get(2))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                                .crossFade()
                                .override(70, 70)
                                .centerCrop()
                                .into(mImage3ImageView);
                    case 2:
                        Glide.with(getActivity())
                                .load(mReview.getImagePaths().get(1))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                                .crossFade()
                                .override(70, 70)
                                .centerCrop()
                                .into(mImage2ImageView);
                    case 1:
                        Glide.with(getActivity())
                                .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                                .crossFade()
                                .override(70, 70)
                                .centerCrop()
                                .into(mImage1ImageView);
                        break;
                }
            }
        }
    }
}
