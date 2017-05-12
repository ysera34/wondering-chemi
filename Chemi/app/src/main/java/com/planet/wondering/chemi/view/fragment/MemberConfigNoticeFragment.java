package com.planet.wondering.chemi.view.fragment;

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
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.config.Notice;
import com.planet.wondering.chemi.model.config.NoticeBody;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.planet.wondering.chemi.network.Config.Notice.NOTICE_PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 3. 20..
 */

public class MemberConfigNoticeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberConfigNoticeFragment.class.getSimpleName();

    public static MemberConfigNoticeFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigNoticeFragment fragment = new MemberConfigNoticeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mBackLayout;
    private NoticeAdapter mNoticeAdapter;
    private RecyclerView mNoticeRecyclerView;
    private ArrayList<Notice> mNotices;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotices = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_notice, container, false);

        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_notice_back_layout);
        mBackLayout.setOnClickListener(this);
        mNoticeRecyclerView = (RecyclerView) view.findViewById(R.id.member_config_notice_recycler_view);
        mNoticeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mNoticeRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
//            @Override
//            public void onShowView() {
//                ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
//            }
//
//            @Override
//            public void onHideView() {
//                ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
//            }
//        });

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
        requestNoticeList();
    }

    private void updateUI() {
        if (mNoticeAdapter == null) {
            mNoticeAdapter = new NoticeAdapter(mNotices);
            mNoticeRecyclerView.setAdapter(mNoticeAdapter);
        } else {
            mNoticeAdapter.setParentList(mNotices, true);
//            mNoticeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_notice_back_layout:
                getActivity().onBackPressed();
                break;
        }
    }

    private void requestNoticeList() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + NOTICE_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mNotices = Parser.parseNoticeList(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                "공지사항을 가져오는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestNotice(final Notice notice) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + NOTICE_PATH + notice.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        NoticeBody noticeBody = Parser.parserNotice(response);
                        notice.getChildList().add(noticeBody);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                "공지사항을 가져오는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private class NoticeAdapter extends ExpandableRecyclerAdapter<Notice, NoticeBody, ParentNoticeHolder, ChildNoticeHolder> {

        private LayoutInflater mLayoutInflater;

        public NoticeAdapter(@NonNull List<Notice> parentList) {
            super(parentList);
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @NonNull
        @Override
        public ParentNoticeHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_notification_parent, parentViewGroup, false);
            return new ParentNoticeHolder(view);
        }

        @NonNull
        @Override
        public ChildNoticeHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_notification_child, childViewGroup, false);
            return new ChildNoticeHolder(view);
        }

        @Override
        public void onBindParentViewHolder(@NonNull ParentNoticeHolder parentViewHolder, int parentPosition, @NonNull Notice parent) {
            parentViewHolder.bindParentNotice(parent);
        }

        @Override
        public void onBindChildViewHolder(@NonNull ChildNoticeHolder childViewHolder, int parentPosition, int childPosition, @NonNull NoticeBody child) {
            childViewHolder.bindChildNotice(child);
        }
    }

    private class ParentNoticeHolder extends ParentViewHolder {

        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180.0f;

        private Notice mParentNotice;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mDownArrowImageView;

        public ParentNoticeHolder(@NonNull View itemView) {
            super(itemView);


            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_notification_parent_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_notification_parent_date_text_view);
            mDownArrowImageView = (ImageView) itemView.findViewById(R.id.list_item_notification_parent_arrow_image_view);
        }

        public void bindParentNotice(Notice notice) {
            mParentNotice = notice;
            mTitleTextView.setText(String.valueOf(mParentNotice.getTitle()));
            mDateTextView.setText(String.valueOf(mParentNotice.getCreateDate()));
        }

        @Override
        public void onClick(View v) {
            if (mParentNotice.getChildList() != null && mParentNotice.getChildList().size() == 0) {
                requestNotice(mParentNotice);
            }
            super.onClick(v);
        }

        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (expanded) {
                mDownArrowImageView.setRotation(ROTATED_POSITION);
            } else {
                mDownArrowImageView.setRotation(INITIAL_POSITION);
            }
        }

        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            mDownArrowImageView.startAnimation(rotateAnimation);
        }
    }

    private class ChildNoticeHolder extends ChildViewHolder {

        private NoticeBody mNoticeBody;

        private TextView mDescriptionTextView;

        public ChildNoticeHolder(@NonNull View itemView) {
            super(itemView);
            mDescriptionTextView = (TextView)
                    itemView.findViewById(R.id.list_item_notification_child_description_text_view);
        }

        public void bindChildNotice(NoticeBody noticeBody) {
            mNoticeBody = noticeBody;
            mDescriptionTextView.setText(String.valueOf(mNoticeBody.getDescription()));
        }
    }

}
