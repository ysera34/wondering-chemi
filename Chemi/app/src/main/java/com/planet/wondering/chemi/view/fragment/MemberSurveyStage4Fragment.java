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
 * Created by yoon on 2017. 3. 10..
 */

public class MemberSurveyStage4Fragment extends Fragment implements View.OnClickListener {

    public static MemberSurveyStage4Fragment newInstance() {

        Bundle args = new Bundle();

        MemberSurveyStage4Fragment fragment = new MemberSurveyStage4Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mMemberSurveyBabyTrueLayout;
    private LinearLayout mMemberSurveyBabyFalseLayout;
    private ImageView mMemberSurveyBabyTrueImageView;
    private ImageView mMemberSurveyBabyFalseImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_survey_stage4, container, false);
        mMemberSurveyBabyTrueLayout = (LinearLayout) view.findViewById(R.id.member_survey_baby_true_layout);
        mMemberSurveyBabyTrueLayout.setOnClickListener(this);
        mMemberSurveyBabyFalseLayout = (LinearLayout) view.findViewById(R.id.member_survey_baby_false_layout);
        mMemberSurveyBabyFalseLayout.setOnClickListener(this);
        mMemberSurveyBabyTrueImageView = (ImageView) view.findViewById(R.id.member_survey_baby_true_image_view);
        mMemberSurveyBabyFalseImageView = (ImageView) view.findViewById(R.id.member_survey_baby_false_image_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isCheckedTrue = false;
    private boolean isCheckedFalse = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_survey_baby_true_layout:
                if (!isCheckedTrue) {
                    mMemberSurveyBabyTrueImageView.setImageResource(R.drawable.ic_circle_check_white_true);
                    isCheckedTrue = true;

                    mMemberSurveyBabyFalseImageView.setImageResource(R.drawable.ic_circle_check_white_false);
                    isCheckedFalse = false;
                } else {
                    mMemberSurveyBabyTrueImageView.setImageResource(R.drawable.ic_circle_check_white_false);
                    isCheckedTrue = false;
                }
                break;
            case R.id.member_survey_baby_false_layout:
                if (!isCheckedFalse) {
                    mMemberSurveyBabyFalseImageView.setImageResource(R.drawable.ic_circle_check_white_true);
                    isCheckedFalse = true;

                    mMemberSurveyBabyTrueImageView.setImageResource(R.drawable.ic_circle_check_white_false);
                    isCheckedTrue = false;
                } else {
                    mMemberSurveyBabyFalseImageView.setImageResource(R.drawable.ic_circle_check_white_false);
                    isCheckedFalse = false;
                }
                break;
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
