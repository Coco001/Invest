package cqupt.myinvest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;
import cqupt.myinvest.common.BaseActivity;
import cqupt.myinvest.utils.BitmapUtils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.media.MediaRecorder.VideoSource.CAMERA;

/**
 * 用户信息的activity.
 */

public class UserInfoActivity extends BaseActivity {
    @Bind(R.id.iv_title_left)
    ImageView mIvTitleLeft;
    @Bind(R.id.tv_title_name)
    TextView mTvTitleName;
    @Bind(R.id.iv_title_right)
    ImageView mIvTitleRight;
    @Bind(R.id.iv_user_icon)
    ImageView mIvUserIcon;
    @Bind(R.id.tv_user_change)
    TextView mTvUserChange;
    @Bind(R.id.btn_user_logout)
    Button mBtnUserLogout;

    // 返回码：系统图库
    private static final int CODE_IMAGE = 100;
    // 返回码：相机
    private static final int CODE_CAMERA = 200;
    // IMAGE TYPE
    private static final String IMAGE_TYPE = "image/*";
    // Temp照片路径
    public static String TEMP_IMAGE_PATH;

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle() {
        mIvTitleLeft.setVisibility(View.VISIBLE);
        mTvTitleName.setText("用户信息");
        mIvTitleRight.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.tv_user_change)
    public void showDialogCustom() {
        String[] mCustomItems = new String[]{"本地图册", "相机拍照"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择来源：");
        builder.setItems(mCustomItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (0 == which) {// 本地图册
                    //运行时权限检查
                    if (ContextCompat.checkSelfPermission(UserInfoActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(UserInfoActivity.this,
                            READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
                        startActivityForResult(intent, CODE_IMAGE);
                    } else {
                        ActivityCompat.requestPermissions(UserInfoActivity.this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                READ_EXTERNAL_STORAGE}, 1);
                    }
                } else if (1 == which) {// 系统相机
                    /*//方式一
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri = Uri.fromFile(new File(TEMP_IMAGE_PATH));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CODE_CAMERA);*/
                    //方式二
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, CAMERA);
                }
            }
        }).setCancelable(false);
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
                    startActivityForResult(intent, CODE_IMAGE);
                } else {
                    Toast.makeText(UserInfoActivity.this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 调用图库相机回调方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_IMAGE && data != null) {
                // 相册
                Uri selectedImage = data.getData();
                //android各个不同的系统版本,对于获取外部存储上的资源，返回的Uri对象都可能各不一样,
                // 所以要保证无论是哪个系统版本都能正确获取到图片资源的话就需要针对各种情况进行一个处理了
                //这里返回的uri情况就有点多了
                //在4.4.2之前返回的uri是:content://media/external/images/media/3951或者file://....
                // 在4.4.2返回的是content://com.android.providers.media.documents/document/image

                String pathResult = getPath(selectedImage);
                //存储--->内存
                Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
                Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, mIvUserIcon.getWidth(), mIvUserIcon.getHeight());
                //bitmap圆形裁剪
                Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);
                //加载显示
                mIvUserIcon.setImageBitmap(circleImage);
                //保存到本地
                saveImage(circleImage);
            } else if (requestCode == CODE_CAMERA) {
                // 相机
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                //对获取到的bitmap进行压缩、圆形处理
                bitmap = BitmapUtils.zoom(bitmap, mIvUserIcon.getWidth(), mIvUserIcon.getHeight());
                bitmap = BitmapUtils.circleBitmap(bitmap);
                //加载显示
                mIvUserIcon.setImageBitmap(bitmap);
                //保存到本地
                saveImage(bitmap);
            }
        }
    }

    //保存图片到本地
    private void saveImage(Bitmap bitmap) {
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = this.getExternalFilesDir("");

        } else {//手机内部存储
            //路径：data/data/包名/files
            filesDir = this.getFilesDir();

        }
        FileOutputStream fos = null;
        try {
            File file = new File(filesDir, "icon.png");
            fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据系统相册选择的文件获取路径
     *
     * @param uri
     */
    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        //高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }


        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * uri路径查询字段
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    @OnClick(R.id.iv_title_left)
    public void back(View view) {
        //1.销毁当前的页面
        this.removeCurrentActivity();
        //2.显示用户自定义选择的图片
        //我的资产界面的onresume()方法调用，在此方法中读取用户选择的图片
    }

    @OnClick(R.id.btn_user_logout)
    public void logout() {
        //将保存在本地的数据信息删除
        SharedPreferences info = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        info.edit().clear().commit();

        File fileDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            fileDir = this.getExternalFilesDir("");
        } else {
            fileDir = this.getFilesDir();
        }
        File file = new File(fileDir, "icon.png");
        if (file.exists()) {
            file.delete();
        }
        //显示首页的界面
        this.removeAllActivity();
        this.setupActivity(MainActivity.class, null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

}
