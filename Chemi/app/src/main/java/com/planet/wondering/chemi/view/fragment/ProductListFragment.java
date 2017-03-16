package com.planet.wondering.chemi.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Pager;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;
import com.planet.wondering.chemi.util.listener.OnRecyclerViewScrollListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.ProductActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.planet.wondering.chemi.network.Config.Product.QUERY_CATEGORY;
import static com.planet.wondering.chemi.network.Config.Product.QUERY_PATH;
import static com.planet.wondering.chemi.network.Config.Product.QUERY_TAG;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.encodeUTF8;

/**
 * Created by yoon on 2017. 1. 18..
 */

public class ProductListFragment extends Fragment {

    private static final String TAG = ProductListFragment.class.getSimpleName();

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_PRODUCT_IDS = "product_ids";
    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_TAG_NAME = "tag_name";

    public static ProductListFragment newInstance() {

        Bundle args = new Bundle();

        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductListFragment newInstance(int productId) {

        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);

        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductListFragment newInstance(ArrayList<Integer> productIds) {

        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PRODUCT_IDS, productIds);

        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductListFragment newInstance(byte categoryId) {

        Bundle args = new Bundle();
        args.putByte(ARG_CATEGORY_ID, categoryId);

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

    private StringBuilder mUrlBuilder;
    private ArrayList<Product> mProducts;
    private ArrayList<Integer> mProductIds;
//    private Product mProduct;
    private Pager mPager;

    private AutoCompleteTextView mSearchAutoCompleteTextView;
    private String mTagName;
    private byte mCategoryId;
    private TextView mProductTotalTextView;
    private Button mProductSortButton;

    private RecyclerView mProductRecyclerView;
    private ProductAdapter mProductAdapter;
    private ProgressBar mProductListProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTagName = getArguments().getString(ARG_TAG_NAME, null);
        mCategoryId = getArguments().getByte(ARG_CATEGORY_ID, (byte) -1);

        mProducts = new ArrayList<>();
        mProductIds = new ArrayList<>();
        mUrlBuilder = new StringBuilder();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        mSearchAutoCompleteTextView =
                (AutoCompleteTextView) view.findViewById(R.id.product_list_search_auto_text_view);
        mSearchAutoCompleteTextView.setText(mTagName);
        mSearchAutoCompleteTextView.setEnabled(false);

        mProductTotalTextView = (TextView) view.findViewById(R.id.product_total_text_view);
        mProductSortButton = (Button) view.findViewById(R.id.product_sort_button);
        mProductRecyclerView = (RecyclerView) view.findViewById(R.id.product_recycler_view);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SeparatorDecoration decoration =
                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
        mProductRecyclerView.addItemDecoration(decoration);
        mProductRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onShowView() {
                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
            }

            @Override
            public void onHideView() {
                ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
            }
        });
        mProductRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = ((LinearLayoutManager) mProductRecyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                if (lastItem == mProductAdapter.getItemCount() - 1
                        && mPager.getTotal() > mProductAdapter.getItemCount()) {
                    // all product == > mTagName = ""
                    if (mTagName != null) {
                        requestTagProductList(mTagName);
                    } else if (mCategoryId > 0) {
                        requestCategoryProductList(mCategoryId);
                    }
                }
            }
        });
        mProductListProgressBar = (ProgressBar) view.findViewById(R.id.product_list_progress_bar);

        updateUI();

        if (mTagName != null) {
            requestTagProductList(mTagName);
        } else if (mCategoryId > 0) {
            requestCategoryProductList(mCategoryId);
        }
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
    }

    private void updateUI() {
        if (mProductAdapter == null) {
            mProductAdapter = new ProductAdapter(mProducts);
            mProductRecyclerView.setAdapter(mProductAdapter);
        } else {
            mProductAdapter.setProducts(mProducts);
            mProductAdapter.notifyDataSetChanged();
            mProductTotalTextView.setText(highlightTotalText());
            mProductIds.clear();
            for (Product product : mProducts) {
                mProductIds.add(product.getId());
            }
        }
    }

    private void requestTagProductList(String query) {

        final ProgressDialog progressDialog;
        mProductListProgressBar.setVisibility(View.VISIBLE);

        if (mPager == null) {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH)
                    .append(QUERY_TAG).append(encodeUTF8(query));
//            progressDialog = ProgressDialog.show(getActivity(), getString(R.string.progress_dialog_title_product_list),
//                    getString(R.string.progress_dialog_message_wait), false, false);
        } else {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH).append(mPager.getNextQuery())
                    .append(QUERY_TAG).append(encodeUTF8(query));
