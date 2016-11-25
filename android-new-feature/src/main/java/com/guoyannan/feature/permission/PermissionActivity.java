package com.guoyannan.feature.permission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.guoyannan.feature.R;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        checkPermission();
    }


    private static final int REQUEST_CODE_FOR_PERMISSION_CALLBACK = 0x000001;

    private void checkPermission() {
        //检测权限是否已经被授予
        if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this, Manifest.permission.READ_CONTACTS)) {
                //TODO 如果用户拒绝了上一次的权限请求，你可以在此处给用户一个为什么要授权的解释

            }
        } else {
            //申请授权
            ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_FOR_PERMISSION_CALLBACK);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_FOR_PERMISSION_CALLBACK:
                //如果权限请求被拒绝，则grantResults为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TODO 权限已经被授予，可以查看联系人了
                } else {
                    //权限请求被拒绝
                }
                break;
        }
    }
}
