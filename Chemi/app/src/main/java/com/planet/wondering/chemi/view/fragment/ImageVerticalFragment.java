package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 4. 16..
 */

public class ImageVerticalFragment extends Fragment {

    private static final String TAG = ImageVerticalFragment.class.getSimpleName();

    private static final String ARG_IMAGE_PATHS = "image_paths";

    public static ImageVerticalFragment newInstance() {

        Bundle args = new Bundle();

        ImageVerticalFragment fragment = new ImageVerticalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageVerticalFragment newInstance(ArrayList<String> imagePaths) {

        Bundle args = new Bundle();
        args.putStringArrayList(ARG_IMAGE_PATHS, imagePaths);

        ImageVerticalFragment fragment = new ImageVerticalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<String> mImagePaths;
    private RecyclerView mImageRecyclerView;
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
        View view = inflater.inflate(R.layout.fragment_image_vertical, container, false);
        mImageRecyclerView = (RecyclerView) view.findViewById(R.id.image_recycler_view);
        mImageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (isAdded()) {
            if (mImageAdapter == null) {
                mImageAdapter = new ImageAdapter(mImagePaths);
                mImageRecyclerView.setAdapter(mImageAdapter);
            } else {
                mImageAdapter.setImagePaths(mImagePaths);
                mImageAdapter.notifyDataSetChanged();
            }
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {

        private ArrayList<String> mImagePaths;

        public ImageAdapter(ArrayList<String> imagePaths) {
            mImagePaths = imagePaths;
        }

        public void setImagePaths(ArrayList<String> imagePaths) {
            mImagePaths = imagePaths;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_content_partial_image, parent, false);
            return new ImageHolder(view);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            String imagePath = mImagePaths.get(position);
            holder.bindImage(imagePath);
        }

        @Override
        public int getItemCount() {
            return mImagePaths.size();
        }
    }

    private class ImageHolder extends RecyclerView.ViewHolder {

        private String mImagePath;

        private ImageView mPartialImageView;

        public ImageHolder(View itemView) {
            super(itemView);

            mPartialImageView = (ImageView) itemView.findViewById(R.id.list_item_content_partial_image_view);
        }

        public void bindImage(String imagePath) {
            mImagePath = imagePath;
            Glide.with(getActivity())
                    .load(mImagePath)
                    .crossFade()
                    .into(mPartialImageView);
        }

    }
}
