package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.AppBaseActivity;
import com.planet.wondering.chemi.model.Comment;
import com.planet.wondering.chemi.model.Content;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnCommentEditDialogFinishedListener;
import com.planet.wondering.chemi.util.listener.OnCommentNestedScrollListener;
import com.planet.wondering.chemi.util.listener.OnCommentSelectedListener;
import com.planet.wondering.chemi.util.listener.OnDialogFinishedListener;
import com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment;
import com.planet.wondering.chemi.view.fragment.ContentHorizontalFragment;
import com.planet.wondering.chemi.view.fragment.ContentVerticalFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.common.Common.HORIZONTAL_CONTENT_VIEW_TYPE;
import static com.planet.wondering.chemi.common.Common.VERTICAL_CONTENT_VIEW_TYPE;
import static com.planet.wondering.chemi.network.Config.Comment.COMMENT_PATH;
import static com.planet.wondering.chemi.network.Config.Comment.Key.DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.Content.CONTENT_PATH;
import static com.planet.wondering.chemi.network.Config.Content.Key.KEEPER_PATH;
import static com.planet.wondering.chemi.network.Config.Content.Key.LIKE_PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.LOGIN_DIALOG;

/**
 * Created by yoon on 2017. 3. 31..
 */

public class ContentActivity extends AppBaseActivity implements View.OnClickListener,
        OnCommentSelectedListener, OnDialogFinishedListener,
        OnCommentNestedScrollListener, OnCommentEditDialogFinishedListener {

    private static final String TAG = ContentActivity.class.getSimpleName();

    private static final String EXTRA_CONTENT_ID = "com.planet.wondering.chemi.content_id";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ContentActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int contentId) {
        Intent intent = new Intent(packageContext, ContentActivity.class);
        intent.putExtra(EXTRA_CONTENT_ID, contentId);
        return intent;
    }

    private InputMethodManager mInputMethodManager;

    private FrameLayout mContentFragmentContainer;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private int mContentId;
    private Content mContent;

    private Toolbar mContentToolbar;
    private Menu mContentToolbarMenu;

    private RelativeLayout mContentCommentEditLayout;
    private EditText mContentCommentEditText;
    private TextView mContentCommentSubmitTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mContentId = getIntent().getIntExtra(EXTRA_CONTENT_ID, -1);
        mContent = new Content();

        mContentToolbar = (Toolbar) findViewById(R.id.content_toolbar);
        setSupportActionBar(mContentToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContentFragmentContainer = (FrameLayout) findViewById(R.id.content_fragment_container);
        mContentCommentEditLayout = (RelativeLayout) findViewById(R.id.content_comment_edit_layout);
        mContentCommentEditText = (EditText) findViewById(R.id.content_comment_edit_text);
        mContentCommentSubmitTextView = (TextView) findViewById(R.id.content_comment_submit_text_view);
        mContentCommentSubmitTextView.setOnClickListener(this);
        validationEditText();

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.content_fragment_container);


//        if (mFragment == null) {
//            mFragment = ContentVerticalFragment.newInstance();
//            mFragmentManager.beginTransaction()
//                    .add(R.id.content_fragment_container, mFragment)
//                    .commit();
//        }
        requestContent(mContentId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_comment_submit_text_view:
                if (isValidatedCreateComment) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ContentActivity.this);
                    builder1.setMessage("댓글을 등록하시겠어요?");
                    builder1.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mInputMethodManager.hideSoftInputFromWindow(mContentCommentEditText.getWindowToken(), 0);
                            requestCreateContentComment(mContent);
                        }
                    });
                    builder1.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog1 = builder1.create();
                    dialog1.show();
                } else {
                    Toast.makeText(getApplicationContext(), "댓글을 입력해보세요!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mContentToolbarMenu = menu;
        getMenuInflater().inflate(R.menu.menu_toolbar_content, menu);
        if (mContent != null) {
            if (!mContent.isLike()) {
                mContentToolbarMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_like_false));
            } else {
                mContentToolbarMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_like_true));
            }
            if (!mContent.isArchive()) {
                mContentToolbarMenu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_archive_false));
            } else {
                mContentToolbarMenu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_archive_true));
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_content_like:
                if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
                    requestLikeContent(mContent.isLike());
                } else {
                    CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                            .newInstance(R.drawable.ic_login, R.string.login_info_message, R.string.login_button_title);
                    dialogFragment1.show(getSupportFragmentManager(), LOGIN_DIALOG);
                }
//                mContentToolbarMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_like_true));
                break;
            case R.id.action_content_archive:
                if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
                    requestArchiveContent(mContent.isArchive());
                } else {
                    CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                            .newInstance(R.drawable.ic_login, R.string.login_info_message, R.string.login_button_title);
                    dialogFragment1.show(getSupportFragmentManager(), LOGIN_DIALOG);
                }
