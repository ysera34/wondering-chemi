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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.config.FAQ;
import com.planet.wondering.chemi.model.config.FAQBody;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.planet.wondering.chemi.network.Config.FAQ.FAQ_PATH;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
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
//        mFAQRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
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
        requestFAQList();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
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
//                        Toast.makeText(getActivity(),
//                                "자주 묻는 질문을 가져오는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestFAQ(final FAQ faq) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + FAQ_PATH + faq.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

//                        if (faq.getModifyDate().equals("null")) {
//                            faqBody.setUpdateDate(faq.getCreateDate());
//                        } else {
//                            faqBody.setUpdateDate(faq.getModifyDate());
//                        }
//                        FAQBody faqBody = Parser.parserFAQ(response);
//                        faq.getChildList().add(faqBody);
//                        updateUI();

                        ArrayList<FAQBody> faqBodies = Parser.parserFAQBodies(response);
                        faq.setFAQBodies(faqBodies);
                        updateUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
//                        Toast.makeText(getActivity(),
//                                "자주 묻는 질문을 가져오는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private class FAQAdapter extends ExpandableRecyclerAdapter<FAQ, FAQBody, ParentFAQHolder, ChildViewHolder> {

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
        public ChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
//            View view = mLayoutInflater.inflate(R.layout.list_item_faq_child, childViewGroup, false);
//            return new ChildFAQHolder(view);
            View faqBodyView;
            switch (viewType) {
                default:
                case FAQ_CHILD_ANSWER:
                    faqBodyView = mLayoutInflater.inflate(R.layout.list_item_faq_child_answer, childViewGroup, false);
                    return new ChildFAQAnswerHolder(faqBodyView);
                case FAQ_CHILD_IMAGEPATH:
                    faqBodyView = mLayoutInflater.inflate(R.layout.list_item_faq_child_image, childViewGroup, false);
                    return new ChildFAQImageHolder(faqBodyView);
            }
        }

        @Override
        public void onBindParentViewHolder(@NonNull ParentFAQHolder parentViewHolder, int parentPosition, @NonNull FAQ parent) {
            parentViewHolder.bindParentFAQ(parent);
        }

        @Override
        public void onBindChildViewHolder(@NonNull ChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull FAQBody child) {
//            childViewHolder.bindChildFAQ(child);
            if (childViewHolder instanceof ChildFAQAnswerHolder) {
                ((ChildFAQAnswerHolder) childViewHolder).bindChildFAQAnswer(child);
            }
            if (childViewHolder instanceof ChildFAQImageHolder) {
                ((ChildFAQImageHolder) childViewHolder).bindChildFAQImage(child);
            }
        }

        @Override
        public int getChildViewType(int parentPosition, int childPosition) {
            FAQBody faqBody = mFAQs.get(parentPosition).getFAQBody(childPosition);
            if (faqBody.isText()) {
                return FAQ_CHILD_ANSWER;
            } else {
                return FAQ_CHILD_IMAGEPATH;
            }
        }
    }

    public static final int FAQ_CHILD_ANSWER = 1011;
    public static final int FAQ_CHILD_IMAGEPATH = 1012;

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
            super.onClick(v);
            final int scrollPositionY = (int) v.getY();
            Log.i(TAG, "scrollPositionY:" + scrollPositionY);
            mFAQRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFAQRecyclerView.smoothScrollBy(0, scrollPositionY);
                }
            }, 300);

            if (mParentFAQ.getChildList() != null && mParentFAQ.getChildList().size() == 0) {
                requestFAQ(mParentFAQ);
            }
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
        private int[] mImageViewIds;
        private ImageView[] mImageViews;
