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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.SearchWord;
import com.planet.wondering.chemi.model.storage.SearchLatestStorage;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;
import com.planet.wondering.chemi.util.listener.OnSearchWordSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 5..
 */
public class SearchLatestListFragment extends Fragment {

    private RecyclerView mSearchLatestRecyclerView;
    private LatestSearchAdapter mLatestSearchAdapter;
    private ArrayList<SearchWord> mSearchWords;


    public static SearchLatestListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SearchLatestListFragment fragment = new SearchLatestListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchLatestStorage searchLatestStorage = SearchLatestStorage.getStorage(getActivity());
        mSearchWords = searchLatestStorage.getSearchWords();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_latest_list, container, false);
        mSearchLatestRecyclerView = (RecyclerView) view.findViewById(R.id.search_latest_recycler_view);
        mSearchLatestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SeparatorDecoration decoration =
                new SeparatorDecoration(getActivity(), android.R.color.transparent, 1.5f);
        mSearchLatestRecyclerView.addItemDecoration(decoration);

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
        if (mLatestSearchAdapter == null) {
            mLatestSearchAdapter = new LatestSearchAdapter(mSearchWords);
            mSearchLatestRecyclerView.setAdapter(mLatestSearchAdapter);
        } else {
            mLatestSearchAdapter.notifyDataSetChanged();
        }
    }

    private class LatestSearchAdapter extends RecyclerView.Adapter<LatestSearchHolder> {

        private ArrayList<SearchWord> mSearchWords;

        LatestSearchAdapter(ArrayList<SearchWord> searchWords) {
            mSearchWords = searchWords;
        }

        @Override
        public LatestSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_latest_search, parent, false);
            return new LatestSearchHolder(view);
        }

        @Override
        public void onBindViewHolder(LatestSearchHolder holder, int position) {
            SearchWord searchWord = mSearchWords.get(position);
            holder.bindSearchWord(searchWord);
        }

        @Override
        public int getItemCount() {
            return mSearchWords.size();
        }
    }

    private class LatestSearchHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private SearchWord mSearchWord;

        private TextView mSearchLatestWordTextView;
        private TextView mSearchedDateTextView;
        private RelativeLayout mSearchLatestDeleteRelativeLayout;
        private ImageView mSearchLatestDeleteImageButton;

        LatestSearchHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mSearchLatestWordTextView = (TextView)
                    itemView.findViewById(R.id.list_item_latest_search_word);
            mSearchedDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_latest_search_date);
            mSearchLatestDeleteRelativeLayout = (RelativeLayout)
                    itemView.findViewById(R.id.list_item_latest_delete_view);
            mSearchLatestDeleteRelativeLayout.setOnClickListener(this);
            mSearchLatestDeleteImageButton = (ImageView)
                    itemView.findViewById(R.id.list_item_latest_search_delete_image_button);
        }

        void bindSearchWord(SearchWord searchWord) {
            mSearchWord = searchWord;
            mSearchLatestWordTextView.setText(mSearchWord.getSearchWord());

            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String transformedDate = transFormat.format(mSearchWord.getDate());

            mSearchedDateTextView.setText(transformedDate);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.list_item_latest_delete_view) {
                Toast.makeText(getActivity(), "list_item_latest_search_delete_view", Toast.LENGTH_SHORT).show();
            } else {
                mSelectedListener.onSearchWordSelected(mSearchWord.getSearchWord());
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
