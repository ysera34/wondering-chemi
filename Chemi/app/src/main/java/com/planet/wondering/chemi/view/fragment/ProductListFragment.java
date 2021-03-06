package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Pager;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.adapter.TagCharacterAdapter;
import com.planet.wondering.chemi.util.listener.OnUpdateProductListListener;
import com.planet.wondering.chemi.view.activity.AppBaseActivity;
import com.planet.wondering.chemi.view.activity.MemberActivity;
import com.planet.wondering.chemi.view.activity.ProductActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.planet.wondering.chemi.common.Common.PRODUCT_LIST_ITEM_THUMBNAIL_SCREEN_WIDTH_RATIO;
import static com.planet.wondering.chemi.common.Common.PRODUCT_LIST_ITEM_THUMBNAIL_WIDTH_HEIGHT_RATIO;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.Product.QUERY_CATEGORY;
import static com.planet.wondering.chemi.network.Config.Product.QUERY_PATH;
import static com.planet.wondering.chemi.network.Config.Product.QUERY_TAG;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.NetworkConfig.URL_HOST;
import static com.planet.wondering.chemi.network.Config.encodeUTF8;

/**
 * Created by yoon on 2017. 1. 18..
 */

public class ProductListFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ProductListFragment.class.getSimpleName();

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_PRODUCT_IDS = "product_ids";
    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_CATEGORY_NAME = "category_name";
    private static final String ARG_TAG_NAME = "tag_name";

    public static ProductListFragment newInstance() {

        Bundle args = new Bundle();

        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    public static ProductListFragment newInstance(int productId) {
//
//        Bundle args = new Bundle();
//        args.putInt(ARG_PRODUCT_ID, productId);
//
//        ProductListFragment fragment = new ProductListFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static ProductListFragment newInstance(int categoryId) {

        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);

        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductListFragment newInstance(int categoryId, String categoryName) {

        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        args.putString(ARG_CATEGORY_NAME, categoryName);

        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductListFragment newInstance(String tagName) {

        Bundle args = new Bundle();
        args.putString(ARG_TAG_NAME, tagName);

        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductListFragment newInstance(String tagName, int categoryId, String categoryName) {

        Bundle args = new Bundle();
        args.putString(ARG_TAG_NAME, tagName);
        args.putInt(ARG_CATEGORY_ID, categoryId);
        args.putString(ARG_CATEGORY_NAME, categoryName);

        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private StringBuilder mUrlBuilder;
    private ArrayList<Product> mProducts;
//    private Product mProduct;
    private Pager mPager;

    private InputMethodManager mInputMethodManager;
    private ImageView mProductListBackArrowImageView;
    private AutoCompleteTextView mSearchAutoCompleteTextView;
    private TagCharacterAdapter mTagCharacterAdapter;
    private ImageView mSearchClearImageView;
    private ImageView mSearchImageView;
    private String mTagName;
    private int mCategoryId;
    private String mCategoryName;

    private RecyclerView mProductRecyclerView;
    private ProductAdapter mProductAdapter;
    private ProgressBar mProductListProgressBar;

    private int mScreenWidth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTagName = getArguments().getString(ARG_TAG_NAME, null);
        mCategoryId = getArguments().getInt(ARG_CATEGORY_ID, -1);
        mCategoryName = getArguments().getString(ARG_CATEGORY_NAME, null);

        mProducts = new ArrayList<>();
        mUrlBuilder = new StringBuilder();

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mScreenWidth = ((AppBaseActivity) getActivity()).getScreenWidth();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        mSearchAutoCompleteTextView =
                (AutoCompleteTextView) view.findViewById(R.id.product_list_search_auto_text_view);
        if (mTagName != null) {
            mSearchAutoCompleteTextView.setText(mTagName);
        }
        if (mCategoryName != null) {
            mSearchAutoCompleteTextView.setHint(getString(R.string.category_name_hint_format, mCategoryName));
        }
        mSearchAutoCompleteTextView.setCursorVisible(true);
        mSearchAutoCompleteTextView.setSelection(mSearchAutoCompleteTextView.getText().length());
        mSearchAutoCompleteTextView.setThreshold(1);
        mTagCharacterAdapter = new TagCharacterAdapter(getActivity(), mSearchAutoCompleteTextView,
                android.R.layout.simple_dropdown_item_1line, 2, mCategoryId);
        mSearchAutoCompleteTextView.setAdapter(mTagCharacterAdapter);
        mSearchAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mSearchClearImageView.setVisibility(View.VISIBLE);
                }
                if (charSequence.length() == 0) {
                    mSearchClearImageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSearchAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (textView.getText().length() == 0) {
                        Toast.makeText(getActivity(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        updateTagCategoryProductList();
                    }
                }
                return false;
            }
        });
        mSearchAutoCompleteTextView.setOnClickListener(this);

        mProductListBackArrowImageView = (ImageView) view.findViewById(R.id.product_list_back_arrow_image_view);
        mProductListBackArrowImageView.setOnClickListener(this);
        mSearchClearImageView = (ImageView) view.findViewById(R.id.search_clear_image_view);
        mSearchClearImageView.setOnClickListener(this);
        mSearchImageView = (ImageView) view.findViewById(R.id.search_image_view);
        mSearchImageView.setOnClickListener(this);

        mSearchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateTagCategoryProductList();
            }
        });

        mProductRecyclerView = (RecyclerView) view.findViewById(R.id.product_recycler_view);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        SeparatorDecoration decoration =
