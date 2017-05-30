package com.planet.wondering.chemi.view.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Content;
import com.planet.wondering.chemi.view.custom.CustomProgressDialog;

import java.util.ArrayList;

import static com.planet.wondering.chemi.common.Common.CONTENT_COMMENT_TYPE;
import static com.planet.wondering.chemi.common.Common.VERTICAL_CONTENT_VIEW_TYPE;

/**
 * Created by yoon on 2017. 3. 31..
 */

public class ContentVerticalFragment extends Fragment {

    private static final String TAG = ContentVerticalFragment.class.getSimpleName();

    private static final String ARG_CONTENT = "content";

    public static ContentVerticalFragment newInstance() {

        Bundle args = new Bundle();

        ContentVerticalFragment fragment = new ContentVerticalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ContentVerticalFragment newInstance(Content content) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTENT, content);

        ContentVerticalFragment fragment = new ContentVerticalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Content mContent;

    private NestedScrollView mContentVerticalNestedScrollView;

    private ArrayList<String> mContentImagePaths;
    private RecyclerView mContentImageRecyclerView;
    private ContentImageAdapter mContentImageAdapter;
    private int mImageBindCompletedCount;
    private CustomProgressDialog mProgressDialog;

    private LinearLayout mContentCountLayout;
    private TextView mContentLikeCountTextView;
    private TextView mContentCommentCountTextView;
    private TextView mDividerTextView;

    private FragmentManager mChildFragmentManager;
    private Fragment mCommentFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mContent = (Content) getArguments().getSerializable(ARG_CONTENT);
        mContentImagePaths = new ArrayList<>();

        if (mContent != null ) {
            mContentImagePaths = mContent.getContentImagePaths();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mProgressDialog = new CustomProgressDialog(getActivity());
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgressDialog.show();

        View view = inflater.inflate(R.layout.fragment_content_vertical, container, false);
        mContentVerticalNestedScrollView = (NestedScrollView) view.findViewById(R.id.content_vertical_nested_scroll_view);
//        mContentVerticalNestedScrollView.getParent().requestChildFocus(mContentVerticalNestedScrollView, mContentVerticalNestedScrollView);
        mContentImageRecyclerView = (RecyclerView) view.findViewById(R.id.content_image_recycler_view);
        mContentImageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContentImageRecyclerView.setNestedScrollingEnabled(false);

        mContentCountLayout = (LinearLayout) view.findViewById(R.id.content_count_layout);
        mContentLikeCountTextView = (TextView) view.findViewById(R.id.content_like_count_text_view);
        mContentCommentCountTextView = (TextView) view.findViewById(R.id.content_comment_count_text_view);
        mDividerTextView = (TextView) view.findViewById(R.id.content_comment_divider_text_view);

        mChildFragmentManager = getChildFragmentManager();
        mCommentFragment = mChildFragmentManager.findFragmentById(R.id.content_comment_fragment_container);

        if (mCommentFragment == null) {
            mCommentFragment = CommentFragment.newInstance(
                    mContent.getId(), CONTENT_COMMENT_TYPE, VERTICAL_CONTENT_VIEW_TYPE);
            mChildFragmentManager.beginTransaction()
                    .add(R.id.content_comment_fragment_container, mCommentFragment)
                    .commit();
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        updateUI();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentLikeCountTextView.setText(String.valueOf(mContent.getLikeCount()));
        mContentCommentCountTextView.setText(String.valueOf(mContent.getCommentCount()));
    }

    private void updateUI() {
        if (mContentImageAdapter == null) {
            mContentImageAdapter = new ContentImageAdapter(mContentImagePaths);
            mContentImageRecyclerView.setAdapter(mContentImageAdapter);
        } else {
            mContentImageAdapter.notifyDataSetChanged();
        }
    }

    public void updateLikeCount() {
        int currentLikeCount = Integer.valueOf(mContentLikeCountTextView.getText().toString());
        if (mContent.isLike()) {
            mContentLikeCountTextView.setText(String.valueOf(currentLikeCount + 1));
        } else {
            mContentLikeCountTextView.setText(String.valueOf(currentLikeCount - 1));
        }
    }

    public void commentCountChanged(int commentCount) {
        mContentCommentCountTextView.setText(String.valueOf(commentCount));
    }

    public void updateCommentList(boolean isAddComment) {
        Fragment fragment = mChildFragmentManager.findFragmentById(R.id.content_comment_fragment_container);
        if (fragment instanceof CommentFragment) {
            ((CommentFragment) fragment).updateCommentList(isAddComment);
        }
    }

    public void commentNestedScroll() {
        mContentVerticalNestedScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mContentVerticalNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
            }
        }, 300);
    }

    public void focusSelectedComment(float positionY) {
        final int scrollPositionY = mContentImageRecyclerView.getMeasuredHeight()
                + mContentCountLayout.getMeasuredHeight() + mDividerTextView.getMeasuredHeight() + (int) positionY;
        mContentVerticalNestedScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mContentVerticalNestedScrollView.scrollTo(0, scrollPositionY);

            }
        }, 300);
    }

    public void commentEditDialogFinished(String description) {
        Fragment fragment = mChildFragmentManager.findFragmentById(R.id.content_comment_fragment_container);
        if (fragment instanceof CommentFragment) {
            ((CommentFragment) fragment).commentEditDialogFinished(description);
        }
    }

    private class ContentImageAdapter extends RecyclerView.Adapter<ContentImageHolder> {

        private ArrayList<String> mContentImagePaths;

        public ContentImageAdapter(ArrayList<String> contentImagePaths) {
            mContentImagePaths = contentImagePaths;
        }

        @Override
        public ContentImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_content_partial_image, parent, false);
            return new ContentImageHolder(view);
        }

        @Override
        public void onBindViewHolder(ContentImageHolder holder, int position) {
            holder.bindContentImage(mContentImagePaths.get(position));
        }

        @Override
        public int getItemCount() {
            return mContentImagePaths.size();
        }
    }

    private class ContentImageHolder extends RecyclerView.ViewHolder {

        private String mContentImagePath;

        private ImageView mContentImageImageView;

        public ContentImageHolder(View itemView) {
            super(itemView);

            mContentImageImageView = (ImageView) itemView.findViewById(R.id.list_item_content_partial_image_view);
        }

        public void bindContentImage(String contentImagePath) {
            mContentImagePath = contentImagePath;
            Glide.with(getActivity())
                    .load(mContentImagePath)
                    .listener(mRequestListener)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(mContentImageImageView);
        }
    }

    private RequestListener<String, GlideDrawable> mRequestListener =
            new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model,
                   Target<GlideDrawable> target, boolean isFirstResource) {

            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model,
                   Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            mImageBindCompletedCount++;
            if (mContentImageAdapter.getItemCount() == mImageBindCompletedCount) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
            return false;
        }
    };
}
