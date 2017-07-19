package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.product.InfoChild;
import com.planet.wondering.chemi.model.product.InfoParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 2017. 7. 19..
 */

public class ProductInfoFragment extends Fragment {

    private static final String TAG = ProductInfoFragment.class.getSimpleName();

    private static final String ARG_PRODUCT = "product";

    public static ProductInfoFragment newInstance() {

        Bundle args = new Bundle();

        ProductInfoFragment fragment = new ProductInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductInfoFragment newInstance(Product product) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);

        ProductInfoFragment fragment = new ProductInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mProductInfoRecyclerView;
    private ProductInfoAdapter mProductInfoAdapter;
    private ArrayList<InfoParent> mInfoParents;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Product product = (Product) getArguments().getSerializable(ARG_PRODUCT);

        mInfoParents = new ArrayList<>();
        String[] infoChildGeneralTitleArr = {"크기", "형태", "구분", "특징"};
        for (int i = 0; i < 3; i++) {
            InfoParent infoParent = new InfoParent();
            switch (i) {
                case 0:
                    infoParent.setTitle("제품 정보");
                    if (product != null) {
                        for (int j = 0; j < 4; j++) {
                            InfoChild infoChild = new InfoChild();
                            infoChild.setTitle(infoChildGeneralTitleArr[j]);
                            infoChild.setDescription(product.getInfoStrings().get(j));
                            infoParent.getChildList().add(infoChild);
                        }
                    }
                    break;
                case 1:
                    infoParent.setTitle("재질 정보");
                    break;
                case 2:
                    infoParent.setTitle("성분 정보");
                    break;
            }
            mInfoParents.add(infoParent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);
        mProductInfoRecyclerView = (RecyclerView) view.findViewById(R.id.product_info_recycler_view);
        mProductInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProductInfoRecyclerView.setNestedScrollingEnabled(false);
        updateUI();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateUI() {
        if (mProductInfoAdapter == null) {
            mProductInfoAdapter = new ProductInfoAdapter(mInfoParents);
            mProductInfoRecyclerView.setAdapter(mProductInfoAdapter);
        } else {
            mProductInfoAdapter.setParentList(mInfoParents, true);
        }
    }

    private class ProductInfoAdapter extends ExpandableRecyclerAdapter<InfoParent, InfoChild, InfoParentHolder, ChildViewHolder> {

        private LayoutInflater mLayoutInflater;

        public ProductInfoAdapter(@NonNull List<InfoParent> parentList) {
            super(parentList);
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @NonNull
        @Override
        public InfoParentHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_product_info_parent, parentViewGroup, false);
            return new InfoParentHolder(view);
        }

        @NonNull
        @Override
        public ChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
            View view;
            switch (viewType) {
                default:
                case GENERAL_VIEW_TYPE:
                    view = mLayoutInflater.inflate(R.layout.list_item_product_info_child_general, childViewGroup, false);
                    return new InfoChildGeneralHolder(view);
                case GRID_VIEW_TYPE:
                    view = mLayoutInflater.inflate(R.layout.list_item_product_info_child_grid, childViewGroup, false);
                    return new InfoChildGridHolder(view);
                case QUOTATION_VIEW_TYPE:
                    view = mLayoutInflater.inflate(R.layout.list_item_product_info_child_quotation, childViewGroup, false);
                    return new InfoChildQuotationHolder(view);
            }
        }

        @Override
        public void onBindParentViewHolder(@NonNull InfoParentHolder parentViewHolder, int parentPosition, @NonNull InfoParent parent) {
            parentViewHolder.bindInfoParent(parent);
        }

        @Override
        public void onBindChildViewHolder(@NonNull ChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull InfoChild child) {
            if (childViewHolder instanceof InfoChildGeneralHolder) {
                ((InfoChildGeneralHolder) childViewHolder).bindInfoChild(child);
            }
            if (childViewHolder instanceof InfoChildGridHolder) {
                ((InfoChildGridHolder) childViewHolder).bindInfoChild(child);
            }
            if (childViewHolder instanceof InfoChildQuotationHolder) {
                ((InfoChildQuotationHolder) childViewHolder).bindInfoChild(child);
            }
        }

        @Override
        public int getChildViewType(int parentPosition, int childPosition) {
            switch (parentPosition) {
                case 0:
                    return GENERAL_VIEW_TYPE;
                case 1:
                    return GRID_VIEW_TYPE;
                case 2:
                    return QUOTATION_VIEW_TYPE;
            }
            return 0;
        }
    }

    private static final int GENERAL_VIEW_TYPE = 1;
    private static final int GRID_VIEW_TYPE = 2;
    private static final int QUOTATION_VIEW_TYPE = 3;


    private class InfoParentHolder extends ParentViewHolder {

        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180.0f;

        private InfoParent mInfoParent;

        private TextView mTitleTextView;
        private ImageView mArrowImageView;

        public InfoParentHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_product_info_parent_title_text_view);
            mArrowImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_product_info_parent_arrow_image_view);
        }

        public void bindInfoParent(InfoParent infoParent) {
            mInfoParent = infoParent;
            mTitleTextView.setText(String.valueOf(mInfoParent.getTitle()));
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);

//            final int scrollPositionY = (int) v.getY();
//            mProductInfoRecyclerView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mProductInfoRecyclerView.smoothScrollBy(0, scrollPositionY);
//                }
//            }, 300);

        }

        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (expanded) {
                mArrowImageView.setRotation(ROTATED_POSITION);
            } else {
                mArrowImageView.setRotation(INITIAL_POSITION);
            }
        }

        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);
            RotateAnimation rotateAnimation;
            if (expanded) {
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else {
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }
            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            mArrowImageView.startAnimation(rotateAnimation);
        }
    }

    private class InfoChildHolder extends ChildViewHolder {

        private InfoChild mInfoChild;

        public InfoChildHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindInfoChild(InfoChild infoChild) {
            mInfoChild = infoChild;
        }
    }

    private class InfoChildGeneralHolder extends ChildViewHolder {

        private InfoChild mInfoChild;

        private TextView mTitleTextView;
        private TextView mDescriptionTextView;

        public InfoChildGeneralHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView
                    .findViewById(R.id.list_item_product_info_child_general_title_text_view);
            mDescriptionTextView = (TextView) itemView
                    .findViewById(R.id.list_item_product_info_child_general_description_text_view);
        }

        public void bindInfoChild(InfoChild infoChild) {
            mInfoChild = infoChild;
            mTitleTextView.setText(String.valueOf(mInfoChild.getTitle()));
            mDescriptionTextView.setText(String.valueOf(mInfoChild.getDescription()));
        }
    }

    private class InfoChildGridHolder extends ChildViewHolder {

        private InfoChild mInfoChild;

        public InfoChildGridHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindInfoChild(InfoChild infoChild) {
            mInfoChild = infoChild;
        }
    }

    private class InfoChildQuotationHolder extends ChildViewHolder {

        private InfoChild mInfoChild;

        public InfoChildQuotationHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindInfoChild(InfoChild infoChild) {
            mInfoChild = infoChild;
        }
    }


}
