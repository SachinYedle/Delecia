package com.delecia.ui.activities;

import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delecia.R;
import com.delecia.app.MyApplication;
import com.delecia.interfaces.TaskCompletedListener;
import com.delecia.mappers.UserDataMapper;
import com.delecia.retrofit.responses.LoginResponse;
import com.delecia.retrofit.services.UserDataService;
import com.delecia.utils.CustomLog;
import com.delecia.utils.Navigator;
import com.delecia.utils.SharedPreferencesData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,TaskCompletedListener{

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
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    validateLogin();
                    return true;
                }
                return false;
            }
        });
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
                validateLogin();
                break;
            case R.id.register_textview:
                Navigator.navigateToRegistrationActivity(MyApplication.getCurrentActivityContext());
                break;
            case R.id.forgot_pwd_textview:
                Toast.makeText(MainActivity.this,"forget",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void validateLogin() {
        // Reset errors.
        phoneEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String phone = phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordEditText.setText("");
            passwordEditText.setError("Password (6-15 chars)");
            cancel = true;
        }

        // Check for a valid phone number.
        if (TextUtils.isEmpty(phone) || phone.length() != 10) {
            phoneEditText.setText("");
            phoneEditText.setError("enter valid phone number");
            cancel = true;
        }

        if (!cancel) {
            new UserDataMapper().login(this,phone,password);
            /*String deviceId = Settings.Secure.getString(MyApplication.getCurrentActivityContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //TODO replace with token returned from google after GCM registration
        String deviceToken = "123456";
        */
        }
    }

    @Override
    public void onTaskCompleted(LoginResponse loginResponse) {
        if (loginResponse != null) {
            if (loginResponse.getData().equals("Success")) {
                SharedPreferencesData preferences = new SharedPreferencesData(MyApplication.getCurrentActivityContext());
                preferences.setUserPhone(phoneEditText.getText().toString());
                finish();
                Navigator.navigateToHomeActivity(MyApplication.getCurrentActivityContext());
            } else if (loginResponse.getData().equals("failed")) {
                CustomLog.d("Response",loginResponse.getData()+" "+passwordEditText.getText().toString());
                passwordEditText.setText("");
                passwordEditText.setError("Password not Correct (6-15 chars)");
            } else {
                passwordEditText.setText("");
                phoneEditText.setError("Mobile not Registered");
            }
        }
    }

    @Override
    public void onTaskFailed(String request) {
        Toast.makeText(MyApplication.getCurrentActivityContext(),request,Toast.LENGTH_SHORT).show();
    }
}
