package com.planet.wondering.chemi.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.content.Section;

/**
 * Created by yoon on 2017. 7. 4..
 */

public class ContentSectionFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ContentSectionFragment.class.getSimpleName();

    private static final String ARG_CONTENT_SECTION = "content_section";

    public static ContentSectionFragment newInstance() {

        Bundle args = new Bundle();

        ContentSectionFragment fragment = new ContentSectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ContentSectionFragment newInstance(Section section) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTENT_SECTION, section);

        ContentSectionFragment fragment = new ContentSectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Section mSection;
    private ImageView mContentExtendImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mSection = (Section) getArguments().getSerializable(ARG_CONTENT_SECTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_section, container, false);
        mContentExtendImageView = (ImageView) view.findViewById(R.id.content_extend_image_view);
        mContentExtendImageView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity())
                .load(mSection.getImagePath())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(mContentExtendImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_extend_image_view:
                if (mSection.getReferences() != null) {
                    String reference = mSection.getReferences().get(0).getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(reference));
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
        }
    }
}