//                mContentToolbarMenu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_archive_true));
                break;
            case R.id.action_content_share:
                Toast.makeText(getApplicationContext(), "action_share", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "action_share");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestContent(int contentId) {

        Log.i(TAG, "url : " + URL_HOST + CONTENT_PATH + contentId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + CONTENT_PATH + contentId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mContent = Parser.parseContent(response);
//                        bindContent(mContent);
                        switch (mContent.getViewType()) {
                            case VERTICAL_CONTENT_VIEW_TYPE:
                                mFragment = ContentVerticalFragment.newInstance(mContent);
                                break;
                            case HORIZONTAL_CONTENT_VIEW_TYPE:
                                mFragment = ContentHorizontalFragment.newInstance(mContent);
                                break;
                        }
                        mFragmentManager.beginTransaction()
                                .add(R.id.content_fragment_container, mFragment)
                                .commit();
                        invalidateOptionsMenu();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getApplicationContext(),
                                R.string.progress_dialog_message_error, Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getApplicationContext()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestCreateContentComment(Content content) {

        Map<String, String> params = new HashMap<>();
        params.put(DESCRIPTION, mContentCommentEditText.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + CONTENT_PATH + content.getId() + COMMENT_PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "댓글이 등록되었어요", Toast.LENGTH_SHORT).show();

                        mContentCommentEditText.getText().clear();
                        mContentCommentEditText.clearFocus();
                        mContentCommentSubmitTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                        mContentCommentSubmitTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                        isValidatedCreateComment = false;

                        Fragment fragment = mFragmentManager.findFragmentById(R.id.content_fragment_container);
                        switch (mContent.getViewType()) {
                            case VERTICAL_CONTENT_VIEW_TYPE:
                                if (fragment instanceof ContentVerticalFragment) {
                                    ((ContentVerticalFragment) fragment).updateCommentList(true);
                                }
                                break;
                            case HORIZONTAL_CONTENT_VIEW_TYPE:
                                if (fragment instanceof ContentHorizontalFragment) {
                                    ((ContentHorizontalFragment) fragment).updateCommentList(true);
                                }
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getApplicationContext(),
                                "컨텐츠의 댓글을 등록하는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getApplicationContext()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestLikeContent(final boolean isLike) {

        int requestMethodId;
        if (!isLike) {
            requestMethodId = Request.Method.POST;
        } else {
            requestMethodId = Request.Method.DELETE;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                requestMethodId, URL_HOST + CONTENT_PATH + mContent.getId() + LIKE_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {

                            if (!isLike) {
                                Toast.makeText(getApplicationContext(), "\"좋아요\"하였습니다.", Toast.LENGTH_SHORT).show();
                                mContent.setLike(true);
                            } else {
                                Toast.makeText(getApplicationContext(), "\"좋아요\"취소하였습니다.", Toast.LENGTH_SHORT).show();
                                mContent.setLike(false);
                            }
                            invalidateOptionsMenu();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getApplicationContext(),
                                "컨텐츠 좋아요하는 중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getApplicationContext()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestArchiveContent(final boolean isArchive) {

        int requestMethodId;
        if (!isArchive) {
            requestMethodId = Request.Method.POST;
        } else {
            requestMethodId = Request.Method.DELETE;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                requestMethodId, URL_HOST + CONTENT_PATH + mContent.getId() + KEEPER_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {

                            if (!isArchive) {
                                Toast.makeText(getApplicationContext(), "보관함에 보관되었어요.", Toast.LENGTH_SHORT).show();
                                mContent.setArchive(true);
                            } else {
                                Toast.makeText(getApplicationContext(), "보관함에세 삭제되었어요.", Toast.LENGTH_SHORT).show();
                                mContent.setArchive(false);
                            }
                            invalidateOptionsMenu();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getApplicationContext(),
                                "컨텐츠 보관 중에 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getApplicationContext()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

//    mContentFragmentContainer
    public void showCommentEditText() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ABOVE, R.id.content_comment_edit_layout);
        mContentFragmentContainer.setLayoutParams(params);

        mContentCommentEditLayout.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(2));
    }

    public void hideCommentEditText() {
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mContentFragmentContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mContentFragmentContainer.setLayoutParams(params);
            }
        }, 200);

        mContentCommentEditLayout.animate().translationY(mContentCommentEditLayout.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
    }

    public void setStatusBarTranslucent(boolean makeTranslucent) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 144);
        if (makeTranslucent) {
            params.setMargins(0, 72, 0, 0);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            params.setMargins(0, 0, 0, 0);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mContentToolbar.setLayoutParams(params);
    }

    private boolean isValidatedCreateComment = false;

    private void validationEditText() {
        mContentCommentEditText.addTextChangedListener(new TextValidator(mContentCommentEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.length() > 0) {
                    mContentCommentSubmitTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                    mContentCommentSubmitTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                    isValidatedCreateComment = true;
                } else {
                    mContentCommentSubmitTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                    mContentCommentSubmitTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                    isValidatedCreateComment = false;
                }
            }
        });
    }

    @Override
    public void onCommentSelected(Comment comment) {
        String userNameString = "@" + comment.getUserName() + " ";
        mContentCommentEditText.setText(String.valueOf(userNameString));
        mContentCommentEditText.setSelection(userNameString.length());
//        mCommentCreateEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        mContentCommentEditText.requestFocus();
        mInputMethodManager.showSoftInput(mContentCommentEditText, InputMethodManager.SHOW_IMPLICIT);
//        mReviewReadNestedScrollView.smoothScrollTo(0, (int) comment.getPositionY() + 300);
//        Fragment fragment = mChildFragmentManager.findFragmentById(R.id.review_comment_fragment_container);
//        if (fragment instanceof CommentFragment) {
//
//        }
    }

    @Override
    public void onDialogFinished(boolean isChose) {
        if (isChose) {
            startActivity(MemberStartActivity.newIntent(getApplicationContext()));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onCommentNestedScroll() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.content_fragment_container);
        if (fragment instanceof ContentHorizontalFragment) {
            ((ContentHorizontalFragment) fragment).commentNestedScroll();
        } else if (fragment instanceof ContentVerticalFragment) {
            ((ContentVerticalFragment) fragment).commentNestedScroll();
        }
    }

    @Override
    public void onCommentEditDialogFinished(String description) {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.content_fragment_container);
        if (fragment instanceof ContentHorizontalFragment) {
            ((ContentHorizontalFragment) fragment).commentEditDialogFinished(description);
        } else if (fragment instanceof ContentVerticalFragment) {
            ((ContentVerticalFragment) fragment).commentEditDialogFinished(description);
        }
    }
}
