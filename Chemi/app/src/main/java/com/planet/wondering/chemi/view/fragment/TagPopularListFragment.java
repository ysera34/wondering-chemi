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
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Tag;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;
import com.planet.wondering.chemi.util.listener.OnTagSelectedListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.Tag.Key.LOWEST_QUEST_DEFAULT;
import static com.planet.wondering.chemi.network.Config.Tag.PATH;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 1. 26..
 */

public class TagPopularListFragment extends Fragment {

    private static final String TAG = TagPopularListFragment.class.getSimpleName();

    public static TagPopularListFragment newInstance() {

        Bundle args = new Bundle();

        TagPopularListFragment fragment = new TagPopularListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mTagPopularRecyclerView;
    private TagPopularAdapter mTagPopularAdapter;
    private ArrayList<Tag> mTags;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTags = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_popular_list, container, false);
        mTagPopularRecyclerView = (RecyclerView) view.findViewById(R.id.tag_popular_recycler_view);
        mTagPopularRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SeparatorDecoration decoration =
                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
        mTagPopularRecyclerView.addItemDecoration(decoration);

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
//        updateUI();

        requestTagPopularList();
    }

    private void updateUI() {
        if (mTagPopularAdapter == null) {
            mTagPopularAdapter = new TagPopularAdapter(mTags);
            mTagPopularRecyclerView.setAdapter(mTagPopularAdapter);
        } else {
            mTagPopularAdapter.setTags(mTags);
            mTagPopularAdapter.notifyDataSetChanged();
        }
    }

    public void requestTagPopularList() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH + LOWEST_QUEST_DEFAULT,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mTags = Parser.parseTagPopularList(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private class TagPopularAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Tag> mTags = new ArrayList<>();

        public TagPopularAdapter(ArrayList<Tag> tags) {
            mTags = tags;
        }

        public void setTags(ArrayList<Tag> tags) {
            mTags = tags;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            switch (viewType) {
                case VIEW_TYPE_ITEM:
                    view = layoutInflater.inflate(R.layout.list_item_tag_popular, parent, false);
                    return new TagViewHolder(view);
                case VIEW_TYPE_FOOTER:
                    view = layoutInflater.inflate(R.layout.list_item_tag_popular_footer, parent, false);
                    return new TagFooterViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof TagViewHolder) {
                Tag tag = mTags.get(position);
                ((TagViewHolder) holder).bindTag(tag);
            } else if (holder instanceof TagFooterViewHolder) {
                ((TagFooterViewHolder) holder).bindDate(mTags.get(0).getRankDate());
            }
        }

        @Override
        public int getItemCount() {
            if (mTags.size() == 0) {
                return 0;
            } else {
                return mTags.size() + 1;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mTags.size() > 0 && position == mTags.size()) {
                return VIEW_TYPE_FOOTER;
            }
            return VIEW_TYPE_ITEM;
        }
    }

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    private class TagViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Tag mTag;

        private TextView mTagRatingNumberTextView;
        private TextView mTagNameTextView;
        private ImageView mTagVariationStateImageView;
        private TextView mTagVariationValueTextView;

        public TagViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTagRatingNumberTextView = (TextView)
                    itemView.findViewById(R.id.list_item_tag_popular_rating_number_text_view);
            mTagNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_tag_popular_tag_name_text_view);
            mTagVariationStateImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_tag_popular_tag_variation_state_image_view);
            mTagVariationValueTextView = (TextView)
                    itemView.findViewById(R.id.list_item_tag_popular_variation_value_text_view);
        }

        public void bindTag(Tag tag) {
            mTag = tag;
            mTagRatingNumberTextView.setText(String.valueOf(mTag.getRank()));
            mTagNameTextView.setText(mTag.getName());
            mTagVariationStateImageView.setImageResource(mTag.getStateImageResId());
            mTagVariationValueTextView.setText(String.valueOf(mTag.getAbsoluteVariation()));
        }

        @Override
        public void onClick(View v) {
            mSelectedListener.onTagSelected(mTag.getName());
        }
    }

    private class TagFooterViewHolder extends RecyclerView.ViewHolder {

        private Date mUpdateDate;

        private TextView mUpdateDateTextView;

        public TagFooterViewHolder(View itemView) {
            super(itemView);

            mUpdateDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_tag_popular_footer_update_date_text_view);
        }

        public void bindDate(Date updateDate) {
            mUpdateDate = updateDate;
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String transformedDate = transFormat.format(mUpdateDate);
            mUpdateDateTextView.setText(getString(R.string.list_item_tag_popular_update_date, transformedDate));
        }
    }

    OnTagSelectedListener mSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mSelectedListener = (OnTagSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTagSelectedListener");
        }
    }
}
