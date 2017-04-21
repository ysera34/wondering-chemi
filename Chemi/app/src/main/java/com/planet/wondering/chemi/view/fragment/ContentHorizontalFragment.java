package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Content;

import java.util.ArrayList;

import static com.planet.wondering.chemi.common.Common.CONTENT_COMMENT_TYPE;

/**
 * Created by yoon on 2017. 4. 20..
 */

public class ContentHorizontalFragment extends Fragment {

    private static final String TAG = ContentHorizontalFragment.class.getSimpleName();

    private static final String ARG_CONTENT = "content";

    public static ContentHorizontalFragment newInstance() {

        Bundle args = new Bundle();

        ContentHorizontalFragment fragment = new ContentHorizontalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ContentHorizontalFragment newInstance(Content content) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTENT, content);

        ContentHorizontalFragment fragment = new ContentHorizontalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Content mContent;

    private ViewPager mContentImageViewPager;
    private ArrayList<Fragment> mContentImageFragments;
    private TextView mContentIndicatorTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mContent = (Content) getArguments().getSerializable(ARG_CONTENT);
        mContentImageFragments = new ArrayList<>();

        if (mContent != null) {
            for (int i = 0; i < mContent.getContentImagePaths().size(); i++) {
                mContentImageFragments.add(
                        ContentImageFragment.newInstance(mContent.getContentImagePaths().get(i)));
            }
            mContentImageFragments.add(CommentFragment.newInstance(mContent.getId(), CONTENT_COMMENT_TYPE));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_horizontal, container, false);
        mContentImageViewPager = (ViewPager) view.findViewById(R.id.content_image_view_pager);
        mContentImageViewPager.setAdapter(
                new ContentImageAdapter(getChildFragmentManager(), mContentImageFragments));
        mContentImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mContent.getContentImagePaths().size()) {
                    mContentIndicatorTextView.setVisibility(View.GONE);
                } else {
                    mContentIndicatorTextView.setVisibility(View.VISIBLE);
                }
                mContentIndicatorTextView.setText(getString(R.string.content_indicator_format,
                        String.valueOf(position + 1), String.valueOf(mContent.getContentImagePaths().size())));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mContentIndicatorTextView = (TextView) view.findViewById(R.id.content_indicator_text_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentIndicatorTextView.setText(getString(R.string.content_indicator_format,
                String.valueOf(1), String.valueOf(mContent.getContentImagePaths().size())));
    }

    private class ContentImageAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> mContentImageFragments;

        public ContentImageAdapter(FragmentManager fm, ArrayList<Fragment> contentImageFragments) {
            super(fm);
            mContentImageFragments = contentImageFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mContentImageFragments.get(position);
        }

        @Override
        public int getCount() {
            return mContentImageFragments.size();
        }
    }

}
