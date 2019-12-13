package com.lab.myattendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lab.myattendance.R;
import com.lab.myattendance.datalayer.local.SharedPref;
import com.lab.myattendance.datalayer.model.response.LectureResponse;
import com.lab.myattendance.datalayer.model.response.StudentCourseAttendance;
import com.lab.myattendance.model.CourseDetailsAdapter;
import com.lab.myattendance.utilies.AppConstants;
import com.lab.myattendance.viewmodel.CoursesViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

/**
 * This Activity display the Student Attendance, It is navigated from @StudentHistory that displays the Student attended Courses and when press
 * on any course it opens this activity that display the lectures inside this course
 */
public class StudentAttendanceActivity extends AppCompatActivity {

    // These values are gotten from the previous activity @StudentHistory
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_NAME = "course_name";
    public static final String STUDENT_NAME = "student_name";
    public static final String STUDENT_ID = "student_id";

    // get references for the activity Fields to update them with the values, @BindView is one of the ButterKnife  annotations
    @BindView(R.id.rc_details)
    protected RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    // The recyclerView Adapter
    private CourseDetailsAdapter adapter;
    private int courseID;
    private String courseName;
    private String studentName;
    private int studentID;

    // ViewModel Object used to make the APIs call and update the UI with the response
    private CoursesViewModel coursesViewModel;

    // The loader that is displayed when start API calling
    private SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_details);

        // Initialize the ViewModel and the Loader
        coursesViewModel = ViewModelProviders.of(this).get(CoursesViewModel.class);
        spotsDialog = new SpotsDialog(this, R.style.waiting_dialog);

        // Use ButterKnife to get the view references from the layout
        ButterKnife.bind(this);

        // Parse the incoming intent
        parseIntent();

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
     * Parse the incoming intent from the StudentHistory Activity to get the Following data, {CourseID,StudentID} Are used to call the API
     * */
    private void parseIntent() {
        courseID = getIntent().getIntExtra(COURSE_ID, 0);
        courseName = getIntent().getStringExtra(COURSE_NAME);
        studentName = getIntent().getStringExtra(STUDENT_NAME);
        studentID = getIntent().getIntExtra(STUDENT_ID, 0);

        initUI();
    }

    /**
     * Setup the Toolbar and Initialize the Activity Fields
     */
    private void initUI() {

        // set up a toolbar for the activity
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Course: " + courseName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // SetUp the RecyclerView and Attach the adapter to it
        adapter = new CourseDetailsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        initObserver();
    }

    /**
     * Listen for the ViewModel Methods to receive the response which will update the UI
     */
    private void initObserver() {

        // Add a callback for the viewModel to update the UI when it finished the API call
        coursesViewModel.getGetStudentAttendanceLiveData().observe(this, lectureResponses -> {
            spotsDialog.dismiss();
            updateUI(lectureResponses);
        });

        // Add a callback for the viewModel to update the UI when fails after calling the API
        coursesViewModel.getStringMutableLiveData().observe(this, error -> {
            Toast.makeText(StudentAttendanceActivity.this, R.string.error_login, Toast.LENGTH_LONG).show();
            spotsDialog.dismiss();
        });

        // Make sure there is an internet connection to call the Check API
        if (AppConstants.isNetworkConnected(this)) {
            spotsDialog.show();
            // Call the Student Courses API
            coursesViewModel.getLecturesByCourseIDAndStudentID(studentID, courseID);
        } else {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Called after the viewModel got the response and update the UI with it
     */
    private void updateUI(List<StudentCourseAttendance> lectureResponses) {
        adapter.updateData(lectureResponses);
    }
}
