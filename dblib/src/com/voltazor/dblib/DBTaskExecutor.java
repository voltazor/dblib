package com.voltazor.dblib;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by: Дима Довбня
 * Date: 03.09.13 18:22
 */
public class DBTaskExecutor implements ExecutorCallback {

    private Queue<BaseDBTask> taskQueue = new ArrayDeque<BaseDBTask>();
    private boolean executing = false;

    public synchronized void execute(BaseDBTask task) {
        task.setExecutorCallback(this);
        taskQueue.add(task);
        nextTask();
    }

    @Override
    public void finished(long time) {
        executing = false;
        nextTask();
    }

    private synchronized void nextTask() {
        if (executing) {
            return;
        }

        BaseDBTask task;
        if ((task = taskQueue.poll()) != null) {
            executing = true;
            task.execute();
        }
    }

}
