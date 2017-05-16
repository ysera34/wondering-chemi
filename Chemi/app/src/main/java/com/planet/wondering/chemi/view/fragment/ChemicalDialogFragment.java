package com.planet.wondering.chemi.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.model.Hazard;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 22..
 */

public class ChemicalDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = ChemicalDialogFragment.class.getSimpleName();

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_CHEMICAL_ID = "chemical_id";
    private static final String ARG_CHEMICAL = "chemical";

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

    public static ChemicalDialogFragment newInstance(Chemical chemical) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CHEMICAL, chemical);

        ChemicalDialogFragment fragment = new ChemicalDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mProductId;
    private int mChemicalId;
    private Chemical mChemical;

    private AlertDialog.Builder mBuilder;

//    private CircleHazardView mChemicalDialogCircleHazardView;
    private TextView mChemicalDialogCircleTextView;
    private TextView mChemicalDialogReasonTextView;
    private TextView mChemicalDialogNameKoTextView;
    private TextView mChemicalDialogNameEngTextView;
    private TextView mChemicalDialogHazardLineTextView;
    private TextView mChemicalDialogPurposeTextView;
    private ImageView mChemicalDialogAllergyImageView;
    private TextView mChemicalDialogAllergyTextView;
    private RecyclerView mChemicalDialogHazardInfoRecyclerView;
    private HazardAdapter mHazardAdapter;
    private ArrayList<Hazard> mHazards;

    private TextView mChemicalDialogCloseButtonTextView;
    private TextView mChemicalDialogUpdateRequestButtonTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getArguments().getInt(ARG_PRODUCT_ID, -1);
        mChemicalId = getArguments().getInt(ARG_CHEMICAL_ID, -1);
        mChemical = (Chemical) getArguments().getSerializable(ARG_CHEMICAL);
        mHazards = mChemical.getHazards();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_chemical_dialog, null);

        mChemicalDialogCircleTextView = (TextView)
                view.findViewById(R.id.chemical_dialog_circle_text_view);
        mChemicalDialogCircleTextView.setText(mChemical.getHazardValueString());
        mChemicalDialogCircleTextView.setBackgroundResource(mChemical.getHazardIconResId());
//        mChemicalDialogCircleHazardView = (CircleHazardView)
//                view.findViewById(R.id.chemical_dialog_circle_hazard_view);
//        mChemicalDialogCircleHazardView.setCircleColor(mChemical.getHazardColorResId());
//        mChemicalDialogCircleHazardView.setHazardValueText(mChemical.getHazardValueString());
        mChemicalDialogReasonTextView = (TextView)
                view.findViewById(R.id.chemical_dialog_reason_text_view);
        mChemicalDialogReasonTextView.setText(getString(mChemical.getHazardReasonStringResId()));

        mChemicalDialogNameKoTextView = (TextView)
                view.findViewById(R.id.chemical_dialog_name_ko_text_view);
        mChemicalDialogNameKoTextView.setSelected(true);
        mChemicalDialogNameKoTextView.setText(mChemical.getNameKo());
        mChemicalDialogNameEngTextView = (TextView)
                view.findViewById(R.id.chemical_dialog_name_eng_text_view);
        mChemicalDialogNameEngTextView.setSelected(true);
        mChemicalDialogNameEngTextView.setText(getString(R.string.chemical_dialog_name_eng_format, mChemical.getNameEn()));
        mChemicalDialogHazardLineTextView = (TextView)
                view.findViewById(R.id.chemical_dialog_hazard_bg_text_view);
        mChemicalDialogHazardLineTextView.setBackgroundColor(getResources().getColor(mChemical.getHazardColorResId()));
        mChemicalDialogPurposeTextView = (TextView)
                view.findViewById(R.id.chemical_dialog_purpose_text_view);
        if (mChemical.getPurpose().equals("null")) {
            mChemicalDialogPurposeTextView.setVisibility(View.GONE);
        } else {
            mChemicalDialogPurposeTextView.setText(mChemical.getPurpose());
        }
