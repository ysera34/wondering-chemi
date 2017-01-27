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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.view.activity.ProductListActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
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

//        addSearchFragment(SearchPopularListFragment.newInstance(),
//                getString(R.string.search_popular_fragment_title));
//        addSearchFragment(SearchLatestListFragment.newInstance(),
//                getString(R.string.search_latest_fragment_title));
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
        startActivity(ProductListActivity.newIntent(getActivity(), searchWord));
    }

    private class TagCharacterAdapter extends ArrayAdapter<String> implements Filterable {

        private ArrayList<String> mTagResults;

        public TagCharacterAdapter(Context context, int resource) {
            super(context, resource);
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

        @NonNull
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        try {
                            String query = constraint.toString();
//                            requestTagCharacterList(query);
                            mTagResults = new TagCharacterTask().execute(query).get();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                        filterResults.values = mTagResults;
                        filterResults.count = mTagResults.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_tag_character, parent, false);

            String tag = mTagResults.get(position);
            TextView tagTextView = (TextView) view.findViewById(R.id.list_item_tag_name_text_view);
            tagTextView.setText(tag);

            return view;
        }

        private class TagCharacterTask extends AsyncTask<String, Void, ArrayList<String>> {

            @Override
            protected ArrayList<String> doInBackground(String... params) {
                try {
                    String url = URL_HOST + PATH + CHARACTER_QUERY + URLEncoder.encode(params[0], "UTF-8");
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

        private void requestTagCharacterList(String character) {

            String utf8Query = null;
            try {
                utf8Query = URLEncoder.encode(character, "utf-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "UnsupportedEncodingException : " + e.getMessage());
            }
            Log.i("url", URL_HOST + PATH + CHARACTER_QUERY + utf8Query);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, URL_HOST + PATH + CHARACTER_QUERY + utf8Query,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mTagResults = Parser.parseTagStringList(response);
                            for (String s : mTagResults) {
                                Log.i(TAG, s);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.getMessage());
                        }
                    }
            );

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
        }
    }
}