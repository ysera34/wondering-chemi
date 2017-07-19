package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.category.CategoryPart;
import com.planet.wondering.chemi.model.category.CategoryPiece;
import com.planet.wondering.chemi.model.category.CategoryStorage;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;
import com.planet.wondering.chemi.view.activity.ProductListActivity;

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
    private int mCategoryPartSelectedId;
    private String mCategoryPieceHeaderName;
    private ArrayList<CategoryPart> mCategoryParts;
    private ArrayList<CategoryPiece> mCategoryPieces;

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
        mCategoryPieces = mCategoryParts.get(mCategoryPartSelectedId).getCategoryPieces();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_detail, container, false);
        mCategoryPartRecyclerView = (RecyclerView) view.findViewById(R.id.category_part_recycler_view);
        mCategoryPartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SeparatorDecoration decoration =
                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
        mCategoryPartRecyclerView.addItemDecoration(decoration);

        mCategoryPieceRecyclerView = (RecyclerView) view.findViewById(R.id.category_piece_recycler_view);
        mCategoryPieceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {
        if (isAdded()) {
            if (mCategoryPartAdapter == null) {
                mCategoryPartAdapter = new CategoryPartAdapter(mCategoryParts);
                mCategoryPartRecyclerView.setAdapter(mCategoryPartAdapter);

                mCategoryPieceAdapter = new CategoryPieceAdapter(mCategoryPieces);
                mCategoryPieceRecyclerView.setAdapter(mCategoryPieceAdapter);
            } else {
                mCategoryPartAdapter.notifyDataSetChanged();
            }
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
            if (mCategoryPartSelectedId == position) {
                mCategoryPieceHeaderName = mCategoryParts.get(position).getName();
            }
        }

        @Override
        public int getItemCount() {
            return mCategoryParts.size();
        }
    }

    private class CategoryPieceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<CategoryPiece> mCategoryPieces;

        public CategoryPieceAdapter(ArrayList<CategoryPiece> categoryPieces) {
            mCategoryPieces = categoryPieces;
        }

        public void setCategoryPieces(ArrayList<CategoryPiece> categoryPieces) {
            mCategoryPieces = categoryPieces;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            switch (viewType) {
                case VIEW_TYPE_PIECE_HEADER:
                    view = layoutInflater.inflate(R.layout.list_item_category_piece_header, parent, false);
                    return new CategoryPieceHeader(view);
                case VIEW_TYPE_PIECE_ITEM:
                    view = layoutInflater.inflate(R.layout.list_item_category_piece, parent, false);
                    return new CategoryPieceHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CategoryPieceHeader) {
                ((CategoryPieceHeader) holder).bindCategoryPieceHeader(mCategoryPieceHeaderName);
            }
            if (holder instanceof CategoryPieceHolder) {
                holder.setIsRecyclable(false);
                ((CategoryPieceHolder) holder).bindCategoryPiece(mCategoryPieces.get(position - 1));
            }
        }

        @Override
        public int getItemCount() {
            return mCategoryPieces.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return VIEW_TYPE_PIECE_HEADER;
            } else {
                return VIEW_TYPE_PIECE_ITEM;
            }
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
            mCategoryPartLayout.setOnClickListener(this);
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

            mCategoryPartSelectedId = getAdapterPosition();
            setSelectedLayout(mCategoryPartIconImageView.isSelected());
            mCategoryPartAdapter.notifyDataSetChanged();

            mCategoryPieceHeaderName = mCategoryPart.getName();
            if (mCategoryPartSelectedId > RecyclerView.NO_POSITION) {
                mCategoryPieces = mCategoryParts.get(mCategoryPartSelectedId).getCategoryPieces();
                mCategoryPieceAdapter.setCategoryPieces(mCategoryPieces);
                mCategoryPieceAdapter.notifyDataSetChanged();
            }
        }

        private void setSelectedLayout(boolean isSelected) {
            if (isSelected) {
                mCategoryPartIndicatorView.setBackgroundResource(R.color.colorPrimary);
                mCategoryPartLayout.setBackgroundResource(R.color.colorWhite);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mCategoryPartLayout.setElevation(1.0f);
//                }
                mCategoryPartNameTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                mCategoryPartIndicatorView.setBackgroundResource(android.R.color.transparent);
                mCategoryPartLayout.setBackgroundResource(R.color.colorWhiteBackground);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mCategoryPartLayout.setElevation(0.0f);
//                }
                mCategoryPartNameTextView.setTextColor(getResources().getColor(R.color.colorFontFrenchGray));
            }
        }
    }

    private static final int VIEW_TYPE_PIECE_HEADER = -1;
    private static final int VIEW_TYPE_PIECE_ITEM = 0;

    private class CategoryPieceHeader extends RecyclerView.ViewHolder {

        private String mPieceHeaderName;
        private TextView mPieceHeaderNameTextView;

        public CategoryPieceHeader(View itemView) {
            super(itemView);
            mPieceHeaderNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_category_piece_header_name_text_view);
        }

        public void bindCategoryPieceHeader(String pieceHeaderName) {
            mPieceHeaderName = pieceHeaderName;
            mPieceHeaderNameTextView.setText(String.valueOf(mPieceHeaderName));
        }
    }

    private class CategoryPieceHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private CategoryPiece mCategoryPiece;

        private LinearLayout mCategoryPieceLayout;
        private TextView mCategoryPieceNameTextView;

        public CategoryPieceHolder(View itemView) {
            super(itemView);
            mCategoryPieceLayout = (LinearLayout)
                    itemView.findViewById(R.id.list_item_category_piece_layout);
            mCategoryPieceNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_category_piece_name_text_view);
        }

        public void bindCategoryPiece(CategoryPiece categoryPiece) {
            mCategoryPiece = categoryPiece;
            if (mCategoryPiece.isHasClickListener()) {
                mCategoryPieceNameTextView.setOnClickListener(this);
            } else {
                mCategoryPieceNameTextView.setClickable(false);
                mCategoryPieceNameTextView.setTextColor(getResources().getColor(R.color.colorArmadillo));
            }
            if (mCategoryPiece.getName() != null) {
                mCategoryPieceNameTextView.setText(String.valueOf(mCategoryPiece.getName()));
            } else {
                mCategoryPieceNameTextView.setVisibility(View.GONE);
                final String categoryPieceName = mCategoryPieces.get(getAdapterPosition() - 2).getName();
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                for (int i = 0; i < mCategoryPiece.getCategoryPieces().size(); i++) {
                    View view = layoutInflater.inflate(R.layout.list_item_category_piece_extend, mCategoryPieceLayout, false);
                    final CategoryPiece categoryPieceExtend = mCategoryPiece.getCategoryPieces().get(i);
                    TextView pieceExtendTextView = (TextView) view.findViewById(R.id.list_item_category_piece_extend_name_text_view);
                    pieceExtendTextView.setText(String.valueOf(categoryPieceExtend.getName()));
                    pieceExtendTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(ProductListActivity.newIntent(getActivity(),
                                    categoryPieceExtend.getNumber(), categoryPieceName + " " + categoryPieceExtend.getName()));
                        }
                    });
                    mCategoryPieceLayout.addView(view);
                }
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.list_item_category_piece_name_text_view:
                    startActivity(ProductListActivity.newIntent(getActivity(),
                            mCategoryPiece.getNumber(), mCategoryPiece.getName()));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCategoryParts = null;
        mCategoryPieces = null;
    }
}
