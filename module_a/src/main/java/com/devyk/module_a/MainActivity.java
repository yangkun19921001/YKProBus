package com.devyk.module_a;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.devyk.component_eventbus.proevent.EventManager;
import com.devyk.component_eventbus.proevent.dispatcher.IMessageHandler;

public class MainActivity extends Activity implements IMessageHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventManager.getInstance().registerMessager(0x001, this);
    }


    /**
     * 接收其它进程发送过来的消息
     *
     * @return
     */
    @Override
    public Handler getHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x001:
                        if (msg.getData() != null && msg.getData().getString("message") != null) {
                            String message = msg.getData().getString("message");
                            Toast.makeText(getApplicationContext(), "收到其它进程发送过来的消息：" + message, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
    }
}
