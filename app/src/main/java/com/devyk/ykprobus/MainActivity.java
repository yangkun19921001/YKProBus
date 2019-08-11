package com.devyk.ykprobus;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.devyk.component_eventbus.proevent.EventManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventManager.getInstance().bindApplication(getApplicationContext(),"com.devyk.module_a");
    }

    public void sendProMessage(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("message", "进程间开始传递信息");
        EventManager.getInstance().sendMessage(0x001, bundle);

    }
}
