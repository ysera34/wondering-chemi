package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.custom.BarGraphView;

/**
 * Created by yoon on 2017. 7. 17..
 */

public class ChemicalChartFragment extends Fragment {

    private static final String TAG = ChemicalChartFragment.class.getSimpleName();

    private static final String ARG_WHOLE_CHEMICALS = "whole_chemicals";
    private static final String ARG_CHEMICAL_COUNT = "chemical_count";
    private static final String ARG_NUMBER_OF_EACH_EWG_RATINGS = "number_of_each_ewg_ratings";

    public static ChemicalChartFragment newInstance() {

        Bundle args = new Bundle();

        ChemicalChartFragment fragment = new ChemicalChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChemicalChartFragment newInstance(
            boolean wholeChemicals, int chemicalCount, int[] numberOfEachEWGRatings) {

        Bundle args = new Bundle();
        args.putBoolean(ARG_WHOLE_CHEMICALS, wholeChemicals);
        args.putInt(ARG_CHEMICAL_COUNT, chemicalCount);
        args.putSerializable(ARG_NUMBER_OF_EACH_EWG_RATINGS, numberOfEachEWGRatings);

        ChemicalChartFragment fragment = new ChemicalChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView mChemicalWholeTextView;
    private LinearLayout mChartLayout;
    private TextView mChemicalChartTitleTextView;
    private boolean mWholeChemicals;
    private int mChemicalCount;
    private int[] mNumberOfEachEWGRatings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWholeChemicals = getArguments().getBoolean(ARG_WHOLE_CHEMICALS);
        mChemicalCount = getArguments().getInt(ARG_CHEMICAL_COUNT);
        mNumberOfEachEWGRatings = getArguments().getIntArray(ARG_NUMBER_OF_EACH_EWG_RATINGS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chemical_chart, container, false);
        mChemicalWholeTextView = (TextView) view.findViewById(R.id.chemical_whole_text_view);
        mChemicalChartTitleTextView = (TextView) view.findViewById(R.id.chemical_chart_title_text_view);
        mChartLayout = (LinearLayout) view.findViewById(R.id.chemical_chart_layout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mWholeChemicals) {
            mChemicalWholeTextView.setText("전성분 정보");
        } else {
            mChemicalWholeTextView.setText("주성분 정보");
        }
        mChemicalChartTitleTextView.setText(highlightChartTitleName());
        addChartView();
    }

    private void addChartView() {

        int hazardColorResIdArr[] = {
                R.color.hazard2, R.color.hazard3, R.color.hazard4, R.color.hazard1,};
        int hazardNameResIdArr[] = {
                R.string.chemical_ewg_value_name_2, R.string.chemical_ewg_value_name_3,
                R.string.chemical_ewg_value_name_4, R.string.chemical_ewg_value_name_1,};

        for (int i = 0; i < hazardColorResIdArr.length; i++) {
            BarGraphView barGraphView = new BarGraphView(getContext(), hazardNameResIdArr[i], mNumberOfEachEWGRatings[i + 1],
                    getPixelFromDp(getChartViewWidth(230, mNumberOfEachEWGRatings[i + 1], mNumberOfEachEWGRatings[0])),
                    hazardColorResIdArr[i]);
            mChartLayout.addView(barGraphView);
        }
    }

    private int getChartViewWidth(int maxLength, int hazardCount, int allCount) {
        return maxLength * hazardCount / allCount;
    }

    private int getPixelFromDp(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private SpannableString highlightChartTitleName() {
        SpannableString spannableString = new SpannableString(
                getString(R.string.chemical_chart_title_format, String.valueOf(mChemicalCount)));
        int startIndex = 2;
        int endIndex = startIndex + String.valueOf(mChemicalCount).length();
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
