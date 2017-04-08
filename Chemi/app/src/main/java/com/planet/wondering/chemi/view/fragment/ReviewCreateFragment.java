package com.planet.wondering.chemi.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.BottomSheetMenu;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Config;
import com.planet.wondering.chemi.network.MultipartRequest;
import com.planet.wondering.chemi.network.MultipartRequestHelper;
import com.planet.wondering.chemi.util.adapter.BottomSheetMenuAdapter;
import com.planet.wondering.chemi.util.helper.ImageHandler;
import com.planet.wondering.chemi.util.helper.ReviewSharedPreferences;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnReviewEditListener;
import com.planet.wondering.chemi.view.activity.ReviewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.Review.Key.DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.Review.Key.RATING;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.encodeUTF8;

/**
 * Created by yoon on 2017. 2. 23..
 */

public class ReviewCreateFragment extends Fragment
        implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private static final String TAG = ReviewCreateFragment.class.getSimpleName();

    private static final String ARG_PRODUCT = "product";
    private static final String ARG_REVIEW_CONTENT = "review_content";


    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1000;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 2000;
    private static final int PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE = 9001;
    private String[] mStoragePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static ReviewCreateFragment newInstance() {

        Bundle args = new Bundle();

        ReviewCreateFragment fragment = new ReviewCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewCreateFragment newInstance(String reviewContent) {

        Bundle args = new Bundle();
        args.putString(ARG_REVIEW_CONTENT, reviewContent);

        ReviewCreateFragment fragment = new ReviewCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewCreateFragment newInstance(Product product) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);

        ReviewCreateFragment fragment = new ReviewCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewCreateFragment newInstance(Product product, String reviewContent) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        args.putString(ARG_REVIEW_CONTENT, reviewContent);

        ReviewCreateFragment fragment = new ReviewCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Toolbar mReviewCreateToolbar;
    private TextView mReviewCreateProductNameTextView;
    private TextView mReviewCreateMessageTextView;
    private RatingBar mReviewCreateRatingValueRatingBar;
    private TextView mReviewCreateReviewLengthTextView;
    private TextView mReviewCreateReviewTextView;
    private String mReviewHint;
    private String mReviewContent;
    private ImageView mReviewCreateImage1ImageView;
    private ImageView mReviewCreateImage2ImageView;
    private ImageView mReviewCreateImage3ImageView;

    private ImageHandler mReviewImageHandler;

    private BottomSheetDialog mMenuBottomSheetDialog;
    private Uri mImageUri;
    private File mImageDir;
    private String mImagePath;
    private Bitmap mImage1Bitmap;
    private Bitmap mImage2Bitmap;
    private Bitmap mImage3Bitmap;

    private Product mProduct;
    private Review mReview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProduct = (Product) getArguments().getSerializable(ARG_PRODUCT);
        mReview = new Review();
        mReviewImageHandler = new ImageHandler(getActivity());

        mReviewHint = getString(R.string.review_create_review_hint);
        mReviewContent = getArguments().getString(ARG_REVIEW_CONTENT, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_create, container, false);

        mReviewCreateToolbar = (Toolbar) view.findViewById(R.id.review_create_toolbar);
        ((ReviewActivity) getActivity()).setSupportActionBar(mReviewCreateToolbar);
        ((ReviewActivity) getActivity()).getSupportActionBar().setTitle("리뷰 작성");
        mReviewCreateProductNameTextView = (TextView) view.findViewById(R.id.review_create_product_name_text_view);
        mReviewCreateMessageTextView = (TextView) view.findViewById(R.id.review_create_message_text_view);
        mReviewCreateRatingValueRatingBar = (RatingBar) view.findViewById(R.id.review_create_rating_value_rating_bar);
        mReviewCreateRatingValueRatingBar.setOnRatingBarChangeListener(this);

        mReviewCreateReviewLengthTextView = (TextView) view.findViewById(R.id.review_create_review_text_length_text_view);
        mReviewCreateReviewTextView = (TextView) view.findViewById(R.id.review_create_review_text_view);
        mReviewCreateReviewTextView.setOnClickListener(this);

        mReviewCreateImage1ImageView = (ImageView) view.findViewById(R.id.review_create_review_image1_image_view);
        mReviewCreateImage1ImageView.setOnClickListener(this);
        mReviewCreateImage2ImageView = (ImageView) view.findViewById(R.id.review_create_review_image2_image_view);
        mReviewCreateImage2ImageView.setOnClickListener(this);
        mReviewCreateImage3ImageView = (ImageView) view.findViewById(R.id.review_create_review_image3_image_view);
        mReviewCreateImage3ImageView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mReviewCreateProductNameTextView.setText(mProduct.getName());
        if (mReviewContent.equals("") || mReviewContent == null) {
            mReviewCreateReviewTextView.setText(mReviewHint);
        } else {
            mReviewCreateReviewTextView.setText(mReviewContent);
        }
        mReviewCreateReviewLengthTextView.setText(
                getString(R.string.review_create_review_length_format,
                        String.valueOf(mReviewCreateReviewTextView.getText().length())));
    }

    private String mImage1Path;
    private String mImage2Path;
    private String mImage3Path;

    @Override
    public void onResume() {
        super.onResume();
        ReviewSharedPreferences preferences = new ReviewSharedPreferences();
        float ratingValue = preferences.getStoredRatingValue(getActivity());
        if (ratingValue != 0.0f) {
            mReviewCreateRatingValueRatingBar.setRating(ratingValue);
        }

        mReview.setImagePathMap(getActivity());
        int imageViewArrayLength = mReview.getImagePathMapSize();
        if (imageViewArrayLength > 0) {
            switch (imageViewArrayLength) {
                case 3:
                    mReviewCreateImage3ImageView.setVisibility(View.VISIBLE);
                    mReviewCreateImage2ImageView.setVisibility(View.VISIBLE);
                    File file3 = new File(mReview.getImagePathMap().get(3));
                    mImage3Path = file3.getAbsolutePath();
                    mImage3Bitmap = BitmapFactory.decodeFile(mImage3Path);
                    mReviewCreateImage3ImageView.setImageBitmap(mImage3Bitmap);

                case 2:
                    mReviewCreateImage3ImageView.setVisibility(View.VISIBLE);
                    mReviewCreateImage2ImageView.setVisibility(View.VISIBLE);
//                    mReviewCreateImage2ImageView.setImageURI(
//                            Uri.fromFile(new File(mReview.getImagePathMap().get(2))));
                    File file2 = new File(mReview.getImagePathMap().get(2));
                    mImage2Path = file2.getAbsolutePath();
                    mImage2Bitmap = BitmapFactory.decodeFile(mImage2Path);
                    mReviewCreateImage2ImageView.setImageBitmap(mImage2Bitmap);
                case 1:
                    mReviewCreateImage2ImageView.setVisibility(View.VISIBLE);
//                    mReviewCreateImage1ImageView.setImageURI(
//                            Uri.fromFile(new File(mReview.getImagePathMap().get(1))));
                    File file1 = new File(mReview.getImagePathMap().get(1));
                    mImage1Path = file1.getAbsolutePath();
                    mImage1Bitmap = BitmapFactory.decodeFile(mImage1Path);
                    mReviewCreateImage1ImageView.setImageBitmap(mImage1Bitmap);
                    break;
            }
            Log.i(TAG, "imageViewArrayLength: " + String.valueOf(imageViewArrayLength));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_review_create, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_review_confirm:
//                Toast.makeText(getActivity(), "action_review_confirm", Toast.LENGTH_SHORT).show();
                requestCreateReview();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        String[] messageArray = getResources().getStringArray(R.array.review_rating_message_array);

        for (int i = 0; i < messageArray.length; i++) {
            if (rating == 0.5f * i) {
                mReviewCreateMessageTextView.setText(messageArray[i]);
                break;
            }
        }
        ReviewSharedPreferences preferences = new ReviewSharedPreferences();
        if (mReviewCreateRatingValueRatingBar.getRating() != 0.0f) {
            preferences.setStoreRatingValue(getActivity(), mReviewCreateRatingValueRatingBar.getRating());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_create_review_text_view:
                String reviewContent = mReviewCreateReviewTextView.getText().toString();
                if (reviewContent.equals(mReviewHint)) {
                    mReviewEditListener.onReviewEdit("", true);
                } else {
                    mReviewEditListener.onReviewEdit(reviewContent, true);
                }
                break;
            case R.id.review_create_review_image1_image_view:
                if (mImage1Bitmap == null) {
                    checkStoragePermission(1);
                } else {
                    createEditImageMenuBottomSheetDialog(1);
                }
                break;
            case R.id.review_create_review_image2_image_view:
                if (mImage2Bitmap == null) {
                    checkStoragePermission(2);
                } else {
                    createEditImageMenuBottomSheetDialog(2);
                }
                break;
            case R.id.review_create_review_image3_image_view:
                if (mImage3Bitmap == null) {
                    checkStoragePermission(3);
                } else {
                    createEditImageMenuBottomSheetDialog(3);
                }
                break;
        }
    }

    OnReviewEditListener mReviewEditListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mReviewEditListener = (OnReviewEditListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implements OnReviewEditListener");
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
                    if (mReviewImageHandler.hasCamera()) {
                        startActivityForResult(mReviewImageHandler.dispatchTakePictureIntent(),
                                CAMERA_CAPTURE_IMAGE_REQUEST_CODE + imagePosition);
                    } else {
                        Toast.makeText(getActivity(), "카메라를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 1 /* gallery */) {
                    startActivityForResult(mReviewImageHandler.pickGalleryPictureIntent(),
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
        bottomSheetMenus.add(new BottomSheetMenu(0, R.string.bottom_sheet_menu_pick));
        bottomSheetMenus.add(new BottomSheetMenu(0, R.string.bottom_sheet_menu_delete));

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
                    deleteImage(imagePosition);
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

    private void deleteImage(int imagePosition) {
        switch (imagePosition) {
            case 1:
                if (mImage2Bitmap == null && mImage3Bitmap == null) {
                    mReviewCreateImage1ImageView.setImageBitmap(null);
                    mImage1Path = null;
                    mReview.removeImagePath(getActivity(), 1);
                    mImage1Bitmap = null;
                    mReviewCreateImage2ImageView.setVisibility(View.INVISIBLE);
                } else if (mImage2Bitmap != null && mImage3Bitmap == null) {
                    mReviewCreateImage1ImageView.setImageBitmap(mImage2Bitmap);
                    mImage1Path = mImage2Path;
                    mReview.removeImagePath(getActivity(), 1);
                    mReview.putImagePath(getActivity(), 1, mImage1Path);
                    mImage1Bitmap = mImage2Bitmap;
                    mReviewCreateImage2ImageView.setImageBitmap(null);
                    mImage2Path = null;
                    mReview.removeImagePath(getActivity(), 2);
                    mImage2Bitmap = null;
                    mReviewCreateImage3ImageView.setVisibility(View.INVISIBLE);
                } else if (mImage2Bitmap != null && mImage3Bitmap != null) {
                    mReviewCreateImage1ImageView.setImageBitmap(mImage2Bitmap);
                    mImage1Path = mImage2Path;
                    mReview.removeImagePath(getActivity(), 1);
                    mReview.putImagePath(getActivity(), 1, mImage1Path);
                    mImage1Bitmap = mImage2Bitmap;
                    mReviewCreateImage2ImageView.setImageBitmap(mImage3Bitmap);
                    mImage2Path = mImage3Path;
                    mReview.removeImagePath(getActivity(), 2);
                    mReview.putImagePath(getActivity(), 2, mImage2Path);
                    mImage2Bitmap = mImage3Bitmap;
                    mReviewCreateImage3ImageView.setImageBitmap(null);
                    mImage3Path = null;
                    mReview.removeImagePath(getActivity(), 3);
                    mImage3Bitmap = null;
                }
                break;
            case 2:
                if (mImage2Bitmap != null && mImage3Bitmap == null) {
                    mReviewCreateImage2ImageView.setImageBitmap(null);
                    mImage2Path = null;
                    mReview.removeImagePath(getActivity(), 2);
                    mImage2Bitmap = null;
                    mReviewCreateImage3ImageView.setVisibility(View.INVISIBLE);
                } else if (mImage2Bitmap != null && mImage3Bitmap != null) {
                    mReviewCreateImage2ImageView.setImageBitmap(mImage3Bitmap);
                    mImage2Path = mImage3Path;
                    Log.i(TAG, "mImage2Path " + mImage2Path);
                    Log.i(TAG, "mImage3Path " + mImage3Path);
                    mReview.removeImagePath(getActivity(), 2);
                    mReview.putImagePath(getActivity(), 2, mImage2Path);
                    mImage2Bitmap = mImage3Bitmap;

                    mReviewCreateImage3ImageView.setImageBitmap(null);
                    mImage3Path = null;
                    mReview.removeImagePath(getActivity(), 3);
                    mImage3Bitmap = null;
                }
                break;
            case 3:
                mReviewCreateImage3ImageView.setImageBitmap(null);
                mImage3Path = null;
                mReview.removeImagePath(getActivity(), 3);
                mImage3Bitmap = null;
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
                    mReviewImageHandler.handleCameraImage(mReviewCreateImage1ImageView);
                }
                isHasUploadImage1 = true;
                mReviewCreateImage2ImageView.setVisibility(View.VISIBLE);
                mReview.putImagePath(getActivity(), 1, mImage1Path);
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 2:
                if (resultCode == RESULT_OK) {
                    mReviewImageHandler.handleCameraImage(mReviewCreateImage2ImageView);
                }
                isHasUploadImage2 = true;
                mReviewCreateImage3ImageView.setVisibility(View.VISIBLE);
                mReview.putImagePath(getActivity(), 2, mImage2Path);
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 3:
                if (resultCode == RESULT_OK) {
                    mReviewImageHandler.handleCameraImage(mReviewCreateImage3ImageView);
                }
                isHasUploadImage3 = true;
                mReview.putImagePath(getActivity(), 3, mImage3Path);
                break;
//            case GALLERY_IMAGE_REQUEST_CODE + 1:
//                galleryImageUri = data.getData();
//                if (galleryImageUri != null) {
////                    mReviewCreateImage1ImageView.setImageURI(galleryImageUri);
//                    mImagePath = getRealPathFromURI(galleryImageUri);
//                    mImage1Bitmap = handleBigCameraPhoto(mReviewCreateImage1ImageView);
//                    mImage1Path = mImagePath;
//                    mReviewCreateImage2ImageView.setVisibility(View.VISIBLE);
//                    mReview.putImagePath(getActivity(), 1, mImage1Path);
//                } else {
//
//                }
//                break;
//            case GALLERY_IMAGE_REQUEST_CODE + 2:
//                galleryImageUri = data.getData();
//                if (galleryImageUri != null) {
////                    mReviewCreateImage2ImageView.setImageURI(galleryImageUri);
//                    mImagePath = getRealPathFromURI(galleryImageUri);
//                    mImage2Bitmap = handleBigCameraPhoto(mReviewCreateImage2ImageView);
//                    mImage2Path = mImagePath;
//                    mReviewCreateImage3ImageView.setVisibility(View.VISIBLE);
//                    mReview.putImagePath(getActivity(), 2, mImage2Path);
//                } else {
//
//                }
//                break;
//            case GALLERY_IMAGE_REQUEST_CODE + 3:
//                galleryImageUri = data.getData();
//                if (galleryImageUri != null) {
////                    mReviewCreateImage3ImageView.setImageURI(galleryImageUri);
//                    mImagePath = getRealPathFromURI(galleryImageUri);
//                    mImage3Bitmap = handleBigCameraPhoto(mReviewCreateImage3ImageView);
//                    mImage3Path = mImagePath;
//                    mReview.putImagePath(getActivity(), 3, mImage3Path);
//                } else {
//
//                }
//                break;
        }
    }

    private void checkStoragePermission(int imagePosition) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            dismissMenuBottomSheetDialog();
            Toast.makeText(getActivity(), "SD 카드가 없으므로 취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

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
            }
            else {
                // already have permission
                createPickImageMenuBottomSheetDialog(imagePosition);
            }
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

    private void requestCreateReview() {

        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST, URL_HOST + PATH + mProduct.getId() + Config.Review.PATH,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
//                        String responseString = new String(response.data);
//                        try {
//                            JSONObject responseJSONObject = new JSONObject(responseString);
//                            ArrayList<Review> reviews = Parser.parseReviewList(responseJSONObject);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        String responseString = new String(response.data);
//                        Log.i(TAG, responseString.toString());
                        getActivity().onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                R.string.progress_dialog_message_error, Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                return super.getHeaders();
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(RATING, String.valueOf(mReviewCreateRatingValueRatingBar.getRating()));
                params.put(DESCRIPTION, encodeUTF8(mReviewCreateReviewTextView.getText().toString()));
                return params;
            }

            @Override
            protected Map<String, ArrayList<DataPart>> getByteDataArray() throws AuthFailureError {
                Map<String, ArrayList<DataPart>> params = new HashMap<>();

                ArrayList<DataPart> imageDataPart = new ArrayList<>();

                if (isHasUploadImage1) {
                    imageDataPart.add(new DataPart("reviewImage1.jpg", MultipartRequestHelper.getFileDataFromDrawable(
                            getActivity(), mReviewCreateImage1ImageView.getDrawable()), "image/*"));
                }
                if (isHasUploadImage2) {
                    imageDataPart.add(new DataPart("reviewImage2.jpg", MultipartRequestHelper.getFileDataFromDrawable(
                            getActivity(), mReviewCreateImage2ImageView.getDrawable()), "image/*"));
                }
                if (isHasUploadImage3) {
                    imageDataPart.add(new DataPart("reviewImage3.jpg", MultipartRequestHelper.getFileDataFromDrawable(
                            getActivity(), mReviewCreateImage3ImageView.getDrawable()), "image/*"));
                }

                params.put("images", imageDataPart);

                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest, TAG);
    }
}
