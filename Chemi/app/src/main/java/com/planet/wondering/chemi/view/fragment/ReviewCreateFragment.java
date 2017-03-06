package com.planet.wondering.chemi.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
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

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.BottomSheetMenu;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.util.adapter.BottomSheetMenuAdapter;
import com.planet.wondering.chemi.util.helper.ReviewSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnReviewEditListener;
import com.planet.wondering.chemi.view.activity.ReviewActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    String[] storagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
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
                Toast.makeText(getActivity(), "action_review_confirm", Toast.LENGTH_SHORT).show();
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
        bottomSheetMenus.add(new BottomSheetMenu(R.drawable.ic_search_primary_24dp, R.string.bottom_sheet_menu_camera));
        bottomSheetMenus.add(new BottomSheetMenu(R.drawable.ic_search_primary_24dp, R.string.bottom_sheet_menu_gallery));

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.layout_bottom_sheet_menu_recycler_view, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bottom_sheet_menu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BottomSheetMenuAdapter bottomSheetMenuAdapter = new BottomSheetMenuAdapter(bottomSheetMenus);
        recyclerView.setAdapter(bottomSheetMenuAdapter);
        bottomSheetMenuAdapter.setItemClickListener(new BottomSheetMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BottomSheetMenuAdapter.MenuItemHolder itemHolder, int position) {
                String state = Environment.getExternalStorageState();
                if (!Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                    dismissMenuBottomSheetDialog();
                    Toast.makeText(getActivity(), "SD 카드가 없으므로 취소되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    dismissMenuBottomSheetDialog();
                    if (position == 0 /* camera */) {
                        if (hasCamera()) {
                            startActivityForResult(dispatchTakePictureIntent(), CAMERA_CAPTURE_IMAGE_REQUEST_CODE + imagePosition);
                        } else {
                            Toast.makeText(getActivity(), "카메라를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (position == 1 /* gallery */) {
                    // gallery
                        startActivityForResult(galleryIntent(), GALLERY_IMAGE_REQUEST_CODE + imagePosition);
                    }
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
        bottomSheetMenus.add(new BottomSheetMenu(R.drawable.ic_search_primary_24dp, R.string.bottom_sheet_menu_pick));
        bottomSheetMenus.add(new BottomSheetMenu(R.drawable.ic_search_primary_24dp, R.string.bottom_sheet_menu_delete));

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
                    checkStoragePermission(imagePosition);
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

    private File getAlbumDir() {

        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStorageDirectory()
                    + "/dcim/" + getString(R.string.app_name));

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d(TAG, "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v(TAG, "External storage is not mounted READ/WRITE");
        }
        return storageDir;
    }

    private static final String PNG_FILE_PREFIX = "IMG_";
    private static final String PNG_FILE_SUFFIX = ".png";

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyymmdd_HHmmss").format(new Date());
        String imageFileName = PNG_FILE_PREFIX + timeStamp + "_";
        File albumFolder = getAlbumDir();
        File imageFile = File.createTempFile(imageFileName, PNG_FILE_SUFFIX, albumFolder);
        return imageFile;
    }

    private File setupPhotoFile() throws IOException {
        File file = createImageFile();
        mImagePath = file.getAbsolutePath();
        return file;
    }

    private Bitmap setPicture(ImageView imageView) {
        int targetWidth = imageView.getWidth();
        int targetHeight = imageView.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImagePath, options);

        int photoWidth = options.outWidth;
        int photoHeight = options.outHeight;

        int scaleFactor = 1;
        if ((targetWidth > 0) || (targetHeight > 0)) {
            scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight);
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mImagePath, options);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            rotationAngle = 90;
        }
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            rotationAngle = 180;
        }
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            rotationAngle = 270;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, 0, 0, options.outWidth, options.outHeight, matrix, true);
    }

    private Intent galleryAddPictureIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mImagePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
        return mediaScanIntent;
    }

    private Intent dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        try {
            file = setupPhotoFile();
            mImagePath = file.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
            mImagePath = null;
        }
        return takePictureIntent;
    }

    private Bitmap handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        return (Bitmap) extras.get("data");
    }

    private Bitmap handleBigCameraPhoto(ImageView imageView) {
        if (mImagePath != null) {
            Bitmap bitmap = setPicture(imageView);
            imageView.setImageBitmap(bitmap);
            getActivity().sendBroadcast(galleryAddPictureIntent());
//            mImagePath = null;
            return bitmap;
        }
        return null;
    }

    private Bitmap getThumbnailBitmap(String imagePath, int thumbnailSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        if ((options.outWidth == -1 || options.outHeight == -1)) {
            return null;
        }
        int originalSize = (options.outHeight > options.outWidth) ? options.outHeight : options.outWidth;
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inSampleSize = originalSize / thumbnailSize;
        return BitmapFactory.decodeFile(imagePath, options1);
    }

    private Bitmap getRotatedBitmap(String imagePath) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bounds);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            rotationAngle = 90;
        }
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            rotationAngle = 180;
        }
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            rotationAngle = 270;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
    }

    public Intent cameraIntent() {
        mImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), getString(R.string.app_name));

        if (!mImageDir.exists()) {
            Toast.makeText(getActivity(), "저장할 디렉토리를 생성 하였습니다.", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (mImagePath != null) {
            mImagePath = "";
        }
        String imageName = "upload_" + String.valueOf(System.currentTimeMillis() / 100) + ".png";
        File imageFile = new File(mImageDir, imageName);
        mImagePath = imageFile.getAbsolutePath();

//        try {
//            ExifInterface exif = new ExifInterface(mImagePath);
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//            int rotate = 0;
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotate -= 90;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotate -= 90;
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotate-= 90;
//            }
//            Canvas canvas = new Canvas(bitmap);
//            canvas.rotate(rotate);
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }

        mImageUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        return intent;
    }

    public Intent galleryIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(Media.CONTENT_TYPE);
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri galleryImageUri;
        if (resultCode != Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 1:
//                mReviewCreateImage1ImageButton.setImageURI(Uri.fromFile(new File(mImagePath)));
                mImage1Path = mImagePath;
                mImage1Bitmap = handleBigCameraPhoto(mReviewCreateImage1ImageView);
                mReviewCreateImage2ImageView.setVisibility(View.VISIBLE);
                mReview.putImagePath(getActivity(), 1, mImage1Path);
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 2:
//                mReviewCreateImage2ImageButton.setImageURI(Uri.fromFile(new File(mImagePath)));
                mImage2Path = mImagePath;
                mImage2Bitmap = handleBigCameraPhoto(mReviewCreateImage2ImageView);
                mReviewCreateImage3ImageView.setVisibility(View.VISIBLE);
                mReview.putImagePath(getActivity(), 2, mImage2Path);
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 3:
//                mReviewCreateImage3ImageView.setImageURI(Uri.fromFile(new File(mImagePath)));
                mImage3Path = mImagePath;
                mImage3Bitmap = handleBigCameraPhoto(mReviewCreateImage3ImageView);
                mReview.putImagePath(getActivity(), 3, mImage3Path);
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 1:
                galleryImageUri = data.getData();
                if (galleryImageUri != null) {
//                    mReviewCreateImage1ImageView.setImageURI(galleryImageUri);
                    mImagePath = getRealPathFromURI(galleryImageUri);
                    mImage1Bitmap = handleBigCameraPhoto(mReviewCreateImage1ImageView);
                    mImage1Path = mImagePath;
                    mReviewCreateImage2ImageView.setVisibility(View.VISIBLE);
                    mReview.putImagePath(getActivity(), 1, mImage1Path);
                } else {

                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 2:
                galleryImageUri = data.getData();
                if (galleryImageUri != null) {
//                    mReviewCreateImage2ImageView.setImageURI(galleryImageUri);
                    mImagePath = getRealPathFromURI(galleryImageUri);
                    mImage2Bitmap = handleBigCameraPhoto(mReviewCreateImage2ImageView);
                    mImage2Path = mImagePath;
                    mReviewCreateImage3ImageView.setVisibility(View.VISIBLE);
                    mReview.putImagePath(getActivity(), 2, mImage2Path);
                } else {

                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 3:
                galleryImageUri = data.getData();
                if (galleryImageUri != null) {
//                    mReviewCreateImage3ImageView.setImageURI(galleryImageUri);
                    mImagePath = getRealPathFromURI(galleryImageUri);
                    mImage3Bitmap = handleBigCameraPhoto(mReviewCreateImage3ImageView);
                    mImage3Path = mImagePath;
                    mReview.putImagePath(getActivity(), 3, mImage3Path);
                } else {

                }
                break;
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private boolean hasCamera() {
        return (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA));
    }

    private void checkStoragePermission(int imagePosition) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), storagePermissions[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), storagePermissions[1]) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), storagePermissions[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), storagePermissions[0])) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("저장소 권한을 요청합니다.")
                            .setMessage("리뷰 사진을 저장한 갤러리의 접근 권한을 요청합니다.");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestStoragePermissions();
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
                    requestStoragePermissions();
                }
            }
            else {
                // already have permission
                createPickImageMenuBottomSheetDialog(imagePosition);
            }
        }
    }

    private void requestStoragePermissions() {
        requestPermissions(storagePermissions, PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG + "granted", "Storage Permission has been granted by user : " +
                            "PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE : " + PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
                    createPickImageMenuBottomSheetDialog(1);
                } else {
                    Log.i(TAG + "denied", "Storage Permission has been denied by user : " +
                            "PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE : " + PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
                    Toast.makeText(getActivity(), "권한이 없으므로 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
