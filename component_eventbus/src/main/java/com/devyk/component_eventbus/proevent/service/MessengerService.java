package com.devyk.component_eventbus.proevent.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * <pre>
 *     author  : devyk on 2019-08-09 09:46
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is Messenger 服务端 处理消息接收并转发
 * </pre>
 */
public class MessengerService extends Service {
    private MessengerManager mServiceMessenger;

    private String TAG = "MessengerService";

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceMessenger.getServiceMessenger().getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        init();
    }


    /**
     * 对服务端做一些初始化操作
     */
    private void init() {
        mServiceMessenger = MessengerManager.getServiceMessenger(getApplicationContext());

    }
}
