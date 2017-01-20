package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.custom.HexagonFilterLayout;

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

    private HexagonFilterLayout[] mHexagonFilterLayouts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrangeHexagonFilterLayouts(0);
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
}
