package com.planet.wondering.chemi.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.BottomSheetMenu;
import com.planet.wondering.chemi.util.adapter.BottomSheetMenuAdapter;
import com.planet.wondering.chemi.util.adapter.TagCharacterAdapter;
import com.planet.wondering.chemi.util.helper.ImageHandler;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.planet.wondering.chemi.common.Common.BRAND_TAG_CHARACTER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.PRODUCT_TAG_CHARACTER_REQUEST_CODE;

/**
 * Created by yoon on 2017. 3. 20..
 */

public class MemberConfigRequestFragment extends Fragment
        implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = MemberConfigRequestFragment.class.getSimpleName();

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1200;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 2200;
    private static final int EMAIL_SEND_REQUEST_CODE = 3200;
    private static final int PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE = 9201;
    private String[] mStoragePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static MemberConfigRequestFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigRequestFragment fragment = new MemberConfigRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private InputMethodManager mInputMethodManager;
    private NestedScrollView mConfigRequestNestedScrollView;

    private LinearLayout mBackLayout;
    private RelativeLayout mConfirmLayout;
    private AutoCompleteTextView mBrandAutoCompleteTextView;
    private ImageView mBrandClearImageView;
    private TagCharacterAdapter mBrandCharacterAdapter;
    private AutoCompleteTextView mProductAutoCompleteTextView;
    private ImageView mProductClearImageView;
    private TagCharacterAdapter mProductCharacterAdapter;
    private ImageView mImage1ImageView;
    private ImageView mImage2ImageView;
    private ImageView mImage3ImageView;
    private EditText mDetailsEditText;

    private ImageHandler mRequestProductImageHandler;

    private BottomSheetDialog mMenuBottomSheetDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mRequestProductImageHandler = new ImageHandler(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_request, container, false);
        mConfigRequestNestedScrollView = (NestedScrollView)
                view.findViewById(R.id.member_config_request_nested_scroll_view);
        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_request_back_layout);
        mBackLayout.setOnClickListener(this);
        mConfirmLayout = (RelativeLayout) view.findViewById(R.id.member_config_request_confirm_layout);
        mConfirmLayout.setOnClickListener(this);
        mBrandAutoCompleteTextView = (AutoCompleteTextView)
                view.findViewById(R.id.member_config_request_brand_auto_complete_text_view);
        mBrandAutoCompleteTextView.setThreshold(1);
        mBrandAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mBrandClearImageView.setVisibility(View.VISIBLE);
                }
                if (s.length() == 0) {
                    mBrandClearImageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBrandAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (v.getText().length() == 0) {
                        Toast.makeText(getActivity(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        mBrandClearImageView = (ImageView) view.findViewById(R.id.member_config_request_brand_clear_image_view);
        mBrandClearImageView.setOnClickListener(this);
        mProductAutoCompleteTextView = (AutoCompleteTextView)
                view.findViewById(R.id.member_config_request_product_auto_complete_text_view);
        mProductAutoCompleteTextView.setThreshold(1);
        mProductAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mProductClearImageView.setVisibility(View.VISIBLE);
                }
                if (s.length() == 0) {
                    mProductClearImageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mProductAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (v.getText().length() == 0) {
                        Toast.makeText(getActivity(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        mProductClearImageView = (ImageView) view.findViewById(R.id.member_config_request_product_clear_image_view);
        mProductClearImageView.setOnClickListener(this);

        mBrandCharacterAdapter = new TagCharacterAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line, BRAND_TAG_CHARACTER_REQUEST_CODE);
        mBrandAutoCompleteTextView.setAdapter(mBrandCharacterAdapter);

        mProductCharacterAdapter = new TagCharacterAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line, PRODUCT_TAG_CHARACTER_REQUEST_CODE);
        mProductAutoCompleteTextView.setAdapter(mProductCharacterAdapter);

        mImage1ImageView = (ImageView) view.findViewById(R.id.member_config_request_image1_image_view);
        mImage1ImageView.setOnClickListener(this);
        mImage2ImageView = (ImageView) view.findViewById(R.id.member_config_request_image2_image_view);
        mImage2ImageView.setOnClickListener(this);
        mImage3ImageView = (ImageView) view.findViewById(R.id.member_config_request_image3_image_view);
        mImage3ImageView.setOnClickListener(this);
        mDetailsEditText = (EditText) view.findViewById(R.id.member_config_request_details_edit_text);
        mDetailsEditText.setOnFocusChangeListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mInputMethodManager.hideSoftInputFromWindow(mBrandAutoCompleteTextView.getWindowToken(), 0);
        mInputMethodManager.hideSoftInputFromWindow(mProductAutoCompleteTextView.getWindowToken(), 0);
        mInputMethodManager.hideSoftInputFromWindow(mDetailsEditText.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_request_back_layout:
                getActivity().onBackPressed();
                break;
            case R.id.member_config_request_confirm_layout:
                sendEmailRequestProduct();
                break;
            case R.id.member_config_request_brand_clear_image_view:
                mBrandAutoCompleteTextView.getText().clear();
                break;
            case R.id.member_config_request_product_clear_image_view:
                mProductAutoCompleteTextView.getText().clear();
                break;
            case R.id.member_config_request_image1_image_view:
                if (!isHasUploadImage1) {
                    checkStoragePermission(1);
                } else {
                    createEditImageMenuBottomSheetDialog(1);
                }
                break;
            case R.id.member_config_request_image2_image_view:
                if (!isHasUploadImage2) {
                    checkStoragePermission(2);
                } else {
                    createEditImageMenuBottomSheetDialog(2);
                }
                break;
            case R.id.member_config_request_image3_image_view:
                if (!isHasUploadImage3) {
                    checkStoragePermission(3);
                } else {
                    createEditImageMenuBottomSheetDialog(3);
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.member_config_request_details_edit_text:
                if (hasFocus) {
//                    mConfigRequestNestedScrollView.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mConfigRequestNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
//                        }
//                    }, 350);
                }
                break;
        }
    }

    private void createPickImageMenuBottomSheetDialog(final int imagePosition) {
        if (dismissMenuBottomSheetDialog()) {
            return;
        }
        ArrayList<BottomSheetMenu> bottomSheetMenus = new ArrayList<>();
        bottomSheetMenus.add(new BottomSheetMenu(R.drawable.ic_camera, R.string.bottom_sheet_menu_camera));
        bottomSheetMenus.add(new BottomSheetMenu(R.drawable.ic_gallery, R.string.bottom_sheet_menu_gallery));

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
                if (position == 0 /* camera */) {
                    if (mRequestProductImageHandler.hasCamera()) {
                        startActivityForResult(mRequestProductImageHandler.dispatchTakePictureIntent(),
                                CAMERA_CAPTURE_IMAGE_REQUEST_CODE + imagePosition);
                    } else {
                        Toast.makeText(getActivity(), "카메라를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 1 /* gallery */) {
                    startActivityForResult(mRequestProductImageHandler.pickGalleryPictureIntent(),
                            GALLERY_IMAGE_REQUEST_CODE + imagePosition);
                }
            }
        });
        mMenuBottomSheetDialog = new BottomSheetDialog(getActivity());
        mMenuBottomSheetDialog.setContentView(view);
        mMenuBottomSheetDialog.show();
    }

    private void createEditImageMenuBottomSheetDialog(final int imagePosition) {
        if (dismissMenuBottomSheetDialog()) {
            return;
        }
        ArrayList<BottomSheetMenu> bottomSheetMenus = new ArrayList<>();
        bottomSheetMenus.add(new BottomSheetMenu(0, R.string.bottom_sheet_menu_image_pick));
        bottomSheetMenus.add(new BottomSheetMenu(0, R.string.bottom_sheet_menu_image_delete));

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
                if (position == 0 /* re_pick */) {
                    createPickImageMenuBottomSheetDialog(imagePosition);
                } else if (position == 1 /* delete */) {
//                    deleteImage(imagePosition);
                    deleteRequestProductUploadImage(imagePosition);
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

    private void deleteRequestProductUploadImage(int imagePosition) {

        switch (imagePosition) {
            case 1:
                if (isHasUploadImage3) {
                    mImage1ImageView.setImageDrawable(mImage2ImageView.getDrawable());
                    mImage2ImageView.setImageDrawable(mImage3ImageView.getDrawable());
                    mImage3ImageView.setImageBitmap(null);
                    isHasUploadImage3 = false;
                } else if (isHasUploadImage2) {
                    mImage1ImageView.setImageDrawable(mImage2ImageView.getDrawable());
                    mImage2ImageView.setImageBitmap(null);
                    isHasUploadImage2 = false;
//                    mImage3ImageView.setVisibility(View.INVISIBLE);
                } else if (isHasUploadImage1) {
                    mImage1ImageView.setImageBitmap(null);
                    isHasUploadImage1 = false;
//                    mImage2ImageView.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                if (isHasUploadImage3) {
                    mImage2ImageView.setImageDrawable(mImage3ImageView.getDrawable());
                    mImage3ImageView.setImageBitmap(null);
                    isHasUploadImage3 = false;
                } else if (isHasUploadImage2) {
                    mImage2ImageView.setImageBitmap(null);
                    isHasUploadImage2 = false;
//                    mImage3ImageView.setVisibility(View.INVISIBLE);
                }
                break;
            case 3:
                if (isHasUploadImage3) {
                    mImage3ImageView.setImageBitmap(null);
                    isHasUploadImage3 = false;
                }
                break;
        }

    }

    private boolean isHasUploadImage1 = false;
    private boolean isHasUploadImage2 = false;
    private boolean isHasUploadImage3 = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 1:
                if (resultCode == RESULT_OK) {
                    mRequestProductImageHandler.handleCameraImage(mImage1ImageView);
                    mImageFilePathArray[0] = mRequestProductImageHandler.handleCameraImage1(mImage1ImageView);
                    isHasUploadImage1 = true;
//                    mImage2ImageView.setVisibility(View.VISIBLE);
                }
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 2:
                if (resultCode == RESULT_OK) {
//                    mRequestProductImageHandler.handleCameraImage(mImage2ImageView);
                    mImageFilePathArray[1] = mRequestProductImageHandler.handleCameraImage1(mImage2ImageView);
                    isHasUploadImage2 = true;
//                    mImage3ImageView.setVisibility(View.VISIBLE);
                }
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 3:
                if (resultCode == RESULT_OK) {
//                    mRequestProductImageHandler.handleCameraImage(mImage3ImageView);
                    mImageFilePathArray[2] = mRequestProductImageHandler.handleCameraImage1(mImage3ImageView);
                    isHasUploadImage3 = true;
                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 1:
                if (resultCode == RESULT_OK) {
//                    mRequestProductImageHandler.handleGalleryImage(data, mImage1ImageView);
                    mImageFilePathArray[0] = mRequestProductImageHandler.handleGalleryImage1(data, mImage1ImageView);
                    isHasUploadImage1 = true;
//                    mImage2ImageView.setVisibility(View.VISIBLE);
                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 2:
                if (resultCode == RESULT_OK) {
//                    mRequestProductImageHandler.handleGalleryImage(data, mImage2ImageView);
                    mImageFilePathArray[1] = mRequestProductImageHandler.handleGalleryImage1(data, mImage2ImageView);
                    isHasUploadImage2 = true;
//                    mImage3ImageView.setVisibility(View.VISIBLE);
                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 3:
                if (resultCode == RESULT_OK) {
                    mRequestProductImageHandler.handleGalleryImage(data, mImage3ImageView);
                    mImageFilePathArray[2] = mRequestProductImageHandler.handleGalleryImage1(data, mImage3ImageView);
                    isHasUploadImage3 = true;
                }
                break;
            case EMAIL_SEND_REQUEST_CODE:
                Log.i("result code", String.valueOf(resultCode));
                if (resultCode == RESULT_OK) {
                    getActivity().onBackPressed();
                }
                break;
        }
    }

    private void checkStoragePermission(int imagePosition) {
//        String state = Environment.getExternalStorageState();
//        if (!Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//            dismissMenuBottomSheetDialog();
//            Toast.makeText(getActivity(), "SD 카드가 없으므로 취소 되었습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), mStoragePermissions[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), mStoragePermissions[1]) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), mStoragePermissions[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), mStoragePermissions[1])) {
                    // Should we show an explanation?

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("저장소 권한을 요청합니다.")
                            .setMessage("리뷰 사진을 저장한 갤러리의 접근 권한을 요청합니다.");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(mStoragePermissions, PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
//                else if () {
//                go to Settings.
//
//                }
                else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(mStoragePermissions, PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
                }
            } else {
                // already have permission
                createPickImageMenuBottomSheetDialog(imagePosition);
            }
        } else {
            createPickImageMenuBottomSheetDialog(imagePosition);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    Log.i(TAG + "granted", "Storage Permission has been granted by user : " +
//                            "PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE : " + PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
                    createPickImageMenuBottomSheetDialog(1);
                } else {
//                    Log.i(TAG + "denied", "Storage Permission has been denied by user : " +
//                            "PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE : " + PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
                    Toast.makeText(getActivity(), "권한이 없으므로 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String[] mImageFilePathArray = new String[3];

    private void sendEmailRequestProduct() {

        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE, Uri.fromParts(
                "mailto", getString(R.string.administrator_support_email), null));
        emailIntent.setType("text/plain");
//        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.administrator_support_email)});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.request_product_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.request_product_description,
                mBrandAutoCompleteTextView.getText().toString(), mProductAutoCompleteTextView.getText().toString())
        + "\n\n" + getString(R.string.request_product_detail_hint) + "\n" + mDetailsEditText.getText().toString());

//        emailIntent.setType("application/image");
//        emailIntent.setType("image/*");
        ArrayList<Uri> uris = new ArrayList<>();
        for (String filePath : mImageFilePathArray) {
            if (filePath != null) {
                Log.i("filePath", filePath);
                Uri uri = Uri.parse("file://" + filePath);
                uris.add(uri);
            }
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

//        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

        emailIntent = Intent.createChooser(emailIntent, getString(R.string.request_product_title));
        startActivityForResult(emailIntent, EMAIL_SEND_REQUEST_CODE);
    }
}
