package com.planet.wondering.chemi.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.activity.ProductListActivity;

import java.util.ArrayList;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class CategoryFragment extends Fragment {

    public static final String TAG = CategoryFragment.class.getSimpleName();

    public static CategoryFragment newInstance() {

        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<CategoryItem> mCategoryItems;
    private CategoryAdapter mCategoryAdapter;
    private RecyclerView mCategoryRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryItems = new ArrayList<>();
        mCategoryItems = getCategoryItems();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        mCategoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        mCategoryAdapter = new CategoryAdapter(mCategoryItems);
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private ArrayList<CategoryItem> mCategoryItems;

        public CategoryAdapter(ArrayList<CategoryItem> categoryItems) {
            mCategoryItems = categoryItems;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_category, parent, false);
            return new CategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position) {
            holder.bindCategory(mCategoryItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mCategoryItems.size();
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private CategoryItem mCategoryItem;

        private ImageView mCategoryImageImageView;
        private TextView mCategoryNameTextView;

        public CategoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mCategoryImageImageView = (ImageView) itemView.findViewById(R.id.list_item_category_image_image_view);
            mCategoryNameTextView = (TextView) itemView.findViewById(R.id.list_item_category_name_text_view);
        }

        public void bindCategory(CategoryItem categoryItem) {
            mCategoryItem = categoryItem;

            mCategoryImageImageView.setImageResource(mCategoryItem.getCategoryImageResId());
            mCategoryNameTextView.setText(getString(mCategoryItem.getCategoryNameResId()));
        }

        @Override
        public void onClick(View v) {
            startActivity(ProductListActivity.newIntent(getActivity(), mCategoryItem.getCategoryId()));
        }
    }

    private class CategoryItem {

        private int mCategoryId;
        private int mCategoryImageResId;
        private int mCategoryNameResId;

        public CategoryItem(int categoryId, int categoryImageResId, int categoryNameResId) {
            mCategoryId = categoryId;
            mCategoryImageResId = categoryImageResId;
            mCategoryNameResId = categoryNameResId;
        }

        public int getCategoryId() {
            return mCategoryId;
        }

        public void setCategoryId(int categoryId) {
            mCategoryId = categoryId;
        }

        public int getCategoryImageResId() {
            return mCategoryImageResId;
        }

        public void setCategoryImageResId(int categoryImageResId) {
            mCategoryImageResId = categoryImageResId;
        }

        public int getCategoryNameResId() {
            return mCategoryNameResId;
        }

        public void setCategoryNameResId(int categoryNameResId) {
            mCategoryNameResId = categoryNameResId;
        }
    }

    public ArrayList<CategoryItem> getCategoryItems() {

        int[] categoryIds = new int[]{1, 2, 4, 5, 3, 6,};

        int[] categoryImageResIds = new int[]{
                R.drawable.ic_category_baby_wet_tissue, R.drawable.ic_category_baby_hair_wash,
                R.drawable.ic_category_baby_lotion, R.drawable.ic_category_baby_tooth_paste,
                R.drawable.ic_category_baby_body_wash, R.drawable.ic_category_adult_etc,};
        int[] categoryNameResIds = new int[]{
                R.string.category_name_baby_wet_tissue, R.string.category_name_baby_hair,
                R.string.category_name_baby_lotion, R.string.category_name_baby_tooth_paste,
                R.string.category_name_baby_body_wash, R.string.category_name_etc,};

        for (int i = 0; i < 6; i++) {
            mCategoryItems.add(new CategoryItem(categoryIds[i], categoryImageResIds[i], categoryNameResIds[i]));
        }
        return mCategoryItems;
    }
}
