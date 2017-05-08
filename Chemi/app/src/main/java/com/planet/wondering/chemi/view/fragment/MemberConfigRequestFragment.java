package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.adapter.TagCharacterAdapter;

import static com.planet.wondering.chemi.common.Common.BRAND_TAG_CHARACTER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.PRODUCT_TAG_CHARACTER_REQUEST_CODE;

/**
 * Created by yoon on 2017. 3. 20..
 */

public class MemberConfigRequestFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberConfigRequestFragment.class.getSimpleName();

    public static MemberConfigRequestFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigRequestFragment fragment = new MemberConfigRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private InputMethodManager mInputMethodManager;
    private NestedScrollView mConfigRequestNestedScrollView;

    private LinearLayout mBackLayout;
    private AutoCompleteTextView mBrandAutoCompleteTextView;
    private ImageView mBrandClearImageView;
    private TagCharacterAdapter mBrandCharacterAdapter;
    private AutoCompleteTextView mProductAutoCompleteTextView;
    private ImageView mProductClearImageView;
    private TagCharacterAdapter mProductCharacterAdapter;
    private ImageView mImage1ImageView;
    private ImageView mImage2ImageView;
    private ImageView mImage3ImageView;
    private EditText mDetailsEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_request, container, false);
        mConfigRequestNestedScrollView = (NestedScrollView)
                view.findViewById(R.id.member_config_request_nested_scroll_view);
        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_request_back_layout);
        mBackLayout.setOnClickListener(this);
        mBrandAutoCompleteTextView = (AutoCompleteTextView)
                view.findViewById(R.id.member_config_request_brand_auto_complete_text_view);
        mBrandAutoCompleteTextView.setThreshold(1);
        mBrandAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mBrandClearImageView.setVisibility(View.VISIBLE);
                }
                if (s.length() == 0) {
                    mBrandClearImageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBrandAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (v.getText().length() == 0) {
                        Toast.makeText(getActivity(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        mBrandClearImageView = (ImageView) view.findViewById(R.id.member_config_request_brand_clear_image_view);
        mBrandClearImageView.setOnClickListener(this);
        mProductAutoCompleteTextView = (AutoCompleteTextView)
                view.findViewById(R.id.member_config_request_product_auto_complete_text_view);
        mProductAutoCompleteTextView.setThreshold(1);
        mProductAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mProductClearImageView.setVisibility(View.VISIBLE);
                }
                if (s.length() == 0) {
                    mProductClearImageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mProductAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (v.getText().length() == 0) {
                        Toast.makeText(getActivity(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        mProductClearImageView = (ImageView) view.findViewById(R.id.member_config_request_product_clear_image_view);
        mProductClearImageView.setOnClickListener(this);

        mBrandCharacterAdapter = new TagCharacterAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line, BRAND_TAG_CHARACTER_REQUEST_CODE);
        mBrandAutoCompleteTextView.setAdapter(mBrandCharacterAdapter);

        mProductCharacterAdapter = new TagCharacterAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line, PRODUCT_TAG_CHARACTER_REQUEST_CODE);
        mProductAutoCompleteTextView.setAdapter(mProductCharacterAdapter);

        mImage1ImageView = (ImageView) view.findViewById(R.id.member_config_request_image1_image_view);
        mImage2ImageView = (ImageView) view.findViewById(R.id.member_config_request_image2_image_view);
        mImage3ImageView = (ImageView) view.findViewById(R.id.member_config_request_image3_image_view);
        mDetailsEditText = (EditText) view.findViewById(R.id.member_config_request_details_edit_text);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_request_back_layout:
                getActivity().onBackPressed();
                break;
            case R.id.member_config_request_brand_clear_image_view:
                mBrandAutoCompleteTextView.getText().clear();
                break;
            case R.id.member_config_request_product_clear_image_view:
                mProductAutoCompleteTextView.getText().clear();
                break;
        }
    }
}
