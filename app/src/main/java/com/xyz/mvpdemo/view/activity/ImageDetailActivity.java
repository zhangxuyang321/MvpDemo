package com.xyz.mvpdemo.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xyz.mvpdemo.R;
import com.xyz.mvpdemo.Util.MyToast;
import com.xyz.mvpdemo.Util.Util;
import com.xyz.mvpdemo.base.BaseActivity;
import com.xyz.mvpdemo.base.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 图片大图显示类，可保存图片到本地
 */
public class ImageDetailActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private ImageView bigImg;
    private ImageButton fab;
    private String pin_id;
    private static final int REQUECT_CODE_SDCARD = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.addActivity(this);
        bigImg = (ImageView) findViewById(R.id.big_img);
        fab = (ImageButton) findViewById(R.id.fab);
        initData();

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String bigImgUrl = intent.getStringExtra("imgUrl") + "_fw658";
            pin_id = intent.getStringExtra("pin_id");
            Glide.with(this).load(bigImgUrl).placeholder(R.mipmap.zw).crossFade().into(bigImg);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //首先检测是否有权限
                if (!EasyPermissions.hasPermissions(ImageDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    EasyPermissions.requestPermissions(ImageDetailActivity.this, "保存图片需要读写内存卡权限", REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    saveImage(pin_id);
                }
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.removeActivity(this);
    }

    public void saveImage(String strFileName) {
        Bitmap bitmap = convertViewToBitmap(bigImg);
        String strPath = Util.getSDPath();

        try {
            assert strPath != null;
            File destDir = new File(strPath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            File imageFile = new File(strPath + "/" + strFileName+".jpg");
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
            MyToast.showToast("下载成功~");

            //通知相册刷新
            updateReciver(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateReciver(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);
    }

    /**
     * AT_MOST：我们可以指定一个上限，要保存的图片的大小不会超过它。
     * EXACTLY：我们指定了一个明确的大小，要求图片保存时满足这个条件。
     * UNSPECIFIED：图片多大，我们就保存多大。
     *
     * @param view 对应的Imageview
     * @return bitmap
     */
    public Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        saveImage(pin_id);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