//        private TextView mUpdateDateTextView;

        public ChildFAQHolder(@NonNull View itemView) {
            super(itemView);
            mAnswerTextView = (TextView)
                    itemView.findViewById(R.id.list_item_faq_child_answer_text_view);
            mImageLayout = (LinearLayout)
                    itemView.findViewById(R.id.list_item_faq_child_image_layout);

            mImageViewIds = new int[]{
                    R.id.list_item_faq_child_image1_image_view, R.id.list_item_faq_child_image2_image_view,
                    R.id.list_item_faq_child_image3_image_view, R.id.list_item_faq_child_image4_image_view,};
            mImageViews = new ImageView[mImageViewIds.length];

            for (int i = 0; i < mImageViews.length; i++) {
                mImageViews[i] = (ImageView) itemView.findViewById(mImageViewIds[i]);
            }
        }

        public void bindChildFAQ(FAQBody faqBody) {
            mFAQBody = faqBody;
//            mUpdateDateTextView.setText(String.valueOf(mFAQBody.getUpdateDate()));
//            mAnswerTextView.setText(String.valueOf(mFAQBody.getAnswer()));
            mAnswerTextView.setText(Html.fromHtml(String.valueOf(mFAQBody.getAnswer())));
            if (mFAQBody.getImagePaths() == null || mFAQBody.getImagePaths().size() == 0) {
                mImageLayout.setVisibility(View.GONE);
            } else {
                for (String str : mFAQBody.getImagePaths()) {
                    Log.i(TAG, "ImagePath: " + str);
                }
                for (ImageView imageView : mImageViews) {
                    imageView.setImageDrawable(null);
                    Glide.clear(imageView);
                }
                for (int i = 0; i < mFAQBody.getImagePaths().size(); i++) {
                    Glide.with(getActivity())
                            .load(mFAQBody.getImagePaths().get(i))
                            .listener(mRequestListener)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .crossFade()
                            .into(mImageViews[i]);
                }
                int emptyImageSize = mImageViews.length - mFAQBody.getImagePaths().size();
                for (int i = 1; i <= emptyImageSize; i++) {
                    mImageViews[mImageViews.length - i].setImageResource(R.drawable.widget_solid_rectangle_black_haze);
                }
            }
        }

        private RequestListener<String, GlideDrawable> mRequestListener =
                new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model,
                       Target<GlideDrawable> target, boolean isFirstResource) {
                Log.w(TAG, "RequestListener:onException: " + e.getMessage());
                Log.w(TAG, "RequestListener:onException: " + target.toString());
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model,
                       Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                final int imageHeight = resource.getIntrinsicHeight();
                Log.i(TAG, "imageHeight: " + imageHeight);
                Log.i(TAG, "model: " + model);
                Log.i(TAG, "target: " + target.toString());
                Log.i(TAG, "isFromMemoryCache: " + isFromMemoryCache);
                Log.i(TAG, "isFirstResource: " + isFirstResource);

                mFAQRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mFAQRecyclerView.smoothScrollBy(0, imageHeight);
                    }
                }, 200);
                return false;
            }
        };
    }

    private class ChildFAQAnswerHolder extends ChildViewHolder {

        private TextView mAnswerTextView;
        private FAQBody mFAQBody;

        public ChildFAQAnswerHolder(@NonNull View itemView) {
            super(itemView);
            mAnswerTextView = (TextView)
                    itemView.findViewById(R.id.list_item_faq_child_answer_text_view);
        }

        public void bindChildFAQAnswer(FAQBody faqBody) {
            mFAQBody = faqBody;
//            mAnswerTextView.setText(String.valueOf(mFAQBody.getAnswer()));
            mAnswerTextView.setText(Html.fromHtml(String.valueOf(mFAQBody.getAnswer())));
        }
    }

    private class ChildFAQImageHolder extends ChildViewHolder {

        private ImageView mImageView;
        private FAQBody mFAQBody;

        public ChildFAQImageHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_faq_child_image_image_view);
        }

        public void bindChildFAQImage(FAQBody faqBody) {
            mFAQBody = faqBody;
            Glide.with(getActivity())
                    .load(mFAQBody.getImagePath())
                    .listener(mRequestListener)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(mImageView);
        }

        private RequestListener<String, GlideDrawable> mRequestListener =
                new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model,
                                       Target<GlideDrawable> target, boolean isFirstResource) {
                Log.w(TAG, "RequestListener:onException: " + e.getMessage());
                Log.w(TAG, "RequestListener:onException: " + target.toString());
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model,
                                           Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                final int imageHeight = resource.getIntrinsicHeight();
                Log.i(TAG, "imageHeight: " + imageHeight);
                Log.i(TAG, "model: " + model);
                Log.i(TAG, "target: " + target.toString());
                Log.i(TAG, "isFromMemoryCache: " + isFromMemoryCache);
                Log.i(TAG, "isFirstResource: " + isFirstResource);

                mFAQRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mFAQBody.isFirstImage()) {
                            mFAQRecyclerView.smoothScrollBy(0, imageHeight / 3);
                        }
                    }
                }, 200);
                return false;
            }
        };
    }
}
