package com.devyk.component_eventbus.proevent;

/**
 * <pre>
 *     author  : devyk on 2019-08-09 10:06
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is Constants 对一些常亮提供统一管理
 * </pre>
 */
public class Constants {


    /**
     * 发送接收消息号统一管理
     */
    public interface IMessageNumber{

        //----------------------- 服务端消息号 -------------------------//
        /**
         * 注册消息
         */
        int VIEW_REGISTER_CLIENT_MSG = 0x1991001;// 注册消息
        /**
         * 销毁消息
         */
        int VIEW_UNREGISTER_CLIENT_MSG = 0x19921002;// 销毁消息
        //消息 TAG
        String DISPATCHER_MESSAGE_TAG = "DISPATCHER_MESSAGE_TAG";
    }
}
