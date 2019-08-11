package com.devyk.component_eventbus.proevent.local;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.devyk.component_eventbus.proevent.Constants;
import com.devyk.component_eventbus.proevent.dispatcher.IMessageHandler;
import com.devyk.component_eventbus.proevent.utils.ProcessUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author  : devyk on 2019-08-09 15:37
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is MessageController 接收来自服务端的消息负责转发
 * </pre>
 */
public class MessageController {

    private static MessageController instance;
    private Context mApplicationContext;
    private Messenger mServiceMessenger;

    private String TAG = getClass().getSimpleName();

    /**
     * 缓存当前注册的 class
     */
    private Map<Integer, IMessageHandler> mCacheMessageHandler = new HashMap<Integer, IMessageHandler>();


    /**
     * 是否绑定成功
     */
    private boolean isBindApplication = false;
    private EventServiceConnection mEventServiceConnection;
    private Messenger mLocalMessenger;
    private LocalMessengerHandler mLocalMessengerHandler;
    private Intent service;

    public static synchronized MessageController getInstance() {
        if (instance == null)
            instance = new MessageController();

        return instance;
    }


    /**
     * 框架绑定  需要跟 sendMessage 在同一个进程
     */
    public void bindApplication(Context context, String proName) {
        if (context == null || TextUtils.isEmpty(proName))
            throw new NullPointerException("init error,context or proName is  null?");
        //已经绑定成功就不需要在绑定了
        if (isBindApplication) return;
        try {
            initLocalMessenger();
            mEventServiceConnection = new EventServiceConnection();
            mApplicationContext = context.getApplicationContext();
            service = new Intent();
            service.setAction("com.devyk.component_eventbus.service");
            service.setPackage(proName);
            mApplicationContext.startService(service);
            mApplicationContext.bindService(service, mEventServiceConnection, 0);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    private void initLocalMessenger() {
        mLocalMessengerHandler = new LocalMessengerHandler();
        mLocalMessenger = new Messenger(mLocalMessengerHandler);
    }

    private Messenger getLocalMessenger() {
        return mLocalMessenger;
    }

    private Handler getLocalMessengerHandler() {
        return mLocalMessengerHandler;
    }

    /**
     * 框架销毁 不在使用
     */
    public void unBindApplication() {
        if (mEventServiceConnection != null) {
            mApplicationContext.unbindService(mEventServiceConnection);
            mLocalMessengerHandler.removeCallbacksAndMessages(null);
            mLocalMessengerHandler = null;
            mApplicationContext.stopService(service);
        }
    }


    /**
     * 向服务器注册一个信使，用来传递从Service到View的消息
     */
    private void registerMessenger() {
        // 注册消息号：Content.MSG_REGISTER_CTRL
        Message message = Message.obtain(null, Constants.IMessageNumber.VIEW_REGISTER_CLIENT_MSG);
        //拿到当前进程的 pid
        message.arg1 = android.os.Process.myPid();
        // 传递自己的信使到服务端
        message.replyTo = getLocalMessenger();
        try {
            getServiceMessenger().send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 注册消息
     * 在需要的地方注册消息
     */
    public void registerMessager(Integer tag, Object object) {
        if (object == null) {
            throw new NullPointerException("registerMessager is  null");
        }

        IMessageHandler iMessageHandler = null;

        if (object instanceof IMessageHandler)
            iMessageHandler = (IMessageHandler) object;

        if (iMessageHandler == null)
            throw new NullPointerException(" IMessageHandler is  null?");

        if (!mCacheMessageHandler.containsKey(tag)) {
            mCacheMessageHandler.put(tag, iMessageHandler);
        }
    }


    /**
     * 将消息转发到注册的地方上去
     *
     * @param message
     */
    public void dispatchMessageToRegister(Message message) {
        if (message != null) {
            if (mCacheMessageHandler.containsKey(message.what)) {
                Handler handler = mCacheMessageHandler.get(message.what).getHandler();
                Message dispatchMessage = handler.obtainMessage();
                dispatchMessage.what = message.what;
                dispatchMessage.setData(message.getData());
                dispatchMessage.sendToTarget();
            }
        }
    }


    /**
     * 反注册消息
     */
    public void unRegisterMessager(int tag) {
        if (mCacheMessageHandler.containsKey(tag)) {
            mCacheMessageHandler.remove(tag);

        }

    }


    /**
     * 得到服务端的消息信使
     *
     * @return
     */
    public Messenger getServiceMessenger() {
        return mServiceMessenger;
    }


    /**
     * 接收到服务端传递过来的消息
     */
    private class LocalMessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String proName = ProcessUtils.getProName(mApplicationContext);
            Log.d(TAG, " LocalMessengerHandler " + proName);
            Log.d(TAG, "收到服务端发来的消息" + msg.what);

        }
    }

    /**
     * 服务端消息是否连接成功
     */
    private class EventServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBindApplication = true;
            // 得到服务信使对象
            mServiceMessenger = new Messenger(service);

            //将本地信使告诉服务端
            registerMessenger();

            String proName = ProcessUtils.getProName(mApplicationContext);
            Log.d(TAG, " EventServiceConnection " + proName);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBindApplication = false;
        }
    }

}
