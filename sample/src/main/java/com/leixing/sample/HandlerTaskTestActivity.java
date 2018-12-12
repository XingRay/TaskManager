package com.leixing.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leixing.taskmanager.HandlerTaskManager;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing1012@qq.com
 * @date : 2018/12/12 14:58
 */
public class HandlerTaskTestActivity extends Activity {
    private TextView tvText;
    private Button btTest;
    private HandlerTaskManager mScheduler;
    private int mCount1;
    private int mCount2;
    private TextView tvText2;


    public static void start(Context context) {
        Intent intent = new Intent(context, HandlerTaskTestActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_handler_task);
        initView();
        mScheduler = new HandlerTaskManager();

        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScheduler.addTask(new Runnable() {
                    @Override
                    public void run() {
                        tvText.setText("" + mCount1++);
                    }
                });

                tvText2.setText("" + mCount2++);
            }
        });
    }

    private void initView() {
        tvText = findViewById(R.id.tv_text);
        btTest = findViewById(R.id.bt_test);
        tvText2 = findViewById(R.id.tv_text2);
    }
}
