package com.planet.wondering.chemi.util.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.network.Parser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.planet.wondering.chemi.common.Common.BRAND_TAG_CHARACTER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.PRODUCT_TAG_CHARACTER_REQUEST_CODE;
import static com.planet.wondering.chemi.network.Config.Product.QUERY_CATEGORY;
import static com.planet.wondering.chemi.network.Config.Tag.BRAND_PATH;
import static com.planet.wondering.chemi.network.Config.Tag.Key.CHARACTER_QUERY;
import static com.planet.wondering.chemi.network.Config.Tag.PATH;
import static com.planet.wondering.chemi.network.Config.Tag.PRODUCT_PATH;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 4. 17..
 */

public class TagCharacterAdapter extends ArrayAdapter<String>
        implements Filterable {

    private static final String TAG = TagCharacterAdapter.class.getSimpleName();

    private Context mContext;
    private AutoCompleteTextView mSearchView;
    private ArrayList<String> mTagRequestResults;
    private ArrayList<String> mTagResults;
    private int mRequestId;
    private int mCategoryId;

    public TagCharacterAdapter(Context context, AutoCompleteTextView searchView, int resource, int requestId) {
        super(context, resource);
        mContext = context;
        mSearchView = searchView;
        mTagRequestResults = new ArrayList<>();
        mTagResults = new ArrayList<>();
        mRequestId = requestId;
    }

    public TagCharacterAdapter(Context context, AutoCompleteTextView searchView, int resource, int requestId, int categoryId) {
        super(context, resource);
        mContext = context;
        mSearchView = searchView;
        mTagRequestResults = new ArrayList<>();
        mTagResults = new ArrayList<>();
        mRequestId = requestId;
        mCategoryId = categoryId;
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

    public Filter getFilter() {
        Filter filter = new Filter() {

            private char mTagCharacter = '\u0000';

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (constraint != null) {
                    try {
                        mTagResults.clear();

                        String currentTextUnSpaced = constraint.toString().replaceAll("\\s+", "");
                        int currentTextLength = currentTextUnSpaced.length();

                        if ((currentTextLength) < 1) {
                            mTagRequestResults.clear();
                            mTagResults.clear();
                            mTagCharacter = '\u0000';
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
                                mTagResults.add(theTag);
                            }
                        }

                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                notifyDataSetChanged();
            }

            public char convertConsonantToStandAlone(char consonant) {
                int consonantCode = (int) consonant;
                char theConsonant = consonant;

                switch (consonantCode) {
                    case 4352:
                    case 4520:
                    case 4528:
                        theConsonant = 'ㄱ';
                        break;
                    case 4353:
                    case 4521:
                        theConsonant = 'ㄲ';
                        break;
                    case 4354:
                    case 4523:
                        theConsonant = 'ㄴ';
                        break;
                    case 4355:
                    case 4526:
                        theConsonant = 'ㄷ';
                        break;
                    case 4356:
                        theConsonant = 'ㄸ';
                        break;
                    case 4357:
                    case 4527:
                        theConsonant = 'ㄹ';
                        break;
                    case 4358:
                    case 4529:
                    case 4535:
                        theConsonant = 'ㅁ';
                        break;
                    case 4359:
                    case 4530:
                    case 4536:
                        theConsonant = 'ㅂ';
                        break;
                    case 4360:
                        theConsonant = 'ㅃ';
                        break;
                    case 4361:
                    case 4522:
                    case 4531:
                    case 4537:
                    case 4538:
                        theConsonant = 'ㅅ';
                        break;
                    case 4362:
                    case 4539:
                        theConsonant = 'ㅆ';
                        break;
                    case 4363:
                    case 4540:
                        theConsonant = 'ㅇ';
                        break;
                    case 4364:
                    case 4524:
                    case 4541:
                        theConsonant = 'ㅈ';
                        break;
                    case 4365:
                        theConsonant = 'ㅉ';
                        break;
                    case 4366:
                    case 4542:
                        theConsonant = 'ㅊ';
                        break;
                    case 4367:
                    case 4543:
                        theConsonant = 'ㅋ';
                        break;
                    case 4368:
                    case 4532:
                    case 4544:
                        theConsonant = 'ㅌ';
                        break;
                    case 4369:
                    case 4533:
                    case 4545:
                        theConsonant = 'ㅍ';
                        break;
                    case 4370:
                    case 4525:
                    case 4534:
                    case 4546:
                        theConsonant = 'ㅎ';
                        break;
                }
                return theConsonant;
            }

            public String decomposeKoreanSyllable(String word) {
                StringBuilder theWord = new StringBuilder();
                int length = word.length();
                for (int i = 0; i < length; i++) {
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
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
                }
                return false;
            }
        });

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

    public class TagCharacterTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                String url = null;
                switch (mRequestId) {
                    case 1:
                        url = URL_HOST + PATH + CHARACTER_QUERY + URLEncoder.encode(params[0], "UTF-8");
                        break;
                    case 2:
                        url = URL_HOST + PATH + CHARACTER_QUERY + URLEncoder.encode(params[0], "UTF-8")
                                + QUERY_CATEGORY + mCategoryId;
                        break;
                    case BRAND_TAG_CHARACTER_REQUEST_CODE:
                        url = URL_HOST + BRAND_PATH + CHARACTER_QUERY + URLEncoder.encode(params[0], "UTF-8");
                        break;
                    case PRODUCT_TAG_CHARACTER_REQUEST_CODE:
                        url = URL_HOST + PRODUCT_PATH + CHARACTER_QUERY + URLEncoder.encode(params[0], "UTF-8");
                        break;
                }
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

                switch (mRequestId) {
                    case 1:
                    case 2:
                        return Parser.parseTagStringList(jsonObject);
                    case BRAND_TAG_CHARACTER_REQUEST_CODE:
                        return Parser.parseBrandTagStringList(jsonObject);
                    case PRODUCT_TAG_CHARACTER_REQUEST_CODE:
                        return Parser.parseProductTagStringList(jsonObject);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
            return null;
        }
    }

}
