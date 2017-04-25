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
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Content;
import com.planet.wondering.chemi.view.activity.ContentActivity;

import java.util.ArrayList;

import static com.planet.wondering.chemi.common.Common.CONTENT_COMMENT_TYPE;
import static com.planet.wondering.chemi.common.Common.HORIZONTAL_CONTENT_VIEW_TYPE;

/**
 * Created by yoon on 2017. 4. 20..
 */

public class ContentHorizontalFragment extends Fragment implements View.OnClickListener {

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

    private LinearLayout mContentHorizontalGuideLayout;
    private Animation mShakeAnimation;
    private ViewPager mContentImageViewPager;
    private ArrayList<Fragment> mContentImageFragments;
    private TextView mContentIndicatorTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ContentActivity) getActivity()).setStatusBarTranslucent(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mContent = (Content) getArguments().getSerializable(ARG_CONTENT);
        mContentImageFragments = new ArrayList<>();

        if (mContent != null) {
            for (int i = 0; i < mContent.getContentImagePaths().size(); i++) {
                mContentImageFragments.add(
                        ContentImageFragment.newInstance(mContent.getContentImagePaths().get(i)));
            }
            mContentImageFragments.add(CommentFragment.newInstance(
                    mContent.getId(), CONTENT_COMMENT_TYPE, HORIZONTAL_CONTENT_VIEW_TYPE));
        }

//        mShakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_left_right);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_horizontal, container, false);
//        mContentHorizontalGuideLayout = (LinearLayout) view.findViewById(R.id.content_horizontal_guide_layout);
//        view.findViewById(R.id.content_horizontal_guide_image_view).setAnimation(mShakeAnimation);
//        view.findViewById(R.id.content_horizontal_guide_text_view).setAnimation(mShakeAnimation);
//        view.findViewById(R.id.content_horizontal_guide_confirm_text_view).setOnClickListener(this);

        mContentImageViewPager = (ViewPager) view.findViewById(R.id.content_image_view_pager);

        mContentImageViewPager.setAdapter(
                new ContentImageAdapter(getChildFragmentManager(), mContentImageFragments));

        mContentImageViewPager.setOffscreenPageLimit(mContentImageFragments.size() - 1);
        mContentImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == mContent.getContentImagePaths().size()) {
                    mContentIndicatorTextView.setVisibility(View.GONE);
                    ((ContentActivity) getActivity()).showCommentEditText();
                    ((ContentActivity) getActivity()).setStatusBarTranslucent(false);
                    updateCommentList(false);
                } else {
                    mContentIndicatorTextView.setVisibility(View.VISIBLE);
                    ((ContentActivity) getActivity()).setStatusBarTranslucent(true);
                    ((ContentActivity) getActivity()).hideCommentEditText();
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
        ((ContentActivity) getActivity()).hideCommentEditText();
        mContentIndicatorTextView.setText(getString(R.string.content_indicator_format,
                String.valueOf(1), String.valueOf(mContent.getContentImagePaths().size())));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.content_horizontal_guide_confirm_text_view:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    mContentImageViewPager.getForeground().setAlpha(0);
//                    mShakeAnimation.cancel();
//                    mContentHorizontalGuideLayout.setVisibility(View.GONE);
//                }
//                break;
        }
    }

    public void updateCommentList(boolean isAddComment) {
        ((CommentFragment) mContentImageFragments.get(mContent.getContentImagePaths().size()))
                .updateCommentList(isAddComment);
    }

    public void commentNestedScroll() {
        ((CommentFragment) mContentImageFragments.get(mContent.getContentImagePaths().size()))
                .commentNestedScroll();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ContentActivity) getActivity()).setStatusBarTranslucent(false);
    }
}
