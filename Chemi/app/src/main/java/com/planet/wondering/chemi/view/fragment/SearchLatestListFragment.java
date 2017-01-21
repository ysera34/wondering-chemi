package com.planet.wondering.chemi.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.planet.wondering.chemi.util.listener.OnScrollListener;
import com.planet.wondering.chemi.util.listener.OnSearchWordSelectedListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 5..
 */
public class SearchLatestListFragment extends Fragment {

    private static final String TAG = SearchLatestListFragment.class.getSimpleName();

    private RecyclerView mSearchLatestRecyclerView;
    private LatestSearchAdapter mLatestSearchAdapter;
    private SearchLatestStorage mSearchLatestStorage;
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
        mSearchLatestStorage = SearchLatestStorage.getStorage(getActivity());
        mSearchWords = mSearchLatestStorage.getSearchWords();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_latest_list, container, false);
        mSearchLatestRecyclerView = (RecyclerView) view.findViewById(R.id.search_latest_recycler_view);
        mSearchLatestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SeparatorDecoration decoration =
                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
        mSearchLatestRecyclerView.addItemDecoration(decoration);
        mSearchLatestRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onShowView() {
                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
            }

            @Override
            public void onHideView() {
                ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
            }
        });

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

    private class LatestSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<SearchWord> mSearchWords;

        LatestSearchAdapter(ArrayList<SearchWord> searchWords) {
            mSearchWords = searchWords;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            switch (viewType) {
                case VIEW_TYPE_EMPTY:
                    view = layoutInflater.inflate(R.layout.list_item_latest_search_empty, parent, false);
                    return new EmptyViewHolder(view);
                case VIEW_TYPE_ITEM:
                    view = layoutInflater.inflate(R.layout.list_item_latest_search, parent, false);
                    return new LatestSearchHolder(view);
                case VIEW_TYPE_FOOTER:
                    view = layoutInflater.inflate(R.layout.list_item_latest_search_footer, parent, false);
                    return new FooterViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof LatestSearchHolder) {
                SearchWord searchWord = mSearchWords.get(position);
                ((LatestSearchHolder) holder).bindSearchWord(searchWord);
            }
        }

        @Override
        public int getItemCount() {
            Log.d(TAG + " mSearchWords.size()", String.valueOf(mSearchWords.size()) );
            return mSearchWords.size() > 0 ? mSearchWords.size() + 1 : 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (mSearchWords.size() == 0) {
                return VIEW_TYPE_EMPTY;
            }
            if (mSearchWords.size() > 0 && position == mSearchWords.size()) {
                return VIEW_TYPE_FOOTER;
            }
            return VIEW_TYPE_ITEM;
//            return super.getItemViewType(position);
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

    private class FooterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private RelativeLayout mLatestSearchAllRemoveView;

        FooterViewHolder(View itemView) {
            super(itemView);

            mLatestSearchAllRemoveView = (RelativeLayout)
                    itemView.findViewById(R.id.list_item_latest_delete_view);
            mLatestSearchAllRemoveView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "all delete view", Toast.LENGTH_SHORT).show();
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
//                mSearchWords = mSearchLatestStorage.removeSearchWord(this.getAdapterPosition());
//                mLatestSearchAdapter.notifyDataSetChanged();
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
