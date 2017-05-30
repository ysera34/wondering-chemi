package com.planet.wondering.chemi.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.helper.PrefixTextWatcher;
import com.planet.wondering.chemi.util.listener.OnCommentEditDialogFinishedListener;

import static com.planet.wondering.chemi.common.Common.COMMENT_USER_NAME_DIVIDER;

/**
 * Created by yoon on 2017. 4. 26..
 */

public class CommentEditBottomSheetDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {

    private static final String TAG = CommentEditBottomSheetDialogFragment.class.getSimpleName();

    private static final String ARG_COMMENT_DESCRIPTION = "comment_description";

    public static CommentEditBottomSheetDialogFragment newInstance() {

        Bundle args = new Bundle();

        CommentEditBottomSheetDialogFragment fragment = new CommentEditBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CommentEditBottomSheetDialogFragment newInstance(String description) {

        Bundle args = new Bundle();
        args.putString(ARG_COMMENT_DESCRIPTION, description);

        CommentEditBottomSheetDialogFragment fragment = new CommentEditBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String mUserName;
    private String mCommentDescription;

    private InputMethodManager mInputMethodManager;
    private RelativeLayout mCommentConfirmLayout;
    private TextView mCommentEditCommentUserNameTextView;
    private EditText mCommentEditText;
    private Button mEditCompleteButton;

    private PrefixTextWatcher mPrefixTextWatcher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommentDescription = getArguments().getString(ARG_COMMENT_DESCRIPTION, null);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.fragment_comment_edit, null);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mCommentConfirmLayout = (RelativeLayout) view.findViewById(R.id.comment_edit_confirm_layout);
        mCommentConfirmLayout.setOnClickListener(this);
        mCommentEditText = (EditText) view.findViewById(R.id.comment_edit_comment_edit_text);
        mCommentEditCommentUserNameTextView =
                (TextView) view.findViewById(R.id.comment_edit_comment_user_name_text_view);

        String[] strings = String.valueOf(mCommentDescription).split(COMMENT_USER_NAME_DIVIDER, 2);
        if (strings.length > 1) {
//            mCommentEditText.setText(highlightUserNameText());
            removePrefixTextChangedListener(mCommentEditText);
            addPrefixTextChangedListener(mCommentEditText, strings[0], strings[1]);
            mCommentEditCommentUserNameTextView.setVisibility(View.VISIBLE);
            mCommentEditCommentUserNameTextView.setText(String.valueOf(strings[0]));
//            mCommentEditText.setText(strings[0]);
        } else {
            mCommentEditText.setText(String.valueOf(mCommentDescription));
        }

        mCommentEditText.setSelection(mCommentEditText.getText().length());
        mEditCompleteButton = (Button) view.findViewById(R.id.comment_edit_comment_edit_complete_button);
        mEditCompleteButton.setOnClickListener(this);
        dialog.setContentView(view);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        View parent = (View) view.getParent();
        parent.setFitsSystemWindows(true);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        view.measure(0, 0);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;
        bottomSheetBehavior.setPeekHeight(screenHeight);

        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;

        if (params.getBehavior() instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) params.getBehavior()).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        params.height = screenHeight - statusBarHeight;
        parent.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("댓글 수정을 취소하시겠어요?");
                        builder1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        });
                        builder1.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog1 = builder1.create();
                        dialog1.show();
                        return true;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        });
    }

    private boolean isCompletedCommentEdit = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_edit_confirm_layout:
            case R.id.comment_edit_comment_edit_complete_button:
                if (mCommentEditText.getText().toString().trim().length() > 0) {

                    if (!mCommentDescription.equals(mCommentEditText.getText().toString().trim())) {
                        isCompletedCommentEdit = true;

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("댓글을 정말 수정하시겠어요?");
                        builder1.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                                if (isCompletedCommentEdit) {
//                                    if (!mCommentEditCommentUserNameTextView.isShown()
//                                            && mCommentEditCommentUserNameTextView.getText().equals("")) {
//                                        mCommentEditDialogFinishedListener.onCommentEditDialogFinished(
//                                                mCommentEditText.getText().toString());
//                                    } else {
//                                        mCommentEditDialogFinishedListener.onCommentEditDialogFinished(
//                                                mCommentEditText.getText().toString().substring(
//                                                        mUserName.length(), mCommentEditText.getText().length()) + mUserName);
//                                    }
//                                }
                                mCommentEditDialogFinishedListener.onCommentEditDialogFinished(
                                        mCommentEditText.getText().toString());
//                                mInputMethodManager.hideSoftInputFromWindow(mCommentEditText.getWindowToken(), 0);
                                removePrefixTextChangedListener(mCommentEditText);
                                mCommentEditCommentUserNameTextView.setText("");
                                mCommentEditCommentUserNameTextView.setVisibility(View.GONE);
                                dismiss();
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("변경사항이 없어요.\n댓글 수정을 취소하시겠어요?");
                        builder1.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        });
                        builder1.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog1 = builder1.create();
                        dialog1.show();
                    }
                } else {
                    Toast.makeText(getActivity(), "취소 하시려면 뒤로가기 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
//        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isCompletedCommentEdit) {

        } else {
            Toast.makeText(getActivity(), "댓글 수정이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    OnCommentEditDialogFinishedListener mCommentEditDialogFinishedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCommentEditDialogFinishedListener = (OnCommentEditDialogFinishedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implements OnReviewEditListener");
        }
    }

    private void addPrefixTextChangedListener(EditText editText, String prefixText, String commentDescription) {
        mPrefixTextWatcher = new PrefixTextWatcher(getActivity());
        mPrefixTextWatcher.setPrefixText(editText, prefixText, commentDescription);
        editText.addTextChangedListener(mPrefixTextWatcher);
    }

    private void removePrefixTextChangedListener(EditText editText) {
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        if (mPrefixTextWatcher != null) {
            editText.removeTextChangedListener(mPrefixTextWatcher);
        }

        editText.getText().clear();
        editText.clearFocus();
    }

    private SpannableString highlightUserNameText() {

        int startIndex = 0;
        int endIndex = String.valueOf(mCommentDescription).split("@")[1].length() + 1;
        mUserName = "@" + String.valueOf(mCommentDescription).split("@")[1];

        StringBuilder sb = new StringBuilder();
        sb.append("@")
                .append(String.valueOf(mCommentDescription).split("@")[1]).append(" ")
                .append(String.valueOf(mCommentDescription).split("@")[0]);

        SpannableString spannableString = new SpannableString(sb.toString());

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}
