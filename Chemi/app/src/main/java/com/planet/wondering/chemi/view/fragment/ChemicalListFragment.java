package com.planet.wondering.chemi.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.listener.OnRecyclerViewScrollListener;
import com.planet.wondering.chemi.view.activity.ProductActivity;
import com.planet.wondering.chemi.view.custom.HexagonFilterLayout;

import org.json.JSONObject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.planet.wondering.chemi.network.Config.Chemical.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 1. 19..
 */

public class ChemicalListFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ChemicalListFragment.class.getSimpleName();

    private static final String ARG_PRODUCT = "product";
    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String CHEMICAL_DIALOG = "chemical_dialog";

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

    public static ChemicalListFragment newInstance(Product product) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);

        ChemicalListFragment fragment = new ChemicalListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mProductId;
    private Product mProduct;
    private Chemical mChemical;
    private ArrayList<Chemical> mChemicals;

    private TextView mChemicalWholeTextView;
    private TextView mChemicalSortInfoTextView;
    private TextView mChemicalIncludeAllergyTotalTextView;
    private HexagonFilterLayout[] mHexagonFilterLayouts;
    private RecyclerView mChemicalRecyclerView;
    private ChemicalAdapter mChemicalAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getArguments().getInt(ARG_PRODUCT_ID, -1);
        mProduct = (Product) getArguments().getSerializable(ARG_PRODUCT);
        mChemicals = mProduct.getChemicals();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chemical_list, container, false);

        mChemicalWholeTextView = (TextView) view.findViewById(R.id.chemical_whole_text_view);
        mChemicalSortInfoTextView = (TextView) view.findViewById(R.id.chemical_sort_info_text_view);
        mChemicalIncludeAllergyTotalTextView =
                (TextView) view.findViewById(R.id.chemical_include_allergy_total_text_view);
        mChemicalIncludeAllergyTotalTextView.setText(String.valueOf(mProduct.getAllergyCount()));
        mHexagonFilterLayouts = new HexagonFilterLayout[5];

        int[] filterLayoutIds = new int[]{
                R.id.hexagon1, R.id.hexagon2, R.id.hexagon3, R.id.hexagon4, R.id.hexagon5,};
        int[] numberOfEachEWGRatings = mProduct.getNumberOfEachEWGRating();

        for (int i = 0; i < mHexagonFilterLayouts.length; i++) {
            mHexagonFilterLayouts[i] = (HexagonFilterLayout) view.findViewById(filterLayoutIds[i]);
            mHexagonFilterLayouts[i].setHexagonCount(numberOfEachEWGRatings[i]);
            mHexagonFilterLayouts[i].setOnClickListener(this);
        }

        mChemicalRecyclerView = (RecyclerView) view.findViewById(R.id.chemical_recycler_view);
        mChemicalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        SeparatorDecoration decoration =
//                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
//        mChemicalRecyclerView.addItemDecoration(decoration);
        mChemicalRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onShowView() {
                ((ProductActivity) getActivity()).showBottomNavigationView();
//                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
            }

            @Override
            public void onHideView() {
                ((ProductActivity) getActivity()).hideBottomNavigationView();
//                ((BottomNavigationActivity) getActivity()).hideEditTextLayout();
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrangeHexagonFilterLayouts(0);
        if (mProduct.isWholeChemicals()) {
            mChemicalWholeTextView.setText(getString(R.string.chemical_whole_true_message));
        } else {
            mChemicalWholeTextView.setText(getString(R.string.chemical_whole_false_message));
        }
        mChemicalSortInfoTextView.setText(R.string.chemical_list_sort_notation);
    }

    @Override
    public void onResume() {
        super.onResume();
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
                arrangeChemicalList(0);
                break;
            case R.id.hexagon2:
                arrangeHexagonFilterLayouts(1);
                arrangeChemicalList(1);
                break;
            case R.id.hexagon3:
                arrangeHexagonFilterLayouts(2);
                arrangeChemicalList(2);
                break;
            case R.id.hexagon4:
                arrangeHexagonFilterLayouts(3);
                arrangeChemicalList(3);
                break;
            case R.id.hexagon5:
                arrangeHexagonFilterLayouts(4);
                arrangeChemicalList(4);
                break;
        }
    }

    private void arrangeHexagonFilterLayouts(int hexagonFilterIndex) {

        for (int i = 0; i < mHexagonFilterLayouts.length; i++) {
            if (i == hexagonFilterIndex) {
                mHexagonFilterLayouts[i].scaleUpAnimate();
            } else {
                mHexagonFilterLayouts[i].scaleDownAnimate();
            }
        }
    }

    private void arrangeChemicalList(int hexagonFilterIndex) {
        if (mChemicalAdapter == null) {
            return;
        }
        ArrayList<Chemical> chemicals = mProduct.getChemicalListOfEachEWGRating(hexagonFilterIndex);
        if (hexagonFilterIndex == 0) {
            mChemicalSortInfoTextView.setText(R.string.chemical_list_sort_notation);
        } else {
            Collections.sort(chemicals, mHazardGradeDescChemicalComparator);
            Collections.reverse(chemicals);
            mChemicalSortInfoTextView.setText(R.string.chemical_list_sort_hazard);
        }
        mChemicalAdapter.setChemicals(chemicals);
        mChemicalAdapter.notifyDataSetChanged();
    }

    private class ChemicalAdapter extends RecyclerView.Adapter<ChemicalHolder> {

        private ArrayList<Chemical> mChemicals;

        ChemicalAdapter(ArrayList<Chemical> chemicals) {
            mChemicals = chemicals;
        }

        void setChemicals(ArrayList<Chemical> chemicals) {
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
//            setFadeAnimation(holder.itemView);
        }

        @Override
        public int getItemCount() {
            return mChemicals.size();
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(250);
            view.startAnimation(anim);
        }
    }

    private class ChemicalHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Chemical mChemical;

        private TextView mChemicalNameKoTextView;
        private TextView mChemicalNameEngTextView;
