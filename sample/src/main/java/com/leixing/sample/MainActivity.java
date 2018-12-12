package com.leixing.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btHandlerTask;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        setContentView(R.layout.activity_main);
        initView();
        btHandlerTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandlerTaskTestActivity.start(mContext);
            }
        });
    }

    private void initView() {
        btHandlerTask = findViewById(R.id.bt_handler_task);
    }
}
