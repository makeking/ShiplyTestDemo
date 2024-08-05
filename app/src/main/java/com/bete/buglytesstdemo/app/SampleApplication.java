package com.bete.buglytesstdemo.app;

import android.app.Application;
import android.content.Context;

import com.bete.buglytesstdemo.AppUtils;
import com.bete.buglytesstdemo.FileCommonUtil;
import com.bete.buglytesstdemo.log.LogUtils;
import com.bete.buglytesstdemo.log.Utils;
import com.tencent.rfix.anno.ApplicationLike;
import com.tencent.rfix.entry.DefaultRFixApplicationLike;
import com.tencent.rfix.entry.RFixApplicationLike;


import java.util.Locale;

@ApplicationLike(application = ".SampleProxyApplication")
public class SampleApplication extends Application {
    private static SampleApplication instance;
    public static String systemCountry = Locale.getDefault().getCountry();
    private Boolean isDebug = true;


    //tts语音播放
    //public static TextToSpeech mSpeech = null;
    // 获取Application
    public static Context getAppContext() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // 初始化RFix组件
        final RFixApplicationLike applicationLike = DefaultRFixApplicationLike.createApplicationLike(this);
        SampleApplicationLike.initRFixAsync(applicationLike);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Utils.init(instance);
        initLog();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);

        // 1. 添加 shiply 的内容
        initBugly();

    }

    private void initBugly() {

    }

    void initLog() {
        String logDir;
        logDir = "/SDcard/Testdemo" ;
        FileCommonUtil.createOrExistsDir(logDir);
        LogUtils.Config config = LogUtils.getConfig()
                .setLogSwitch(isDebug())// 设置 log 总开关，包括输出到控制台和文件，默认开
                .setConsoleSwitch(isDebug())// 设置是否输出到控制台开关，默认开
                .setGlobalTag(null)// 设置 log 全局标签，默认为空
                // 当全局标签不为空时，我们输出的 log 全部为该 tag，
                // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                .setLogHeadSwitch(true)// 设置 log 头信息开关，默认为开
                .setLog2FileSwitch(true)// 打印 log 时是否存到文件的开关，默认关
                .setDir(logDir)// 当自定义路径为空时，写入应用的/cache/log/目录中
                .setFilePrefix("log")// 当文件前缀为空时，默认为"util"，即写入文件为"util-yyyy-MM-dd.txt"
                .setBorderSwitch(false)// 输出日志是否带边框开关，默认开
                .setSingleTagSwitch(true)// 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
                .setConsoleFilter(LogUtils.V)// log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(LogUtils.D)// log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setStackDeep(1)// log 栈深度，默认为 1
                .setStackOffset(0)// 设置栈偏移，比如二次封装的话就需要设置，默认为 0
                .setSaveDays(30)// 设置日志可保留天数，默认为 -1 表示无限时长
                // 新增 ArrayList 格式化器，默认已支持 Array, Throwable, Bundle, Intent 的格式化输出
                ;
        LogUtils.d(config.toString());
        //todo 增加文件夹存储检测和剩余存储检测,根据检测删除log文件
//        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG);
    }

    private Boolean isDebug() {
        if (isDebug == null)
            isDebug = AppUtils.isAppDebug();
        return isDebug;
    }
}
