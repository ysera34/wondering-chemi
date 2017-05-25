package com.planet.wondering.chemi.view.fragment;

import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Content;
import com.planet.wondering.chemi.model.Pager;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.listener.OnRecyclerViewScrollListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.ContentActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.planet.wondering.chemi.network.Config.Content.QUERY_CATEGORY;
import static com.planet.wondering.chemi.network.Config.Content.QUERY_PATH;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 3. 28..
 */

public class ContentListFragment extends Fragment {

    private static final String TAG = ContentListFragment.class.getSimpleName();

    private static final String ARG_CATEGORY_ID = "category_id";

    public static ContentListFragment newInstance() {

        Bundle args = new Bundle();

        ContentListFragment fragment = new ContentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ContentListFragment newInstance(int categoryId) {

        Bundle args = new Bundle();
        args.getInt(ARG_CATEGORY_ID, categoryId);

        ContentListFragment fragment = new ContentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mCategoryId;
    private ArrayList<Content> mContents;

    private RecyclerView mContentRecyclerView;
    private ContentAdapter mContentAdapter;

    private StringBuilder mUrlBuilder;
    private Pager mPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContents = new ArrayList<>();

        mCategoryId = getArguments().getInt(ARG_CATEGORY_ID, -1);
//        Log.i(TAG, "mCategoryId : " + mCategoryId);

        mUrlBuilder = new StringBuilder();
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
        mContentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = ((LinearLayoutManager) mContentRecyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                if (lastItem == mContentAdapter.getItemCount() - 1
                        && mPager.getTotal() > mContentAdapter.getItemCount()) {
                    requestContentList(mCategoryId);
                }
            }
        });
        mContentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = ((LinearLayoutManager) mContentRecyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                int itemSize = mContentRecyclerView.getAdapter().getItemCount();
                if (lastItem == itemSize - 1) {
                    ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
                }
                int firstItem = ((LinearLayoutManager) mContentRecyclerView.getLayoutManager())
                        .findFirstCompletelyVisibleItemPosition();
                if (firstItem == 0) {
                    ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
                }
            }
        });

        updateUI();
        requestContentList(mCategoryId);
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
            mContentAdapter.setContents(mContents);
            mContentAdapter.notifyDataSetChanged();
        }
    }

    private void requestContentList(int categoryId) {

        if (mPager == null) {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH)
                    .append(QUERY_CATEGORY).append(categoryId);
        } else {
            mUrlBuilder.delete(0, mUrlBuilder.length());
            mUrlBuilder.append(URL_HOST).append(QUERY_PATH).append(mPager.getNextQuery())
                    .append(QUERY_CATEGORY).append(categoryId);
        }
        Log.i(TAG, "url : " + mUrlBuilder.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mUrlBuilder.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        mContents = Parser.parseContentList(response);
                        mContents.addAll(Parser.parseContentList(response));
                        mPager = Parser.parseListPaginationQuery(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private class ContentAdapter extends RecyclerView.Adapter<ContentHolder> {

        private ArrayList<Content> mContents;

        public ContentAdapter(ArrayList<Content> contents) {
            mContents = contents;
        }

        public void setContents(ArrayList<Content> contents) {
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

        private ImageView mContentImageView;
        private TextView mContentTitleTextView;
        private TextView mContentSubTitleTextView;
        private TextView mContentLikeCountTextView;
        private TextView mContentViewCountTextView;
        private TextView mContentCommentCountTextView;

        public ContentHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mContentImageView = (ImageView) itemView.findViewById(R.id.list_item_content_image_view);
            mContentTitleTextView = (TextView) itemView.findViewById(R.id.list_item_content_title_text_view);
            mContentSubTitleTextView = (TextView) itemView.findViewById(R.id.list_item_content_sub_title_text_view);
            mContentLikeCountTextView = (TextView) itemView.findViewById(R.id.list_item_content_like_count_text_view);
            mContentViewCountTextView = (TextView) itemView.findViewById(R.id.list_item_content_view_count_text_view);
            mContentCommentCountTextView = (TextView) itemView.findViewById(R.id.list_item_content_comment_count_text_view);
        }

        public void bindContent(Content content) {
            mContent = content;

            Glide.with(getActivity())
                    .load(mContent.getImagePath())
                    .crossFade()
//                    .override()
                    .into(mContentImageView);
            mContentTitleTextView.setText(String.valueOf(mContent.getTitle()));
            mContentSubTitleTextView.setText(String.valueOf(mContent.getSubTitle()));
            mContentLikeCountTextView.setText(String.valueOf(mContent.getLikeCount()));
            mContentViewCountTextView.setText(String.valueOf(mContent.getViewCount()));
            mContentCommentCountTextView.setText(String.valueOf(mContent.getCommentCount()));
        }

        @Override
        public void onClick(View v) {
//            startActivity(ContentActivity.newIntent(getActivity()));
//            startActivity(ContentActivity.newIntent(getActivity(), mContent.getId()));
            getActivity().startActivityForResult(
                    ContentActivity.newIntent(getActivity(), mContent.getId()), CONTENT_REQUEST_CODE);
        }
    }

    public static final int CONTENT_REQUEST_CODE = 8800;
    public static final String EXTRA_RESPONSE_CONTENT_ID = "com.planet.wondering.chemi.response_content_id";
    public static final String EXTRA_RESPONSE_CONTENT_LIKE_COUNT = "com.planet.wondering.chemi.response_content_like_count";
    public static final String EXTRA_RESPONSE_CONTENT_VIEW_COUNT = "com.planet.wondering.chemi.response_content_view_count";
    public static final String EXTRA_RESPONSE_CONTENT_COMMENT_COUNT = "com.planet.wondering.chemi.response_content_comment_count";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int contentId = data.getIntExtra(EXTRA_RESPONSE_CONTENT_ID, -1);
                int contentLikeCount = data.getIntExtra(EXTRA_RESPONSE_CONTENT_LIKE_COUNT, -1);
                int contentViewCount = data.getIntExtra(EXTRA_RESPONSE_CONTENT_VIEW_COUNT, -1);
                int contentCommentCount = data.getIntExtra(EXTRA_RESPONSE_CONTENT_COMMENT_COUNT, -1);
//                Log.i(TAG, "contentId: " + contentId);
//                Log.i(TAG, "contentLikeCount: " + contentLikeCount);
//                Log.i(TAG, "contentViewCount: " + contentViewCount);
//                Log.i(TAG, "contentCommentCount: " + contentCommentCount);
                for (Content content : mContents) {
                    if (content.getId() == contentId) {
                        content.setLikeCount(contentLikeCount);
                        content.setViewCount(contentViewCount);
                        content.setCommentCount(contentCommentCount);
                        break;
                    }
                }
                mContentAdapter.notifyDataSetChanged();
            }
        }
    }
}
