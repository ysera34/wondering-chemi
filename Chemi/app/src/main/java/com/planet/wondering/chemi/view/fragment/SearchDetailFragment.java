package com.planet.wondering.chemi.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.adapter.TagCharacterAdapter;
import com.planet.wondering.chemi.view.activity.ProductListActivity;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class SearchDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SearchDetailFragment.class.getSimpleName();

    private static final String ARG_CATEGORY_GROUP_ID = "category_group_id";

    private AutoCompleteTextView mSearchAutoCompleteTextView;
    private TagCharacterAdapter mTagCharacterAdapter;
    private RelativeLayout mSearchClearLayout;
    private ImageButton mSearchClearImageButton;
    private ImageButton mSearchImageButton;

    private TabLayout mSearchTabLayout;
    private ViewPager mSearchViewPager;
    private ArrayList<Fragment> mSearchListFragments;
    private ArrayList<String> mSearchListFragmentTitles;

    public static SearchDetailFragment newInstance() {

        Bundle args = new Bundle();

        SearchDetailFragment fragment = new SearchDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSearchListFragments = new ArrayList<>();
        mSearchListFragmentTitles = new ArrayList<>();

        addSearchFragment(TagPopularListFragment.newInstance(),
                getString(R.string.search_popular_fragment_title));
        addSearchFragment(TagLatestListFragment.newInstance(),
                getString(R.string.search_latest_fragment_title));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_detail, container, false);
        mSearchAutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.search_auto_text_view);
        mSearchAutoCompleteTextView.setThreshold(1);
        mSearchAutoCompleteTextView.setOnClickListener(this);
        mSearchAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mSearchClearImageButton.setVisibility(View.VISIBLE);
                }
                if (charSequence.length() == 0) {
                    mSearchClearImageButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSearchAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (textView.getText().length() == 0) {
                        Toast.makeText(getActivity(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(ProductListActivity.newIntent(
                                getActivity(), textView.getText().toString()));
                    }
                }
                return false;
            }
        });

        mSearchClearLayout = (RelativeLayout) view.findViewById(R.id.search_clear_image_layout);
        mSearchClearLayout.setOnClickListener(this);
        mSearchClearImageButton = (ImageButton) view.findViewById(R.id.search_clear_image_button);
        mSearchClearImageButton.setOnClickListener(this);
        mSearchImageButton = (ImageButton) view.findViewById(R.id.search_image_button);
        mSearchImageButton.setOnClickListener(this);

        mSearchTabLayout = (TabLayout) view.findViewById(R.id.search_tabLayout);
        mSearchViewPager = (ViewPager) view.findViewById(R.id.search_viewPager);

        FragmentManager fm = getChildFragmentManager();
        mSearchViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return mSearchListFragments.get(position);
            }

            @Override
            public int getCount() {
                return mSearchListFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mSearchListFragmentTitles.get(position);
            }
        });
        mSearchViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mSearchViewPager.getCurrentItem() == 0) {
                    mSearchAutoCompleteTextView.setCursorVisible(false);
                } else if (mSearchViewPager.getCurrentItem() == 1) {
                    mSearchAutoCompleteTextView.setCursorVisible(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mSearchTabLayout.setupWithViewPager(mSearchViewPager);

        mTagCharacterAdapter = new TagCharacterAdapter(getActivity(), mSearchAutoCompleteTextView,
                android.R.layout.simple_dropdown_item_1line, 1);
        mSearchAutoCompleteTextView.setAdapter(mTagCharacterAdapter);
        mSearchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tag = mTagCharacterAdapter.getItem(position);
                startActivity(ProductListActivity.newIntent(getActivity(), tag));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSearchAutoCompleteTextView.getText().length() > 0) {
            mSearchViewPager.setCurrentItem(1);
            mSearchAutoCompleteTextView.requestFocus();
            mSearchAutoCompleteTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mSearchAutoCompleteTextView, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 200);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_auto_text_view:
                mSearchViewPager.setCurrentItem(1);
                break;
            case R.id.search_clear_image_layout:
            case R.id.search_clear_image_button:
                mSearchAutoCompleteTextView.getText().clear();
                break;
            case R.id.search_image_button:
                if (mSearchAutoCompleteTextView.getText().length() == 0) {
                    Toast.makeText(getActivity(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(ProductListActivity.newIntent(
                            getActivity(), mSearchAutoCompleteTextView.getText().toString()));
                }
                break;
        }
    }

    private void addSearchFragment(Fragment fragment, String title) {
        mSearchListFragments.add(fragment);
        mSearchListFragmentTitles.add(title);
    }

    public void updateSearchEditText(String searchWord) {
        mSearchAutoCompleteTextView.setText(String.valueOf(searchWord));
        mSearchAutoCompleteTextView.setSelection(searchWord.length());
        startActivity(ProductListActivity.newIntent(getActivity(), searchWord));
    }
}
