package com.delecia.interfaces;

import com.delecia.retrofit.responses.LoginResponse;

/**
 * Created by Sachin on 12/18/2016.
 */
public interface TaskCompletedListener {
    void onTaskCompleted(LoginResponse loginResponse);
    void onTaskFailed(String request);
}
