package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 5. 8..
 */

public class IntroPageFragment extends Fragment {

    private static final String TAG = IntroPageFragment.class.getSimpleName();

    private static final String ARG_INTRO_PAGE_ID = "intro_page_id";

    public static IntroPageFragment newInstance() {

        Bundle args = new Bundle();

        IntroPageFragment fragment = new IntroPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static IntroPageFragment newInstance(int pageId) {

        Bundle args = new Bundle();
        args.putInt(ARG_INTRO_PAGE_ID, pageId);

        IntroPageFragment fragment = new IntroPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView mTitleTextView;
    private TextView mSubTitleTextView;
    private ImageView mImageImageView;

    private int mPageId;
    private ArrayList<Integer> mIntroImages;
    private String[] mIntroMessageArray;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageId = getArguments().getInt(ARG_INTRO_PAGE_ID, -1);

        mIntroImages = new ArrayList<>();
        mIntroImages.add(R.drawable.intro_slide01);
        mIntroImages.add(R.drawable.intro_slide02);
        mIntroImages.add(R.drawable.intro_slide03);
        mIntroImages.add(R.drawable.intro_slide04);
        mIntroImages.add(R.drawable.intro_slide05);

        mIntroMessageArray = getResources().getStringArray(R.array.intro_slide_message_array);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_page, container, false);
        mTitleTextView = (TextView) view.findViewById(R.id.intro_title_text_view);
        mSubTitleTextView = (TextView) view.findViewById(R.id.intro_sub_title_text_view);
        mImageImageView = (ImageView) view.findViewById(R.id.intro_image_image_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleTextView.setText(mIntroMessageArray[mPageId * 2]);
        mSubTitleTextView.setText(mIntroMessageArray[(mPageId * 2) + 1]);
        mImageImageView.setImageResource(mIntroImages.get(mPageId));
    }
}
