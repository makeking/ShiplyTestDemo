package com.bete.buglytesstdemo.app;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.bete.buglytesstdemo.log.CustomRFixLog;
import com.tencent.rfix.anno.ApplicationLike;
import com.tencent.rfix.entry.DefaultRFixApplicationLike;
import com.tencent.rfix.entry.RFixApplicationLike;
import com.tencent.rfix.lib.RFixInitializer;
import com.tencent.rfix.lib.RFixParams;
import com.tencent.rfix.loader.entity.RFixLoadResult;
import com.tencent.rfix.loader.log.RFixLog;

@ApplicationLike(application = ".SampleLikeApplication")
public class SampleApplicationLike extends DefaultRFixApplicationLike {

    private static final String TAG = "RFix.AppApplication";

    public SampleApplicationLike(Application application, RFixLoadResult loadResult) {
        super(application, loadResult);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);

        // 初始化RFix组件
        initRFixAsync(this);
    }

    public static void initRFix(RFixApplicationLike applicationLike) {
        Log.d(TAG, "initRFix...");

        // 初始化日志接口
        RFixLog.setLogImpl(new CustomRFixLog());

        RFixParams params = new RFixParams("f70145d417", "745fd3d0-9a19-46ff-8b15-2d5b48156677")
                .setDeviceId("000000")
                .setDeviceManufacturer(Build.MANUFACTURER)
                .setDeviceModel(Build.MODEL)
                .setUserId("123456")
                .setCustomProperty("appid", "1000");

        // 初始化RFix组件
        RFixInitializer.initialize(applicationLike, params);
    }

    public static void initRFixAsync(RFixApplicationLike applicationLike) {
        Log.d(TAG, "initRFixAsync...");

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                Log.e(TAG, "initRFixAsync fail!", e);
            }

            initRFix(applicationLike);
        });
        thread.start();
    }
}
