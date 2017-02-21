package com.planet.wondering.chemi.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 2. 20..
 */

public class MemberSurveyInfoFragment extends Fragment
        implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = MemberSurveyInfoFragment.class.getSimpleName();

    public static MemberSurveyInfoFragment newInstance() {

        Bundle args = new Bundle();

        MemberSurveyInfoFragment fragment = new MemberSurveyInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Button mMemberSurveyInfoSkipButton;
    private TextView mMemberSurveyInfoProgressStageTextView;
    private ProgressBar mMemberSurveyInfoProgressBar;
    private ViewPager mMemberSurveyInfoViewPager;
    private TextView mMemberSurveyInfoConfirmButtonTextView;

    private ArrayList<Fragment> mMemberSurveyStageFragments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMemberSurveyStageFragments = new ArrayList<>();
        mMemberSurveyStageFragments.add(MemberSurveyStage1Fragment.newInstance());
        mMemberSurveyStageFragments.add(MemberSurveyStage2Fragment.newInstance());
        mMemberSurveyStageFragments.add(MemberSurveyStage3Fragment.newInstance());
        mMemberSurveyStageFragments.add(MemberSurveyStage2Fragment.newInstance());
        mMemberSurveyStageFragments.add(MemberSurveyStage3Fragment.newInstance());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_survey_info, container, false);
        mMemberSurveyInfoSkipButton = (Button) view.findViewById(R.id.member_survey_info_skip_button);
        mMemberSurveyInfoSkipButton.setOnClickListener(this);
        mMemberSurveyInfoProgressStageTextView =
                (TextView) view.findViewById(R.id.member_survey_info_progress_stage_text_view);
        mMemberSurveyInfoProgressBar =
                (ProgressBar) view.findViewById(R.id.member_survey_info_progress_bar);
        mMemberSurveyInfoViewPager =
                (ViewPager) view.findViewById(R.id.member_survey_info_view_pager);
        mMemberSurveyInfoViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mMemberSurveyStageFragments.get(position);
            }

            @Override
            public int getCount() {
                return mMemberSurveyStageFragments.size();
            }
        });
        mMemberSurveyInfoViewPager.addOnPageChangeListener(this);

        mMemberSurveyInfoConfirmButtonTextView =
                (TextView) view.findViewById(R.id.member_survey_info_confirm_button_text_view);
        mMemberSurveyInfoConfirmButtonTextView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMemberSurveyInfoProgressStageTextView.setText(
                getString(R.string.survey_info_progress_stage_format,
                        String.valueOf(mMemberSurveyStageFragments.size()),
                        String.valueOf(mMemberSurveyInfoViewPager.getCurrentItem() + 1)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_survey_info_skip_button:
                //AlertDialog
                break;
            case R.id.member_survey_info_confirm_button_text_view:
                int currentStage = mMemberSurveyInfoViewPager.getCurrentItem();
                if (currentStage < mMemberSurveyStageFragments.size() - 1) {
                    mMemberSurveyInfoViewPager.setCurrentItem(currentStage + 1);
//                    mMemberSurveyInfoProgressStageTextView.setText(
//                            getString(R.string.survey_info_progress_stage_format,
//                            String.valueOf(mMemberSurveyStageFragments.size()),
//                            String.valueOf(mMemberSurveyInfoViewPager.getCurrentItem() + 1)));
                    updateProgressBar(true);
                }
                break;
        }
    }

    private int mViewPagerLastPosition = -1;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // forward : 000000001 ; backward : 000000000
        Log.i(TAG, "onPageScrolled position : " + String.valueOf(position));
        // forward : 0 ~ 0.999 ; backward : 0.999 ~ 0
        Log.i(TAG, "onPageScrolled positionOffset : " + String.valueOf(positionOffset));
        // forward : 0 ~ 1440 ; backward : 1440 ~ 0
        Log.i(TAG, "onPageScrolled positionOffsetPixels : " + String.valueOf(positionOffsetPixels));
        int progressStatus;
        progressStatus = (int) ((positionOffset) * (position + 1)
                * (mMemberSurveyInfoProgressBar.getMax() / mMemberSurveyStageFragments.size()));
        mMemberSurveyInfoProgressBar.setProgress(progressStatus);
        if (position == mMemberSurveyInfoViewPager.getCurrentItem()) {
            progressStatus = position
            * (mMemberSurveyInfoProgressBar.getMax() / mMemberSurveyStageFragments.size());
            mMemberSurveyInfoProgressBar.setProgress(progressStatus);
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.i(TAG, "onPageSelected position : " + String.valueOf(position));
        mMemberSurveyInfoProgressStageTextView.setText(
                getString(R.string.survey_info_progress_stage_format,
                        String.valueOf(mMemberSurveyStageFragments.size()),
                        String.valueOf(position + 1)));
//        mMemberSurveyInfoProgressBar.setProgress(
//                (position + 1)
//                * (mMemberSurveyInfoProgressBar.getMax() / mMemberSurveyStageFragments.size()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 0: stop; 1: touch in; 2: touch out;
        Log.i(TAG, "onPageScrollStateChanged position : " + String.valueOf(state));
    }

    public void updateConfirmButtonTextView(int stageNumber, boolean isCompleted) {
        switch (stageNumber) {
            case 1:
                if (isCompleted) {
                    mMemberSurveyInfoConfirmButtonTextView
                            .setTextColor(getResources().getColorStateList(R.color.color_selector_primary_white));
                    mMemberSurveyInfoConfirmButtonTextView
                            .setBackgroundResource(R.drawable.selector_opaque_white_transparent_white);
                } else {
                    mMemberSurveyInfoConfirmButtonTextView
                            .setTextColor(getResources().getColor(R.color.colorReef));
                    mMemberSurveyInfoConfirmButtonTextView
                            .setBackgroundResource(R.drawable.widget_solid_oval_rectangle_pale);
                }
                break;
        }
    }

    public void updateProgressBar(boolean isCompleted) {

        int progress = mMemberSurveyInfoProgressBar.getProgress();
        int stepSize = 100 / mMemberSurveyStageFragments.size();

        if (isCompleted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mMemberSurveyInfoProgressBar.setProgress(progress + stepSize, true);
            } else {
                mMemberSurveyInfoProgressBar.setProgress(progress + stepSize);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mMemberSurveyInfoProgressBar.setProgress(progress - stepSize, true);
            } else {
                mMemberSurveyInfoProgressBar.setProgress(progress - stepSize);
            }
        }
    }

//    public void onBackPressed() {
//
//    }
}
