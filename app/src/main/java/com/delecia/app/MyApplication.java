package com.delecia.app;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.delecia.BuildConfig;
import com.delecia.R;
import com.delecia.retrofit.services.UserDataService;
import com.delecia.utils.Constants;
import com.delecia.utils.CustomLog;
import com.delecia.utils.SharedPreferencesData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sachin on 17/12/16.
 */

public class MyApplication extends Application {

    private static SharedPreferencesData sharedPreferencesData;
    private Retrofit retrofit;
    private static Context applicationContext;
    private static MyApplication instance;
    private static Context context;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeVariables();
        buildRetrofitClient();
        if (!BuildConfig.IS_PRODUCTION) {
            saveDataDb();
        }
    }

    public void saveDataDb() {
        CustomLog.d("MyApplication","SaveDB");
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//com.delecia//databases//" +
                        Constants.DB_NAME;
                String backupDBPath = "delecia_db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB)
                            .getChannel();
                    FileChannel dst = new FileOutputStream(backupDB)
                            .getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            CustomLog.e("MyApplication : ",e.getMessage());
        }
    }

    public void initializeVariables(){
        instance = this;
        sharedPreferencesData = new SharedPreferencesData(this);
        applicationContext = this.getApplicationContext();
    }

    private void buildRetrofitClient() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.HOST).build();
    }
    public UserDataService getUserDataService(){
        if(retrofit == null){
            buildRetrofitClient();
        }
        return retrofit.create(UserDataService.class);
    }

    public boolean isConnectedToInterNet() {
        ConnectivityManager connectivity = (ConnectivityManager) getCurrentActivityContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }
    public static Context getCurrentActivityContext() {
        if (context == null) {
            return applicationContext;
        } else {
            return context;
        }
    }
    public static void setCurrentActivityContext(Context mContext) {
        context = mContext;
    }

    public static synchronized MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


    public void showProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = new ProgressDialog(getCurrentActivityContext(), R.style
                .ProgressDialogStyle);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setCancelable(false);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * Hides progress bar
     */
    public void hideProgressDialog() {
        CustomLog.d("Progress","hide");
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Shows progress bar
     */
    public void showProgressDialog(String title, String description) {

        CustomLog.d("Progress","Show");
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = new ProgressDialog(getCurrentActivityContext(), R.style
                .ProgressDialogStyle);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(description);
        mProgressDialog.setCancelable(false);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }
    public void updateTextInProgressDialog(String text){
        try {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.setMessage(text);
            }
        } catch (Exception e) {
            CustomLog.e("Update Message Error",e.getMessage());
        }
    }
}
