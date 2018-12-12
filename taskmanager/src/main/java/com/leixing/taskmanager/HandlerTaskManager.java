package com.leixing.taskmanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * description : HandlerTaskManager
 *
 * @author : leixing
 * email : leixing1012@qq.com
 * @date : 2018/12/12 15:00
 */
public class HandlerTaskManager {
    private static final int MSG_PRODUCT_TOKEN = 100;
    private final Handler mHandler;
    private final List<Runnable> mTasks;

    private int mMaxTokenCount = 1;
    private int mTokenCount;
    private int mTokenProduceIntervalMills = 1000;

    public HandlerTaskManager() {
        this(Looper.getMainLooper());
    }

    public HandlerTaskManager(Looper looper) {
        mTasks = new LinkedList<>();
        mTokenCount = mMaxTokenCount;
        mHandler = new InnerHandler(looper);
    }

    public void addTask(Runnable task) {
        mTasks.add(task);
        consumeTokenByTask();
        sendProductTokenMessage();
    }

    public HandlerTaskManager maxTokenCount(int maxTokenCount) {
        mMaxTokenCount = maxTokenCount;
        mTokenCount = mMaxTokenCount;
        return this;
    }

    public HandlerTaskManager tokenProduceIntervalMills(int tokenProduceIntervalMills) {
        if (mTokenProduceIntervalMills <= 0) {
            throw new IllegalArgumentException();
        }
        mTokenProduceIntervalMills = tokenProduceIntervalMills;
        return this;
    }

    private void consumeTokenByTask() {
        if (mTasks.isEmpty() || mTokenCount == 0) {
            return;
        }

        for (Iterator<Runnable> iterator = mTasks.iterator(); iterator.hasNext(); ) {
            Runnable task = iterator.next();
            task.run();
            iterator.remove();
            mTokenCount--;
            if (mTokenCount == 0) {
                break;
            }
        }
    }

    private void sendProductTokenMessage() {
        if (mHandler.hasMessages(MSG_PRODUCT_TOKEN)) {
            return;
        }
        Message message = mHandler.obtainMessage(MSG_PRODUCT_TOKEN);
        if (message.obj == null) {
            message.obj = new WeakReference<>(this);
        }
        mHandler.sendMessageDelayed(message, mTokenProduceIntervalMills);
    }

    private void onTokenProduct() {
        mTokenCount++;
        consumeTokenByTask();
        if (mTokenCount < mMaxTokenCount) {
            sendProductTokenMessage();
        }
    }

    private static class InnerHandler extends Handler {
        InnerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_PRODUCT_TOKEN && msg.obj != null) {
                WeakReference reference = (WeakReference) msg.obj;
                Object o = reference.get();
                if (!(o instanceof HandlerTaskManager)) {
                    return;
                }
                HandlerTaskManager taskManager = (HandlerTaskManager) o;
                taskManager.onTokenProduct();
            }
        }
    }
}
