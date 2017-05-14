package com.planet.wondering.chemi.view.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ChangeTransform;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class SearchFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private static final String ARG_IS_FIRST_USER = "is_first_user";

    private RelativeLayout mSearchView;
    private Button mSearchButton;
    private ImageButton mSearchImageButton;

    private Animation mSearchViewAnimation;

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SearchFragment newInstance(boolean isFirstUser) {

        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_FIRST_USER, isFirstUser);

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchViewAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_up_scale_up);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchView = (RelativeLayout) view.findViewById(R.id.search_view);
        mSearchImageButton = (ImageButton) view.findViewById(R.id.search_image_button);
        mSearchImageButton.setOnClickListener(this);
        mSearchButton = (Button) view.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Glide.with(getActivity())
//                .load("http://develop.inframincer.org/search_background.gif")
//                .asGif()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(mSearchBackgroundImageView);

        if (getArguments().getBoolean(ARG_IS_FIRST_USER, false)) {
            MemberCongratulationDialogFragment dialogFragment = MemberCongratulationDialogFragment.newInstance();
            dialogFragment.show(getFragmentManager(), "congratulation_dialog");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_image_button:
            case R.id.search_button:
//                FragmentTransaction ft = getFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                SearchDetailFragment detailFragment = SearchDetailFragment.newInstance();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    detailFragment.setSharedElementEnterTransition(new ChangeTransform());
                    detailFragment.setEnterTransition(new ChangeTransform());
                    detailFragment.setExitTransition(new ChangeTransform());
                    detailFragment.setSharedElementReturnTransition(new ChangeTransform());
                }

//                mSearchView.startAnimation(mSearchViewAnimation);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addSharedElement(mSearchView, getString(R.string.search_view))
                        .addSharedElement(mSearchButton, getString(R.string.search_edit_text))
                        .addSharedElement(mSearchImageButton, getString(R.string.search_image_button))
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
