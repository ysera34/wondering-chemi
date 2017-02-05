package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.storage.ProductStorage;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;
import com.planet.wondering.chemi.util.listener.OnScrollListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.ProductActivity;

import java.util.ArrayList;

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

    private ProductStorage mProductStorage;
    private ArrayList<Product> mProducts;
    private ArrayList<Integer> mProductIds;
    private Product mProduct;

    private AutoCompleteTextView mSearchAutoCompleteTextView;
    private String mTagName;
    private TextView mProductTotalTextView;
    private Button mProductSortButton;

    private RecyclerView mProductRecyclerView;
    private ProductAdapter mProductAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTagName = getArguments().getString(ARG_TAG_NAME);

        mProductStorage = ProductStorage.getStorage(getActivity());
        mProducts = mProductStorage.getProducts();
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
        mProductRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onShowView() {
                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
            }

            @Override
            public void onHideView() {
                ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
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
        if (mProductAdapter == null) {
            mProductAdapter = new ProductAdapter(mProducts);
            mProductRecyclerView.setAdapter(mProductAdapter);
        } else {
            mProductAdapter.notifyDataSetChanged();
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {

        private ArrayList<Product> mProducts;

        ProductAdapter(ArrayList<Product> products) {
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
            setFadeAnimation(holder.itemView);
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

            mProductBrandTextView.setText(String.valueOf(mProduct.getBrand()));
            mProductNameTextView.setText(String.valueOf(mProduct.getName()));
            mProductReviewRatingBar.setRating(mProduct.getRatingValue());
            mProductReviewRatingValueTextView.setText(String.valueOf(mProduct.getRatingValue()));
            mProductReviewRatingCountTextView.setText(String.valueOf(mProduct.getRatingCount()));
        }

        @Override
        public void onClick(View view) {
            startActivity(ProductActivity.newIntent(getActivity(), mProduct.getId()));
        }
    }

    private void highlightTextPart(TextView textView, int index, String regularExpression) {
        String fullText = textView.getText().toString();
        int startPos = 0;
        int endPos = fullText.length();
        String[] textParts = fullText.split(regularExpression);
        if (index < 0 || index > textParts.length - 1) {
            return;
        }
        if (textParts.length > 1) {
            startPos = fullText.indexOf(textParts[index]);
            endPos = fullText.indexOf(regularExpression, startPos);
            if (endPos == -1) {
                endPos = fullText.length();
            }
        }
        Spannable spannable = new SpannableString(fullText);
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(
                getActivity(), 0, getResources().getColor(R.color.colorPrimary));
        spannable.setSpan(textAppearanceSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);

        /*
        int index = 3;
        String regularExpression = " ";
        String text = "Hello StackOverflow From BNK!";
        TextView textView = (TextView) findViewById(R.id.textView);
        if (textView != null) {
            textView.setText(text);
            highlightTextPart(textView, index, regularExpression);
        }
         */

    }
}
