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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.category.CategoryPart;
import com.planet.wondering.chemi.model.category.CategoryStorage;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 7. 1..
 */

public class CategoryDetailFragment extends Fragment {

    private static final String TAG = CategoryDetailFragment.class.getSimpleName();

    private static final String ARG_CATEGORY_GROUP_ID = "category_group_id";

    public static CategoryDetailFragment newInstance() {

        Bundle args = new Bundle();

        CategoryDetailFragment fragment = new CategoryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CategoryDetailFragment newInstance(int categoryGroupId) {

        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_GROUP_ID, categoryGroupId);

        CategoryDetailFragment fragment = new CategoryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mCategoryGroupId;
    private ArrayList<CategoryPart> mCategoryParts;
    private int mCategoryPartSelectedId;

    private RecyclerView mCategoryPartRecyclerView;
    private RecyclerView mCategoryPieceRecyclerView;
    private CategoryPartAdapter mCategoryPartAdapter;
    private CategoryPieceAdapter mCategoryPieceAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryGroupId = getArguments().getInt(ARG_CATEGORY_GROUP_ID, -1);

        CategoryStorage categoryStorage = CategoryStorage.getCategoryStorage(getActivity());
        mCategoryParts = categoryStorage.getCategoryGroup(mCategoryGroupId).getCategoryParts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_detail, container, false);
        mCategoryPartRecyclerView = (RecyclerView) view.findViewById(R.id.category_part_recycler_view);
        mCategoryPartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        SeparatorDecoration decoration =
//                new SeparatorDecoration(getActivity(), android.R.color.black, 0.7f);
//        mCategoryPartRecyclerView.addItemDecoration(decoration);

        updateUI();
        return view;
    }

    private void updateUI() {
        if (mCategoryPartAdapter == null) {
            mCategoryPartAdapter = new CategoryPartAdapter(mCategoryParts);
            mCategoryPartRecyclerView.setAdapter(mCategoryPartAdapter);
        } else {
            mCategoryPartAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class CategoryPartAdapter extends RecyclerView.Adapter<CategoryPartHolder> {

        private ArrayList<CategoryPart> mCategoryParts;

        public CategoryPartAdapter(ArrayList<CategoryPart> categoryParts) {
            mCategoryParts = categoryParts;
        }

        @Override
        public CategoryPartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_category_part, parent, false);
            return new CategoryPartHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryPartHolder holder, int position) {
            holder.getCategoryPartIconImageView().setSelected(mCategoryPartSelectedId == position);
            holder.bindCategoryPart(mCategoryParts.get(position));
        }

        @Override
        public int getItemCount() {
            return mCategoryParts.size();
        }
    }

    private class CategoryPieceAdapter extends RecyclerView.Adapter<CategoryPieceHolder> {

        @Override
        public CategoryPieceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(CategoryPieceHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class CategoryPartHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private CategoryPart mCategoryPart;
        private View mCategoryPartIndicatorView;
        private LinearLayout mCategoryPartLayout;
        private ImageView mCategoryPartIconImageView;
        private TextView mCategoryPartNameTextView;

        public CategoryPartHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCategoryPartIndicatorView = (View)
                    itemView.findViewById(R.id.list_item_category_part_indicator_view);
            mCategoryPartLayout = (LinearLayout)
                    itemView.findViewById(R.id.list_item_category_part_layout);
            mCategoryPartIconImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_category_part_icon_image_view);
            mCategoryPartNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_category_part_name_text_view);
        }

        public ImageView getCategoryPartIconImageView() {
            return mCategoryPartIconImageView;
        }

        public void bindCategoryPart(CategoryPart categoryPart) {
            mCategoryPart = categoryPart;
            mCategoryPartIconImageView.setImageResource(mCategoryPart.getImageResId());
            mCategoryPartNameTextView.setText(String.valueOf(mCategoryPart.getName()));
            setSelectedLayout(mCategoryPartIconImageView.isSelected());
        }

        @Override
        public void onClick(View v) {

            long start0 = System.nanoTime();
            mCategoryPartAdapter.notifyItemChanged(mCategoryPartSelectedId);
            long start1 = System.nanoTime();
            mCategoryPartSelectedId = getAdapterPosition();
            long start2 = System.nanoTime();
            mCategoryPartAdapter.notifyItemChanged(mCategoryPartSelectedId);
            long start3 = System.nanoTime();
            setSelectedLayout(mCategoryPartIconImageView.isSelected());
            long start4 = System.nanoTime();

            Log.i(TAG, "start 0~1 : " + String.valueOf((start1 - start0) / 1000));
            Log.i(TAG, "start 1~2 : " + String.valueOf((start2 - start1) / 1000));
            Log.i(TAG, "start 2~3 : " + String.valueOf((start3 - start2) / 1000));
            Log.i(TAG, "start 3~4 : " + String.valueOf((start4 - start3) / 1000));
        }

        private void setSelectedLayout(boolean isSelected) {
            if (isSelected) {
                mCategoryPartIndicatorView.setBackgroundResource(R.color.colorPrimary);
                mCategoryPartLayout.setBackgroundResource(android.R.color.transparent);
                mCategoryPartNameTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                mCategoryPartIndicatorView.setBackgroundResource(android.R.color.transparent);
                mCategoryPartLayout.setBackgroundResource(R.color.colorWhiteBackground);
                mCategoryPartNameTextView.setTextColor(getResources().getColor(R.color.colorFontFrenchGray));

            }
        }
    }

    private class CategoryPieceHolder extends RecyclerView.ViewHolder {

        public CategoryPieceHolder(View itemView) {
            super(itemView);
        }
    }
}