//        mChemicalDialogPurposeTextView.setText(mChemical.getPurpose());


        mChemicalDialogAllergyImageView = (ImageView)
                view.findViewById(R.id.chemical_dialog_allergy_image_view);
        mChemicalDialogAllergyImageView.setImageResource(mChemical.getAllergyIconResId());
        mChemicalDialogAllergyTextView = (TextView)
                view.findViewById(R.id.chemical_dialog_allergy_text_view);
        mChemicalDialogAllergyTextView.setText(mChemical.getAllergyDescription());

        mChemicalDialogHazardInfoRecyclerView = (RecyclerView)
                view.findViewById(R.id.chemical_dialog_hazard_info_recycler_view);
        mChemicalDialogHazardInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHazardAdapter = new HazardAdapter(mHazards);
        mChemicalDialogHazardInfoRecyclerView.setAdapter(mHazardAdapter);

        mChemicalDialogCloseButtonTextView = (TextView)
                view.findViewById(R.id.chemical_dialog_close_button_text_view);
        mChemicalDialogCloseButtonTextView.setOnClickListener(this);
        mChemicalDialogUpdateRequestButtonTextView = (TextView)
                view.findViewById(R.id.chemical_dialog_update_request_button_text_view);
        mChemicalDialogUpdateRequestButtonTextView.setOnClickListener(this);

        mBuilder = new AlertDialog.Builder(getActivity())
                .setView(view)
//                .setIcon(R.drawable.chemical_composition_dangerous_status2)
//                .setTitle(mChemical.getNameKo())
//                .setPositiveButton("positive", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton("negative", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNeutralButton("neutral", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
                ;
        return mBuilder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getDialog().getWindow().getAttributes().windowAnimations = R.style.ChemicalDialogAnimation;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chemical_dialog_close_button_text_view:
                this.dismiss();
                break;
            case R.id.chemical_dialog_update_request_button_text_view:
//                Toast.makeText(getActivity(), "수정 요청 하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
                        "mailto", getString(R.string.administrator_support_email), null));
//                        intent.setData(Uri.parse("mailto:chemi.helper@gmail.com"));
                intent.setType("text/plain");
//                        intent.putExtra(Intent.EXTRA_EMAIL, "chemi.helper@gmail.com");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.administrator_support_email)});
                intent.putExtra(Intent.EXTRA_TEXT, getChemicalReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.request_chemical_subject));
                intent = Intent.createChooser(intent, getString(R.string.request_chemical_title));
                startActivity(intent);
                break;
        }
    }

    private class HazardAdapter extends RecyclerView.Adapter<HazardHolder> {

        private ArrayList<Hazard> mHazards;

        public HazardAdapter(ArrayList<Hazard> hazards) {
            mHazards = hazards;
        }

        @Override
        public HazardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_hazard, parent, false);
            return new HazardHolder(view);
        }

        @Override
        public void onBindViewHolder(HazardHolder holder, int position) {
            Hazard hazard = mHazards.get(position);
            holder.bindHazard(hazard);
        }

        @Override
        public int getItemCount() {
            return mHazards.size();
        }
    }

    private class HazardHolder extends RecyclerView.ViewHolder {

        private Hazard mHazard;

        private ImageView mHazardIconImageView;
        private TextView mHazardTitleTextView;
        private TextView mHazardDescriptionTextView;

        public HazardHolder(View itemView) {
            super(itemView);

            mHazardIconImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_hazard_icon_image_view);
            mHazardTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_hazard_title_text_view);
            mHazardDescriptionTextView = (TextView)
                    itemView.findViewById(R.id.list_item_hazard_description_text_view);
        }

        public void bindHazard(Hazard hazard) {
            mHazard = hazard;
            mHazardIconImageView.setImageResource(mHazard.getIconResId());
            mHazardTitleTextView.setText(mHazard.getClassName());
            mHazardDescriptionTextView.setText(mHazard.getDescription());
        }
    }

    private String getChemicalReport() {

        return getString(R.string.request_chemical_description, mChemical.getNameKo());
    }

}
