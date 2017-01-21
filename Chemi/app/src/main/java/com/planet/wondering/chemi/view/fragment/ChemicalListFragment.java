package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.storage.ProductStorage;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;
import com.planet.wondering.chemi.util.listener.OnScrollListener;
import com.planet.wondering.chemi.view.activity.ProductActivity;
import com.planet.wondering.chemi.view.custom.CircleHazardView;
import com.planet.wondering.chemi.view.custom.HexagonFilterLayout;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 19..
 */

public class ChemicalListFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ChemicalListFragment.class.getSimpleName();

    private static final String ARG_PRODUCT_ID = "product_id";

    public static ChemicalListFragment newInstance() {

        Bundle args = new Bundle();

        ChemicalListFragment fragment = new ChemicalListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChemicalListFragment newInstance(int productId) {

        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);

        ChemicalListFragment fragment = new ChemicalListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mProductId;
    private Product mProduct;
    private ArrayList<Chemical> mChemicals;

    private HexagonFilterLayout[] mHexagonFilterLayouts;
    private RecyclerView mChemicalRecyclerView;
    private ChemicalAdapter mChemicalAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getArguments().getInt(ARG_PRODUCT_ID, 0);
        mProduct = ProductStorage.getStorage(getActivity()).getProduct(mProductId);

        mChemicals = mProduct.getChemicals();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chemical_list, container, false);
        mHexagonFilterLayouts = new HexagonFilterLayout[5];

        int[] filterLayoutIds = new int[]{
                R.id.hexagon1, R.id.hexagon2, R.id.hexagon3, R.id.hexagon4, R.id.hexagon5,
        };

        for (int i = 0; i < mHexagonFilterLayouts.length; i++) {
            mHexagonFilterLayouts[i] = (HexagonFilterLayout) view.findViewById(filterLayoutIds[i]);
            mHexagonFilterLayouts[i].setOnClickListener(this);
        }

        mChemicalRecyclerView = (RecyclerView) view.findViewById(R.id.chemical_recycler_view);
        mChemicalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SeparatorDecoration decoration =
                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
        mChemicalRecyclerView.addItemDecoration(decoration);
        mChemicalRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onShowView() {
                ((ProductActivity) getActivity()).showBottomNavigationView();
            }

            @Override
            public void onHideView() {
                ((ProductActivity) getActivity()).hideBottomNavigationView();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrangeHexagonFilterLayouts(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (mChemicalAdapter == null) {
            mChemicalAdapter = new ChemicalAdapter(mChemicals);
            mChemicalRecyclerView.setAdapter(mChemicalAdapter);
        } else {
            mChemicalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hexagon1:
                arrangeHexagonFilterLayouts(0);
                break;
            case R.id.hexagon2:
                arrangeHexagonFilterLayouts(1);
                break;
            case R.id.hexagon3:
                arrangeHexagonFilterLayouts(2);
                break;
            case R.id.hexagon4:
                arrangeHexagonFilterLayouts(3);
                break;
            case R.id.hexagon5:
                arrangeHexagonFilterLayouts(4);
                break;
        }
    }

    private void arrangeHexagonFilterLayouts(int index) {

        for (int i = 0; i < mHexagonFilterLayouts.length; i++) {
            if (i == index) {
                mHexagonFilterLayouts[i].scaleUpAnimate();
            } else {
                mHexagonFilterLayouts[i].scaleDownAnimate();
            }
        }
    }

    private class ChemicalAdapter extends RecyclerView.Adapter<ChemicalHolder> {

        private ArrayList<Chemical> mChemicals;

        ChemicalAdapter(ArrayList<Chemical> chemicals) {
            mChemicals = chemicals;
        }

        @Override
        public ChemicalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_chemical, parent, false);
            return new ChemicalHolder(view);
        }

        @Override
        public void onBindViewHolder(ChemicalHolder holder, int position) {
            Chemical chemical = mChemicals.get(position);
            holder.bindChemical(chemical);
        }

        @Override
        public int getItemCount() {
            return mChemicals.size();
        }
    }

    private class ChemicalHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Chemical mChemical;

        private TextView mChemicalNameKoTextView;
        private TextView mChemicalNameEngTextView;
        private CircleHazardView mChemicalCircleHazardView;

        ChemicalHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mChemicalNameKoTextView = (TextView)
                    itemView.findViewById(R.id.list_item_chemical_name_ko_text_view);
            mChemicalNameEngTextView = (TextView)
                    itemView.findViewById(R.id.list_item_chemical_name_eng_text_view);
            mChemicalCircleHazardView = (CircleHazardView)
                    itemView.findViewById(R.id.list_item_chemical_circle_hazard_view);
        }

        void bindChemical(Chemical chemical) {
            mChemical = chemical;

            mChemicalNameKoTextView.setText(String.valueOf(mChemical.getNameKo()));
            mChemicalNameEngTextView.setText(String.valueOf(mChemical.getNameEn()));
            mChemicalCircleHazardView.setCircleColor(mChemical.getHazardColorResId());
            mChemicalCircleHazardView.setHazardValueText(mChemical.getHazardValueString());
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), String.valueOf(mChemical.getNameKo()), Toast.LENGTH_SHORT).show();
        }
    }
}
