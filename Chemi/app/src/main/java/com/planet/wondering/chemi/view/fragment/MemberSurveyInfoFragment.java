package com.planet.wondering.chemi.view.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.view.activity.MemberActivity;
import com.planet.wondering.chemi.view.activity.MemberStartActivity;
import com.planet.wondering.chemi.view.activity.SearchActivity;
import com.planet.wondering.chemi.view.custom.CustomProgressDialog;
import com.planet.wondering.chemi.view.custom.SwipeableViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.common.Common.IS_NOW_USED_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.BIRTH_YEAR;
import static com.planet.wondering.chemi.network.Config.User.Key.CHILD_HAS_ALLERGY;
import static com.planet.wondering.chemi.network.Config.User.Key.CHILD_HAS_DRY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.GENDER;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_ALLERGY;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_CHILD;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_DRY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_OILY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 2. 20..
 */

public class MemberSurveyInfoFragment extends Fragment
        implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = MemberSurveyInfoFragment.class.getSimpleName();

    private static final String ARG_REQUEST_CODE = "request_code";

    public static MemberSurveyInfoFragment newInstance() {

        Bundle args = new Bundle();

        MemberSurveyInfoFragment fragment = new MemberSurveyInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberSurveyInfoFragment newInstance(int requestCode) {

        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);

        MemberSurveyInfoFragment fragment = new MemberSurveyInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageButton mMemberSurveyPrevPageImageButton;
    private Button mMemberSurveyInfoSkipButton;
    private TextView mMemberSurveyInfoProgressStageTextView;
    private ProgressBar mMemberSurveyInfoProgressBar;
    private ViewPager mMemberSurveyInfoViewPager;
    private TextView mMemberSurveyInfoConfirmButtonTextView;

    private ArrayList<Fragment> mMemberSurveyStageFragments;
    private User mUser;
    private int mRequestCode;

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
        mRequestCode = getArguments().getInt(ARG_REQUEST_CODE, -1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_survey_info, container, false);
        mMemberSurveyPrevPageImageButton = (ImageButton)
                view.findViewById(R.id.member_survey_prev_page_image_button);
        mMemberSurveyPrevPageImageButton.setOnClickListener(this);
        mMemberSurveyInfoSkipButton = (Button) view.findViewById(R.id.member_survey_info_skip_button);
        mMemberSurveyInfoSkipButton.setOnClickListener(this);
        mMemberSurveyInfoProgressStageTextView =
                (TextView) view.findViewById(R.id.member_survey_info_progress_stage_text_view);
        mMemberSurveyInfoProgressBar =
                (ProgressBar) view.findViewById(R.id.member_survey_info_progress_bar);
        mMemberSurveyInfoViewPager =
                (SwipeableViewPager) view.findViewById(R.id.member_survey_info_view_pager);
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
        if (mMemberSurveyInfoViewPager.getCurrentItem() == 0) {
            mMemberSurveyPrevPageImageButton.setVisibility(View.GONE);
        } else {
            mMemberSurveyPrevPageImageButton.setVisibility(View.VISIBLE);
        }
        mMemberSurveyInfoProgressStageTextView.setText(
                getString(R.string.survey_info_progress_stage_format,
                        String.valueOf(mMemberSurveyStageFragments.size()),
                        String.valueOf(mMemberSurveyInfoViewPager.getCurrentItem() + 1)));
    }

    @Override
    public void onClick(View v) {
        int currentStage = mMemberSurveyInfoViewPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.member_survey_prev_page_image_button:
                if (currentStage < mMemberSurveyStageFragments.size()) {
                    mMemberSurveyInfoViewPager.setCurrentItem(currentStage - 1);
                    enableConfirmButton();
//                    mMemberSurveyInfoConfirmButtonTextView.setEnabled(true);
//                    mMemberSurveyInfoConfirmButtonTextView
//                            .setTextColor(getResources().getColorStateList(R.color.color_selector_primary_white));
//                    mMemberSurveyInfoConfirmButtonTextView
//                            .setBackgroundResource(R.drawable.selector_opaque_white_transparent_white);
                }
                break;
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
                        if (mRequestCode == IS_NOW_USED_USER_REQUEST_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("is_now_used_user", true);
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            getActivity().finish();
                        } else {
                            startActivity(SearchActivity.newIntent(getActivity(), true));
                            getActivity().finish();
                        }
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
                if (currentStage < mMemberSurveyStageFragments.size() - 1) {
                    mMemberSurveyInfoViewPager.setCurrentItem(currentStage + 1);

                } else if (currentStage == mMemberSurveyStageFragments.size() - 1) {
                    requestSubmitUserInfo(mUser);
//                    startActivity(SearchActivity.newIntent(getActivity()));
//                    getActivity().finish();
                }
                if (currentStage == 3 && !mUser.isHasChild()) {
                    requestSubmitUserInfo(mUser);
//                    startActivity(SearchActivity.newIntent(getActivity()));
//                    getActivity().finish();
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
        }
    }


    @Override
    public void onPageSelected(int position) {
        if (!isShownPage[position]) {
            disableConfirmButton();
        } else {
            enableConfirmButton();
        }

//        isShownPage[position] = true;

        if (position == 0) {
            mMemberSurveyPrevPageImageButton.setVisibility(View.GONE);
        } else {
            mMemberSurveyPrevPageImageButton.setVisibility(View.VISIBLE);
        }

        mMemberSurveyInfoProgressStageTextView.setText(
                getString(R.string.survey_info_progress_stage_format,
                        String.valueOf(mMemberSurveyStageFragments.size()),
                        String.valueOf(position + 1)));

        if (position == 4) {
            mMemberSurveyInfoConfirmButtonTextView.setText("정보 입력 마치고 시작하기");
        } else {
            mMemberSurveyInfoConfirmButtonTextView.setText("다 음");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 1:touch in; 2: touch out and find its rightful place; 0: stop;
//        Log.i(TAG, "onPageScrollStateChanged position : " + String.valueOf(state));
    }

    private boolean[] isShownPage = new boolean[]{false, false, false, false, false};

    public void updateConfirmButtonTextView(int stageNumber, boolean isCompleted) {
        isShownPage[stageNumber - 1] = isCompleted;
        switch (stageNumber) {
            case 1:case 2:case 3:case 4:case 5:
                if (isCompleted) {
                    enableConfirmButton();
                } else {
                    disableConfirmButton();
                }
                break;
        }

        if (stageNumber == 4) {
            if (isCompleted) {
                if (!mUser.isHasChild()) {
                    mMemberSurveyInfoConfirmButtonTextView.setText("정보 입력 마치고 시작하기");
                } else {
                    mMemberSurveyInfoConfirmButtonTextView.setText("다 음");
                }
            } else {
                if (!mUser.isHasChild()) {
                    mMemberSurveyInfoConfirmButtonTextView.setText("다 음");
                } else {
                    mMemberSurveyInfoConfirmButtonTextView.setText("다 음");
                }
            }
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
                // hasChild true : 0; hasChild false : 1;
                if (value == 0) {
                    mUser.setHasChild(true);
                } else {
                    mUser.setHasChild(false);
                }
                break;
            case 5:
                mUser.setChildHasDrySkin(false);
                mUser.setChildHasAllergy(false);
                if (value / 10 == 1) {
                    mUser.setChildHasDrySkin(true);
                }
                if (value % 10 == 1) {
                    mUser.setChildHasAllergy(true);
                }
                break;
        }
    }

    private void requestSubmitUserInfo(User user) {

        final CustomProgressDialog progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        Log.i(TAG, user.toString());

        Map<String, String> params = new HashMap<>();
        params.put(GENDER, String.valueOf(user.getGender()));
        params.put(BIRTH_YEAR, String.valueOf(user.getBirthYear()));
        params.put(HAS_DRY_SKIN, String.valueOf(user.getHasDrySkin()));
        params.put(HAS_OILY_SKIN, String.valueOf(user.getHasOilySkin()));
        params.put(HAS_ALLERGY, String.valueOf(user.getHasAllergy()));
        params.put(HAS_CHILD, String.valueOf(user.getHasChild()));
        params.put(CHILD_HAS_DRY_SKIN, String.valueOf(user.getChildHasDrySkin()));
        params.put(CHILD_HAS_ALLERGY, String.valueOf(user.getChildHasAllergy()));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, URL_HOST + PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        progressDialog.dismiss();

                        if (mRequestCode == IS_NOW_USED_USER_REQUEST_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("is_now_used_user", true);
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            getActivity().finish();
                        } else {
                            if (getActivity() instanceof MemberStartActivity) {
                                startActivity(SearchActivity.newIntent(getActivity(), true));
                                getActivity().finish();
                            } else if (getActivity() instanceof MemberActivity) {
                                getActivity().finish();
                                startActivity(MemberActivity.newIntent(getActivity(), true));
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void enableConfirmButton() {
        mMemberSurveyInfoConfirmButtonTextView.setEnabled(true);
        mMemberSurveyInfoConfirmButtonTextView
                .setTextColor(getResources().getColorStateList(R.color.color_selector_primary_white));
        mMemberSurveyInfoConfirmButtonTextView
                .setBackgroundResource(R.drawable.selector_opaque_white_transparent_white);


    }

    private void disableConfirmButton() {
        mMemberSurveyInfoConfirmButtonTextView.setEnabled(false);
        mMemberSurveyInfoConfirmButtonTextView
                .setTextColor(getResources().getColor(R.color.colorReef));
        mMemberSurveyInfoConfirmButtonTextView
                .setBackgroundResource(R.drawable.widget_solid_oval_rectangle_pale);
    }
}
