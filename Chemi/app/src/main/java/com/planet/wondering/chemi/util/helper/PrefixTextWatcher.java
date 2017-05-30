package com.planet.wondering.chemi.util.helper;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import com.planet.wondering.chemi.R;

import static com.planet.wondering.chemi.common.Common.COMMENT_USER_NAME_DIVIDER;
import static com.planet.wondering.chemi.common.Common.COMMENT_USER_NAME_PREFIX;

/**
 * Created by yoon on 2017. 5. 30..
 */

public class PrefixTextWatcher implements TextWatcher {

    private static final String TAG = PrefixTextWatcher.class.getSimpleName();

    private Context mContext;
    private StringBuilder mPrefixStringBuilder;
    private EditText mEditText;
    private String mPrefixText;
    private String mDescription;

    public PrefixTextWatcher(Context context) {
        mContext = context;
        mPrefixStringBuilder = new StringBuilder();
    }

    public void setPrefixText(EditText editText, String prefixText) {
        mEditText = editText;
        mPrefixText = prefixText;
        mPrefixStringBuilder.append(COMMENT_USER_NAME_PREFIX).append(mPrefixText).append(COMMENT_USER_NAME_DIVIDER);
        mEditText.setText(highlightPrefixText());
        Selection.setSelection(mEditText.getText(), mEditText.getText().length());
    }

    public void setPrefixText(EditText editText, String prefixText, String description) {
        mEditText = editText;
        mPrefixText = prefixText;
        mDescription = description;
        mPrefixStringBuilder/*.append(COMMENT_USER_NAME_PREFIX)*/.append(mPrefixText).append(COMMENT_USER_NAME_DIVIDER);
        mEditText.setText(highlightPrefixText());
        Selection.setSelection(mEditText.getText(), mEditText.getText().length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mDescription == null) {
            if (!s.toString().contains(mPrefixStringBuilder.toString())) {
                mEditText.setText(highlightPrefixText());
                Selection.setSelection(mEditText.getText(), mEditText.getText().length());
            }
        } else {
            if (!s.toString().contains(mPrefixStringBuilder.toString())) {
                mEditText.setText(highlightPrefixText());
                Selection.setSelection(mEditText.getText(), mEditText.getText().length());
            }
        }
    }

    private SpannableString highlightPrefixText() {

        int startIndex = 0;
        int endIndex = String.valueOf(mPrefixStringBuilder.toString()).split(COMMENT_USER_NAME_DIVIDER, 2)[0].length();

        SpannableString spannableString;
        if (mDescription == null) {
            spannableString = new SpannableString(mPrefixStringBuilder.toString());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(mPrefixStringBuilder.toString()).append(mDescription);
            spannableString = new SpannableString(stringBuilder);
            mDescription = null;
        }

        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),
                startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorWhite)),
                endIndex, endIndex + COMMENT_USER_NAME_DIVIDER.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;


    }
}
