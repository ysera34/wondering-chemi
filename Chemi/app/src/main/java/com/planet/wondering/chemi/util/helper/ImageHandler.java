package com.planet.wondering.chemi.util.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ImageView;

import com.planet.wondering.chemi.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by yoon on 2017. 4. 1..
 */

public class ImageHandler {

    private static final String TAG = ImageHandler.class.getSimpleName();

    private static final String CAMERA_DIR = "/dcim/";
    private static final String JPEG_FILE_PREFIX = "IMAGE_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private Context mContext;

    private String mAlbumName;
    private String mCurrentImageAbsolutePath;

    public ImageHandler(Context context) {
        mContext = context;
        mAlbumName = mContext.getString(R.string.app_name);
    }

    public boolean hasCamera() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private File getAlbumStorageDir() {
        return new File(
                Environment.getExternalStorageDirectory() + CAMERA_DIR + mAlbumName);
    }

    private File getAlbumDir() {

        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = getAlbumStorageDir();

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.i(TAG, mAlbumName + ": failed to create directory.");
                    }
                }
            }
        } else {
            Log.w(TAG, mAlbumName + ": External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";

        return File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
                getAlbumDir());
    }

    private File setUpImageFile() throws IOException {

        File imageFile = createImageFile();
        mCurrentImageAbsolutePath = imageFile.getAbsolutePath();

        return imageFile;
    }

    public String getCurrentImageAbsolutePath() {
        if (mCurrentImageAbsolutePath != null) {
            return mCurrentImageAbsolutePath;
        }
        return null;
    }

    private Bitmap setPicture(ImageView imageView) {

        /* Get the size of the ImageView */
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        decodeFile(mCurrentImageAbsolutePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduces less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }
        Log.d(TAG, "scaleFactor : " + scaleFactor);

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentImageAbsolutePath, bmOptions);
        Log.d(TAG, "setPicture >> " + "bitmap width : " + bitmap.getWidth() +
                " | bitmap height : " + bitmap.getHeight());
        return rotateImageIfRequired(bitmap);
    }

    public Intent dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = null;

        try {
            file = setUpImageFile();
            mCurrentImageAbsolutePath = file.getAbsolutePath();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = mContext.getPackageName() + ".fileprovider";
                Uri imageUri = FileProvider.getUriForFile(mContext, authorities, file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
            mCurrentImageAbsolutePath = null;
        }

        return takePictureIntent;
    }

    public Intent pickGalleryPictureIntent() {

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        return pickIntent;
    }

    public void handleCameraImage(ImageView imageView) {

        if (mCurrentImageAbsolutePath != null) {
            imageView.setImageBitmap(setPicture(imageView));
            galleryAddPicture();
            mCurrentImageAbsolutePath = null;
        }
    }

    public void handleGalleryImage(Intent intent, ImageView imageView) {

        mCurrentImageAbsolutePath = getRealPathFromURI(intent.getData());
        imageView.setImageBitmap(setPicture(imageView));
        mCurrentImageAbsolutePath = null;
    }

    private void galleryAddPicture() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentImageAbsolutePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    private Bitmap rotateImageIfRequired(Bitmap bitmap) {

        try {
            ExifInterface exifInterface = new ExifInterface(mCurrentImageAbsolutePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(bitmap, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(bitmap, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(bitmap, 270);
                default:
                    return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap rotateImage(Bitmap bitmap, int degree) {

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        Log.d(TAG, "rotateImage >> " + "rotatedBitmap width : " + rotatedBitmap.getWidth() +
                " | rotatedBitmap height : " + rotatedBitmap.getHeight());
        return rotatedBitmap;
    }

    private String getRealPathFromURI(Uri uri) {

        String imagePath;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = mContext.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        imagePath = cursor.getString(columnIndex);
        cursor.close();

        return imagePath;
    }


}