//            progressDialog = ProgressDialog.show(getActivity(), getString(R.string.progress_dialog_title_product_list_next),
//                    getString(R.string.progress_dialog_message_wait), false, false);
        }

        Log.i(TAG, mUrlBuilder.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mUrlBuilder.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
                        mProductListProgressBar.setVisibility(View.GONE);
                        mProducts.addAll(Parser.parseProductList(response));
                        mPager = Parser.parseProductListPagingQuery(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        mProductListProgressBar.setVisibility(View.GONE);
                        Log.e(TAG, error.getMessage());
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

    private void requestCategoryProductList(byte categoryId) {

        mProductListProgressBar.setVisibility(View.VISIBLE);

        if (mPager == null) {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH)
                    .append(QUERY_CATEGORY).append(categoryId);
//            progressDialog = ProgressDialog.show(getActivity(), getString(R.string.progress_dialog_title_product_list),
//                    getString(R.string.progress_dialog_message_wait), false, false);
        } else {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH).append(mPager.getNextQuery())
                    .append(QUERY_CATEGORY).append(categoryId);
//            progressDialog = ProgressDialog.show(getActivity(), getString(R.string.progress_dialog_title_product_list_next),
//                    getString(R.string.progress_dialog_message_wait), false, false);
        }

        Log.i(TAG, mUrlBuilder.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mUrlBuilder.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
                        mProductListProgressBar.setVisibility(View.GONE);
                        mProducts.addAll(Parser.parseProductList(response));
                        mPager = Parser.parseProductListPagingQuery(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        mProductListProgressBar.setVisibility(View.GONE);
                        Log.e(TAG, error.getMessage());
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

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {

        private ArrayList<Product> mProducts;

        public ProductAdapter(ArrayList<Product> products) {
            mProducts = products;
        }

        public void setProducts(ArrayList<Product> products) {
            mProducts = products;
        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_product, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductHolder holder, int position) {
            Product product = mProducts.get(position);
            holder.bindProduct(product);
//            setFadeAnimation(holder.itemView);
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(250);
            view.startAnimation(anim);
        }
    }

    private class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Product mProduct;

        private ImageView mProductImageView;
        private TextView mProductBrandTextView;
        private TextView mProductNameTextView;
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
            mProductReviewRatingBar = (RatingBar)
                    itemView.findViewById(R.id.list_item_product_review_rating_bar);
            mProductReviewRatingValueTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_review_rating_value_text_view);
            mProductReviewRatingCountTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_review_rating_count_text_view);
        }

        void bindProduct(Product product) {
            mProduct = product;

            Glide.with(getActivity())
                    .load(mProduct.getImagePath())
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                    .crossFade()
                    .override(300, 200)
                    .into(mProductImageView);
            mProductBrandTextView.setText(String.valueOf(mProduct.getBrand()));
            mProductNameTextView.setText(String.valueOf(mProduct.getName()));
            mProductReviewRatingBar.setRating(mProduct.getRatingValue());
            mProductReviewRatingValueTextView.setText(String.valueOf(mProduct.getRatingValue()));
            mProductReviewRatingCountTextView.setText(getString(R.string.product_review_count, String.valueOf(mProduct.getRatingCount())));
        }

        @Override
        public void onClick(View view) {
//            startActivity(ProductActivity.newIntent(getActivity(), mProduct.getId()));
            if (mTagName != null) {
                startActivity(ProductActivity.newIntent(getActivity(), mProduct.getId(), (byte) 0));
            } else if (mCategoryId > 0) {
                startActivity(ProductActivity.newIntent(getActivity(), mProduct.getId(), (byte) 1));
            }
//            startActivity(ProductPagerActivity.newIntent(getActivity(), mProductIds, mProduct.getId()));
        }
    }

    private SpannableString highlightTotalText() {
        SpannableString spannableString = new SpannableString(
                getString(R.string.product_total_count, String.valueOf(mProducts.size())));
        int startIndex = 6;
        int endIndex;
        if (mProducts.size() >= 0 && mProducts.size() < 10) {
            endIndex = startIndex + 1;
        } else if (mProducts.size() >= 10 && mProducts.size() <= 99) {
            endIndex = startIndex + 2;
        } else {
            endIndex = startIndex + 3;
        }
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}
