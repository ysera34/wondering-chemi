package com.planet.wondering.chemi.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 5. 14..
 */

public class MemberCongratulationDialogFragment extends DialogFragment {

    private static final String TAG = MemberCongratulationDialogFragment.class.getSimpleName();

    public static MemberCongratulationDialogFragment newInstance() {

        Bundle args = new Bundle();

        MemberCongratulationDialogFragment fragment = new MemberCongratulationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AlertDialog.Builder mBuilder;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_member_congratulation_dialog, null);

        mBuilder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return mBuilder.create();

    }
}
