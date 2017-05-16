package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.planet.wondering.chemi.model.BottomSheetMenu;
import com.planet.wondering.chemi.model.Comment;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.adapter.BottomSheetMenuAdapter;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnCommentNestedScrollListener;
import com.planet.wondering.chemi.util.listener.OnCommentSelectedListener;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.planet.wondering.chemi.common.Common.CHILD_COMMENT_CLASS;
import static com.planet.wondering.chemi.common.Common.CONTENT_COMMENT_TYPE;
import static com.planet.wondering.chemi.common.Common.HORIZONTAL_CONTENT_VIEW_TYPE;
import static com.planet.wondering.chemi.common.Common.PARENT_COMMENT_CLASS;
import static com.planet.wondering.chemi.common.Common.REVIEW_COMMENT_TYPE;
import static com.planet.wondering.chemi.common.Common.VERTICAL_CONTENT_VIEW_TYPE;
import static com.planet.wondering.chemi.network.Config.Comment.AUTHOR_PATH;
import static com.planet.wondering.chemi.network.Config.Comment.COMMENT_PATH;
import static com.planet.wondering.chemi.network.Config.Comment.Key.DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.Content.CONTENT_PATH;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.Review.PATH;
import static com.planet.wondering.chemi.network.Config.Review.REVIEW_PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
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
    private static final String ARG_CONTENT_VIEW_TYPE = "content_view_type";

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

    public static CommentFragment newInstance(int relevantId, int commentType, int viewType) {

        Bundle args = new Bundle();
        args.putInt(ARG_RELEVANT_ID, relevantId);
        args.putInt(ARG_COMMENT_TYPE, commentType);
        args.putInt(ARG_CONTENT_VIEW_TYPE, viewType);

        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mCommentCountLayout;
    private TextView mCommentCountTextView;
    private LinearLayout mCommentEmptyLayout;
    private RecyclerView mCommentRecyclerView;
    private CommentAdapter mCommentAdapter;
    private ArrayList<Comment> mComments;

    private int mReviewId;
    private int mContentId;
    private int mRelevantId;
    private int mCommentId;
    private int mCommentType;
    private int mContentViewType;

    private BottomSheetDialog mMenuBottomSheetDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComments = new ArrayList<>();
        mRelevantId = getArguments().getInt(ARG_RELEVANT_ID, -1);
        mCommentType = getArguments().getInt(ARG_COMMENT_TYPE, -1);
        mContentViewType = getArguments().getInt(ARG_CONTENT_VIEW_TYPE, -1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        mCommentCountLayout = (LinearLayout) view.findViewById(R.id.comment_count_layout);
        mCommentCountTextView = (TextView) view.findViewById(R.id.comment_count_text_view);
        mCommentEmptyLayout = (LinearLayout) view.findViewById(R.id.comment_empty_layout);
        mCommentRecyclerView = (RecyclerView) view.findViewById(R.id.comment_recycler_view);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mCommentType == REVIEW_COMMENT_TYPE) {
            mCommentRecyclerView.setNestedScrollingEnabled(false);
        } else if (mCommentType == CONTENT_COMMENT_TYPE) {
            if (mContentViewType == VERTICAL_CONTENT_VIEW_TYPE) {
                mCommentRecyclerView.setNestedScrollingEnabled(false);
            } else if (mContentViewType == HORIZONTAL_CONTENT_VIEW_TYPE) {

            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                mCommentCountLayout.setElevation(7f);
//            }
        }
//        SeparatorDecoration decoration =
//                new SeparatorDecoration(getActivity(), android.R.color.transparent, 0.7f);
//        mCommentRecyclerView.addItemDecoration(decoration);

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

        updateCommentList(false);
    }

    private void updateUI() {

        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(mComments);
            mCommentRecyclerView.setAdapter(mCommentAdapter);
        } else {
            if (mCommentType == REVIEW_COMMENT_TYPE) {
                mCommentCountTextView.setText(String.valueOf(mComments.size()));
            } else if (mCommentType == CONTENT_COMMENT_TYPE) {
                int childItemCount = 0;
                for (Comment c : mComments) {
                    childItemCount += c.getChildList().size();
                }
                mCommentCountTextView.setText(String.valueOf(mComments.size() + childItemCount));
            }
            if (mComments.size() == 0) {
                mCommentEmptyLayout.setVisibility(View.VISIBLE);
                mCommentRecyclerView.setVisibility(View.GONE);
            } else {
                mCommentEmptyLayout.setVisibility(View.GONE);
                mCommentRecyclerView.setVisibility(View.VISIBLE);
                mCommentAdapter.setParentList(mComments, true);
//                mCommentAdapter.notifyDataSetChanged();
            }
        }
    }

    public void updateCommentList(boolean isAddComment) {
        requestComment(mRelevantId, mCommentType, isAddComment);
    }

    public void commentNestedScroll() {
        if (mContentViewType == HORIZONTAL_CONTENT_VIEW_TYPE) {
            mCommentRecyclerView.smoothScrollToPosition(mCommentRecyclerView.getAdapter().getItemCount());
        }
    }

    public void requestComment(final int relevantId, final int commentType, final boolean isAddComment) {

        String url = null;
        if (commentType == REVIEW_COMMENT_TYPE) {
            url = URL_HOST + PATH + File.separator + relevantId;
        } else if (commentType == CONTENT_COMMENT_TYPE) {
            url = URL_HOST + CONTENT_PATH + relevantId;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mComments = Parser.parseCommentList(response, commentType);
                        updateUI();
                        if (isAddComment) {
                            mCommentNestedScrollListener.onCommentNestedScroll();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
//                        Toast.makeText(getActivity(),
//                                "댓글를 가져오는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
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
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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

        @NonNull
        @Override
        public ParentCommentHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
            View view = null;
//            switch (viewType) {
//                case VIEW_TYPE_EMPTY:
//                    view = mLayoutInflater.inflate(R.layout.list_item_comment_empty, parentViewGroup, false);
//                    break;
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

    }

    private static final int VIEW_TYPE_HEADER = -221;
    private static final int VIEW_TYPE_EMPTY = 220;
    private static final int VIEW_TYPE_ITEM_PARENT = 221;
    private static final int VIEW_TYPE_ITEM_CHILD = 222;

    private class EmptyCommentHolder extends ParentViewHolder {

        public EmptyCommentHolder(View itemView) {
            super(itemView);
        }
    }

    private class ParentCommentHolder extends ParentViewHolder
            implements View.OnClickListener {

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
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_parent_date_text_view);
            mDescriptionTextView = (TextView)
                    itemView.findViewById(R.id.list_item_comment_parent_description_text_view);
        }

        public void bindParentComment(Comment comment) {
            mParentComment = comment;

            if (!mParentComment.getUserImagePath().equals("null")) {
                Glide.with(getActivity())
                        .load(mParentComment.getUserImagePath())
                        .crossFade()
                        .into(mUserImageView);
            } else {
//                if (mParentComment.getUserImagePath() == null ||
//                        mParentComment.getUserImagePath().equals("null"))
//                if (mParentComment.getId() == 27) {
//                    Log.i(TAG, mParentComment.getUserImagePath());
//                }
                switch (mParentComment.getUserGender()) {
                    case -1:
                        mUserImageView.setImageResource(R.drawable.ic_user);
                        break;
                    case 0:
                        mUserImageView.setImageResource(R.drawable.ic_user_profile_mommy);
                        break;
                    case 1:
                        mUserImageView.setImageResource(R.drawable.ic_user_profile_daddy);
                        break;
                }
            }
            mUserNameTextView.setText(String.valueOf(mParentComment.getUserName()));
            mDateTextView.setText(String.valueOf(mParentComment.getDate()));
            mDescriptionTextView.setText(String.valueOf(mParentComment.getDescription()));
        }

        @Override
        public void onClick(View v) {
            if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
                if (mParentComment.getUserName().equals("")) {
                    Toast.makeText(getActivity(), "해당 글에는 답글을 작성할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    mParentComment.setPositionY(v.getY());
                    requestConfirmCommentAuthor(mRelevantId, mParentComment, mCommentType, PARENT_COMMENT_CLASS);
                }
            } else {
                Toast.makeText(getActivity(), "로그인 하시면 답글을 작성하실 수 있어요.", Toast.LENGTH_SHORT).show();
//                CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
//                        .newInstance(R.drawable.ic_login, R.string.login_info_message, R.string.login_button_title);
//                dialogFragment1.show(getChildFragmentManager(), LOGIN_DIALOG);
            }
        }
    }

    private class ChildCommentHolder extends ChildViewHolder
            implements View.OnClickListener {

        private Comment mChildComment;

        private CircleImageView mUserImageView;
        private TextView mUserNameTextView;
        private TextView mDateTextView;
        private TextView mDescriptionTextView;

        public ChildCommentHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

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

            if (!mChildComment.getUserImagePath().equals("null")) {
            Glide.with(getActivity())
                    .load(mChildComment.getUserImagePath())
                    .crossFade()
                    .into(mUserImageView);
            } else {
//                if (mChildComment.getUserImagePath() == null ||
//                        mChildComment.getUserImagePath().equals("null"))
                switch (mChildComment.getUserGender()) {
                    case -1:
                        mUserImageView.setImageResource(R.drawable.ic_user);
                        break;
                    case 0:
                        mUserImageView.setImageResource(R.drawable.ic_user_profile_mommy);
                        break;
                    case 1:
                        mUserImageView.setImageResource(R.drawable.ic_user_profile_daddy);
                        break;
                }
            }
            mUserNameTextView.setText(String.valueOf(mChildComment.getUserName()));
            mDateTextView.setText(String.valueOf(mChildComment.getDate()));

            String[] strings = String.valueOf(mChildComment.getDescription()).split("@");
            if (strings.length > 1) {
                mDescriptionTextView.setText(highlightUserNameText());
            } else {
                mDescriptionTextView.setText(String.valueOf(mChildComment.getDescription()));
            }
        }

        @Override
        public void onClick(View v) {
            if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
                if (mChildComment.getUserName().equals("")) {
                    Toast.makeText(getActivity(), "해당 글에는 답글을 작성할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    mChildComment.setPositionY(v.getY());
                    requestConfirmCommentAuthor(mRelevantId, mChildComment, mCommentType, CHILD_COMMENT_CLASS);
                }
            } else {
                Toast.makeText(getActivity(), "로그인 하시면 답글을 작성하실 수 있어요.", Toast.LENGTH_SHORT).show();
//                CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
//                        .newInstance(R.drawable.ic_login, R.string.login_info_message, R.string.login_button_title);
//                dialogFragment1.show(getChildFragmentManager(), LOGIN_DIALOG);
            }
        }

        private SpannableString highlightUserNameText() {

            int startIndex = 0;
            int endIndex = String.valueOf(mChildComment.getDescription()).split("@")[1].length() + 1;

            StringBuilder sb = new StringBuilder();
            sb.append("@")
                    .append(String.valueOf(mChildComment.getDescription()).split("@")[1]).append(" ")
                    .append(String.valueOf(mChildComment.getDescription()).split("@")[0]);

            SpannableString spannableString = new SpannableString(sb.toString());

            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                    startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannableString;
        }
    }

    OnCommentSelectedListener mSelectedListener;
    OnCommentNestedScrollListener mCommentNestedScrollListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mSelectedListener = (OnCommentSelectedListener) context;
            mCommentNestedScrollListener = (OnCommentNestedScrollListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCommentSelectedListener");
        }
    }

    private void editCommentBottomSheetDialog(final Comment comment, final boolean isCommentAuthor,
                                              final int commentClass) {
        if (dismissMenuBottomSheetDialog()) {
            return;
        }
        ArrayList<BottomSheetMenu> bottomSheetMenus = new ArrayList<>();
        if (mCommentType == CONTENT_COMMENT_TYPE) {
            bottomSheetMenus.add(new BottomSheetMenu(0, R.string.bottom_sheet_menu_comment_comment));
        }
        if (isCommentAuthor) {
            bottomSheetMenus.add(new BottomSheetMenu(0, R.string.bottom_sheet_menu_comment_edit));
            bottomSheetMenus.add(new BottomSheetMenu(0, R.string.bottom_sheet_menu_comment_delete));
        }

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.layout_bottom_sheet_menu_recycler_view, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bottom_sheet_menu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BottomSheetMenuAdapter bottomSheetMenuAdapter = new BottomSheetMenuAdapter(bottomSheetMenus);
        recyclerView.setAdapter(bottomSheetMenuAdapter);
        bottomSheetMenuAdapter.setItemClickListener(new BottomSheetMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BottomSheetMenuAdapter.MenuItemHolder itemHolder, int position) {
                dismissMenuBottomSheetDialog();
                if (mCommentType == CONTENT_COMMENT_TYPE) {
                    switch (position) {
                        case 0: /* comment comment */
                            mSelectedListener.onCommentSelected(comment, commentClass);
                            break;
                        case 1: /* comment edit */
                            BottomSheetDialogFragment editCommentFragment =
                                    CommentEditBottomSheetDialogFragment.newInstance(comment.getDescription());
                            editCommentFragment.show(getChildFragmentManager(), "comment_edit");
                            mCommentId = comment.getId();
                            break;
                        case 2: /* comment delete */
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage("댓글을 정말 삭제하시겠어요?");
                            builder1.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestDeleteComment(mRelevantId, comment.getId(), mCommentType);
                                }
                            });
                            builder1.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog dialog1 = builder1.create();
                            dialog1.show();
                            break;
                    }
                } else if (mCommentType == REVIEW_COMMENT_TYPE) {
                    switch (position) {
                        case 0: /* comment edit */
                            BottomSheetDialogFragment editCommentFragment =
                                    CommentEditBottomSheetDialogFragment.newInstance(comment.getDescription());
                            editCommentFragment.show(getChildFragmentManager(), "comment_edit");
                            mCommentId = comment.getId();
                            break;
                        case 1: /* comment delete */
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage("댓글을 정말 삭제하시겠어요?");
                            builder1.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestDeleteComment(mRelevantId, comment.getId(), mCommentType);
                                }
                            });
                            builder1.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog dialog1 = builder1.create();
                            dialog1.show();
                            break;
                    }
                }
            }
        });
        mMenuBottomSheetDialog = new BottomSheetDialog(getActivity());
        mMenuBottomSheetDialog.setContentView(view);
        mMenuBottomSheetDialog.show();
    }

    private boolean dismissMenuBottomSheetDialog() {
        if (mMenuBottomSheetDialog != null && mMenuBottomSheetDialog.isShowing()) {
            mMenuBottomSheetDialog.dismiss();
            return true;
        }
        return false;
    }

    public void commentEditDialogFinished(String description) {
        requestEditComment(mRelevantId, mCommentId, description, mCommentType);
    }

    private boolean isCommentAuthor = false;

    private void requestConfirmCommentAuthor(final int relevantId, final Comment comment,
                                             final int commentType, final int commentClass) {

        String url = null;
        if (mCommentType == REVIEW_COMMENT_TYPE) {
            url = URL_HOST + PATH + COMMENT_PATH + File.separator + comment.getId() + AUTHOR_PATH;
        } else if (mCommentType == CONTENT_COMMENT_TYPE) {
            url = URL_HOST + COMMENT_PATH + File.separator + comment.getId() + AUTHOR_PATH;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        isCommentAuthor = Parser.parseCommentAuthor(response);

                        if (mCommentType == REVIEW_COMMENT_TYPE) {
                            if (isCommentAuthor) {
                                editCommentBottomSheetDialog(comment, isCommentAuthor, commentClass);
                            }
                        } else if (mCommentType == CONTENT_COMMENT_TYPE) {
                            editCommentBottomSheetDialog(comment, isCommentAuthor, commentClass);
                        }
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
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestEditComment(int relevantId, int commentId, String commentDescription,
                                    final int commentType) {

        String url = null;
        if (commentType == REVIEW_COMMENT_TYPE) {
            url = URL_HOST + REVIEW_PATH + relevantId + COMMENT_PATH + File.separator + commentId;
        } else if (commentType == CONTENT_COMMENT_TYPE) {
            url = URL_HOST + CONTENT_PATH + relevantId + COMMENT_PATH + File.separator + commentId;
        }

        Map<String, String> params = new HashMap<>();
        params.put(DESCRIPTION, commentDescription);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mComments = Parser.parseCommentList(response, commentType);
                        updateUI();
                        Toast.makeText(getActivity(), "댓글이 수정되었어요.", Toast.LENGTH_SHORT).show();
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
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestDeleteComment(int relevantId, int commentId, final int commentType) {

        String url = null;
        if (commentType == REVIEW_COMMENT_TYPE) {
            url = URL_HOST + REVIEW_PATH + relevantId + COMMENT_PATH + File.separator + commentId;
        } else if (commentType == CONTENT_COMMENT_TYPE) {
            url = URL_HOST + CONTENT_PATH + relevantId + COMMENT_PATH + File.separator + commentId;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mComments = Parser.parseCommentList(response, commentType);
                        updateUI();
                        Toast.makeText(getActivity(), "댓글이 삭제되었어요.", Toast.LENGTH_SHORT).show();
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
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

}
