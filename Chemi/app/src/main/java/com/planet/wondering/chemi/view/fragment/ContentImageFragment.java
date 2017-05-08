package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 4. 21..
 */
public class ContentImageFragment extends Fragment {

    private static final String TAG = ContentImageFragment.class.getSimpleName();

    private static final String ARG_CONTENT_IMAGE_PATH = "content_image_path";

    public static ContentImageFragment newInstance() {

        Bundle args = new Bundle();

        ContentImageFragment fragment = new ContentImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ContentImageFragment newInstance(String contentImagePath) {

        Bundle args = new Bundle();
        args.putString(ARG_CONTENT_IMAGE_PATH, contentImagePath);

        ContentImageFragment fragment = new ContentImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView mContentPartialImageView;
    private String mContentPartialImagePath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mContentPartialImagePath = getArguments().getString(ARG_CONTENT_IMAGE_PATH, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_image, container, false);
        mContentPartialImageView = (ImageView) view.findViewById(R.id.content_partial_image_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity())
                .load(mContentPartialImagePath)
                .crossFade()
                .into(mContentPartialImageView);
    }
}
