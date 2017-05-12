package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.config.FAQ;
import com.planet.wondering.chemi.model.config.FAQBody;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.listener.OnRecyclerViewScrollListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.planet.wondering.chemi.network.Config.FAQ.FAQ_PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 3. 20..
 */

public class MemberConfigFAQFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberConfigFAQFragment.class.getSimpleName();

    public static MemberConfigFAQFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigFAQFragment fragment = new MemberConfigFAQFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mBackLayout;
    private FAQAdapter mFAQAdapter;
    private RecyclerView mFAQRecyclerView;
    private ArrayList<FAQ> mFAQs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFAQs = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_faq, container, false);
        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_faq_back_layout);
        mBackLayout.setOnClickListener(this);
        mFAQRecyclerView = (RecyclerView) view.findViewById(R.id.member_config_faq_recycler_view);
        mFAQRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFAQRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
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

    @Override
    public void onResume() {
        super.onResume();
        requestFAQList();
    }

    private void updateUI() {
        if (mFAQAdapter == null) {
            mFAQAdapter = new FAQAdapter(mFAQs);
            mFAQRecyclerView.setAdapter(mFAQAdapter);
        } else {
            mFAQAdapter.setParentList(mFAQs, true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_faq_back_layout:
                getActivity().onBackPressed();
                break;
        }
    }

    private void requestFAQList() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + FAQ_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mFAQs = Parser.parseFAQList(response);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                "자주 묻는 질문을 가져오는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestFAQ(final FAQ faq) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + FAQ_PATH + faq.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        FAQBody faqBody = Parser.parserFAQ(response);
                        if (faq.getModifyDate().equals("null")) {
                            faqBody.setUpdateDate(faq.getCreateDate());
                        } else {
                            faqBody.setUpdateDate(faq.getModifyDate());
                        }
                        faq.getChildList().add(faqBody);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                "자주 묻는 질문을 가져오는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private class FAQAdapter extends ExpandableRecyclerAdapter<FAQ, FAQBody, ParentFAQHolder, ChildFAQHolder> {

        private LayoutInflater mLayoutInflater;

        public FAQAdapter(@NonNull List<FAQ> parentList) {
            super(parentList);
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @NonNull
        @Override
        public ParentFAQHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_faq_parent, parentViewGroup, false);
            return new ParentFAQHolder(view);
        }

        @NonNull
        @Override
        public ChildFAQHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_faq_child, childViewGroup, false);
            return new ChildFAQHolder(view);
        }

        @Override
        public void onBindParentViewHolder(@NonNull ParentFAQHolder parentViewHolder, int parentPosition, @NonNull FAQ parent) {
            parentViewHolder.bindParentFAQ(parent);
        }

        @Override
        public void onBindChildViewHolder(@NonNull ChildFAQHolder childViewHolder, int parentPosition, int childPosition, @NonNull FAQBody child) {
            childViewHolder.bindChildFAQ(child);
        }
    }

    private class ParentFAQHolder extends ParentViewHolder {

        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180.0f;

        private FAQ mParentFAQ;

        private TextView mSequenceTextView;
        private TextView mQuestionTextView;
        private ImageView mDownArrowImageView;

        public ParentFAQHolder(@NonNull View itemView) {
            super(itemView);
            mSequenceTextView = (TextView) itemView.findViewById(R.id.list_item_faq_parent_sequence_text_view);
            mQuestionTextView = (TextView) itemView.findViewById(R.id.list_item_faq_parent_question_text_view);
            mDownArrowImageView = (ImageView) itemView.findViewById(R.id.list_item_faq_parent_arrow_image_view);
        }

        public void bindParentFAQ(FAQ faq) {
            mParentFAQ = faq;
            int questionNumber = mFAQs.indexOf(mParentFAQ) + 1;
            String numberStr;
            if (questionNumber < 10) {
                numberStr = "0" + String.valueOf(questionNumber);
            } else {
                numberStr = String.valueOf(questionNumber);
            }
            mSequenceTextView.setText(String.valueOf(numberStr));
//            mQuestionTextView.setText(String.valueOf(mParentFAQ.getQuestion()));
            mQuestionTextView.setText(Html.fromHtml(String.valueOf(mParentFAQ.getQuestion())));
        }

        @Override
        public void onClick(View v) {
            if (mParentFAQ.getChildList() != null && mParentFAQ.getChildList().size() == 0) {
                requestFAQ(mParentFAQ);
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

    private class ChildFAQHolder extends ChildViewHolder {

        private FAQBody mFAQBody;

        private TextView mAnswerTextView;
        private LinearLayout mImageLayout;
        private ImageView mImage1ImageView;
        private TextView mUpdateDateTextView;

        public ChildFAQHolder(@NonNull View itemView) {
            super(itemView);
            mAnswerTextView = (TextView)
                    itemView.findViewById(R.id.list_item_faq_child_answer_text_view);
            mImageLayout = (LinearLayout)
                    itemView.findViewById(R.id.list_item_faq_child_image_layout);
            mImage1ImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_faq_child_image1_image_view);
            mUpdateDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_faq_child_date_text_view);
        }

        public void bindChildFAQ(FAQBody faqBody) {
            mFAQBody = faqBody;
//            mAnswerTextView.setText(String.valueOf(mFAQBody.getAnswer()));
            mAnswerTextView.setText(Html.fromHtml(String.valueOf(mFAQBody.getAnswer())));
            if (mFAQBody.getImagePaths() == null || mFAQBody.getImagePaths().size() == 0) {
                mImageLayout.setVisibility(View.GONE);
            } else {
                Glide.with(getActivity())
                        .load(mFAQBody.getImagePaths().get(0))
                        .crossFade()
                        .into(mImage1ImageView);
            }
            mUpdateDateTextView.setText(String.valueOf(mFAQBody.getUpdateDate()));
        }
    }

}
