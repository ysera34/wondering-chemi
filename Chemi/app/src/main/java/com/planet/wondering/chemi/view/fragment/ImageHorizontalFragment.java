package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 4. 16..
 */

public class ImageHorizontalFragment extends Fragment {

    private static final String TAG = ImageHorizontalFragment.class.getSimpleName();

    private static final String ARG_IMAGE_PATHS = "image_paths";

    public static ImageHorizontalFragment newInstance() {

        Bundle args = new Bundle();

        ImageHorizontalFragment fragment = new ImageHorizontalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageHorizontalFragment newInstance(ArrayList<String> imagePaths) {

        Bundle args = new Bundle();
        args.putStringArrayList(ARG_IMAGE_PATHS, imagePaths);

        ImageHorizontalFragment fragment = new ImageHorizontalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<String> mImagePaths;
    private ViewPager mImageViewPager;
    private ImageAdapter mImageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImagePaths = new ArrayList<>();
        mImagePaths = getArguments().getStringArrayList(ARG_IMAGE_PATHS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_horizontal, container, false);
        mImageViewPager = (ViewPager) view.findViewById(R.id.image_view_pager);
        updateUI();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateUI() {
        if (isAdded()) {
            if (mImageAdapter == null) {
                mImageAdapter = new ImageAdapter(mImagePaths);
                mImageViewPager.setAdapter(mImageAdapter);
            } else {
            }
        }
    }

    private class ImageAdapter extends PagerAdapter {

        private ArrayList<String> mImagePaths;

        public ImageAdapter(ArrayList<String> imagePaths) {
            mImagePaths = imagePaths;
        }

        public void setImagePaths(ArrayList<String> imagePaths) {
            mImagePaths = imagePaths;
        }

        @Override
        public int getCount() {
            return mImagePaths.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_content_partial_image, container, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.list_item_content_partial_image_view);
            Glide.with(getActivity())
                    .load(mImagePaths.get(position))
                    .crossFade()
                    .into(imageView);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}
