package com.guoyannan.feature.permission;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.guoyannan.feature.R;

import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class PermissionActivity extends AppCompatActivity {

    private static final String TAG = "PermissionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        setupView();
    }

    private void setupView() {
        findViewById(R.id.btn_request_runtime_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertContactWithCheck()是自动生成的方法
                PermissionActivityPermissionsDispatcher.insertContactWithCheck(PermissionActivity.this);
            }
        });
    }

    /******************
     * 原生代码申请权限 *
     ******************/

    private static String DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample";
    private static final int REQUEST_CODE_FOR_PERMISSION_CALLBACK = 0x000001;

    private void requestRuntimePermission() {
        // API 23及其以后的版本
        if (Build.VERSION.SDK_INT >= 23) {
            // 检测是否已经被授权
            if (ContextCompat.checkSelfPermission(PermissionActivity.this,
                    Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                //是否给用户一个关于权限申请的解释
                if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this,
                        Manifest.permission.WRITE_CONTACTS)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {
                    //请求权限
                    ActivityCompat.requestPermissions(PermissionActivity.this,
                            new String[]{Manifest.permission.WRITE_CONTACTS},
                            REQUEST_CODE_FOR_PERMISSION_CALLBACK);
                }
            }
        }
        //API 23以前版本
        else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_CODE_FOR_PERMISSION_CALLBACK:
//                //如果权限请求被拒绝，则grantResults为空
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //权限已经被授予，可以添加联系人了
//                    Toast.makeText(PermissionActivity.this, "添加联系人权限被授予", Toast.LENGTH_LONG).show();
//                    insertDummyContact();
//                } else {
//                    //权限请求被拒绝
//                    Toast.makeText(PermissionActivity.this, "添加联系人权限被拒绝", Toast.LENGTH_LONG).show();
//                }
//                break;
//        }
        //权限申请的结果交由PermissionActivityPermissionsDispatcher来处理
        PermissionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * Accesses the Contacts content provider directly to insert a new contact.
     * <p>
     * The contact is called "__DUMMY ENTRY" and only contains a name.
     */
    private void insertDummyContact() {
        // Two operations are needed to insert a new contact.
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);

        // First, set up a new raw contact.
        ContentProviderOperation.Builder op =
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
        operations.add(op.build());

        // Next, set the name for the contact.
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        DUMMY_CONTACT_NAME);
        operations.add(op.build());

        // Apply the operations.
        ContentResolver resolver = getContentResolver();
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (RemoteException e) {
            Log.d(TAG, "Could not add a new contact: " + e.getMessage());
        } catch (OperationApplicationException e) {
            Log.d(TAG, "Could not add a new contact: " + e.getMessage());
        }
    }

    /******************
     * 注解方式申请权限 *
     ******************/

    /**
     * 需要申请权限的操作
     */
    @NeedsPermission(Manifest.permission.WRITE_CONTACTS)
    void insertContact() {
        insertDummyContact();
    }

    /**
     * 解释权限申请原因
     *
     * @param request request
     */
    @OnShowRationale(Manifest.permission.WRITE_CONTACTS)
    void onShowRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("解释为何申请权限")
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.cancel();
                    }
                })
                .show();
    }

    /**
     * 权限申请拒绝
     */
    @OnPermissionDenied(Manifest.permission.WRITE_CONTACTS)
    void onPermissionDenied() {
        Toast.makeText(PermissionActivity.this, "添加联系人权限被拒绝", Toast.LENGTH_LONG).show();
    }

    /**
     * 权限申请不再询问
     */
    @OnNeverAskAgain(Manifest.permission.WRITE_CONTACTS)
    void onNeverAskAgain() {
        Toast.makeText(PermissionActivity.this, "添加联系人权限不再被询问", Toast.LENGTH_LONG).show();
    }
}
