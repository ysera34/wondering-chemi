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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnSurveyCompletedListener;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 2. 20..
 */

public class MemberSurveyStage1Fragment extends Fragment {

    public static MemberSurveyStage1Fragment newInstance() {

        Bundle args = new Bundle();

        MemberSurveyStage1Fragment fragment = new MemberSurveyStage1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mMemberSurveyYearRecyclerView;
    private YearLayoutManager mYearLayoutManager;
    private YearAdapter mYearAdapter;
    private ArrayList<Integer> mYears;
    private int mDefaultYear;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mYears = new ArrayList<>();
        mDefaultYear = 1989;
        for (int i = mDefaultYear - 40 ; i < mDefaultYear + 25; i++) {
            mYears.add(i);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_survey_stage1, container, false);
        mMemberSurveyYearRecyclerView = (RecyclerView) view.findViewById(R.id.member_survey_year_recycler_view);
        mYearLayoutManager = new YearLayoutManager(getActivity());
        mMemberSurveyYearRecyclerView.setLayoutManager(mYearLayoutManager);
        mYearAdapter = new YearAdapter(mYears);
        mMemberSurveyYearRecyclerView.setAdapter(mYearAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class YearLayoutManager extends LinearLayoutManager {

        private boolean isScrollEnable = true;

        public YearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnable(boolean scrollEnable) {
            isScrollEnable = scrollEnable;
        }

        @Override
        public boolean canScrollVertically() {
            return isScrollEnable && super.canScrollVertically();
        }
    }

    private class YearAdapter extends RecyclerView.Adapter<YearHolder> {

        private ArrayList<Integer> mYears;

        public YearAdapter(ArrayList<Integer> years) {
            mYears = years;
        }

        @Override
        public YearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_year, parent, false);
            return new YearHolder(view);
        }

        @Override
        public void onBindViewHolder(YearHolder holder, int position) {
            int year = mYears.get(position);
            holder.bindYear(year);
        }

        @Override
        public int getItemCount() {
            return mYears.size();
        }
    }

    private class YearHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private int mYear;

        private TextView mYearTextView;
        private CheckBox mYearCheckBox;

        public YearHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mYearTextView = (TextView) itemView.findViewById(R.id.list_item_year_text_view);
            mYearCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_year_check_box);
            mYearCheckBox.setOnCheckedChangeListener(this);
        }

        public void bindYear(int year) {
            mYear = year;
            mYearTextView.setText(getString(R.string.survey_year_format, String.valueOf(mYear)));
        }

        @Override
        public void onClick(View v) {
            if (mYearCheckBox.isChecked() && isYearSelected) {
                mYearCheckBox.setChecked(false);
                isYearSelected = false;
                mSurveyCompletedListener.onSurveyCompleted(1, false);
//                mYearLayoutManager.setScrollEnable(false);
            } else if (!mYearCheckBox.isChecked() && !isYearSelected) {
                mYearCheckBox.setChecked(true);
                isYearSelected = true;
                mSurveyCompletedListener.onSurveyCompleted(1, true);
                mMemberSurveyYearRecyclerView.setNestedScrollingEnabled(true);
//                mYearLayoutManager.setScrollEnable(true);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mYearTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                mYearTextView.setBackgroundColor(getResources().getColor(R.color.colorReef));
            } else {
                mYearTextView.setTextColor(getResources().getColor(R.color.colorReef));
                mYearTextView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    private boolean isYearSelected = false;

    OnSurveyCompletedListener mSurveyCompletedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mSurveyCompletedListener = (OnSurveyCompletedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSurveyCompletedListener");
        }
    }
}
