package com.example.master.myapplication;

/**
 * Created by okudo on 2016/12/01.
 */

public interface AsyncTaskCallbacks {
    public void onTaskFinished(Boolean isConnect);
    public void onTaskCancelled(Boolean isConnect);
}
