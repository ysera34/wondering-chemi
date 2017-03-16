package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class DictionaryFragment extends Fragment implements View.OnClickListener {

    public static DictionaryFragment newInstance() {

        Bundle args = new Bundle();

        DictionaryFragment fragment = new DictionaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mDictionaryHeaderLayout;
    private RelativeLayout mDictionaryLogoLayout;
    private RelativeLayout mDictionarySearchLayout;
    private EditText mSearchEditText;
    private ImageView mSearchImageView;

    private Transition mTransition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        mDictionaryHeaderLayout = (LinearLayout) view.findViewById(R.id.dictionary_header_layout);
        mDictionaryLogoLayout = (RelativeLayout) view.findViewById(R.id.dictionary_logo_layout);
        mDictionarySearchLayout = (RelativeLayout) view.findViewById(R.id.dictionary_search_layout);
        mSearchEditText = (EditText) view.findViewById(R.id.dictionary_search_edit_text);
        mSearchEditText.setOnClickListener(this);
        mSearchImageView = (ImageView) view.findViewById(R.id.dictionary_search_image_view);

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
                }
                break;
            case R.id.dictionary_search_image_view:
                break;

        }
    }

    public void hideSearchMode() {
//        mDictionaryHeaderLayout.animate().translationY(0)
//                .setInterpolator(new DecelerateInterpolator(2));
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

    }
}
