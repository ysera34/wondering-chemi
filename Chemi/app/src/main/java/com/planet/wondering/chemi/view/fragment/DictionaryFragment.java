package com.planet.wondering.chemi.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.CTag;
import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.planet.wondering.chemi.network.Config.Chemical.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.Tag.CTAG_PATH;
import static com.planet.wondering.chemi.network.Config.Tag.Key.CHARACTER_QUERY;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class DictionaryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DictionaryFragment.class.getSimpleName();

    public static DictionaryFragment newInstance() {

        Bundle args = new Bundle();

        DictionaryFragment fragment = new DictionaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Transition mTransition;

    private InputMethodManager mInputMethodManager;

    private LinearLayout mDictionaryHeaderLayout;
    private RelativeLayout mDictionaryLogoLayout;
    private RelativeLayout mDictionarySearchLayout;
    private AutoCompleteTextView mSearchEditText;
    private RelativeLayout mSearchClearLayout;
    private ImageButton mSearchClearImageButton;
    private ImageView mSearchImageView;

    private ChemicalCharacterAdapter mChemicalCharacterAdapter;

    private FrameLayout mDictionaryFragmentContainerFrameLayout;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        mDictionaryHeaderLayout = (LinearLayout) view.findViewById(R.id.dictionary_header_layout);
        mDictionaryLogoLayout = (RelativeLayout) view.findViewById(R.id.dictionary_logo_layout);
        mDictionarySearchLayout = (RelativeLayout) view.findViewById(R.id.dictionary_search_layout);
        mSearchEditText = (AutoCompleteTextView) view.findViewById(R.id.dictionary_search_edit_text);
        mSearchEditText.setThreshold(1);
        mSearchEditText.setOnClickListener(this);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    mSearchClearImageButton.setVisibility(View.VISIBLE);
                }
                if (charSequence.length() == 0) {
                    mSearchClearImageButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchClearLayout = (RelativeLayout) view.findViewById(R.id.dictionary_search_clear_image_layout);
        mSearchClearLayout.setOnClickListener(this);
        mSearchClearImageButton = (ImageButton) view.findViewById(R.id.dictionary_search_clear_image_button);
        mSearchClearImageButton.setOnClickListener(this);

        mSearchImageView = (ImageView) view.findViewById(R.id.dictionary_search_image_view);
        mSearchImageView.setOnClickListener(this);

        mChemicalCharacterAdapter = new ChemicalCharacterAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        mSearchEditText.setAdapter(mChemicalCharacterAdapter);
        mSearchEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mInputMethodManager.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);

                CTag cTag = mChemicalCharacterAdapter.getItem(position);

                mSearchEditText.setText(cTag.getDescription());
                mSearchEditText.setSelection(cTag.getDescription().length());

                requestChemical(cTag.getChemicalId(), cTag.isCorrect());
            }
        });

        mDictionaryFragmentContainerFrameLayout = (FrameLayout) view.findViewById(R.id.dictionary_fragment_container);
        mFragmentManager = getChildFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.dictionary_fragment_container);

        if (mFragment == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.dictionary_fragment_container,
                            ChemicalLatestListFragment.newInstance((byte) -1))
                    .commit();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dictionary_search_edit_text:
                if (!isSearchMode) {
                    showSearchMode();
                    isSearchMode = true;
                } else {
//                    hideSearchMode();
//                    isSearchMode = false;
                }
                break;
            case R.id.dictionary_search_image_view:
                if (!isSearchMode) {
                    showSearchMode();
                    isSearchMode = true;
                } else {
                    Toast.makeText(getActivity(), "검색창 안에서 텍스트로 검색", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.dictionary_search_clear_image_layout:
            case R.id.dictionary_search_clear_image_button:
                mSearchEditText.getText().clear();
                mInputMethodManager.showSoftInput(mSearchEditText, 0);
                Fragment fragment = mFragmentManager.findFragmentById(R.id.dictionary_fragment_container);
                if (fragment instanceof ChemicalFragment) {
                    mFragmentManager.beginTransaction()
                            .replace(R.id.dictionary_fragment_container,
                                    ChemicalLatestListFragment.newInstance((byte) -1))
                            .commit();
                }
                break;
        }
    }

    private boolean isSearchMode = false;

    public void showSearchMode() {
        mDictionaryHeaderLayout.animate().translationY(-mDictionaryLogoLayout.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));

        mTransition = TransitionInflater.from(getActivity())
                .inflateTransition(R.transition.dictionary_edit_text_transition);
        TransitionManager.beginDelayedTransition(mDictionarySearchLayout, mTransition);

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_container, DetailFragment.newInstance())
//                .commit();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        mSearchEditText.setLayoutParams(params);

        ViewGroup.LayoutParams layoutParams = mSearchEditText.getLayoutParams();
