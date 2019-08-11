package com.devyk.component_eventbus.proevent.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * <pre>
 *     author  : devyk on 2019-08-11 13:17
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is ProcessUtils
 * </pre>
 */
public class ProcessUtils {

    /**
     * 当前进程名称
     */
    private static String procName;


    /**
     * 获取当前进程 pid
     */
    public static int  getProPid(){
        return android.os.Process.myPid();
    }


    /**
     * 获取当前进程名称
     */
    public static String getProName(Context context){
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == getProPid()) {
                procName = appProcess.processName;
            }
        }
        return procName;
    }
}
