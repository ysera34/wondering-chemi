package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Comment;
import com.planet.wondering.chemi.util.decorator.SeparatorDecoration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yoon on 2017. 4. 19..
 */

public class CommentFragment extends Fragment {

    public static CommentFragment newInstance() {

        Bundle args = new Bundle();

        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mCommentRecyclerView;
    private CommentAdapter mCommentAdapter;
    private ArrayList<Comment> mComments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComments = new ArrayList<>();

        ArrayList<Comment> childComments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Comment comment = new Comment();
            comment.setDescription("child comment test " + i);
            childComments.add(comment);
        }
        for (int i = 0; i < 5; i++) {
            Comment parentComment = new Comment();
            parentComment.setDescription("parent comment test " + i);
            parentComment.setChildComments(childComments);
            mComments.add(parentComment);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        mCommentRecyclerView = (RecyclerView) view.findViewById(R.id.comment_recycler_view);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    private void updateUI() {

        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(mComments);
            mCommentRecyclerView.setAdapter(mCommentAdapter);
        } else {
            mCommentAdapter.notifyDataSetChanged();
        }
    }

    private class CommentAdapter extends ExpandableRecyclerAdapter<Comment, Comment, ParentCommentHolder, ChildCommentHolder> {

        private List<Comment> mComments;
        private LayoutInflater mLayoutInflater;

        public CommentAdapter(@NonNull List<Comment> parentList) {
            super(parentList);
            mComments = parentList;
            mLayoutInflater = LayoutInflater.from(getActivity());
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

    private class ParentCommentHolder extends ParentViewHolder {

        private Comment mParentComment;

        private CircleImageView mUserImageView;
        private TextView mUserNameTextView;
        private TextView mDateTextView;
        private TextView mDescriptionTextView;

        public ParentCommentHolder(View itemView) {
            super(itemView);

            mUserImageView = (CircleImageView)
                    itemView.findViewById(R.id.list_item_comment_parent_user_image_image_view);
            mUserNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_parent_user_name_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_parent_date_text_view);
            mDescriptionTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_parent_description_text_view);
        }

        public void bindParentComment(Comment comment) {
            mParentComment = comment;
//            Glide.with(getActivity())
//                    .load(mParentComment.getUserImagePath())
//                    .crossFade()
//                    .into(mUserImageView);
            mUserNameTextView.setText(String.valueOf(mParentComment.getUserName()));
            mDateTextView.setText(String.valueOf(mParentComment.getDate()));
            mDescriptionTextView.setText(String.valueOf(mParentComment.getDescription()));
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
//            Glide.with(getActivity())
//                    .load(mChildComment.getUserImagePath())
//                    .crossFade()
//                    .into(mUserImageView);
            mUserNameTextView.setText(String.valueOf(mChildComment.getUserName()));
            mDateTextView.setText(String.valueOf(mChildComment.getDate()));
            mDescriptionTextView.setText(String.valueOf(mChildComment.getDescription()));
        }
    }


}
