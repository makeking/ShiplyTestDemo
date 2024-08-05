package com.bete.buglytesstdemo;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.bete.buglytesstdemo.log.LogUtils;
import com.tencent.rfix.lib.dev.AbsRFixDevActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends AppCompatActivity {
    private SplashActivity context;
    private static final int REQUEST_PERMISSION_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light);
        setContentView(R.layout.activity_splash);

        context = this;

        LogUtils.e("SplashActivity is init ");
        new Thread() {
            @Override
            public void run() {
                super.run();
                // 判断
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);//, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_SECURE_SETTINGS, Manifest.permission.CAMERA
            }
        }.start();
    }

    /**
     * 请求权限
     *
     * @param permissions           请求的权限
     * @param requestPermissionCode 请求码
     */
    private int REQUEST_CODE_PERMISSION = 0x00099;

    public void requestPermission(String[] permissions, int requestPermissionCode) {
        REQUEST_CODE_PERMISSION = requestPermissionCode;
        if (checkPermissions(permissions)) {
            // 同意权限
            permissionSuccess(requestPermissionCode);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    public boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                permissionSuccess(REQUEST_CODE_PERMISSION);
            } else {
                permissionFail(REQUEST_CODE_PERMISSION);
                showTipsDialog();
            }
        }
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    public boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.tipInfo))
                .setMessage(getString(R.string.splashTipInfo))
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> AppUtils.exitApp())
                .setPositiveButton(getString(R.string.queding), (dialog, which) -> startAppSettings()).show();
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /*
      权限相关的操作
     */
    public void permissionSuccess(int requestCode) {
//        ShellUtils.execCmd("setprop service.adb.tcp.port 5555", true);
//        ShellUtils.execCmd("stop adbd", true);
//        ShellUtils.execCmd("start adbd", true);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            LogUtils.d("获取权限成功:" + requestCode);

            startActivity(new Intent(this, TestActivity.class));
//            startActivity(new Intent(this, MainActivity.class));
        } else {
            throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    /**
     * 权限获取失败
     *
     * @param requestCode
     */
    public void permissionFail(int requestCode) {
        LogUtils.d("获取权限失败:" + requestCode);
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


}
