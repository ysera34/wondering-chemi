package com.planet.wondering.chemi.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 3. 20..
 */

public class MemberConfigPartnerFragment extends Fragment implements View.OnClickListener {

    public static MemberConfigPartnerFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigPartnerFragment fragment = new MemberConfigPartnerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mBackLayout;
    private TextView mPartnerEmailTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_partner, container, false);

        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_partner_back_layout);
        mBackLayout.setOnClickListener(this);
        mPartnerEmailTextView = (TextView) view.findViewById(R.id.partner_email_text_view);
        mPartnerEmailTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_partner_back_layout:
                getActivity().onBackPressed();
                break;
            case R.id.partner_email_text_view:
                Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
                        "mailto", getString(R.string.administrator_support_email), null));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.administrator_support_email)});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.partner_email_subject));
                emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.partner_email_description));
                emailIntent = Intent.createChooser(emailIntent, getString(R.string.partner_email_title));
                startActivity(emailIntent);
                break;
        }
    }
}
