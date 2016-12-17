package com.delecia.ui.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delecia.R;
import com.delecia.app.MyApplication;
import com.delecia.mappers.UserDataMapper;
import com.delecia.retrofit.responses.LoginResponse;
import com.delecia.retrofit.services.UserDataService;
import com.delecia.utils.CustomLog;
import com.delecia.utils.Navigator;
import com.delecia.utils.SharedPreferencesData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText phoneEditText, passwordEditText;
    private TextView forgetPwdTextView, registerTextView;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.setCurrentActivityContext(MainActivity.this);

        initializeVariables();
        setListenerToViews();
    }

    private void initializeVariables(){
        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        phoneEditText = (EditText)findViewById(R.id.phone_editText);
        passwordEditText = (EditText)findViewById(R.id.password_editText);
        loginButton = (Button)findViewById(R.id.log_in_button);
        forgetPwdTextView = (TextView)findViewById(R.id.forgot_pwd_textview);
        registerTextView = (TextView)findViewById(R.id.register_textview);

    }
    private void setListenerToViews(){
        loginButton.setOnClickListener(this);
        forgetPwdTextView.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_in_button:
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                new UserDataMapper().login(loginListener,phone,password);
                break;
            case R.id.register_textview:
                Toast.makeText(MainActivity.this,"register",Toast.LENGTH_SHORT).show();
                break;
            case R.id.forgot_pwd_textview:
                Toast.makeText(MainActivity.this,"forget",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private UserDataMapper.OnLoginListener loginListener = new UserDataMapper.OnLoginListener() {

        @Override
        public void onTaskCompleted(LoginResponse loginResponse) {
            if (loginResponse != null) {
                if (loginResponse.getData().equals("Success")) {
                    SharedPreferencesData preferences = new SharedPreferencesData(MyApplication.getCurrentActivityContext());
                    preferences.setUserPhone(phoneEditText.getText().toString());
                        /*finish();
                        Navigator.getInstance().navigateToSearchActivity();*/
                } else if (loginResponse.getData().equals("failed")) {
                    passwordEditText.setError("Password not Correct (6-15 chars)");
                } else {
                    phoneEditText.setError("Mobile not Registered");
                }
            } else {
                CustomLog.e("mainActivity","Server Response null");
            }
        }

        @Override
        public void onTaskFailed(String failureString) {
            Toast.makeText(MyApplication.getCurrentActivityContext(),failureString,Toast.LENGTH_SHORT).show();
        }

    };
}
