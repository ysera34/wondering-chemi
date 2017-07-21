package com.planet.wondering.chemi.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnDialogFinishedListener;

/**
 * Created by yoon on 2017. 3. 21..
 */

public class CustomAlertDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = CustomAlertDialogFragment.class.getSimpleName();

    public static final String LOGIN_DIALOG = "login_dialog";
    public static final String LOGOUT_DIALOG = "logout_dialog";
    public static final String CLEAR_DIALOG = "clear_dialog";
    public static final String REVOKE_DIALOG = "revoke_dialog";
    public static final String WITHDRAW_DIALOG = "withdraw_dialog";
    public static final String EXTRA_DIALOG = "extra_dialog";

    private static final String ARG_ICON_ID = "icon_id";
    private static final String ARG_TITLE_ID = "title_id";
    private static final String ARG_DESCRIPTION_ID = "message_id";
    private static final String ARG_POSITIVE_STRING_ID = "positive_string_id";
    private static final String ARG_REQUEST_CODE = "request_code";

    public static CustomAlertDialogFragment newInstance() {

        Bundle args = new Bundle();

        CustomAlertDialogFragment fragment = new CustomAlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    public static CustomAlertDialogFragment newInstance(int iconId, int messageId, int positiveStringId) {
//
//        Bundle args = new Bundle();
//        args.putInt(ARG_ICON_ID, iconId);
//        args.putInt(ARG_DESCRIPTION_ID, messageId);
//        args.putInt(ARG_POSITIVE_STRING_ID, positiveStringId);
//
//        CustomAlertDialogFragment fragment = new CustomAlertDialogFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static CustomAlertDialogFragment newInstance(
            int iconId, int descriptionId, int positiveStringId, int requestCode) {

        Bundle args = new Bundle();
        args.putInt(ARG_ICON_ID, iconId);
        args.putInt(ARG_DESCRIPTION_ID, descriptionId);
        args.putInt(ARG_POSITIVE_STRING_ID, positiveStringId);
        args.putInt(ARG_REQUEST_CODE, requestCode);

        CustomAlertDialogFragment fragment = new CustomAlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CustomAlertDialogFragment newInstance(int iconId,
            int titleId, int descriptionId, int positiveStringId, int requestCode) {

        Bundle args = new Bundle();
        args.putInt(ARG_ICON_ID, iconId);
        args.putInt(ARG_TITLE_ID, titleId);
        args.putInt(ARG_DESCRIPTION_ID, descriptionId);
        args.putInt(ARG_POSITIVE_STRING_ID, positiveStringId);
        args.putInt(ARG_REQUEST_CODE, requestCode);

        CustomAlertDialogFragment fragment = new CustomAlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mIconId;
    private int mTitleId;
    private int mDescriptionId;
    private int mPositiveStringId;
    private int mRequestCode;

    private ImageView mCustomAlertDialogIconImageView;
    private TextView mCustomAlertDialogMessageTextView;

    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private Button mCustomAlertDialogPositiveButton;
    private Button mCustomAlertDialogNegativeButton;

    private AlertDialog.Builder mBuilder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIconId = getArguments().getInt(ARG_ICON_ID, -1);
        mTitleId = getArguments().getInt(ARG_TITLE_ID, -1);
        mDescriptionId = getArguments().getInt(ARG_DESCRIPTION_ID, -1);
        mPositiveStringId = getArguments().getInt(ARG_POSITIVE_STRING_ID, -1);
        mRequestCode = getArguments().getInt(ARG_REQUEST_CODE, -1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_custom_alert_dialog, null);

//        mCustomAlertDialogIconImageView = (ImageView)
//                view.findViewById(R.id.custom_alert_dialog_icon_image_view);
//        mCustomAlertDialogMessageTextView = (TextView)
//                view.findViewById(R.id.custom_alert_dialog_message_text_view);
        mTitleTextView = (TextView)
                view.findViewById(R.id.custom_alert_dialog_title_text_view);
        mDescriptionTextView = (TextView)
                view.findViewById(R.id.custom_alert_dialog_description_text_view);
        mCustomAlertDialogPositiveButton = (Button)
                view.findViewById(R.id.custom_alert_dialog_positive_button);
        mCustomAlertDialogNegativeButton = (Button)
                view.findViewById(R.id.custom_alert_dialog_negative_button);

//        if (mIconId != -1) {
//            mCustomAlertDialogIconImageView.setImageResource(mIconId);
//        }
//        if (mDescriptionId != -1) {
//            mCustomAlertDialogMessageTextView.setText(getString(mDescriptionId));
//        }
        if (mTitleId == -1) {
            mTitleTextView.setVisibility(View.GONE);
        } else {
            mTitleTextView.setText(getString(mTitleId));
        }
        if (mDescriptionId == -1) {
            mDescriptionTextView.setVisibility(View.GONE);
        } else {
            mDescriptionTextView.setText(getString(mDescriptionId));
        }
        if (mPositiveStringId != -1) {
            mCustomAlertDialogPositiveButton.setText(getString(mPositiveStringId));
        }
        mCustomAlertDialogPositiveButton.setOnClickListener(this);
        mCustomAlertDialogNegativeButton.setOnClickListener(this);

        mBuilder = new AlertDialog.Builder(getActivity())
                .setView(view);
//                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        mDialogFinishedListener.onDialogFinished(true);
//                    }
//                })
//                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        mDialogFinishedListener.onDialogFinished(false);
//                    }
//                });
        return mBuilder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        int width = getResources().getDimensionPixelSize(R.dimen.custom_dialog_fragment_width);
//        int height = getResources().getDimensionPixelSize(R.dimen.custom_dialog_fragment_height);
//        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.widget_bg_custom_alert_dialog);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_alert_dialog_positive_button:
                getDialog().dismiss();
                mDialogFinishedListener.onDialogFinished(true, mRequestCode);
                break;
            case R.id.custom_alert_dialog_negative_button:
                getDialog().dismiss();
                mDialogFinishedListener.onDialogFinished(false, mRequestCode);
                break;

        }
    }

    OnDialogFinishedListener mDialogFinishedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mDialogFinishedListener = (OnDialogFinishedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnDialogFinishedListener");
        }
    }
}
