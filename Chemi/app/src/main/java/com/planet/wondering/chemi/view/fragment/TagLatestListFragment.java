package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Tag;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;
import com.planet.wondering.chemi.util.helper.TagSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnSearchWordSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yoon on 2017. 1. 27..
 */

public class TagLatestListFragment extends Fragment {

    private static final String TAG = TagLatestListFragment.class.getSimpleName();

    public static TagLatestListFragment newInstance() {

        Bundle args = new Bundle();

        TagLatestListFragment fragment = new TagLatestListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mTagLatestRecyclerView;
    private TagLatestAdapter mTagLatestAdapter;
    private ArrayList<Tag> mTags;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTags = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_latest_list, container, false);
        mTagLatestRecyclerView = (RecyclerView) view.findViewById(R.id.tag_latest_recycler_view);
        mTagLatestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SeparatorDecoration decoration =
                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
        mTagLatestRecyclerView.addItemDecoration(decoration);

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mTags = TagSharedPreferences.getStoredTags(getActivity());
        if (mTags != null) {
            Collections.reverse(mTags);
        }
        updateUI();
    }

    private void updateUI() {
        if (mTagLatestAdapter == null) {
            mTagLatestAdapter = new TagLatestAdapter(mTags);
            mTagLatestRecyclerView.setAdapter(mTagLatestAdapter);
        } else {
            mTagLatestAdapter.setTags(mTags);
            mTagLatestAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class TagLatestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<Tag> mTags = new ArrayList<>();

        public TagLatestAdapter(ArrayList<Tag> tags) {
            mTags = tags;
        }

        public void setTags(ArrayList<Tag> tags) {
            mTags = tags;
        }

        public void removeTag(Tag tag) {
            mTags.remove(tag);
            TagSharedPreferences.removeStoredTag(getActivity(), tag);
        }

        public void clearTag() {
            mTags.clear();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            switch (viewType) {
                case VIEW_TYPE_EMPTY:
                    view = layoutInflater.inflate(R.layout.list_item_tag_latest_empty, parent, false);
                    return new EmptyViewHolder(view);
                case VIEW_TYPE_ITEM:
                    view = layoutInflater.inflate(R.layout.list_item_tag_latest, parent, false);
                    return new TagViewHolder(view);
                case VIEW_TYPE_FOOTER:
                    view = layoutInflater.inflate(R.layout.list_item_tag_latest_footer, parent, false);
                    return new TagFooterViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof TagViewHolder) {
                Tag tag = mTags.get(position);
                ((TagViewHolder) holder).bindTag(tag);
            }
        }

        @Override
        public int getItemCount() {
            if (mTags == null || mTags.size() == 0) {
                return 1;
            }
            if (mTags.size() > 0) {
                return mTags.size() + 1;
            }
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            if (mTags == null || mTags.size() == 0) {
                return VIEW_TYPE_EMPTY;
            }
            if (mTags.size() > 0 && position == mTags.size()) {
                return VIEW_TYPE_FOOTER;
            }
            return VIEW_TYPE_ITEM;
        }
    }

    private static final int VIEW_TYPE_EMPTY = -1;
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    private class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class TagFooterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private RelativeLayout mTagLatestAllDeleteLayout;

        public TagFooterViewHolder(View itemView) {
            super(itemView);

            mTagLatestAllDeleteLayout = (RelativeLayout)
                    itemView.findViewById(R.id.list_item_tag_latest_all_delete_view);
            mTagLatestAllDeleteLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mTagLatestAdapter.clearTag();
            mTagLatestAdapter.notifyDataSetChanged();
            TagSharedPreferences.removeAllStoredTag(getActivity());
        }
    }

    private class TagViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Tag mTag;

        private TextView mTagNameTextView;
        private TextView mDateTextView;
        private RelativeLayout mTagDeleteLayout;

        public TagViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTagNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_tag_latest_tag_name_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_tag_latest_date_text_view);
            mTagDeleteLayout = (RelativeLayout)
                    itemView.findViewById(R.id.list_item_tag_latest_delete_view);
            mTagDeleteLayout.setOnClickListener(this);
        }

        public void bindTag(Tag tag) {
            mTag = tag;
            mTagNameTextView.setText(mTag.getName());
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String transFormedDate = transFormat.format(mTag.getRankDate());
            mDateTextView.setText(transFormedDate);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.list_item_tag_latest_delete_view) {
                mTagLatestAdapter.removeTag(mTag);
                mTagLatestAdapter.notifyDataSetChanged();
            } else {
                mSelectedListener.onSearchWordSelected(mTag.getName());
            }
        }
    }

    OnSearchWordSelectedListener mSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mSelectedListener = (OnSearchWordSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSearchWordSelectedListener");
        }
    }
}
