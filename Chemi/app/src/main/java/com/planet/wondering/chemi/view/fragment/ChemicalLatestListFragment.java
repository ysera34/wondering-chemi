package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
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
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;
import com.planet.wondering.chemi.util.helper.ChemicalSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnChemicalSelectedListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yoon on 2017. 3. 17..
 */

public class ChemicalLatestListFragment extends Fragment {

    private static final String TAG = ChemicalLatestListFragment.class.getSimpleName();

    private static final String ARG_MODE_ID = "mode_id";

    public static ChemicalLatestListFragment newInstance() {

        Bundle args = new Bundle();

        ChemicalLatestListFragment fragment = new ChemicalLatestListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChemicalLatestListFragment newInstance(byte modeId) {

        Bundle args = new Bundle();
        args.putByte(ARG_MODE_ID, modeId);

        ChemicalLatestListFragment fragment = new ChemicalLatestListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final byte LATEST_MODE = -1;
    private static final byte SUGGESTION_MODE = 1;
    private byte mModeId;

    private RecyclerView mChemicalLatestRecyclerView;
    private ChemicalLatestAdapter mChemicalLatestAdapter;
    private ArrayList<Chemical> mChemicals;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChemicals = new ArrayList<>();
        mModeId = getArguments().getByte(ARG_MODE_ID, (byte) 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chemical_latest_list, container, false);
        mChemicalLatestRecyclerView = (RecyclerView) view.findViewById(R.id.chemical_latest_recycler_view);
        mChemicalLatestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SeparatorDecoration decoration =
                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
        mChemicalLatestRecyclerView.addItemDecoration(decoration);

        updateUI();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        mChemicals = ChemicalSharedPreferences.getStoredChemicals(getActivity());
        if (mChemicals != null) {
            Collections.reverse(mChemicals);
        }
        updateUI();
    }

    private void updateUI() {
        if (mChemicalLatestAdapter == null) {
            mChemicalLatestAdapter = new ChemicalLatestAdapter(mChemicals);
            mChemicalLatestRecyclerView.setAdapter(mChemicalLatestAdapter);
        } else {
            mChemicalLatestAdapter.setChemicals(mChemicals);
            mChemicalLatestAdapter.notifyDataSetChanged();
        }
    }

    private class ChemicalLatestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Chemical> mChemicals;

        public ChemicalLatestAdapter(ArrayList<Chemical> chemicals) {
            mChemicals = chemicals;
        }

        public void setChemicals(ArrayList<Chemical> chemicals) {
            mChemicals = chemicals;
        }

        public void removeChemicals(Chemical chemical) {
            mChemicals.remove(chemical);
            ChemicalSharedPreferences.removeStoredChemical(getActivity(), chemical);
        }

        public void clearChemical() {
            mChemicals.clear();
            ChemicalSharedPreferences.removeAllStoredChemical(getActivity());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    view = layoutInflater.inflate(R.layout.list_item_chemical_latest_header, parent, false);
                    return new HeaderHolder(view);
                case VIEW_TYPE_ITEM:
                    view = layoutInflater.inflate(R.layout.list_item_chemical_latest, parent, false);
                    return new ChemicalHolder(view);
                case VIEW_TYPE_FOOTER:
                    view = layoutInflater.inflate(R.layout.list_item_chemical_latest_footer, parent, false);
                    return new FooterHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderHolder) {
                ((HeaderHolder) holder).bindHeader(mModeId);
            }
            if (holder instanceof ChemicalHolder) {
                Chemical chemical = mChemicals.get(position - 1);
                ((ChemicalHolder) holder).bindChemical(chemical);
            }
        }

        @Override
        public int getItemCount() {
            if (mChemicals == null || mChemicals.size() == 0) {
                return 2;
            } else {
                return mChemicals.size() + 2;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return VIEW_TYPE_HEADER;
            }
            if (((mChemicals == null || mChemicals.size() == 0) && position == 1)
                    || ((mChemicals != null && mChemicals.size() > 0) && position == mChemicals.size() + 1)) {
                return VIEW_TYPE_FOOTER;
            } else {
                return VIEW_TYPE_ITEM;
            }
        }
    }

    private static final int VIEW_TYPE_HEADER = -1;
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    private class HeaderHolder extends RecyclerView.ViewHolder {

        private TextView mHeaderTitleTextView;
        private TextView mHeaderSubTitleTextView;

        public HeaderHolder(View itemView) {
            super(itemView);

            mHeaderTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_chemical_latest_header_title_text_view);
            mHeaderSubTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_chemical_latest_header_sub_title_text_view);
        }

        public void bindHeader(byte modeId) {
            switch (modeId) {
                case LATEST_MODE:
                    mHeaderTitleTextView.setText(getString(R.string.search_chemical_latest_title));
                    if (mChemicals == null || mChemicals.size() == 0) {
                        mHeaderSubTitleTextView.setText(getString(R.string.search_chemical_latest_message));
                    }
                    break;
                case SUGGESTION_MODE:
                    mHeaderTitleTextView.setText(getString(R.string.search_chemical_suggestion_title));
                    mHeaderSubTitleTextView.setText(getString(R.string.search_chemical_suggestion_message));
                    break;
            }
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View itemView) {
            super(itemView);

        }
    }

    private class ChemicalHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Chemical mChemical;

        private TextView mChemicalNameKoTextView;
        private TextView mChemicalNameEngTextView;

        private LinearLayout mChemicalCircleClearLayout;
        private ImageView mChemicalCircleClearImageView;

        public ChemicalHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mChemicalNameKoTextView = (TextView)
                    itemView.findViewById(R.id.list_item_chemical_name_ko_text_view);
            mChemicalNameEngTextView = (TextView)
                    itemView.findViewById(R.id.list_item_chemical_name_eng_text_view);
            mChemicalCircleClearLayout = (LinearLayout)
                    itemView.findViewById(R.id.list_item_chemical_circle_clear_layout);
            mChemicalCircleClearLayout.setOnClickListener(this);
            mChemicalCircleClearImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_chemical_circle_clear_image_view);
            mChemicalCircleClearImageView.setOnClickListener(this);
        }

        public void bindChemical(Chemical chemical) {
            mChemical = chemical;

            mChemicalNameKoTextView.setText(String.valueOf(mChemical.getNameKo()));
            mChemicalNameEngTextView.setText(String.valueOf(mChemical.getNameEn()));
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.list_item_chemical_circle_clear_image_view ||
                    v.getId() == R.id.list_item_chemical_circle_clear_layout) {
                mChemicalLatestAdapter.removeChemicals(mChemical);
                mChemicalLatestAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "최근 검색 성분 상세 요청", Toast.LENGTH_SHORT).show();
                mSelectedListener.onChemicalSelected(mChemical);
            }
        }
    }

    OnChemicalSelectedListener mSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mSelectedListener = (OnChemicalSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnChemicalSelectedListener");
        }
    }
}
