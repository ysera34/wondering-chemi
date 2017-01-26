package com.planet.wondering.chemi.view.fragment;


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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.activity.ProductListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class SearchDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SearchDetailFragment.class.getSimpleName();

    private AutoCompleteTextView mSearchAutoCompleteTextView;
    private List<String> mSearchResults;
    private ArrayAdapter<String> mSearchResultsAdapter;
    private RelativeLayout mSearchClearLayout;
    private ImageButton mSearchClearImageButton;

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

//        mSearchResults = new ArrayList<>();
        String[] sampleArr = getResources().getStringArray(R.array.search_popular_word_array);
        mSearchResults = Arrays.asList(sampleArr);

        mSearchListFragments = new ArrayList<>();
        mSearchListFragmentTitles = new ArrayList<>();

//        addSearchFragment(SearchPopularListFragment.newInstance(),
//                getString(R.string.search_popular_fragment_title));
        addSearchFragment(TagPopularListFragment.newInstance(),
                getString(R.string.search_popular_fragment_title));
        addSearchFragment(SearchLatestListFragment.newInstance(),
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
                    mSearchResultsAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_search_wordpart, mSearchResults);
                    mSearchAutoCompleteTextView.setAdapter(mSearchResultsAdapter);
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

//                    getActivity().getSupportFragmentManager()
//                            .beginTransaction()
//                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                            .replace(R.id.fragment_container, ProductListFragment.newInstance())
//                            .addToBackStack(null)
//                            .commit();
                    startActivity(ProductListActivity.newIntent(getActivity()));
                }
                return false;
            }
        });

        mSearchClearLayout = (RelativeLayout) view.findViewById(R.id.search_clear_image_layout);
        mSearchClearLayout.setOnClickListener(this);
        mSearchClearImageButton = (ImageButton) view.findViewById(R.id.search_clear_image_button);
        mSearchClearImageButton.setOnClickListener(this);

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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_auto_text_view:
                mSearchViewPager.setCurrentItem(1);
                break;
            case R.id.search_clear_image_layout:
                mSearchAutoCompleteTextView.getText().clear();
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
    }
}
