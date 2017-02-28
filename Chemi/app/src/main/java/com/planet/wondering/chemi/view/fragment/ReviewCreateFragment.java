package com.planet.wondering.chemi.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.BottomSheetMenu;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.util.adapter.BottomSheetMenuAdapter;
import com.planet.wondering.chemi.view.activity.ReviewActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yoon on 2017. 2. 23..
 */

public class ReviewCreateFragment extends Fragment
        implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private static final String TAG = ReviewCreateFragment.class.getSimpleName();

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

    private Toolbar mReviewCreateToolbar;
    private LinearLayout mReviewCreateRatingBarLayout;
    private TextView mReviewCreateMessageTextView;
    private RatingBar mReviewCreateRatingValueRatingBar;
    private EditText mReviewCreateReviewEditText;
    private Button mReviewCreateReviewCompleteButton;
    private ImageButton mReviewCreateImage1ImageButton;
    private ImageButton mReviewCreateImage2ImageButton;
    private ImageButton mReviewCreateImage3ImageButton;

    private BottomSheetDialog mMenuBottomSheetDialog;
    private Uri mImageUri;
    private File mImageDir;
    private String mImagePath;

    private Review mReview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        setRetainInstance(true);
    }

//    public void setReview(Review review) {
//        mReview = review;
//    }
//
//    public Review getReview() {
//        return mReview;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_create, container, false);

        mReviewCreateToolbar = (Toolbar) view.findViewById(R.id.review_create_toolbar);
        ((ReviewActivity) getActivity()).setSupportActionBar(mReviewCreateToolbar);
//        mReviewCreateToolbar.setTitle("리뷰 작성");
        ((ReviewActivity) getActivity()).getSupportActionBar().setTitle("리뷰 작성");
        mReviewCreateRatingBarLayout = (LinearLayout) view.findViewById(R.id.review_create_rating_bar_layout);
        mReviewCreateMessageTextView = (TextView) view.findViewById(R.id.review_create_message_text_view);
        mReviewCreateRatingValueRatingBar = (RatingBar) view.findViewById(R.id.review_create_rating_value_rating_bar);
        mReviewCreateRatingValueRatingBar.setOnRatingBarChangeListener(this);
        mReviewCreateReviewEditText = (EditText) view.findViewById(R.id.review_create_review_edit_text);
        mReviewCreateReviewEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 0) {
                    mReviewCreateReviewCompleteButton.setVisibility(View.VISIBLE);
                    mReviewCreateReviewCompleteButton.setEnabled(true);
                } else {
                    mReviewCreateReviewCompleteButton.setVisibility(View.GONE);
                    mReviewCreateReviewCompleteButton.setEnabled(false);
                }
            }
        });
        mReviewCreateReviewCompleteButton = (Button) view.findViewById(R.id.review_create_review_edit_complete_button);
        mReviewCreateImage1ImageButton = (ImageButton) view.findViewById(R.id.review_create_review_image1_image_button);
        mReviewCreateImage1ImageButton.setOnClickListener(this);
        mReviewCreateImage2ImageButton = (ImageButton) view.findViewById(R.id.review_create_review_image2_image_button);
        mReviewCreateImage2ImageButton.setOnClickListener(this);
        mReviewCreateImage3ImageButton = (ImageButton) view.findViewById(R.id.review_create_review_image3_image_button);
        mReviewCreateImage3ImageButton.setOnClickListener(this);
       return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        if (rating > 0.1 && rating <= 1.0) {
            mReviewCreateMessageTextView.setText(messageArray[0]);
        } else if (rating >= 1.1 && rating <= 2.0) {
            mReviewCreateMessageTextView.setText(messageArray[1]);
        } else if (rating > 2.0 && rating <= 3.0) {
            mReviewCreateMessageTextView.setText(messageArray[2]);
        } else if (rating > 3.0 && rating <= 4.0) {
            mReviewCreateMessageTextView.setText(messageArray[3]);
        } else if (rating > 4.0 && rating <= 5.0) {
            mReviewCreateMessageTextView.setText(messageArray[4]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_create_review_image1_image_button:
                checkStoragePermission(1);
//                createMenuBottomSheetDialog();
                break;
            case R.id.review_create_review_image2_image_button:
                checkStoragePermission(2);
                break;
            case R.id.review_create_review_image3_image_button:
                checkStoragePermission(3);
                break;
        }
    }

    private void createMenuBottomSheetDialog(final int imagePosition) {
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
                            startActivityForResult(cameraIntent(), CAMERA_CAPTURE_IMAGE_REQUEST_CODE + imagePosition);
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

    private boolean dismissMenuBottomSheetDialog() {
        if (mMenuBottomSheetDialog != null && mMenuBottomSheetDialog.isShowing()) {
            mMenuBottomSheetDialog.dismiss();
            return true;
        }
        return false;
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
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
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
                mReviewCreateImage1ImageButton.setImageURI(Uri.fromFile(new File(mImagePath)));
                mReviewCreateImage2ImageButton.setVisibility(View.VISIBLE);
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 2:
                mReviewCreateImage2ImageButton.setImageURI(Uri.fromFile(new File(mImagePath)));
                mReviewCreateImage3ImageButton.setVisibility(View.VISIBLE);
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 3:
                mReviewCreateImage3ImageButton.setImageURI(Uri.fromFile(new File(mImagePath)));
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 1:
                galleryImageUri = data.getData();
                if (galleryImageUri != null) {
                    mReviewCreateImage1ImageButton.setImageURI(galleryImageUri);
                    mReviewCreateImage2ImageButton.setVisibility(View.VISIBLE);
                } else {

                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 2:
                galleryImageUri = data.getData();
                if (galleryImageUri != null) {
                    mReviewCreateImage2ImageButton.setImageURI(galleryImageUri);
                    mReviewCreateImage3ImageButton.setVisibility(View.VISIBLE);
                } else {

                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 3:
                galleryImageUri = data.getData();
                if (galleryImageUri != null) {
                    mReviewCreateImage3ImageButton.setImageURI(galleryImageUri);
                } else {

                }
                break;
        }
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
                createMenuBottomSheetDialog(imagePosition);
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
                    createMenuBottomSheetDialog(1);
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
