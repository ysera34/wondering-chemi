package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.LOGOUT_DIALOG;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.REVOKE_DIALOG;

/**
 * Created by yoon on 2017. 2. 11..
 */

public class MemberConfigProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberConfigProfileFragment.class.getSimpleName();

    private static final String ARG_CONFIG_USER = "config_user";

    public static MemberConfigProfileFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigProfileFragment fragment = new MemberConfigProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberConfigProfileFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIG_USER, user);

        MemberConfigProfileFragment fragment = new MemberConfigProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CircleImageView mUserCircleImageView;
    private CircleImageView mCameraCircleImageView;

    private LinearLayout mParentLayout;
    private TextView mParentAgeTextView;
    private TextView mParentDrySkinTextView;
    private TextView mParentOilySkinTextView;
    private TextView mParentDryOilySkinTextView;
    private TextView mParentAllergyTextView;

    private LinearLayout mChildLayout;
    private TextView mChildDrySkinTextView;
    private TextView mChildAllergyTextView;

    private int[] mConfigProfileLayoutIds;
    private RelativeLayout[] mConfigProfileLayouts;

    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mConfigProfileLayoutIds = new int[]{
                R.id.member_config_profile_name_layout, R.id.member_config_profile_change_password_layout,
                R.id.member_config_profile_change_survey_layout, R.id.member_config_profile_sign_out_layout,
                R.id.member_config_profile_revoke_layout,};
        mConfigProfileLayouts = new RelativeLayout[mConfigProfileLayoutIds.length];

        mUser = (User) getArguments().getSerializable(ARG_CONFIG_USER);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_profile, container, false);

        mUserCircleImageView = (CircleImageView) view.findViewById(R.id.member_config_profile_user_image_view);
        mCameraCircleImageView = (CircleImageView) view.findViewById(R.id.member_config_profile_camera_image_view);

        for (int i = 0; i < mConfigProfileLayoutIds.length; i++) {
            mConfigProfileLayouts[i] = (RelativeLayout) view.findViewById(mConfigProfileLayoutIds[i]);
            mConfigProfileLayouts[i].setOnClickListener(this);
        }

        mParentLayout = (LinearLayout) view.findViewById(R.id.member_config_profile_parent_layout);
        mParentAgeTextView = (TextView) view.findViewById(R.id.member_config_profile_parent_age_text_view);
        mParentDrySkinTextView = (TextView) view.findViewById(R.id.member_config_profile_parent_dry_skin_text_view);
        mParentOilySkinTextView = (TextView) view.findViewById(R.id.member_config_profile_parent_oily_skin_text_view);
        mParentDryOilySkinTextView = (TextView) view.findViewById(R.id.member_config_profile_parent_dry_oily_skin_text_view);
        mParentAllergyTextView = (TextView) view.findViewById(R.id.member_config_profile_parent_allergy_text_view);

        mChildLayout = (LinearLayout) view.findViewById(R.id.member_config_profile_child_layout);
        mChildDrySkinTextView = (TextView) view.findViewById(R.id.member_config_profile_child_dry_skin_text_view);
        mChildAllergyTextView = (TextView) view.findViewById(R.id.member_config_profile_child_allergy_text_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mUser.getImagePath() == null || mUser.getImagePath().equals("null")) {
            if (mUser.isHasExtraInfo()) {
                if (mUser.isGender()) {
                    mUserCircleImageView.setImageResource(R.drawable.ic_user_profile_mommy);
                } else {
                    mUserCircleImageView.setImageResource(R.drawable.ic_user_profile_daddy);
                }
            } else {
                mUserCircleImageView.setImageResource(R.drawable.ic_user);
            }
        } else {
            Glide.with(getActivity())
                    .load(mUser.getImagePath())
                    .crossFade()
                    .into(mUserCircleImageView);
        }

        if (mUser.isHasDrySkin() && mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.VISIBLE);
        } else if (mUser.isHasDrySkin() && !mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.VISIBLE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        } else if (!mUser.isHasDrySkin() && mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.VISIBLE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        } else if (!mUser.isHasDrySkin() && !mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        }

        if (mUser.isHasAllergy()) {
            mParentAllergyTextView.setVisibility(View.VISIBLE);
        } else {
            mParentAllergyTextView.setVisibility(View.GONE);
        }

        if (mUser.isHasChild()) {
            mChildLayout.setVisibility(View.VISIBLE);

            if (mUser.isChildHasDrySkin()) {
                mChildDrySkinTextView.setVisibility(View.VISIBLE);
            } else {
                mChildDrySkinTextView.setVisibility(View.GONE);
            }

            if (mUser.isChildHasAllergy()) {
                mChildAllergyTextView.setVisibility(View.VISIBLE);
            } else {
                mChildAllergyTextView.setVisibility(View.GONE);
            }

        } else {
            mChildLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_profile_name_layout:
                mMenuSelectedListener.onMenuSelected(11);
                break;
            case R.id.member_config_profile_change_password_layout:
                mMenuSelectedListener.onMenuSelected(12);
                break;
            case R.id.member_config_profile_change_survey_layout:
                mMenuSelectedListener.onMenuSelected(13);
                break;
            case R.id.member_config_profile_sign_out_layout:
                CustomAlertDialogFragment dialogFragment = CustomAlertDialogFragment
                        .newInstance(R.drawable.ic_logout, R.string.logout_info_message, R.string.logout_button_title);
                dialogFragment.show(getFragmentManager(), LOGOUT_DIALOG);
                mDialogType = 1;
                break;
            case R.id.member_config_profile_revoke_layout:
                CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                        .newInstance(R.drawable.ic_logout, R.string.revoke_info_message, R.string.revoke_button_title);
                dialogFragment1.show(getFragmentManager(), REVOKE_DIALOG);
                mDialogType = 2;
                break;
        }
    }

    private byte mDialogType = 0;

    public void onDialogFinished(boolean isChose) {
        if (isChose) {
            switch (mDialogType) {
                case 1:
                    UserSharedPreferences.removeStoredToken(getActivity());
                    Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                    mDialogType = 0;
                    break;
                case 2:
                    Toast.makeText(getActivity(), "연동해제 되었습니다.", Toast.LENGTH_SHORT).show();
                    mDialogType = 0;
                    break;
            }
        }
    }

    OnMenuSelectedListener mMenuSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mMenuSelectedListener = (OnMenuSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMenuSelectedListener");
        }
    }
}
