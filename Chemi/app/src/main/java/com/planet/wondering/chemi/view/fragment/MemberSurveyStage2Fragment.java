package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnSurveyCompletedListener;

/**
 * Created by yoon on 2017. 2. 20..
 */

public class MemberSurveyStage2Fragment extends Fragment implements View.OnClickListener {

    public static MemberSurveyStage2Fragment newInstance() {

        Bundle args = new Bundle();

        MemberSurveyStage2Fragment fragment = new MemberSurveyStage2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mMemberSurveyFemaleLayout;
    private LinearLayout mMemberSurveyMaleLayout;
    private ImageView mMemberSurveyFemaleImageView;
    private ImageView mMemberSurveyMaleImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_survey_stage2, container, false);
        mMemberSurveyFemaleLayout = (LinearLayout) view.findViewById(R.id.member_survey_female_layout);
        mMemberSurveyFemaleLayout.setOnClickListener(this);
        mMemberSurveyMaleLayout = (LinearLayout) view.findViewById(R.id.member_survey_male_layout);
        mMemberSurveyMaleLayout.setOnClickListener(this);
        mMemberSurveyFemaleImageView = (ImageView)
                view.findViewById(R.id.member_survey_female_check_image_view);
        mMemberSurveyMaleImageView = (ImageView)
                view.findViewById(R.id.member_survey_male_check_image_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isCheckedFemale = false;
    private boolean isCheckedMale = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_survey_female_layout:
                if (!isCheckedFemale) {
                    mMemberSurveyFemaleImageView.setImageResource(R.drawable.ic_circle_check_white_true);
                    isCheckedFemale = true;
                    mMemberSurveyMaleImageView.setImageResource(R.drawable.ic_circle_check_white_false);
                    isCheckedMale = false;

                    mSurveyCompletedListener.onSurveyCompleted(2, true);
                } else {
                    mMemberSurveyFemaleImageView.setImageResource(R.drawable.ic_circle_check_white_false);
                    isCheckedFemale = false;
                    mSurveyCompletedListener.onSurveyCompleted(2, false);
                }
                break;
            case R.id.member_survey_male_layout:
                if (!isCheckedMale) {
                    mMemberSurveyMaleImageView.setImageResource(R.drawable.ic_circle_check_white_true);
                    isCheckedMale = true;
                    mMemberSurveyFemaleImageView.setImageResource(R.drawable.ic_circle_check_white_false);
                    isCheckedFemale = false;

                    mSurveyCompletedListener.onSurveyCompleted(2, true);
                } else {
                    mMemberSurveyMaleImageView.setImageResource(R.drawable.ic_circle_check_white_false);
                    isCheckedMale = false;
                    mSurveyCompletedListener.onSurveyCompleted(2, false);
                }
                break;
        }
        // female : 0; male : 1;
        if (isCheckedFemale) {
            mSurveyCompletedListener.onSurveyValueSubmit(2, 0);
        } else if (isCheckedMale) {
            mSurveyCompletedListener.onSurveyValueSubmit(2, 1);
        }
    }

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
