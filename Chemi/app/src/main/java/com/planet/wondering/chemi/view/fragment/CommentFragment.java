package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Comment;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Config;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnCommentSelectedListener;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.planet.wondering.chemi.network.Config.Review.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;

/**
 * Created by yoon on 2017. 4. 19..
 */

public class CommentFragment extends Fragment {

    private static final String TAG = CommentFragment.class.getSimpleName();

    private static final String ARG_REVIEW_ID = "review_id";
    private static final String ARG_CONTENT_ID = "content_id";
    private static final String ARG_RELEVANT_ID = "relevant_id";
    private static final String ARG_COMMENT_TYPE = "comment_type";

    public static CommentFragment newInstance() {

        Bundle args = new Bundle();

        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CommentFragment newInstance(int relevantId, int commentType) {

        Bundle args = new Bundle();
        args.putInt(ARG_RELEVANT_ID, relevantId);
        args.putInt(ARG_COMMENT_TYPE, commentType);

        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mCommentRecyclerView;
    private CommentAdapter mCommentAdapter;
    private ArrayList<Comment> mComments;

    private int mReviewId;
    private int mContentId;
    private int mRelevantId;
    private int mCommentType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComments = new ArrayList<>();
        mRelevantId = getArguments().getInt(ARG_RELEVANT_ID, -1);
        mCommentType = getArguments().getInt(ARG_COMMENT_TYPE, -1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        mCommentRecyclerView = (RecyclerView) view.findViewById(R.id.comment_recycler_view);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentRecyclerView.setNestedScrollingEnabled(false);
        SeparatorDecoration decoration =
                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
        mCommentRecyclerView.addItemDecoration(decoration);

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

        updateCommentList();
    }

    private void updateUI() {

        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(mComments);
            mCommentRecyclerView.setAdapter(mCommentAdapter);
        } else {
            mCommentAdapter.setParentList(mComments, true);
            mCommentAdapter.notifyDataSetChanged();
        }
    }

    public void updateCommentList() {
        requestComment(mRelevantId, mCommentType);
    }

    public void requestComment(final int relevantId, final int commentType) {
        /* 1 : review comment, 2 : content comment */

        String url = null;
        if (commentType == 1) {
            url = URL_HOST + PATH + File.separator + relevantId;
        } else if (commentType == 2) {
            url = URL_HOST + Config.Content.PATH + File.separator + relevantId;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mComments = Parser.parseCommentList(response, commentType);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                "댓글를 가져오는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private class CommentAdapter extends ExpandableRecyclerAdapter<Comment, Comment, ParentCommentHolder, ChildCommentHolder> {

        private List<Comment> mComments;
        private LayoutInflater mLayoutInflater;

        public CommentAdapter(@NonNull List<Comment> parentList) {
            super(parentList);
            mComments = parentList;
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public void setParentList(@NonNull List<Comment> parentList, boolean preserveExpansionState) {
            super.setParentList(parentList, preserveExpansionState);
        }

        @NonNull
        @Override
        public ParentCommentHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
            View view = null;
//            switch (viewType) {
//                case VIEW_TYPE_ITEM_PARENT:
                    view = mLayoutInflater.inflate(R.layout.list_item_comment_parent, parentViewGroup, false);
//                    break;
//            }
            return new ParentCommentHolder(view);
        }

        @NonNull
        @Override
        public ChildCommentHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
            View view = null;
//            switch (viewType) {
//                case VIEW_TYPE_ITEM_CHILD:
                    view = mLayoutInflater.inflate(R.layout.list_item_comment_child, childViewGroup, false);
//                    break;
//            }
            return new ChildCommentHolder(view);
        }

        @Override
        public void onBindParentViewHolder(
                @NonNull ParentCommentHolder parentViewHolder, int parentPosition, @NonNull Comment parent) {
            parentViewHolder.bindParentComment(parent);
        }

        @Override
        public void onBindChildViewHolder(
                @NonNull ChildCommentHolder childViewHolder, int parentPosition, int childPosition, @NonNull Comment child) {
            childViewHolder.bindChildComment(child);
        }

//        @Override
//        public boolean isParentViewType(int viewType) {
//            return viewType == VIEW_TYPE_HEADER || viewType == VIEW_TYPE_ITEM_PARENT;
//        }

        //        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//            View view;
//            switch (viewType) {
//                case VIEW_TYPE_HEADER:
//                    view = layoutInflater.inflate(R.layout.list_item_comment_header, parent, false);
//                    return new HeaderCommentHolder(view);
//                case VIEW_TYPE_ITEM_PARENT:
//                    view = layoutInflater.inflate(R.layout.list_item_comment_parent, parent, false);
//                    return new ParentCommentHolder(view);
//                case VIEW_TYPE_ITEM_CHILD:
//                    view = layoutInflater.inflate(R.layout.list_item_comment_child, parent, false);
//                    return new ChildCommentHolder(view);
//            }
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            if (holder instanceof HeaderCommentHolder) {
//                ((HeaderCommentHolder) holder).bindHeaderComment(mComments.size());
//            }
//            if (holder instanceof ParentCommentHolder) {
//                ((ParentCommentHolder) holder).bindParentComment(mComments.);
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            int totalCommentCount = 0;
//            for (Comment c : mComments) {
//                totalCommentCount += c.getChildCommentCount();
//            }
//            return totalCommentCount + 1;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            if (position == 0) {
//                return VIEW_TYPE_HEADER;
//            }
//
//        }
    }

    private static final int VIEW_TYPE_HEADER = -1;
    private static final int VIEW_TYPE_ITEM_PARENT = 1;
    private static final int VIEW_TYPE_ITEM_CHILD = 2;

    private class HeaderCommentHolder extends RecyclerView.ViewHolder {

        private TextView mCommentCountTextView;

        public HeaderCommentHolder(View itemView) {
            super(itemView);
            mCommentCountTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_header_comment_count_text_view);
        }

        public void bindHeaderComment(int count) {
            mCommentCountTextView.setText(String.valueOf(count));
        }
    }

    private class ParentCommentHolder extends ParentViewHolder implements View.OnClickListener {

        private Comment mParentComment;

        private CircleImageView mUserImageView;
        private TextView mUserNameTextView;
        private TextView mDateTextView;
        private TextView mDescriptionTextView;

        public ParentCommentHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mUserImageView = (CircleImageView)
                    itemView.findViewById(R.id.list_item_comment_parent_user_image_image_view);
            mUserNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_parent_user_name_text_view);
            mUserNameTextView.setOnClickListener(this);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_parent_date_text_view);
            mDescriptionTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_parent_description_text_view);
        }

        public void bindParentComment(Comment comment) {
            mParentComment = comment;
            Glide.with(getActivity())
                    .load(mParentComment.getUserImagePath())
                    .crossFade()
                    .into(mUserImageView);
            mUserNameTextView.setText(String.valueOf(mParentComment.getUserName()));
            mDateTextView.setText(String.valueOf(mParentComment.getDate()));
            mDescriptionTextView.setText(String.valueOf(mParentComment.getDescription()));
        }

        @Override
        public void onClick(View v) {
            /* 1 : review comment, 2 : content comment */
            super.onClick(v);
            if (v.getId() == R.id.list_item_comment_parent_user_name_text_view) {
                if (mCommentType == 1) {
                    Toast.makeText(getActivity(), "review " + mParentComment.getUserName(), Toast.LENGTH_SHORT).show();
                } else if (mCommentType == 2) {
                    int clickedPosition = -1;
                    for (Comment comment : mComments) {
                        if (comment.getId() == mParentComment.getId()) {
                            clickedPosition = mComments.indexOf(mParentComment);
                        }
                    }
                    float y = mCommentRecyclerView.getChildAt(clickedPosition).getY();
                    mParentComment.setPositionY(y);

                    mSelectedListener.onCommentSelected(mParentComment);
                }
            } else {
                if (mCommentType == 1) {
                    Toast.makeText(getActivity(), "review " + mParentComment.getDescription(), Toast.LENGTH_SHORT).show();
                } else if (mCommentType == 2) {
                    Toast.makeText(getActivity(), "content " + mParentComment.getDescription(), Toast.LENGTH_SHORT).show();
                }
            }




            switch (v.getId()) {
                case R.id.list_item_comment_parent_user_name_text_view:



                    break;
            }
        }
    }

    private class ChildCommentHolder extends ChildViewHolder {

        private Comment mChildComment;

        private CircleImageView mUserImageView;
        private TextView mUserNameTextView;
        private TextView mDateTextView;
        private TextView mDescriptionTextView;

        public ChildCommentHolder(View itemView) {
            super(itemView);

            mUserImageView = (CircleImageView)
                    itemView.findViewById(R.id.list_item_comment_child_user_image_image_view);
            mUserNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_child_user_name_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_child_date_text_view);
            mDescriptionTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_child_description_text_view);
        }

        public void bindChildComment(Comment comment) {
            mChildComment = comment;
            Glide.with(getActivity())
                    .load(mChildComment.getUserImagePath())
                    .crossFade()
                    .into(mUserImageView);
            mUserNameTextView.setText(String.valueOf(mChildComment.getUserName()));
            mDateTextView.setText(String.valueOf(mChildComment.getDate()));
            mDescriptionTextView.setText(String.valueOf(mChildComment.getDescription()));
        }
    }

    OnCommentSelectedListener mSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mSelectedListener = (OnCommentSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnChemicalSelectedListener");
        }
    }

}