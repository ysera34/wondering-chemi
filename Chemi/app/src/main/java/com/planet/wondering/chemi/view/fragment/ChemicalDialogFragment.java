package com.planet.wondering.chemi.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.model.storage.ChemicalStorage;

/**
 * Created by yoon on 2017. 1. 22..
 */

public class ChemicalDialogFragment extends DialogFragment {

    private static final String TAG = ChemicalDialogFragment.class.getSimpleName();

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_CHEMICAL_ID = "chemical_id";

    public static ChemicalDialogFragment newInstance() {

        Bundle args = new Bundle();

        ChemicalDialogFragment fragment = new ChemicalDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChemicalDialogFragment newInstance(int chemicalId) {

        Bundle args = new Bundle();
        args.putInt(ARG_CHEMICAL_ID, chemicalId);

        ChemicalDialogFragment fragment = new ChemicalDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChemicalDialogFragment newInstance(int productId, int chemicalId) {

        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);
        args.putInt(ARG_CHEMICAL_ID, chemicalId);

        ChemicalDialogFragment fragment = new ChemicalDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mProductId;
    private int mChemicalId;
    private Chemical mChemical;

    private AlertDialog.Builder mBuilder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getArguments().getInt(ARG_PRODUCT_ID, -1);
        mChemicalId = getArguments().getInt(ARG_CHEMICAL_ID, -1);
        mChemical = ChemicalStorage.getStorage(getActivity()).getChemical(mChemicalId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_chemical_dialog, null);

        mBuilder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setIcon(R.drawable.widget_solid_circle_hazard1)
                .setTitle(mChemical.getNameKo())
                .setPositiveButton("positive", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("negative", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("neutral", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return mBuilder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ChemicalDialogAnimation;
    }
}
