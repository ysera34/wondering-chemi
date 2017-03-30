package com.planet.wondering.chemi.view.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.view.activity.ProductListActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.planet.wondering.chemi.network.Config.Tag.Key.CHARACTER_QUERY;
import static com.planet.wondering.chemi.network.Config.Tag.PATH;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class SearchDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SearchDetailFragment.class.getSimpleName();

    private AutoCompleteTextView mSearchAutoCompleteTextView;
    private TagCharacterAdapter mTagCharacterAdapter;
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
                    startActivity(ProductListActivity.newIntent(
                            getActivity(), textView.getText().toString()));
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

        mTagCharacterAdapter = new TagCharacterAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
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

    private class TagCharacterAdapter extends ArrayAdapter<String> implements Filterable {

        private ArrayList<String> mTagRequestResults;
        private ArrayList<String> mTagResults;

        public TagCharacterAdapter(Context context, int resource) {
            super(context, resource);
            mTagRequestResults = new ArrayList<>();
            mTagResults = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mTagResults.size();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return mTagResults.get(position);
        }

        public void refreshAdapter(ArrayList<String> tagResults) {
            mTagResults.clear();
            mTagResults.addAll(tagResults);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                private char mTagCharacter = '\u0000';

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        try {
                            mTagResults.clear();
                            String query = constraint.toString();
//                            Log.i("query", query);
//                            Log.i("deComposer", decomposeKoreanSyllable(constraint.toString()));

                            String currentTextUnSpaced = constraint.toString().replaceAll("\\s+", "");
                            int currentTextLength = currentTextUnSpaced.length();

                            if ((currentTextLength) < 1) {
                                mTagRequestResults.clear();
                                mTagCharacter = '\u0000';
                                filterResults.values = mTagRequestResults;
                                filterResults.count = mTagRequestResults.size();
                                return filterResults;
                            }

                            String currentTextDecomposed = decomposeKoreanSyllable(currentTextUnSpaced);
                            currentTextLength = currentTextDecomposed.length();

                            char firstCharacter = currentTextDecomposed.charAt(0);
                            if (firstCharacter != mTagCharacter) {
                                mTagRequestResults = new TagCharacterTask().execute(Character.toString(firstCharacter)).get();
                                mTagCharacter = firstCharacter;
                            }

                            for (int i = 0; i < mTagRequestResults.size(); i++) {
                                String theTag = mTagRequestResults.get(i);
                                String theTagUnSpaced = theTag.replaceAll("\\s+", "");
                                String theTagDecomposed = decomposeKoreanSyllable(theTagUnSpaced);
                                int theTagDecomposedLength = theTagDecomposed.length();

                                if (theTagDecomposedLength < currentTextLength) {
                                    continue;
                                }

                                int j;
                                for (j = 0; j <currentTextLength; j++) {
                                    if (Character.toUpperCase(theTagDecomposed.charAt(j)) != Character.toUpperCase(currentTextDecomposed.charAt(j))
                                            && currentTextDecomposed.charAt(j) != ((char) 0x11A7) && j != currentTextLength - 1) {
                                        break;
                                    } else if (j == currentTextLength - 1 && j % 3 == 2 && currentTextDecomposed.charAt(j) != ((char) 0x11A7)
                                            && theTagDecomposedLength > currentTextLength
                                            && convertConsonantToStandAlone(currentTextDecomposed.charAt(j)) != theTagDecomposed.charAt(j + 1)
                                            && currentTextDecomposed.charAt(j) != theTagDecomposed.charAt(j)) {
                                        break;
                                    }
                                }
                                if (j == currentTextLength) {
                                    mTagResults.add(theTag);
                                }
                            }

                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                        filterResults.values = mTagResults;
                        filterResults.count = mTagResults.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, final FilterResults results) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (results != null && results.count > 0) {
                                notifyDataSetChanged();
                            } else {
                                notifyDataSetInvalidated();
                            }
                        }
                    });
                }

                public char convertConsonantToStandAlone(char consonant) {
                    int consonantCode = (int) consonant;
                    char theConsonant = consonant;

                    switch (consonantCode) {
                        case 4352: case 4520: case 4528:
                            theConsonant='ㄱ';
                            break;
                        case 4353: case 4521:
                            theConsonant='ㄲ';
                            break;
                        case 4354: case 4523:
                            theConsonant='ㄴ';
                            break;
                        case 4355: case 4526:
                            theConsonant='ㄷ';
                            break;
                        case 4356:
                            theConsonant='ㄸ';
                            break;
                        case 4357: case 4527:
                            theConsonant='ㄹ';
                            break;
                        case 4358: case 4529: case 4535:
                            theConsonant='ㅁ';
                            break;
                        case 4359: case 4530: case 4536:
                            theConsonant='ㅂ';
                            break;
                        case 4360:
                            theConsonant='ㅃ';
                            break;
                        case 4361: case 4522: case 4531: case 4537: case 4538:
                            theConsonant='ㅅ';
                            break;
                        case 4362: case 4539:
                            theConsonant='ㅆ';
                            break;
                        case 4363: case 4540:
                            theConsonant='ㅇ';
                            break;
                        case 4364: case 4524: case 4541:
                            theConsonant='ㅈ';
                            break;
                        case 4365:
                            theConsonant='ㅉ';
                            break;
                        case 4366: case 4542:
                            theConsonant='ㅊ';
                            break;
                        case 4367: case 4543:
                            theConsonant='ㅋ';
                            break;
                        case 4368: case 4532: case 4544:
                            theConsonant='ㅌ';
                            break;
                        case 4369: case 4533: case 4545:
                            theConsonant='ㅍ';
                            break;
                        case 4370: case 4525: case 4534: case 4546:
                            theConsonant='ㅎ';
                            break;
                    }
                    return theConsonant;
                }

                public String decomposeKoreanSyllable(String word) {
                    StringBuilder theWord = new StringBuilder();
                    int length = word.length();
                    for(int i = 0; i < length; i++) {
                        char theCharacter = word.charAt(i);
                        if (theCharacter >= '가' && theCharacter <= '힣') {
                            int consonant2 = (theCharacter - 0xAC00) % 28;
                            int vowel = ((theCharacter - 0xAC00) / 28) % 21;
                            int consonant1 = ((theCharacter - 0xAC00) / 28) / 21;
                            char theConsonant2 = (char) (consonant2 + 0x11A8 - 1);
                            char theVowel = (char) (vowel + 0x1161);
                            char theConsonant1 = (char) (consonant1 + 0x1100);

                            theConsonant1 = convertConsonantToStandAlone(theConsonant1);
                            theWord = theWord.append(theConsonant1).append(theVowel).append(theConsonant2);
                        } else {
                            theWord = theWord.append(theCharacter).append((char) 0x11A7).append((char) 0x11A7);
                        }
                    }
                    return theWord.toString();
                }

            };
            return filter;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_item_tag_character, parent, false);
                convertView.setTag(new TagCharacterHolder(convertView));
            }
            initializeViews(getItem(position), (TagCharacterHolder) convertView.getTag());
            return convertView;
        }

        private void initializeViews(String object, TagCharacterHolder holder) {
            //TODO implement
            holder.listItemTagNameTextView.setText(object);
        }

        protected class TagCharacterHolder {
            private TextView listItemTagNameTextView;

            public TagCharacterHolder(View view) {
                listItemTagNameTextView = (TextView) view.findViewById(R.id.list_item_tag_name_text_view);
            }
        }

        private class TagCharacterTask extends AsyncTask<String, Void, ArrayList<String>> {

            @Override
            protected ArrayList<String> doInBackground(String... params) {
                try {
                    String url = URL_HOST + PATH + CHARACTER_QUERY + URLEncoder.encode(params[0], "UTF-8");
                    Log.i("url", url);
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    in.close();
                    connection.disconnect();
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    return Parser.parseTagStringList(jsonObject);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    return null;
                }
            }
        }
    }
}