package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Content;
import com.planet.wondering.chemi.util.listener.OnRecyclerViewScrollListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.ContentActivity;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 3. 28..
 */

public class ContentListFragment extends Fragment {

    private static final String TAG = ContentListFragment.class.getSimpleName();

    public static ContentListFragment newInstance() {

        Bundle args = new Bundle();

        ContentListFragment fragment = new ContentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<Content> mContents;

    private RecyclerView mContentRecyclerView;
    private ContentAdapter mContentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mContents.add(new Content());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_list, container, false);
        mContentRecyclerView = (RecyclerView) view.findViewById(R.id.content_recycler_view);
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        SeparatorDecoration decoration =
//                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
//        mContentRecyclerView.addItemDecoration(decoration);
        mContentRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onShowView() {
                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
            }

            @Override
            public void onHideView() {
                ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
            }
        });

        updateUI();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateUI() {
        if (mContentAdapter == null) {
            mContentAdapter = new ContentAdapter(mContents);
            mContentRecyclerView.setAdapter(mContentAdapter);
        } else {
            mContentAdapter.notifyDataSetChanged();
        }
    }

    private class ContentAdapter extends RecyclerView.Adapter<ContentHolder> {

        private ArrayList<Content> mContents;

        public ContentAdapter(ArrayList<Content> contents) {
            mContents = contents;
        }

        @Override
        public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_content, parent, false);
            return new ContentHolder(view);
        }

        @Override
        public void onBindViewHolder(ContentHolder holder, int position) {
            Content content = mContents.get(position);
            holder.bindContent(content);
        }

        @Override
        public int getItemCount() {
            return mContents.size();
        }
    }

    private class ContentHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Content mContent;

        public ContentHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void bindContent(Content content) {
            mContent = content;
        }

        @Override
        public void onClick(View v) {
            startActivity(ContentActivity.newIntent(getActivity()));
        }
    }
}
