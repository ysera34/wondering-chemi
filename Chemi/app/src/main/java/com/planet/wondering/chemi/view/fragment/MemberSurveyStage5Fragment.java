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

public class MemberSurveyStage5Fragment extends Fragment implements View.OnClickListener {

    public static MemberSurveyStage5Fragment newInstance() {

        Bundle args = new Bundle();

        MemberSurveyStage5Fragment fragment = new MemberSurveyStage5Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int[] mSkinTypeLayoutIds;
    private LinearLayout[] mSkinTypeLayouts;
    private int[] mSkinTypeImageViewIds;
    private ImageView[] mSkinTypeImageViews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSkinTypeLayoutIds = new int[]{
                R.id.member_survey_baby_skin_type1_layout, R.id.member_survey_baby_skin_type2_layout,
                R.id.member_survey_baby_skin_type3_layout,};
        mSkinTypeLayouts = new LinearLayout[mSkinTypeLayoutIds.length];
        mSkinTypeImageViewIds = new int[]{
                R.id.member_survey_baby_skin_type1_image_view, R.id.member_survey_baby_skin_type2_image_view,
                R.id.member_survey_baby_skin_type3_image_view,};
        mSkinTypeImageViews = new ImageView[mSkinTypeImageViewIds.length];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_survey_stage5, container, false);
        for (int i = 0; i < mSkinTypeLayoutIds.length; i++) {
            mSkinTypeLayouts[i] = (LinearLayout) view.findViewById(mSkinTypeLayoutIds[i]);
            mSkinTypeLayouts[i].setOnClickListener(this);
            mSkinTypeImageViews[i] = (ImageView) view.findViewById(mSkinTypeImageViewIds[i]);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_survey_baby_skin_type1_layout:
                checkImageView(0);
                break;
            case R.id.member_survey_baby_skin_type2_layout:
                checkImageView(1);
                break;
            case R.id.member_survey_baby_skin_type3_layout:
                checkImageView(2);
                break;
        }
    }

    private boolean[] hasSkinTypes = new boolean[]{false, false, false};

    private void checkImageView(int position) {

//        for (int i = 0; i < hasSkinTypes.length; i++) {
//            mSkinTypeImageViews[i].setImageResource(R.drawable.ic_circle_check_white_false);
//        }
//        mSkinTypeImageViews[position].setImageResource(R.drawable.ic_circle_check_white_true);
        if (!hasSkinTypes[position]) {
            mSkinTypeImageViews[position].setImageResource(R.drawable.ic_circle_check_white_true);
            hasSkinTypes[position] = true;
        } else {
            mSkinTypeImageViews[position].setImageResource(R.drawable.ic_circle_check_white_false);
            hasSkinTypes[position] = false;
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
