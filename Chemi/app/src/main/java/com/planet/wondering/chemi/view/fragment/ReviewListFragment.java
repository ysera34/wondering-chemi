package com.planet.wondering.chemi.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.Common;
import com.planet.wondering.chemi.model.Pager;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Config;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.view.activity.MemberActivity;
import com.planet.wondering.chemi.view.activity.ReviewActivity;
import com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.kakao.util.exception.KakaoException.ErrorType.KAKAOTALK_NOT_INSTALLED;
import static com.planet.wondering.chemi.common.Common.LOGIN_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.PRODUCT_SHARE_TEMPLATE_CODE;
import static com.planet.wondering.chemi.common.Common.PROMOTE_EXTRA_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.Product.KEEP_PATH;
import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.LOGIN_DIALOG;

/**
 * Created by yoon on 2017. 1. 19..
 */

public class ReviewListFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ReviewListFragment.class.getSimpleName();

    private static final String ARG_PRODUCT = "product";
    private static final String ARG_PRODUCT_ID = "product_id";

    private static final int REVIEW_CREATE_REQUEST_CODE = 8011;

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

    private StringBuilder mUrlBuilder;
    private Pager mPager;

    private int mProductId;
    private Product mProduct;
    private Review mReview;
    private ArrayList<Review> mReviews;

    private Fragment mChartFragment;
    private FragmentManager mChildFragmentManager;

    private ImageView mFaqButtonImageView;
    private ImageView mArchiveButtonImageView;
    private ImageView mShareButtonImageView;

    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getArguments().getInt(ARG_PRODUCT_ID, -1);
        mProduct = (Product) getArguments().getSerializable(ARG_PRODUCT);

        mChildFragmentManager = getChildFragmentManager();

        mReviews = new ArrayList<>();
        mUrlBuilder = new StringBuilder();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);

        int[] numberOfEachEWGRatings = mProduct.getNumberOfEachEWGRating();
        int chemicalCount = mProduct.getChemicals().size();
        mChartFragment = mChildFragmentManager.findFragmentById(R.id.product_extend_fragment_container);
        if (mChartFragment == null) {
            if (mProduct.getProductType() == 1) {
                mChildFragmentManager.beginTransaction()
                        .add(R.id.product_extend_fragment_container,
                                ChemicalChartFragment.newInstance(
                                        mProduct.isWholeChemicals(), chemicalCount, numberOfEachEWGRatings))
                        .commit();
            } else if (mProduct.getProductType() == 2) {
                mChildFragmentManager.beginTransaction()
                        .add(R.id.product_extend_fragment_container,
                                ProductInfoFragment.newInstance(mProduct))
                        .commit();
            }
        }

        mReviewRecyclerView = (RecyclerView) view.findViewById(R.id.review_recycler_view);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mReviewRecyclerView.setNestedScrollingEnabled(false);
        mReviewRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = ((LinearLayoutManager) mReviewRecyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                if (lastItem == mReviewAdapter.getItemCount() - 1 && mPager != null
                        && mPager.getTotal() > mReviewAdapter.getItemCount() - 1) {
                    requestReviewList();
                }
            }
        });

        updateUI();

        mFaqButtonImageView = (ImageView) view.findViewById(R.id.faq_button_image_view);
        mArchiveButtonImageView = (ImageView) view.findViewById(R.id.archive_button_image_view);
        mShareButtonImageView = (ImageView) view.findViewById(R.id.share_button_image_view);
        mFaqButtonImageView.setOnClickListener(this);
        mArchiveButtonImageView.setOnClickListener(this);
        mShareButtonImageView.setOnClickListener(this);
        updateArchiveImageView();

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
        updateReviewList();
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

    private void updateArchiveImageView() {
        if (mProduct.isArchive()) {
            mArchiveButtonImageView.setImageResource(R.drawable.ic_toolbar_archive_true);
        } else {
            mArchiveButtonImageView.setImageResource(R.drawable.ic_toolbar_archive_false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REVIEW_CREATE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
//                    updateReviewList();
                }
                break;
            case PROMOTE_EXTRA_DIALOG_REQUEST_CODE:
                Log.i(TAG, "PROMOTE_EXTRA_DIALOG_REQUEST_CODE : " + PROMOTE_EXTRA_DIALOG_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faq_button_image_view:
                startActivity(MemberActivity.newIntent(getActivity(), 4));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.archive_button_image_view:
                if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
                    requestArchiveProduct(mProduct.isArchive());
                } else {
                    CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                            .newInstance(R.drawable.ic_login, R.string.login_info_message,
                                    R.string.login_button_title, LOGIN_DIALOG_REQUEST_CODE);
                    dialogFragment1.show(getChildFragmentManager(), LOGIN_DIALOG);
                }
                break;
            case R.id.share_button_image_view:
                requestShareProductToKakao();
                break;
        }
    }

    private void updateReviewList() {

        mPager = null;
        mReviews.clear();
        updateUI();
        requestReviewList();
    }

    public void requestReviewList() {

        if (mPager == null) {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(PATH).append(mProduct.getId()).append(Config.Review.PATH);
        } else {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(PATH).append(mProduct.getId()).append(Config.Review.PATH)
                    .append("/?").append(mPager.getNextQuery());
        }

        Log.i(TAG, mUrlBuilder.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mUrlBuilder.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, response.toString());
//                        mReviews = Parser.parseReviewList(response);
                        mProduct.setRatingCount(Parser.parseReviewCount(response));
                        mReviews.addAll(Parser.parseReviewList(response));
                        mPager = Parser.parseListPaginationQuery(response);
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
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
//                    view = layoutInflater.inflate(R.layout.list_item_review_header, parent, false);
                    view = layoutInflater.inflate(R.layout.list_item_review_count_header, parent, false);
                    return new ReviewHeaderHolder(view);
                case VIEW_TYPE_ITEM:
                    view = layoutInflater.inflate(R.layout.list_item_review, parent, false);
                    return new ReviewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            if (holder instanceof ReviewHeaderHolder) {
//                ((ReviewHeaderHolder) holder).bindReviewHeader(mReviews.size());
//            }
            if (holder instanceof ReviewHolder) {
                Review review = mReviews.get(position - 1);
                ((ReviewHolder) holder).bindReview(review);
            } else if (holder instanceof ReviewHeaderHolder) {
                ((ReviewHeaderHolder) holder).bindReviewHeader(mProduct.getRatingCount());
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
//        private LinearLayout mReviewCreateLayout;

        public ReviewHeaderHolder(View itemView) {
            super(itemView);

            mReviewCountTextView = (TextView)
                    itemView.findViewById(R.id.list_item_review_header_count_text_view);
//            mReviewCreateLayout = (LinearLayout)
//                    itemView.findViewById(R.id.list_item_review_header_review_create_layout);
//            mReviewCreateLayout.setOnClickListener(this);
        }

        public void bindReviewHeader(int reviewCount) {
//            mReviewCountTextView.setText(getString(R.string.review_count_format, String.valueOf(reviewCount)));
            mReviewCountTextView.setText(String.valueOf(reviewCount));
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            itemView.setOnClickListener(this);

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
//            Log.i(TAG, mReview.toString());

            mUserNameTextView.setText(mReview.getUser().getName());

            mRatingBar.setRating(mReview.getRatingValue());
            mDateTextView.setText(mReview.getDate());

            if (mReview.getUser().getImagePath() == null || mReview.getUser().getImagePath().equals("null")) {
                if (mReview.getUser().isGender()) {
                    mUserProfileImageView.setImageResource(R.drawable.ic_user_profile_mommy);
                } else {
                    mUserProfileImageView.setImageResource(R.drawable.ic_user_profile_daddy);
                }
            } else {
                Glide.with(getActivity())
                        .load(mReview.getUser().getImagePath())
                        .crossFade()
                        .into(mUserProfileImageView);
            }

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

                    case 1:
                        Glide.with(getActivity())
                                .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                                .crossFade()
                                .override(210, 210)
//                                .centerCrop()
                                .into(mImage1ImageView);
//                        mImage2ImageView.setVisibility(View.GONE);
//                        mImage3ImageView.setVisibility(View.GONE);
                        mImage2ImageView.setImageResource(R.drawable.widget_solid_rectangle_white);
                        mImage3ImageView.setImageResource(R.drawable.widget_solid_rectangle_white);
                        break;
                    case 2:
                        Glide.with(getActivity())
                                .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                                .crossFade()
                                .override(210, 210)
//                                .centerCrop()
                                .into(mImage1ImageView);
                        Glide.with(getActivity())
                                .load(mReview.getImagePaths().get(1))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                                .crossFade()
                                .override(210, 210)
//                                .centerCrop()
                                .into(mImage2ImageView);
                        mImage3ImageView.setImageResource(R.drawable.widget_solid_rectangle_white);
                        break;
                    case 3:
                        Glide.with(getActivity())
                                .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                                .crossFade()
                                .override(210, 210)
//                                .centerCrop()
                                .into(mImage1ImageView);
                        Glide.with(getActivity())
                                .load(mReview.getImagePaths().get(1))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                                .crossFade()
                                .override(210, 210)
//                                .centerCrop()
                                .into(mImage2ImageView);
                        Glide.with(getActivity())
                                .load(mReview.getImagePaths().get(2))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                                .crossFade()
                                .override(210, 210)
//                                .centerCrop()
                                .into(mImage3ImageView);
                        break;
                }
            }
        }

        @Override
        public void onClick(View v) {
//            startActivity(ReviewActivity.newIntent(getActivity(), mProduct, mReview, Common.REVIEW_READ_REQUEST_CODE));
            startActivity(ReviewActivity.newIntent(getActivity(), mReview.getId(), Common.REVIEW_READ_REQUEST_CODE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void requestArchiveProduct(final boolean isArchive) {

        int requestMethodId;
        if (!isArchive) {
            requestMethodId = Request.Method.POST;
        } else {
            requestMethodId = Request.Method.DELETE;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                requestMethodId, URL_HOST + PATH + mProduct.getId() + KEEP_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {

                            if (!isArchive) {
                                Toast.makeText(getActivity(), "보관함에 보관되었어요.", Toast.LENGTH_SHORT).show();
                                mProduct.setArchive(true);
                            } else {
                                Toast.makeText(getActivity(), "보관함에세 삭제되었어요.", Toast.LENGTH_SHORT).show();
                                mProduct.setArchive(false);
                            }
                            updateArchiveImageView();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
//                        Toast.makeText(getApplicationContext(),
//                                "상품을 보관하는 중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestShareProductToKakao() {

        String prefixTitle = "";
        if (UserSharedPreferences.getStoredUserName(getActivity()) != null) {
            prefixTitle = getString(R.string.share_user_name_format,
                    UserSharedPreferences.getStoredUserName(getActivity())) + " ";
        }

        Map<String, String> templateArgs = new HashMap<>();
        templateArgs.put("${imagePath}", mProduct.getImagePath());
        templateArgs.put("${title}", prefixTitle + getString(R.string.share_product_title_format,
                mProduct.getBrand(), mProduct.getName()));
        templateArgs.put("${description}", getString(R.string.share_product_description_format,
                mProduct.getBrand(), mProduct.getName()));
        templateArgs.put("${product_id}", String.valueOf(mProduct.getId()));

        KakaoLinkService.getInstance().sendCustom(getActivity(), PRODUCT_SHARE_TEMPLATE_CODE,
                templateArgs, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
//                Logger.e(errorResult.toString());
//                Toast.makeText(getApplicationContext(), errorResult.toString(), Toast.LENGTH_LONG).show();

                        if (errorResult.getException().toString().split(":")[0].equals(KAKAOTALK_NOT_INSTALLED.toString())) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage(getString(com.kakao.kakaolink.R.string.com_kakao_alert_install_kakaotalk));
                            builder1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(KakaoTalkLinkProtocol.TALK_MARKET_URL_PREFIX)));
                                }
                            });
                            builder1.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog dialog1 = builder1.create();
                            dialog1.show();
                        }
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                    }
                });
    }
}
