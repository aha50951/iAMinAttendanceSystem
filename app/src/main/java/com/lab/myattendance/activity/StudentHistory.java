package com.lab.myattendance.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lab.myattendance.R;
import com.lab.myattendance.datalayer.local.SharedPref;
import com.lab.myattendance.datalayer.model.response.CoursesResponse;
import com.lab.myattendance.datalayer.model.response.LoginResponse;
import com.lab.myattendance.model.CourseAdapter;
import com.lab.myattendance.utilies.AppConstants;
import com.lab.myattendance.viewmodel.StudentViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;


/**
 * This Activity displays the courses attended by the Student and when press on any course it should navigate to the Course Lectures
 *
 * @CourseAdapter Is used to fill the RecyclerView with the data returned from the API
 */
public class StudentHistory extends AppCompatActivity {

    // get references for the activity Fields to update them with the values, @BindView is one of the ButterKnife  annotations
    @BindView(R.id.rc_history)
    protected RecyclerView recyclerView;

    @BindView(R.id.toolbar_history)
    protected Toolbar toolbar;

    // List adapter to fill it with the data
    private CourseAdapter adapter;

    // ViewModel Object used to make the APIs call and update the UI with the response
    private StudentViewModel studentViewModel;

    // Loader that appears when call the APIs
    private SpotsDialog spotsDialog;
    private Integer studentID;
    private String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_histroy);

        // Initialize the ViewModel and the Loader
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        spotsDialog = new SpotsDialog(this, R.style.waiting_dialog);

        // Use ButterKnife to get the view references from the layout
        ButterKnife.bind(this);


        // get the User data which is saved previously after the first login and is saved in a SharedPreference file
        LoginResponse loginResponse = SharedPref.getInstance(this).getLoginModel();
        if (loginResponse != null) {
            studentID = loginResponse.getId();
            studentName = loginResponse.getName() + " " + loginResponse.getLastName();
        }

        initUI();
        initObserver();
    }

    /**
     * Called to provide the menu for the toolbar and this menu contains the Logout Icon
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /**
     * Handle the toolbar items action [one for the logout and the other to return back ]
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_logout:
                navigateToMainActivity();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Clear the Login details and Navigate to the MainActivity to login with another user
     */
    private void navigateToMainActivity() {
        SharedPref.getInstance(this).saveLoginModel(null);
        SharedPref.getInstance(this).setLogin(false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Listen for the ViewModel Methods to receive the response which will update the UI
     */
    private void initObserver() {

        // Add a callback for the viewModel to update the UI when it finished the API call
        studentViewModel.getGetStudentCourses().observe(this, response -> {
            spotsDialog.dismiss();
            updateUI(response);
        });

        // Add a callback for the viewModel to update the UI when fails after calling the API
        studentViewModel.getStringMutableLiveData().observe(this, error -> {
            Toast.makeText(StudentHistory.this, R.string.error_login, Toast.LENGTH_LONG).show();
            spotsDialog.dismiss();
        });

        // Make sure there is an internet connection to call the Check API
        if (AppConstants.isNetworkConnected(this)) {
            spotsDialog.show();

            // Call the Student Courses API
            studentViewModel.getCoursesByStudentID(studentID);
        } else {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Called after the viewModel got the response and update the UI with it
     */
    private void updateUI(List<CoursesResponse> response) {

        spotsDialog.dismiss();
        adapter.updateItems(response);
    }

    /**
     * Setup the Toolbar and Initialize the Activity Fields
     */
    private void initUI() {

        // set up a toolbar for the activity
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(studentName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // SetUp the RecyclerView and Attach the adapter to it
        adapter = new CourseAdapter(this, CourseAdapter.LOGIN_STUDENT, studentName, studentID);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);
    }



}
