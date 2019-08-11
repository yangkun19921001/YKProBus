package com.devyk.component_eventbus.proevent.dispatcher;

import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.devyk.component_eventbus.proevent.local.MessageController;

import java.util.List;

/**
 * <pre>
 *     author  : devyk on 2019-08-09 14:19
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is MessageDispatcher 负责服务端的消息转发到注册模块中去
 * </pre>
 */
public class MessageDispatcher {

    private static MessageDispatcher sMessageDispatcher;
    private String TAG = "MessageDispatcher";


    /**
     * 单例
     */
    public static synchronized MessageDispatcher getInstance() {
            if (sMessageDispatcher == null) {
                sMessageDispatcher = new MessageDispatcher();
            }
        return sMessageDispatcher;
    }


    /**
     * 将消息反馈到本地端
     *
     * @param mClients 需要发送到本地端
     * @param msg
     */
    public void dispatchMessage(List<Messenger> mClients, Message msg) {
        //拿到需要转发到注册接收消息处
        Log.d(TAG,"收到客服端的信使对象" + mClients.size());
        if (msg != null) {
            Message obtain = Message.obtain();
            obtain.what = msg.what;
            obtain.setData(msg.getData());
            try {
                if (mClients.size() == 1) {
                    if (mClients.get(0) != null) {
                        mClients.get(0).send(obtain);
                    }
                } else {
                    while (mClients.iterator().hasNext()) {
                        mClients.iterator().next().send(obtain);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将消息下发到消息控制器转发
     * @param message
     */
    public void  dispatchMessageToRegister(Message message){
        MessageController.getInstance().dispatchMessageToRegister(message);
   }

    /**
     * 将消息发送到本地端
     */
    public void sendMessageLocal() {
    }
}
