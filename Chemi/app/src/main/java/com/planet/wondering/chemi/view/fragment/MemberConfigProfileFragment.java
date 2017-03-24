package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.LOGOUT_DIALOG;

/**
 * Created by yoon on 2017. 2. 11..
 */

public class MemberConfigProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberConfigProfileFragment.class.getSimpleName();

    public static MemberConfigProfileFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigProfileFragment fragment = new MemberConfigProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CircleImageView mUserCircleImageView;
    private CircleImageView mCameraCircleImageView;
    private int[] mConfigProfileLayoutIds;
    private RelativeLayout[] mConfigProfileLayouts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mConfigProfileLayoutIds = new int[]{
                R.id.member_config_profile_name_layout, R.id.member_config_profile_change_password_layout,
                R.id.member_config_profile_change_survey_layout, R.id.member_config_profile_sign_out_layout,};
        mConfigProfileLayouts = new RelativeLayout[mConfigProfileLayoutIds.length];
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                break;
        }
    }

    public void onDialogFinished(boolean isChose) {
        if (isChose) {
            UserSharedPreferences.removeStoredToken(getActivity());
            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
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
