package com.planet.wondering.chemi.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.AppBaseActivity;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.view.fragment.IntroPageFragment;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 5. 8..
 */

public class IntroActivity extends AppBaseActivity implements View.OnClickListener {

    private static final String TAG = IntroActivity.class.getSimpleName();

    private ViewPager mIntroViewPager;
    private int[] mIndicatorIds;
    private TextView[] mIndicators;
    private TextView mIntroSkipButtonTextView;
    private Button mIntroEndButton;
    private IntroViewPagerAdapter mIntroViewPagerAdapter;
    private ArrayList<Integer> mIntroImageResIds;

    private ArrayList<Fragment> mIntroFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UserSharedPreferences.getStoredIntroSlide(getApplicationContext())) {
            Intent intent = new Intent(IntroActivity.this, MemberStartActivity.class);
            startActivity(intent);
            finish();
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

        setContentView(R.layout.activity_intro);
        mIntroViewPager = (ViewPager) findViewById(R.id.intro_view_pager);

        mIndicatorIds = new int[]{
                R.id.intro_indicator_1, R.id.intro_indicator_2, R.id.intro_indicator_3,
                R.id.intro_indicator_4, R.id.intro_indicator_5};
        mIndicators = new TextView[mIndicatorIds.length];
        for (int i = 0; i < mIndicators.length; i++) {
            mIndicators[i] = (TextView) findViewById(mIndicatorIds[i]);
        }
        handleIndicators(0);

        mIntroSkipButtonTextView = (TextView) findViewById(R.id.intro_skip_button_text_view);
        mIntroSkipButtonTextView.setOnClickListener(this);
        mIntroEndButton = (Button) findViewById(R.id.intro_end_button);
        mIntroEndButton.setOnClickListener(this);

//        mIntroImageResIds = new ArrayList<>();
//        mIntroImageResIds.add(R.drawable.intro_slide01);
//        mIntroImageResIds.add(R.drawable.intro_slide02);
//        mIntroImageResIds.add(R.drawable.intro_slide03);
//        mIntroImageResIds.add(R.drawable.intro_slide04);
//        mIntroImageResIds.add(R.drawable.intro_slide05);

//        mIntroViewPagerAdapter = new IntroViewPagerAdapter(mIntroImageResIds);
//        mIntroViewPager.setAdapter(mIntroViewPagerAdapter);

        mIntroFragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mIntroFragments.add(IntroPageFragment.newInstance(i));
        }
        mIntroViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mIntroFragments.get(position);
            }

            @Override
            public int getCount() {
                return mIntroFragments.size();
            }
        });
        mIntroViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handleIndicators(position);
                if (position == mIntroFragments.size() - 1) {
                    mIntroSkipButtonTextView.setVisibility(View.GONE);
                    mIntroEndButton.setVisibility(View.VISIBLE);
                } else {
                    mIntroSkipButtonTextView.setVisibility(View.VISIBLE);
                    mIntroEndButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.intro_skip_button_text_view:
                startMainActivity();
                break;
            case R.id.intro_end_button:
                startMainActivity();
                break;
        }
    }

    private void handleIndicators(int index) {
        for (int i = 0; i < mIndicators.length; i++) {
            if (i == index) {
                mIndicators[i].setBackgroundResource(R.drawable.widget_solid_oval_indicator_true);
            } else {
                mIndicators[i].setBackgroundResource(R.drawable.widget_solid_oval_indicator_false);
            }
        }
    }

    private void startMainActivity() {
        UserSharedPreferences.setStoredIntroSlide(getApplicationContext(), true);
        Intent intent = new Intent(IntroActivity.this, MemberStartActivity.class);
        startActivity(intent);
        finish();
    }

    private class IntroViewPagerAdapter extends PagerAdapter {

        private ArrayList<Integer> mIntroImageResIds;
        private LayoutInflater mLayoutInflater;

        public IntroViewPagerAdapter(ArrayList<Integer> introImageResIds) {
            mIntroImageResIds = introImageResIds;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
            mLayoutInflater = LayoutInflater.from(getApplicationContext());
            View view = mLayoutInflater.inflate(R.layout.list_item_intro, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.list_item_intro_image_image_view);

//            imageView.setImageResource(mIntroImageResIds.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mIntroImageResIds.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            View view = (View) object;
            container.removeView(view);
        }
    }
}
