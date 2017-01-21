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
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.SearchWord;
import com.planet.wondering.chemi.model.storage.SearchPopularStorage;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;
import com.planet.wondering.chemi.util.listener.OnSearchWordSelectedListener;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 5..
 */
public class SearchPopularListFragment extends Fragment {

    private RecyclerView mSearchPopularRecyclerView;
    private PopularSearchAdapter mPopularSearchAdapter;
    private ArrayList<SearchWord> mSearchWords;

    public static SearchPopularListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SearchPopularListFragment fragment = new SearchPopularListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchPopularStorage searchPopularStorage = SearchPopularStorage.getStorage(getActivity());
        mSearchWords = searchPopularStorage.getSearchWords();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_popular_list, container, false);
        mSearchPopularRecyclerView = (RecyclerView) view.findViewById(R.id.search_popular_recycler_view);
        mSearchPopularRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SeparatorDecoration decoration =
                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
        mSearchPopularRecyclerView.addItemDecoration(decoration);

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
        if (mPopularSearchAdapter == null) {
            mPopularSearchAdapter = new PopularSearchAdapter(mSearchWords);
            mSearchPopularRecyclerView.setAdapter(mPopularSearchAdapter);
        } else {
            mPopularSearchAdapter.notifyDataSetChanged();
        }
    }

    private class PopularSearchAdapter extends RecyclerView.Adapter<PopularSearchHolder> {

        private ArrayList<SearchWord> mSearchWords;

        PopularSearchAdapter(ArrayList<SearchWord> searchWords) {
            mSearchWords = searchWords;
        }

        @Override
        public PopularSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_popular_search, parent, false);
            return new PopularSearchHolder(view);
        }

        @Override
        public void onBindViewHolder(PopularSearchHolder holder, int position) {
            SearchWord searchWord = mSearchWords.get(position);
            holder.bindSearchWord(searchWord);
        }

        @Override
        public int getItemCount() {
            return mSearchWords.size();
        }
    }

    private class PopularSearchHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private SearchWord mSearchWord;

        private TextView mSearchRatingNumberTextView;
        private TextView mSearchWordTextView;
        private ImageView mSearchVariationStateImageView;
        private TextView mSearchVariationValueTextView;

        PopularSearchHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mSearchRatingNumberTextView = (TextView)
                    itemView.findViewById(R.id.list_item_popular_search_rating_number);
            mSearchWordTextView = (TextView)
                    itemView.findViewById(R.id.list_item_popular_search_word);
            mSearchVariationStateImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_search_variation_state_image);
            mSearchVariationValueTextView = (TextView)
                    itemView.findViewById(R.id.list_item_popular_search_variation_value);
        }

        void bindSearchWord(SearchWord searchWord) {
            mSearchWord = searchWord;
            mSearchRatingNumberTextView.setText(String.valueOf(mSearchWord.getRatingNumber()));
            mSearchWordTextView.setText(mSearchWord.getSearchWord());
            mSearchVariationStateImageView.setImageResource(mSearchWord.getStateImageResId());
            mSearchVariationValueTextView.setText(String.valueOf(mSearchWord.getVariationValue()));
        }

        @Override
        public void onClick(View v) {
            mSelectedListener.onSearchWordSelected(mSearchWord.getSearchWord());
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
