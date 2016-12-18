package com.delecia.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delecia.R;
import com.delecia.app.MyApplication;
import com.delecia.interfaces.TaskCompletedListener;
import com.delecia.mappers.UserDataMapper;
import com.delecia.retrofit.responses.LoginResponse;
import com.delecia.utils.Navigator;
import com.delecia.utils.SharedPreferencesData;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, TaskCompletedListener {

    private EditText nameEditText,phoneEditText,emailEditText,passwdEditText,passwdConfEditText;
    private TextView loginTextView;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.setCurrentActivityContext(RegistrationActivity.this);
        setContentView(R.layout.activity_registration);

        initalizeViews();
        setListenersToViews();
    }

    private  void initalizeViews(){
        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.registration_form));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameEditText = (EditText)findViewById(R.id.registration_name_editText);
        phoneEditText = (EditText)findViewById(R.id.registration_phone_editText);
        emailEditText = (EditText)findViewById(R.id.registration_email_editText);
        passwdEditText = (EditText)findViewById(R.id.registration_password_editText);
        passwdConfEditText = (EditText)findViewById(R.id.registration_confirm_password_editText);
        signUpButton = (Button)findViewById(R.id.registration_sign_up_button);
        loginTextView = (TextView)findViewById(R.id.registration_login_textview);
    }
    private void setListenersToViews(){
        signUpButton.setOnClickListener(this);
        loginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registration_sign_up_button:
                validateInput();
                break;
            case R.id.registration_login_textview:
                Navigator.navigateToMainActivity(MyApplication.getCurrentActivityContext());
                break;
        }
    }

    private void validateInput() {
        // Reset errors.
        emailEditText.setError(null);
        passwdEditText.setError(null);

        // Store values at the time of the login attempt.
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwdEditText.getText().toString().trim();
        String confirmPassword = passwdConfEditText.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email Required");
            focusView = emailEditText;
            cancel = true;
        } else {
            if (!isEmailValid(email)) {
                emailEditText.setText("");
                emailEditText.setError("Email invalid");
                focusView = emailEditText;
                cancel = true;
            }
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwdEditText.setText("");
            passwdConfEditText.setText("");
            passwdEditText.setError("Password must be(6-15 chars)");
            focusView = passwdEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            passwdConfEditText.setText("");
            passwdConfEditText.setError("Passwords does not match");
            focusView = passwdConfEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(name) || name.length() < 1 || name.length() > 25) {
            nameEditText.setText("");
            nameEditText.setError("enter min(2) and max(25) chars");
            focusView = nameEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone) || phone.length() != 10) {
            phoneEditText.setText("");
            phoneEditText.setError("enter valid phone number");
            focusView = phoneEditText;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            new UserDataMapper().register(this,name,phone,email,password);
        }
    }

    private boolean isEmailValid(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onTaskCompleted(LoginResponse loginResponse) {
        if (loginResponse != null) {
            if (loginResponse.getData().equals("Added")) {
                Toast.makeText(MyApplication.getCurrentActivityContext(),"Registration Successfull",Toast.LENGTH_SHORT).show();
                finish();
                Navigator.navigateToMainActivity(MyApplication.getCurrentActivityContext());
            } else if (loginResponse.getData().equals("email")) {
                emailEditText.setError("Email Already Registered");
            } else {
                phoneEditText.setError("Mobile Already Registered");
            }
        }
    }

    @Override
    public void onTaskFailed(String request) {
        Toast.makeText(MyApplication.getCurrentActivityContext(),request,Toast.LENGTH_SHORT).show();
    }
}
