package com.lab.myattendance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lab.myattendance.R;
import com.lab.myattendance.datalayer.local.SharedPref;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lab.myattendance.activity.LoginActivity.LOGIN_STUDENT;


/**
 * This Activity provides two actions
 * 1- Login as A Student
 * 2- Login as A Lecturer
 * */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the user logged in before so we should navigate to the specified activity according to the saved login type else display this screen
        if (SharedPref.getInstance(this).isLoggedin()) {
            if (SharedPref.getInstance(this).getUserType() == LOGIN_STUDENT) {
                startActivity(new Intent(this, StudentActivity.class));
            } else {
                startActivity(new Intent(this, LecturerActivity.class));
            }
            finish();
        }

        // Use ButterKnife to get the view references from the layout
        ButterKnife.bind(this);
    }

    /**
     * navigate to the Lecturer Activity
     * */
    @OnClick(R.id.bt_lecturer)
    protected void onLecturerClicked() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.LOGIN_TYPE, LoginActivity.LOGIN_LECTURER);
        startActivity(intent);
        finish();
    }

    /**
     * navigate to the Student Activity
     * */
    @OnClick(R.id.bt_student)
    protected void onStudentClicked() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.LOGIN_TYPE, LOGIN_STUDENT);
        startActivity(intent);
        finish();
    }
}
