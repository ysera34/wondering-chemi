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
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.model.Hazard;
import com.planet.wondering.chemi.util.helper.ChemicalSharedPreferences;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 3. 17..
 */
public class ChemicalFragment extends Fragment {

    private static final String TAG = ChemicalFragment.class.getSimpleName();

    private static final String ARG_CHEMICAL = "chemical";

    public static ChemicalFragment newInstance() {

        Bundle args = new Bundle();

        ChemicalFragment fragment = new ChemicalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChemicalFragment newInstance(Chemical chemical) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CHEMICAL, chemical);

        ChemicalFragment fragment = new ChemicalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Chemical mChemical;

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

//    private TextView mChemicalCloseButtonTextView;
//    private TextView mChemicalUpdateRequestButtonTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChemical = (Chemical) getArguments().getSerializable(ARG_CHEMICAL);
        mHazards = mChemical.getHazards();
        ChemicalSharedPreferences.addStoreChemical(getActivity(), mChemical);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chemical, container, false);

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
        mChemicalDialogNameKoTextView.setText(mChemical.getNameKo());
        mChemicalDialogNameEngTextView = (TextView)
                view.findViewById(R.id.chemical_dialog_name_eng_text_view);
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

//        mChemicalCloseButtonTextView = (TextView)
//                view.findViewById(R.id.chemical_close_button_text_view);
//        mChemicalCloseButtonTextView.setOnClickListener(this);
//        mChemicalUpdateRequestButtonTextView = (TextView)
//                view.findViewById(R.id.chemical_update_request_button_text_view);
//        mChemicalUpdateRequestButtonTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
}
