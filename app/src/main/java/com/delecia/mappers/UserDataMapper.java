package com.delecia.mappers;

import android.content.Context;
import android.widget.Toast;

import com.delecia.app.MyApplication;
import com.delecia.interfaces.TaskCompletedListener;
import com.delecia.retrofit.responses.LoginResponse;
import com.delecia.retrofit.services.UserDataService;
import com.delecia.utils.CustomLog;
import com.delecia.utils.SharedPreferencesData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin1 on 17/12/16.
 */

public class UserDataMapper {

    private TaskCompletedListener taskCompletedListener;

    public void login(TaskCompletedListener listener, String email, String phone){
        if (MyApplication.getInstance().isConnectedToInterNet()){
            this.taskCompletedListener = listener;
            UserDataService userDataService = MyApplication.getInstance().getUserDataService();
            Call<LoginResponse> call = userDataService.login(email,phone);
            call.enqueue(loginResponseCallback);
        }
    }
    private Callback<LoginResponse> loginResponseCallback = new
            Callback<LoginResponse>() {

                @Override
                public void onResponse(Call<LoginResponse> call,
                                       Response<LoginResponse>
                                               response) {
                    if (response.code() == 401) {
                        taskCompletedListener.onTaskFailed("session expired");
                    } else if (response.code() == 504) {
                        taskCompletedListener.onTaskFailed("Unknown host");
                    } else if (response.isSuccessful()) {
                        CustomLog.i("Response",""+response.body().getData());
                        taskCompletedListener.onTaskCompleted(response.body());
                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    taskCompletedListener.onTaskFailed("Network Connection Error");
                    CustomLog.e("login callback","user info data service error");
                }
            };
    public void register(TaskCompletedListener listener, String name, String phone,String email,String password){
        if (MyApplication.getInstance().isConnectedToInterNet()){
            this.taskCompletedListener = listener;
            UserDataService userDataService = MyApplication.getInstance().getUserDataService();
            Call<LoginResponse> call = userDataService.register(name,phone,email,password);
            call.enqueue(loginResponseCallback);
        }
    }
}