//        layoutParams.width = 1080;
//        layoutParams.height = 300;
        layoutParams.width = mSearchEditText.getWidth() * 5 / 6;

        mSearchEditText.setLayoutParams(layoutParams);
        mSearchImageView.setImageResource(R.drawable.ic_search_white_36dp);

        mDictionaryFragmentContainerFrameLayout.animate().translationY(-mDictionaryLogoLayout.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
    }

    public void hideSearchMode() {
        mDictionaryHeaderLayout.animate().translationY(0)
                .setInterpolator(new AccelerateInterpolator(2));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        mSearchEditText.setLayoutParams(params);

        ViewGroup.LayoutParams layoutParams = mSearchEditText.getLayoutParams();

        layoutParams.width = mSearchEditText.getWidth() * 6 / 5;

        mSearchEditText.setLayoutParams(layoutParams);
        mSearchImageView.setImageResource(R.drawable.ic_search_primary_36dp);

        mDictionaryFragmentContainerFrameLayout.animate().translationY(0)
                .setInterpolator(new AccelerateInterpolator(2));
    }

    public void updateSearchEditText(Chemical chemical) {
        mInputMethodManager.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
        if (!isSearchMode) {
            showSearchMode();
        }

        mSearchEditText.setText(String.valueOf(chemical.getNameKo()));
        mSearchEditText.setSelection(chemical.getNameKo().length());

        requestChemical(chemical.getId(), true);
    }

    public void replaceFragment() {
        mFragment = mFragmentManager.findFragmentById(R.id.dictionary_fragment_container);

        if (mFragment instanceof ChemicalLatestListFragment) {
            mFragmentManager.beginTransaction()
                    .add(R.id.dictionary_fragment_container, ChemicalFragment.newInstance())
                    .commit();
        } else if (mFragment instanceof ChemicalFragment) {
            mFragmentManager.beginTransaction()
                    .add(R.id.dictionary_fragment_container, ChemicalLatestListFragment.newInstance((byte) -1))
                    .commit();
        }
    }

    public void onBackPressed() {
        mFragment = mFragmentManager.findFragmentById(R.id.dictionary_fragment_container);

        if (mFragment instanceof ChemicalLatestListFragment) {
            getActivity().finish();
        } else if (mFragment instanceof ChemicalFragment) {
            mSearchEditText.getText().clear();
            mFragmentManager.beginTransaction()
                    .replace(R.id.dictionary_fragment_container, ChemicalLatestListFragment.newInstance((byte) -1))
                    .commit();
        }
    }

    private class ChemicalCharacterAdapter extends ArrayAdapter<CTag> implements Filterable {

        private ArrayList<CTag> mChemicalRequestResults;
        private ArrayList<CTag> mChemicalResults;

        public ChemicalCharacterAdapter(Context context, int resource) {
            super(context, resource);
            mChemicalRequestResults = new ArrayList<>();
            mChemicalResults = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mChemicalResults.size();
        }

        @Nullable
        @Override
        public CTag getItem(int position) {
            return mChemicalResults.get(position);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            final Filter filter = new Filter() {

                private char mChemicalCharacter = '\u0000';

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        try {
                            mChemicalResults.clear();
//                            String query = constraint.toString();

                            String currentTextUnSpaced = constraint.toString().replaceAll("\\s+", "");
                            int currentTextLength = currentTextUnSpaced.length();

                            if ((currentTextLength) < 1) {
                                mChemicalRequestResults.clear();
                                mChemicalCharacter = '\u0000';
                                filterResults.values = mChemicalRequestResults;
                                filterResults.count = mChemicalRequestResults.size();
                                return filterResults;
                            }

                            String currentTextDecomposed = decomposeKoreanSyllable(currentTextUnSpaced);
                            currentTextLength = currentTextDecomposed.length();

                            char firstCharacter = currentTextDecomposed.charAt(0);
                            if (firstCharacter != mChemicalCharacter) {
                                mChemicalRequestResults = new ChemicalCharacterTask().execute(Character.toString(firstCharacter)).get();
                                mChemicalCharacter = firstCharacter;
                            }

                            for (int i = 0; i < mChemicalRequestResults.size(); i++) {
                                String theTag = mChemicalRequestResults.get(i).getDescription();
                                String theTagUnSpaced = theTag.replaceAll("\\s+", "");
                                String theTagDecomposed = decomposeKoreanSyllable(theTagUnSpaced);
                                int theTagDecomposedLength = theTagDecomposed.length();

                                if (theTagDecomposedLength < currentTextLength) {
                                    continue;
                                }

                                int j;
                                for (j = 0; j < currentTextLength; j++) {
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
                                    mChemicalResults.add(mChemicalRequestResults.get(i));
                                }
                            }

                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                        filterResults.values = mChemicalResults;
                        filterResults.count = mChemicalResults.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_item_chemical_character, parent, false);
                convertView.setTag(new ChemicalCharacterHolder(convertView));
            }
            initializeViews(getItem(position), (ChemicalCharacterHolder) convertView.getTag());
            return convertView;
        }

        private void initializeViews(CTag cTag, ChemicalCharacterHolder holder) {
            holder.mChemicalNameKoTextView.setText(String.valueOf(cTag.getDescription()));
            holder.mChemicalNameEnTextView.setText(String.valueOf(cTag.getChemicalId()));
        }

        class ChemicalCharacterHolder {

            private TextView mChemicalNameKoTextView;
            private TextView mChemicalNameEnTextView;

            public ChemicalCharacterHolder(View view) {
                mChemicalNameKoTextView = (TextView) view.findViewById(R.id.list_item_chemical_name_ko_text_view);
                mChemicalNameEnTextView = (TextView) view.findViewById(R.id.list_item_chemical_name_eng_text_view);
            }
        }

        private class ChemicalCharacterTask extends AsyncTask<String, Void, ArrayList<CTag>> {

            @Override
            protected ArrayList<CTag> doInBackground(String... params) {
                try {
                    String url = URL_HOST + CTAG_PATH + CHARACTER_QUERY + URLEncoder.encode(params[0], "UTF-8");
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
                    return Parser.parseCTagList(jsonObject);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    return null;
                }
            }
        }
    }

    private void requestChemical(int chemicalId, final boolean isCorrect) {

        final ProgressDialog progressDialog =
                ProgressDialog.show(getActivity(), getString(R.string.progress_dialog_title_chemical),
                        getString(R.string.progress_dialog_message_wait), false, false);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH + chemicalId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Chemical chemical = Parser.parseChemical(response);
                        progressDialog.dismiss();

                        if (isCorrect) {
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.dictionary_fragment_container, ChemicalFragment.newInstance(chemical))
                                    .commit();
                        } else {
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.dictionary_fragment_container,
                                            ChemicalLatestListFragment.newInstance((byte) 1, chemical))
                                    .commit();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
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
