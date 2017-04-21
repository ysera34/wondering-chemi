package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.planet.wondering.chemi.util.listener.OnCommentSelectedListener;
import com.planet.wondering.chemi.view.fragment.ContentHorizontalFragment;
import com.planet.wondering.chemi.view.fragment.ContentVerticalFragment;

import org.json.JSONObject;

import static com.planet.wondering.chemi.common.Common.HORIZONTAL_CONTENT_VIEW_TYPE;
import static com.planet.wondering.chemi.common.Common.VERTICAL_CONTENT_VIEW_TYPE;
import static com.planet.wondering.chemi.network.Config.Content.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 3. 31..
 */

public class ContentActivity extends AppBaseActivity implements OnCommentSelectedListener {

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

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private int mContentId;
    private Content mContent;

    private Toolbar mContentToolbar;
    private Menu mContentToolbarMenu;

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

        mContentCommentEditText = (EditText) findViewById(R.id.content_comment_edit_text);
        mContentCommentSubmitTextView = (TextView) findViewById(R.id.content_comment_submit_text_view);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.content_fragment_container);


//        if (mFragment == null) {
//            mFragment = ContentVerticalFragment.newInstance();
//            mFragmentManager.beginTransaction()
//                    .add(R.id.content_fragment_container, mFragment)
//                    .commit();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestContent(mContentId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (mContent != null) {
//            if (!mContent.isArchive()) {
//
//            }
//        }
        mContentToolbarMenu = menu;
        getMenuInflater().inflate(R.menu.menu_toolbar_content, menu);
        if (mContent != null) {
            if (!mContent.isLike()) {

            } else {

            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_content_like:
                Toast.makeText(getApplicationContext(), "action_like", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "action_like");
                mContentToolbarMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_like_true));
                break;
            case R.id.action_content_archive:
                Toast.makeText(getApplicationContext(), "action_archive", Toast.LENGTH_SHORT).show();
                mContentToolbarMenu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_archive_true));
                Log.i(TAG, "action_archive");
                break;
            case R.id.action_content_share:
                Toast.makeText(getApplicationContext(), "action_share", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "action_share");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestContent(int contentId) {

//        Log.i(TAG, "url : " + URL_HOST + QUERY_PATH + QUERY_CATEGORY + categoryId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH + contentId,
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
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
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
}
