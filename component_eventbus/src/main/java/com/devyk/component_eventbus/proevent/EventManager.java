package com.devyk.component_eventbus.proevent;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.devyk.component_eventbus.proevent.local.MessageController;


/**
 * <pre>
 *     author  : devyk on 2019-08-08 22:33
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is EventManager 组件间/进程间 通信管理类
 * </pre>
 */
public class EventManager<T> {

    private final String TAG = getClass().getSimpleName();
    private static EventManager instance;


    /**
     * 定义一个单例
     */
    public static synchronized EventManager getInstance() {
            if (instance == null)
                instance = new EventManager();
        return instance;
    }

    /**
     * 框架初始化
     */
    public void bindApplication(Context context,String proName) {
        if (context == null)
            throw new NullPointerException("init error,context is not null?");
        MessageController.getInstance().bindApplication(context,proName);
    }

    /**
     * 框架销毁 不在使用
     */
    public void unBindApplication() {
        MessageController.getInstance().unBindApplication();
    }

    /**
     * 注册消息
     * 在需要的地方注册消息
     */
    public void registerMessager(int messageType,Object object) {
        MessageController.getInstance().registerMessager(messageType,object);
    }

    /**
     * 反注册消息
     */
    public void unRegisterMessager(int messageType) {
        MessageController.getInstance().unRegisterMessager(messageType);
    }


    /**
     * 发送消息
     */
    public  void sendMessage(int what, Bundle entity) {
        Message message = new Message();
        message.what = what;
        message.setData(entity);

        if (MessageController.getInstance().getServiceMessenger() != null) {
            try {
                MessageController.getInstance().getServiceMessenger().send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }
    }


}
