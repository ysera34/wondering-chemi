package com.planet.wondering.chemi.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planet.wondering.chemi.R;

import static com.planet.wondering.chemi.common.Common.APP_RELEASE_MARKET_URL;

/**
 * Created by yoon on 2017. 5. 14..
 */

public class MemberCongratulationDialogFragment extends DialogFragment
        implements View.OnClickListener {

    private static final String TAG = MemberCongratulationDialogFragment.class.getSimpleName();

    public static final String ARG_RELEASE = "release_key";

    public static MemberCongratulationDialogFragment newInstance() {

        Bundle args = new Bundle();

        MemberCongratulationDialogFragment fragment = new MemberCongratulationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberCongratulationDialogFragment newInstance(boolean isRelease) {

        Bundle args = new Bundle();
        args.putBoolean(ARG_RELEASE, isRelease);

        MemberCongratulationDialogFragment fragment = new MemberCongratulationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AlertDialog.Builder mBuilder;
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private TextView mConfirmButtonTextView;

    private boolean mRelease;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRelease = getArguments().getBoolean(ARG_RELEASE, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_member_congratulation_dialog, null);
        mTitleTextView = (TextView) view
                .findViewById(R.id.congratulation_title_text_view);
        mDescriptionTextView = (TextView) view
                .findViewById(R.id.congratulation_description_text_view);
        mConfirmButtonTextView = (TextView) view
                .findViewById(R.id.member_congratulation_confirm_button_text_view);
        mConfirmButtonTextView.setOnClickListener(this);

        mBuilder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return mBuilder.create();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.widget_bg_custom_alert_dialog);
        setCancelable(false);
//        android:text="케미 회원가입이 완료되었습니다.\n케미와 함께 더욱 안전한 가정을 꾸려보세요."
        if (mRelease) {
            mTitleTextView.setText("감사합니다.");
            mDescriptionTextView.setText("그동안 테스트에 참여해 주셔서 감사합니다.\n케미의 정식 버전 링크를 준비하였습니다.\n아래 버튼으로 새로운 케미를 만나보세요 :)");
            mConfirmButtonTextView.setText("지금 확인하기");
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_congratulation_confirm_button_text_view:
                if (!mRelease) {
                    getDialog().dismiss();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(APP_RELEASE_MARKET_URL)));
                    ActivityCompat.finishAffinity(getActivity());
                }
                break;
        }
    }
}
