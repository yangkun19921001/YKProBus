package com.devyk.component_eventbus.proevent.dispatcher;


import android.os.Handler;

/**
 * <pre>
 *     author  : devyk on 2019-08-09 14:35
 *     blog    : https://juejin.im/user/578259398ac2470061f3a3fb/posts
 *     github  : https://github.com/yangkun19921001
 *     mailbox : yang1001yk@gmail.com
 *     desc    : This is IMessageHandler 负责接收消息的
 * </pre>
 */
public interface IMessageHandler {

    Handler getHandler();
}
