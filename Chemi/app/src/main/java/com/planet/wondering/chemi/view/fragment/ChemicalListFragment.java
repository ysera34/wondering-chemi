package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 1. 19..
 */

public class ChemicalListFragment extends Fragment {

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chemical_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
