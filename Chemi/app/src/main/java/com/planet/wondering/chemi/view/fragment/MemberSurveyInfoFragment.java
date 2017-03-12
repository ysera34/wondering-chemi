package com.planet.wondering.chemi.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.view.activity.SearchActivity;

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
    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMemberSurveyStageFragments = new ArrayList<>();
        mMemberSurveyStageFragments.add(MemberSurveyStage1Fragment.newInstance());
        mMemberSurveyStageFragments.add(MemberSurveyStage2Fragment.newInstance());
        mMemberSurveyStageFragments.add(MemberSurveyStage3Fragment.newInstance());
        mMemberSurveyStageFragments.add(MemberSurveyStage4Fragment.newInstance());
        mMemberSurveyStageFragments.add(MemberSurveyStage5Fragment.newInstance());

        mUser = new User();
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
        mMemberSurveyInfoViewPager.setOffscreenPageLimit(mMemberSurveyStageFragments.size() - 1);
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
        mMemberSurveyInfoConfirmButtonTextView = (TextView) view.findViewById(R.id.member_survey_info_confirm_button_text_view);
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
                Log.d(TAG, "create AlertDialog");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("부가정보 입력 다음에 하기");
                builder.setMessage("부가정보 입력을 나중에 하실건가요?");
                builder.setCancelable(false);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(SearchActivity.newIntent(getActivity()));
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
            case R.id.member_survey_info_confirm_button_text_view:
                int currentStage = mMemberSurveyInfoViewPager.getCurrentItem();
                if (currentStage < mMemberSurveyStageFragments.size() - 1) {
                    mMemberSurveyInfoViewPager.setCurrentItem(currentStage + 1);
                } else if (currentStage == mMemberSurveyStageFragments.size() - 1) {
                    requestSubmitUserInfo(mUser);
                    startActivity(SearchActivity.newIntent(getActivity()));
                    getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int progressStatus;
        progressStatus = (int) ((positionOffset) * (position + 1)
                * (mMemberSurveyInfoProgressBar.getMax() / mMemberSurveyStageFragments.size()));
        mMemberSurveyInfoProgressBar.setProgress(progressStatus);
        if (position == mMemberSurveyInfoViewPager.getCurrentItem()) {
            progressStatus = position
            * (mMemberSurveyInfoProgressBar.getMax() / mMemberSurveyStageFragments.size());
            mMemberSurveyInfoProgressBar.setProgress(progressStatus);
            mCurrentPosition = position;
        }
    }

    private int mCurrentPosition;

    @Override
    public void onPageSelected(int position) {
        mMemberSurveyInfoProgressStageTextView.setText(
                getString(R.string.survey_info_progress_stage_format,
                        String.valueOf(mMemberSurveyStageFragments.size()),
                        String.valueOf(position + 1)));

        // mMemberSurveyInfoConfirmButtonTextView initialize
        mMemberSurveyInfoConfirmButtonTextView
                .setTextColor(getResources().getColor(R.color.colorReef));
        mMemberSurveyInfoConfirmButtonTextView
                .setBackgroundResource(R.drawable.widget_solid_oval_rectangle_pale);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 1: touch in; 2: touch out and find its rightful place; 0: stop;
//        Log.i(TAG, "onPageScrollStateChanged position : " + String.valueOf(state));
    }

    public void updateConfirmButtonTextView(int stageNumber, boolean isCompleted) {
        switch (stageNumber) {
            case 1:case 2:case 3:
                if (isCompleted) {
                    mMemberSurveyInfoConfirmButtonTextView.setEnabled(true);
                    mMemberSurveyInfoConfirmButtonTextView
                            .setTextColor(getResources().getColorStateList(R.color.color_selector_primary_white));
                    mMemberSurveyInfoConfirmButtonTextView
                            .setBackgroundResource(R.drawable.selector_opaque_white_transparent_white);
                } else {
                    mMemberSurveyInfoConfirmButtonTextView.setEnabled(false);
                    mMemberSurveyInfoConfirmButtonTextView
                            .setTextColor(getResources().getColor(R.color.colorReef));
                    mMemberSurveyInfoConfirmButtonTextView
                            .setBackgroundResource(R.drawable.widget_solid_oval_rectangle_pale);
                }
                break;
        }
    }

    public void updateUserInfo(int stageNumber, int value) {
        switch (stageNumber) {
            case 1:
                mUser.setBirthYear(value);
                break;
            case 2:
                // female : 0; male : 1;
                if (value == 0) {
                    mUser.setGender(true);
                } else {
                    mUser.setGender(false);
                }
                break;
            case 3:
//                if (value / 1000 == 1) {
//                    mUser.setHasDrySkin(false);
//                    mUser.setHasOilySkin(false);
//                    mUser.setHasAllergy(false);
//                    break;
//                }
                mUser.setHasDrySkin(false);
                mUser.setHasOilySkin(false);
                mUser.setHasAllergy(false);
                if (value / 100 == 1) {
                    mUser.setHasDrySkin(true);
                }
                if (value / 10 == 1 || value / 10 == 11) {
                    mUser.setHasOilySkin(true);
                }
                if (value % 10 == 1) {
                    mUser.setHasAllergy(true);
                }
                break;
            case 4:
                break;
            case 5:
                break;
        }
        Log.i(TAG, mUser.toString());
    }

    private void requestSubmitUserInfo(User user) {

    }
}