//        private CircleHazardView mChemicalCircleHazardView;
        private ImageView mChemicalAllergyImageView;
        private TextView mChemicalCircleTextView;

        ChemicalHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mChemicalNameKoTextView = (TextView)
                    itemView.findViewById(R.id.list_item_chemical_name_ko_text_view);
//            mChemicalNameKoTextView.setSelected(true);
            mChemicalNameEngTextView = (TextView)
                    itemView.findViewById(R.id.list_item_chemical_name_eng_text_view);
//            mChemicalNameEngTextView.setSelected(true);
//            mChemicalCircleHazardView = (CircleHazardView)
//                    itemView.findViewById(R.id.list_item_chemical_circle_hazard_view);
            mChemicalAllergyImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_chemical_allergy_image_view);
            mChemicalCircleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_chemical_circle_text_view);
        }

        void bindChemical(Chemical chemical) {
            mChemical = chemical;

            mChemicalNameKoTextView.setText(String.valueOf(mChemical.getNameKo()));
            mChemicalNameEngTextView.setText(String.valueOf(mChemical.getNameEn()));
//            mChemicalCircleHazardView.setCircleColor(mChemical.getHazardColorResId());
//            mChemicalCircleHazardView.setHazardValueText(mChemical.getHazardValueString());
            if (mChemical.isAllergy()) {
                mChemicalAllergyImageView.setVisibility(View.VISIBLE);
            } else {
                mChemicalAllergyImageView.setVisibility(View.GONE);
            }

            mChemicalCircleTextView.setText(mChemical.getHazardValueString());
            mChemicalCircleTextView.setBackgroundResource(mChemical.getHazardIconResId());
        }

        @Override
        public void onClick(View view) {
//            ChemicalDialogFragment dialogFragment = ChemicalDialogFragment.newInstance(mChemical.getId());
//            dialogFragment.show(getFragmentManager(), CHEMICAL_DIALOG);
            requestChemical(mChemical.getId());
        }
    }

    private void requestChemical(int chemicalId) {

        final ProgressDialog progressDialog =
                ProgressDialog.show(getActivity(), getString(R.string.progress_dialog_title_chemical),
                        getString(R.string.progress_dialog_message_wait), false, false);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH + chemicalId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mChemical = Parser.parseChemical(response);
                        progressDialog.dismiss();
                        ChemicalDialogFragment dialogFragment = ChemicalDialogFragment.newInstance(mChemical);
                        dialogFragment.show(getFragmentManager(), CHEMICAL_DIALOG);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e(TAG, error.getMessage());
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private final static Comparator mHazardGradeDescChemicalComparator = new Comparator() {

        private final Collator mCollator = Collator.getInstance();

        @Override
        public int compare(Object o, Object t1) {

            int int1 = ((Chemical) o).getMaxHazard();
            int int2 = ((Chemical) t1).getMaxHazard();

            String str1;
            String str2;

            if (int1 == 10) {
                str1 = String.valueOf(int1);
            } else {
                str1 = "0" + String.valueOf(int1);
            }

            if (int2 == 10) {
                str2 = String.valueOf(int2);
            } else {
                str2 = "0" + String.valueOf(int2);
            }

            return mCollator.compare(str1, str2);
        }
    };

}
