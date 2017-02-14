package com.planet.wondering.chemi.util.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by yoon on 2017. 2. 14..
 */

public abstract class TextValidator implements TextWatcher {

    public static final String TAG = TextValidator.class.getSimpleName();

    private final EditText mEditText;

    public TextValidator(EditText editText) {
        mEditText = editText;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = mEditText.getText().toString();
        validate(mEditText, text);
    }
}