//                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
//        mProductRecyclerView.addItemDecoration(decoration);
//        mProductRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
//            @Override
//            public void onShowView() {
//                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
//            }
//
//            @Override
//            public void onHideView() {
//                ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
//            }
//        });
        mProductRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = ((LinearLayoutManager) mProductRecyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                if (lastItem == mProductAdapter.getItemCount() - 1 && mPager != null
                        && mPager.getTotal() > mProductAdapter.getItemCount() - 2) {
                    // all product == > mTagName = ""
                    if (mTagName != null && mCategoryId == -1) {
                        requestTagProductList(mTagName);
                    } else if (mTagName == null && mCategoryId > 0) {
                        requestCategoryProductList(mCategoryId);
                    } else if (mTagName != null && mCategoryId > 0) {
                        requestTagCategoryProductList(mTagName, mCategoryId);
                    }
                }
            }
        });
//        mProductRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int lastItem = ((LinearLayoutManager) mProductRecyclerView.getLayoutManager())
//                        .findLastCompletelyVisibleItemPosition();
//                int itemSize = mProductRecyclerView.getAdapter().getItemCount();
//                if (lastItem == itemSize - 1) {
//                    ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
//                }
//                int firstItem = ((LinearLayoutManager) mProductRecyclerView.getLayoutManager())
//                        .findFirstCompletelyVisibleItemPosition();
//                if (firstItem == 0) {
//                    ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
//                }
//            }
//        });

        mProductListProgressBar = (ProgressBar) view.findViewById(R.id.product_list_progress_bar);

        updateUI();

        if (mTagName != null && mCategoryId == -1) {
            requestTagProductList(mTagName);
        } else if (mTagName == null && mCategoryId > 0) {
            requestCategoryProductList(mCategoryId);
        } else if (mTagName != null && mCategoryId > 0) {
            requestTagCategoryProductList(mTagName, mCategoryId);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_list_back_arrow_image_view:
                getActivity().onBackPressed();
                break;
            case R.id.product_list_search_auto_text_view:
                if (mTagName != null) {
                    getActivity().onBackPressed();
                } else if (mCategoryId > 0) {

                }
                break;
            case R.id.search_clear_image_view:
                mSearchAutoCompleteTextView.getText().clear();
                break;
            case R.id.search_image_view:
                if (mSearchAutoCompleteTextView.getText().length() == 0) {
                    Toast.makeText(getActivity(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), mSearchAutoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                    updateTagCategoryProductList();
                }
                break;
        }
    }

    private void updateUI() {
        if (mProductAdapter == null) {
            mProductAdapter = new ProductAdapter(mProducts);
            mProductRecyclerView.setAdapter(mProductAdapter);
        } else {
            mProductAdapter.setProducts(mProducts);
            mProductAdapter.notifyDataSetChanged();
        }
    }

    private void updateTagCategoryProductList() {

        mUpdateProductListListener.OnTagCategoryUpdated(mSearchAutoCompleteTextView.getText().toString(), mCategoryId, mCategoryName);
        mSearchAutoCompleteTextView.dismissDropDown();
        mInputMethodManager.hideSoftInputFromWindow(mSearchAutoCompleteTextView.getWindowToken(), 0);
        /* ignore dropdown */
        mSearchAutoCompleteTextView.setFocusable(false);
        mSearchAutoCompleteTextView.setFocusableInTouchMode(false);
        mSearchAutoCompleteTextView.setText(mTagName);
        mSearchAutoCompleteTextView.setFocusable(true);
        mSearchAutoCompleteTextView.setFocusableInTouchMode(true);
    }

    private void requestTagProductList(String query) {

        mProductListProgressBar.setVisibility(View.VISIBLE);

        if (mPager == null) {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH)
                    .append(QUERY_TAG).append(encodeUTF8(query));
        } else {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH).append(mPager.getNextQuery())
                    .append(QUERY_TAG).append(encodeUTF8(query));
        }

        Log.i(TAG, mUrlBuilder.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mUrlBuilder.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProductListProgressBar.setVisibility(View.GONE);
                        mProducts.addAll(Parser.parseProductList(response));
                        mPager = Parser.parseListPaginationQuery(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProductListProgressBar.setVisibility(View.GONE);
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

    private void requestCategoryProductList(int categoryId) {

        mProductListProgressBar.setVisibility(View.VISIBLE);

        if (mPager == null) {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH)
                    .append(QUERY_CATEGORY).append(categoryId);
        } else {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH).append(mPager.getNextQuery())
                    .append(QUERY_CATEGORY).append(categoryId);
        }

        Log.i(TAG, mUrlBuilder.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mUrlBuilder.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProductListProgressBar.setVisibility(View.GONE);
                        mProducts.addAll(Parser.parseProductList(response));
                        mPager = Parser.parseListPaginationQuery(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        mProductListProgressBar.setVisibility(View.GONE);
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

    private void requestTagCategoryProductList(String query, int categoryId) {

        mProductListProgressBar.setVisibility(View.VISIBLE);

        if (mPager == null) {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH)
                    .append(QUERY_TAG).append(encodeUTF8(query)).append(QUERY_CATEGORY).append(categoryId);
        } else {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH).append(mPager.getNextQuery())
                    .append(QUERY_TAG).append(encodeUTF8(query)).append(QUERY_CATEGORY).append(categoryId);
        }

        Log.i(TAG, mUrlBuilder.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mUrlBuilder.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProductListProgressBar.setVisibility(View.GONE);
                        mProducts.addAll(Parser.parseProductList(response));
                        mPager = Parser.parseListPaginationQuery(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProductListProgressBar.setVisibility(View.GONE);
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

    private class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {

        private ArrayList<Product> mProducts;

        public ProductAdapter(ArrayList<Product> products) {
            mProducts = products;
        }

        public void setProducts(ArrayList<Product> products) {
            mProducts = products;
        }

        @Override
        public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    view = layoutInflater.inflate(R.layout.list_item_product_header, parent, false);
                    return new HeaderHolder(view);
                case VIEW_TYPE_ITEM:
                    view = layoutInflater.inflate(R.layout.list_item_product, parent, false);
                    return new ProductHolder(view);
                case VIEW_TYPE_FOOTER:
                    view = layoutInflater.inflate(R.layout.list_item_product_footer, parent, false);
                    return new FooterHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderHolder) {
                ((HeaderHolder) holder).bindHeader();
            }
            if (holder instanceof ProductHolder) {
                Product product = mProducts.get(position - 1);
                ((ProductHolder) holder).bindProduct(product);
    //            setFadeAnimation(holder.itemView);
            }
            if (holder instanceof FooterHolder) {
                ((FooterHolder) holder).bindFooter();
//                setFadeAnimation(holder.itemView);
            }
        }

        @Override
        public int getItemCount() {
//            return mProducts.size() + 1;
            return mProducts.size() + 2; /* Header, Footer */
        }

        @Override
        public int getItemViewType(int position) {
//            if (position == 0) {
//                return VIEW_TYPE_HEADER;
//            }  else {
//                return VIEW_TYPE_ITEM;
//            }
            if (position == 0) {
                return VIEW_TYPE_HEADER;
            }
            if (((mProducts == null || mProducts.size() == 0) && position == 1)
                    || ((mProducts != null && mProducts.size() > 0) && position == mProducts.size() + 1)) {
                return VIEW_TYPE_FOOTER;
            } else {
                return VIEW_TYPE_ITEM;
            }
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(250);
            view.startAnimation(anim);
        }
    }

    private static final int VIEW_TYPE_HEADER = -1;
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    private class HeaderHolder extends RecyclerView.ViewHolder {

        private TextView mHeaderTotalTextView;
        private TextView mHeaderSortButtonTextView;

        public HeaderHolder(View itemView) {
            super(itemView);
            mHeaderTotalTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_header_total_text_view);
            mHeaderSortButtonTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_header_sort_button_text_view);
        }

        public void bindHeader() {
            if (mPager != null) {
                mHeaderTotalTextView.setText(highlightTotalText(mPager.getTotal()));
            }
        }
    }

    private class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Product mProduct;

        private ImageView mProductImageView;
        private TextView mProductBrandTextView;
        private TextView mProductNameTextView;
        private LinearLayout mProductReviewRatingLayout;
        private RatingBar mProductReviewRatingBar;
        private TextView mProductReviewRatingValueTextView;
        private TextView mProductReviewRatingCountTextView;

        ProductHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mProductImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_product_image_view);
            mProductBrandTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_brand_text_view);
            mProductNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_name_text_view);
            mProductReviewRatingLayout = (LinearLayout)
                    itemView.findViewById(R.id.list_item_product_review_rating_layout);
            mProductReviewRatingBar = (RatingBar)
                    itemView.findViewById(R.id.list_item_product_review_rating_bar);
            mProductReviewRatingValueTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_review_rating_value_text_view);
            mProductReviewRatingCountTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_review_rating_count_text_view);
        }

        void bindProduct(Product product) {
            mProduct = product;

            int thumbnailWidth = (int) (mScreenWidth * PRODUCT_LIST_ITEM_THUMBNAIL_SCREEN_WIDTH_RATIO);
            int thumbnailHeight = (int) (thumbnailWidth * PRODUCT_LIST_ITEM_THUMBNAIL_WIDTH_HEIGHT_RATIO);

            Glide.with(getActivity())
                    .load(mProduct.getImagePath())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .override(thumbnailWidth, thumbnailHeight)
                    .centerCrop()
                    .crossFade()
                    .into(mProductImageView);
            mProductBrandTextView.setText(String.valueOf(mProduct.getBrand()));
            mProductNameTextView.setText(String.valueOf(mProduct.getName()));

            mProductReviewRatingBar.setRating(mProduct.getRatingValue());
            mProductReviewRatingValueTextView.setText(String.valueOf(mProduct.getRatingValue()));
            mProductReviewRatingCountTextView.setText(getString(R.string.product_review_count, String.valueOf(mProduct.getRatingCount())));

            if (mProduct.getRatingCount() < 5) {
                mProductReviewRatingLayout.setVisibility(View.GONE);
            } else {
                mProductReviewRatingLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            startActivity(ProductActivity.newIntent(getActivity(), mProduct.getId()));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mEmptyImageView;
        private ImageView mScrollPromoteArrowImageView;
        private TextView mMessageTextView;
        private TextView mRequestButtonTextView;

        public FooterHolder(View itemView) {
            super(itemView);
            mEmptyImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_product_empty_image_image_view);
            mScrollPromoteArrowImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_product_scroll_promote_arrow_image_view);
            mMessageTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_footer_message_text_view);
            mRequestButtonTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_empty_request_button_text_view);
            mRequestButtonTextView.setOnClickListener(this);
        }

        public void bindFooter() {

            if (mPager == null) {
                if (mProductAdapter.getItemCount() == 2) {
                    mEmptyImageView.setVisibility(View.GONE);
                    mScrollPromoteArrowImageView.setVisibility(View.GONE);
                    mMessageTextView.setVisibility(View.GONE);
                }
            } else {
                if (mPager.getTotal() == 0 && mProductAdapter.getItemCount() == 2) {
                    mEmptyImageView.setVisibility(View.VISIBLE);
                    mScrollPromoteArrowImageView.setVisibility(View.GONE);
                    mMessageTextView.setVisibility(View.VISIBLE);
                    mMessageTextView.setText(
                            getString(R.string.product_list_result_empty_request_promote_message));
                    mRequestButtonTextView.setVisibility(View.VISIBLE);
                } else if (mPager.getTotal() + 2 > mProductAdapter.getItemCount()) {
                    mEmptyImageView.setVisibility(View.GONE);
                    mScrollPromoteArrowImageView.setVisibility(View.VISIBLE);
                    mMessageTextView.setVisibility(View.GONE);
                    mRequestButtonTextView.setVisibility(View.GONE);
                } else {
//                    mEmptyImageView.setVisibility(View.VISIBLE);
                    mEmptyImageView.setVisibility(View.GONE);
                    mScrollPromoteArrowImageView.setVisibility(View.GONE);
                    mMessageTextView.setVisibility(View.VISIBLE);
                    mMessageTextView.setText(
                            getString(R.string.product_list_result_empty_request_promote_message));
                    mRequestButtonTextView.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.list_item_product_empty_request_button_text_view:
                    startActivity(MemberActivity.newIntent(getActivity(), 6));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        }
    }

    private SpannableString highlightTotalText(int total) {
        SpannableString spannableString = new SpannableString(
                getString(R.string.product_total_count, String.valueOf(total)));
        int startIndex = 6;
        int endIndex;
        if (total >= 0 && total < 10) {
            endIndex = startIndex + 1;
        } else if (total >= 10 && total <= 99) {
            endIndex = startIndex + 2;
        } else {
            endIndex = startIndex + 3;
        }
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    OnUpdateProductListListener mUpdateProductListListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mUpdateProductListListener = (OnUpdateProductListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnUpdateProductListListener");
        }
    }
}
