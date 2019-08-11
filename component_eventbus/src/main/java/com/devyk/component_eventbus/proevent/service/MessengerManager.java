package com.devyk.component_eventbus.proevent.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;


import com.devyk.component_eventbus.proevent.Constants;
import com.devyk.component_eventbus.proevent.dispatcher.MessageDispatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author  : devyk on 2019-08-09 09:50
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is MessengerManager
 *
 *    信使管理类
 *      1. 单例模式管理信使
 *      2. 初始化各个模块(是在Service启动时调用)
 *      3. 将从View接收到的消息传递到 MessageDispatcher
 *      4. 将消息发送到注册接收的地方
 * </pre>
 */
public class MessengerManager {

    /**
     * LOG TAG
     */
    private String TAG = getClass().getSimpleName();

    /**
     * 服务端 Messenger
     */
    private Messenger mServiceMessenger;

    /**
     * 信使管理
     */
    private static MessengerManager mMessengerManager;

    /**
     * 本地端信使列表
     */
    private List<Messenger> mClients = new ArrayList<Messenger>() {
    };

    /**
     * 对外暴露信使管理
     *
     * @param context
     * @return
     */
    public static synchronized MessengerManager getServiceMessenger(Context context) {
        if (mMessengerManager == null)
            mMessengerManager = new MessengerManager();
        return mMessengerManager;
    }

    /**
     * 初始化服务端 Messenger
     */
    public MessengerManager() {
        if (null == mServiceMessenger)
            mServiceMessenger = new Messenger(mMessengerServiceHandler);
    }

    /**
     * 得到服务端信使对象
     */
    public Messenger getServiceMessenger() {
        return mServiceMessenger;
    }


    /**
     * 接收客服端发来的消息
     */
    public Handler mMessengerServiceHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //需要接收消息转发的注册
                case Constants
                        .IMessageNumber.VIEW_REGISTER_CLIENT_MSG:
                    registerMessenger(msg.replyTo);
                    break;

                //需要销毁消息
                case Constants
                        .IMessageNumber.VIEW_UNREGISTER_CLIENT_MSG:
                    unRegisterMessenger(msg.replyTo);
                    break;
                default:
                    dispatcherMessage(msg);
                    break;
            }
        }
    };

    /**
     * 收到其它消息就直接下发到注册的接收的地方
     *
     * @param msg
     */
    private void dispatcherMessage(Message msg) {
        Log.d(TAG, "当前信使所有对象个数 " + mClients.size());
        Message message = new Message();
        message.setData(msg.getData());
        message.what = msg.what;
        MessageDispatcher.getInstance().dispatchMessageToRegister(message);
    }


    /**
     * 注册信使
     *
     * @param messenger 本地端的信使
     */
    private void registerMessenger(Messenger messenger) {
        if (null != messenger) {
            mClients.add(messenger);
            Log.d(TAG, "收到客服端的信使对象");
        }
    }

    /**
     * 将消息发送出去
     */

    /**
     * 销毁信使
     *
     * @param messenger
     */
    private void unRegisterMessenger(Messenger messenger) {
        if (null != messenger)
            mClients.remove(messenger);

    }

    /**
     * 销毁所有
     */
    private void clearAllMessenger() {
        //先判断是信使列表是否存在
        if (mClients.size() > 0) {
            mClients.clear();
        }

    }


    /**
     * 销毁事务
     */
    public void onMessagerManagerOnDestory() {
        if (null != mMessengerServiceHandler) {
            mMessengerServiceHandler.removeCallbacksAndMessages(null);
            mMessengerServiceHandler = null;
        }

        clearAllMessenger();
    }
}
