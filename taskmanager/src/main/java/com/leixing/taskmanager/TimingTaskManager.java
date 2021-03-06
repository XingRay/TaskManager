package com.ray.lib.java.task;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author : leixing
 * @date : 2018-01-08
 * <p>
 * Email       : leixing1012@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : 任务队列， 调用{@link TimingTaskManager#start()}后执行
 * 通过{@link TimingTaskManager#addTask(Runnable)}加入队列的任务。
 * <p>
 * 轻量级实现，未处理并发问题，外部未加锁时不要并发使用。
 */

@SuppressWarnings("UnusedReturnValue")
public class TimingTaskManager {
    private Queue<Runnable> mTasks;
    private boolean mIsTiming;

    public TimingTaskManager() {

    }

    public TimingTaskManager addTask(Runnable task) {
        if (mIsTiming) {
            task.run();
        } else {
            if (mTasks == null) {
                mTasks = new LinkedList<>();
            }
            mTasks.add(task);
        }
        return this;
    }

    public void start() {
        mIsTiming = true;
        execTasks();
    }

    public void pause() {
        mIsTiming = false;
    }

    public void clear() {
        if (mTasks != null) {
            mTasks.clear();
            mTasks = null;
        }
    }

    private void execTasks() {
        if (mTasks != null) {
            Iterator<Runnable> iterator = mTasks.iterator();
            while (iterator.hasNext()) {
                Runnable task = iterator.next();
                iterator.remove();
                task.run();
            }
        }
    }
}