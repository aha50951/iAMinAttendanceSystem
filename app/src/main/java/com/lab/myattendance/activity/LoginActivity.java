package com.lab.myattendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.lab.myattendance.R;
import com.lab.myattendance.datalayer.local.SharedPref;
import com.lab.myattendance.datalayer.model.response.LoginResponse;
import com.lab.myattendance.utilies.AppConstants;
import com.lab.myattendance.viewmodel.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

/**
 * This activity allow the user to login as A Student or login as A Lecturer
 */
public class LoginActivity extends AppCompatActivity {


    // Login types variables
    public static final String LOGIN_TYPE = "login_type";
    public static final int LOGIN_STUDENT = 1;
    public static final int LOGIN_LECTURER = 2;

    // get references for the activity Fields to update them with the values, @BindView is one of the ButterKnife  annotations
    @BindView(R.id.ed_user_name)
    protected EditText edUserName;

    @BindView(R.id.ed_password)
    protected EditText edPassword;

    private int login_type;
    private LoginViewModel loginViewModel;
    private SpotsDialog spotsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the ViewModel and the Loader
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        spotsDialog = new SpotsDialog(this, R.style.waiting_dialog);

        // Use ButterKnife to get the view references from the layout
        ButterKnife.bind(this);

        // get the selected login type from the login activity
        login_type = getIntent().getIntExtra(LOGIN_TYPE, 1);

        if (login_type == LOGIN_LECTURER) {
            edUserName.setHint(R.string.prompt_lecturer_id);
        } else {
            edUserName.setHint(R.string.prompt_student_id);
        }


    }

    /**
     * called when click on the login button
     */
    @OnClick(R.id.bt_signin)
    protected void onSignInClicked() {

        // Make sure there is an internet connection to call the Check API
        if (AppConstants.isNetworkConnected(this)) {

            // make sure the userName and the Password not empty to proceed
            if (!edUserName.getText().toString().isEmpty() && !edPassword.getText().toString().isEmpty()) {
                spotsDialog.show();

                // Add a callback for the viewModel to update the UI when fails after calling the API
                loginViewModel.getStringMutableLiveData().observe(this, error -> {
                    Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_LONG).show();
                    spotsDialog.dismiss();
                });

                // Add a callback for the viewModel to update the UI when it finished the API call
                loginViewModel.getLoginResponseMutableLiveData().observe(this, loginResponse -> {
                    spotsDialog.dismiss();
                    if (loginResponse.getId() != 0) {
                        updateUI(loginResponse);
                    } else {

                        Toast.makeText(LoginActivity.this, "Invalid user name or password!", Toast.LENGTH_LONG).show();
                    }

                });

                // Call the Login based on the login type
                if (login_type == LOGIN_STUDENT) {
                    loginViewModel.callStudentLogin(edUserName.getText().toString(), edPassword.getText().toString());
                } else {
                    loginViewModel.callLecturerLogin(edUserName.getText().toString(), edPassword.getText().toString());
                }

            } else {
                Toast.makeText(this, "Please enter valid Data!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Called after the viewModel got the response and update the UI with it
     */
    private void updateUI(LoginResponse loginResponse) {

        // save the login details so that in the next time it should navigate directly to the specified Activity
        SharedPref.getInstance(this).setLogin(true);
        SharedPref.getInstance(this).saveLoginModel(loginResponse);

        switch (login_type) {
            case LOGIN_STUDENT:
                // save the Login type "Student"
                SharedPref.getInstance(this).setUserType(LOGIN_STUDENT);
                startActivity(new Intent(this, StudentActivity.class));
                break;

            // save the Login type "Lecturer"
            case LOGIN_LECTURER:
                SharedPref.getInstance(this).setUserType(LOGIN_LECTURER);
                startActivity(new Intent(this, LecturerActivity.class));
                break;
        }
        finish();
    }
}
