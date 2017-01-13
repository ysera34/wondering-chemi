package com.planet.wondering.chemi.view.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ChangeTransform;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class SearchFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private ImageView mSearchBackgroundImageView;
    private RelativeLayout mSearchView;
    private EditText mSearchEditText;
    private Button mSearchTextButton;
    private ImageButton mSearchImageButton;

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchBackgroundImageView = (ImageView) view.findViewById(R.id.search_background_image_view);
//        mSearchEditText = (EditText) view.findViewById(R.id.search_edit_text);
        mSearchView = (RelativeLayout) view.findViewById(R.id.search_view);
        mSearchTextButton = (Button) view.findViewById(R.id.search_edit_text);
        mSearchImageButton = (ImageButton) view.findViewById(R.id.search_image_button);
        mSearchTextButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity())
                .load("http://develop.inframincer.org/search_background.gif")
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mSearchBackgroundImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_edit_text:
//                FragmentTransaction ft = getFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                SearchDetailFragment detailFragment = SearchDetailFragment.newInstance();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    detailFragment.setSharedElementEnterTransition(new ChangeTransform());
                    detailFragment.setEnterTransition(new ChangeTransform());
                    detailFragment.setExitTransition(new ChangeTransform());
                    detailFragment.setSharedElementReturnTransition(new ChangeTransform());
                }

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addSharedElement(mSearchView, getString(R.string.search_view))
                        .addSharedElement(mSearchTextButton, getString(R.string.search_edit_text))
                        .addSharedElement(mSearchImageButton, getString(R.string.search_image_button))
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
