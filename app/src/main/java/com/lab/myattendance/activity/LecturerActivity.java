package com.lab.myattendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.lab.myattendance.R;
import com.lab.myattendance.datalayer.local.SharedPref;
import com.lab.myattendance.datalayer.model.request.CheckRequest;
import com.lab.myattendance.datalayer.model.response.LectureResponse;
import com.lab.myattendance.datalayer.model.response.LoginResponse;
import com.lab.myattendance.utilies.AppConstants;
import com.lab.myattendance.viewmodel.CoursesViewModel;
import com.lab.myattendance.viewmodel.StudentViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

import static com.lab.myattendance.activity.StudentActivity.CHECK_IN;
import static com.lab.myattendance.activity.StudentActivity.CHECK_OUT;


/**
 * This activity for the Lecturer and provides 3 actions to the Lecturer
 * 1- Call the Students API to get All the Students
 * 2- Call the Lectures API to get all the Lectures
 * 3- Add a student attendance
 * 4- Navigate to the QR Generate Activity
 * 5- Navigate to view all the Lecturer's Lectures
 */

public class LecturerActivity extends AppCompatActivity {


    // get references for the activity Fields to update them with the values, @BindView is one of the ButterKnife  annotations
    @BindView(R.id.sp_student)
    AppCompatSpinner spStudent;

    @BindView(R.id.sp_lecture)
    AppCompatSpinner spLectures;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // ViewModel Objects used to make the APIs call and update the UI with the response
    private StudentViewModel studentViewModel;
    private CoursesViewModel coursesViewModel;

    // Loader that appears when call the APIs
    private SpotsDialog spotsDialog;
    private List<LoginResponse> mStudentList;
    private List<LectureResponse> mLecturesList;
    private String lecturerName;
    private int selectedLecture = 0;
    private int selectedStudent = 0;

    private CheckRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer);

        // Initialize the ViewModel and the Loader
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        coursesViewModel = ViewModelProviders.of(this).get(CoursesViewModel.class);
        spotsDialog = new SpotsDialog(this, R.style.waiting_dialog);

        // get the User data which is saved previously after the first login and is saved in a SharedPreference file
        LoginResponse loginResponse = SharedPref.getInstance(this).getLoginModel();
        if (loginResponse != null) {
            lecturerName = loginResponse.getName() + " " + loginResponse.getLastName();
        }

        // Use ButterKnife to get the view references from the layout
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(lecturerName);

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
     * Listen for the ViewModel Methods to receive the response which will update the UI
     */

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

    private void initObserver() {

        // Add a callback for the viewModel to update the UI when it finished the API call
        coursesViewModel.getGetAllLecturesLiveData().observe(this, lectureResponses -> {

            spotsDialog.dismiss();
            this.mLecturesList = lectureResponses;
            updateLecturesSpinner();

        });

        // Add a callback for the viewModel to update the UI when it finished the API call
        studentViewModel.getGetAllStudentsLiveData().observe(this, loginResponses -> {
            this.mStudentList = loginResponses;
            updateStudentSpinner();

            coursesViewModel.getAllLectures();
        });

        // Make sure there is an internet connection to call the Check API
        if (AppConstants.isNetworkConnected(this)) {
            spotsDialog.show();

            // Call all the Student API
            studentViewModel.getAllStudents();
        } else {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Called after the viewModel got the response and update the UI with it
     */
    private void updateLecturesSpinner() {
        String[] lecturesName = new String[mLecturesList.size()];
        for (int i = 0; i < mLecturesList.size(); i++) {
            lecturesName[i] = mLecturesList.get(i).getTitle();
        }

        // Initialize an adapter for the lectures spinner
        ArrayAdapter<String> studentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lecturesName);
        studentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // add ItemSelectedListener to get the index of the selected item
        spLectures.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLecture = i;
                Log.v("postion", "+i");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // attach the adapter to the spinner
        spLectures.setAdapter(studentsAdapter);
    }

    /**
     * Called after the viewModel got the response and update the UI with it
     */
    private void updateStudentSpinner() {
        String[] studentsName = new String[mStudentList.size()];
        for (int i = 0; i < mStudentList.size(); i++) {
            studentsName[i] = mStudentList.get(i).getName() + " " + mStudentList.get(i).getLastName();
        }

        // Initialize an adapter for the Students spinner
        ArrayAdapter<String> studentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentsName);
        studentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // add ItemSelectedListener to get the index of the selected item
        spStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStudent = i;
                Log.v("postion", "+i");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // attach the adapter to the spinner
        spStudent.setAdapter(studentsAdapter);

    }

    /**
     * Called when press on the Generate Button
     */
    @OnClick(R.id.bt_generate_qr)
    protected void onGenerateClicked() {
        Intent intent = new Intent(this, GenerateQRActivity.class);
        startActivity(intent);
    }


    /**
     * Called when press on the ViewCourses Button
     */
    @OnClick(R.id.bt_view_courses)
    protected void onViewClicked() {
        Intent intent = new Intent(this, LecturerCoursesActivity.class);
        startActivity(intent);
    }

    /**
     * Called when press on the Add CheckIn  Button
     */
    @OnClick(R.id.bt_check_in)
    protected void onAddCheckInClicked() {

        // get the start time of the lecture
        LectureResponse.StartTime time = mLecturesList.get(selectedLecture).getStartTime();
        String timeStr = time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds();

        // create a check request to send to the Check API
        request = new CheckRequest();
        request.setStudentId(mStudentList.get(selectedStudent).getId());
        request.setLectureId(mLecturesList.get(selectedLecture).getId());
        request.setCheckType(CHECK_IN);
        request.setCheckTime(timeStr);

        callCheckApi();
    }


    /**
     * Called when press on the Add CheckOut  Button
     */
    @OnClick(R.id.bt_check_out)
    protected void onAddCheckOutClicked() {

        // get the end time of the lecture
        LectureResponse.EndTime time = mLecturesList.get(selectedLecture).getEndTime();
        String timeStr = time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds();


        // create a check request to send to the Check API
        request = new CheckRequest();
        request.setStudentId(mStudentList.get(selectedStudent).getId());
        request.setLectureId(mLecturesList.get(selectedLecture).getId());
        request.setCheckType(CHECK_OUT);
        request.setCheckTime(timeStr);

        callCheckApi();
    }


    private void callCheckApi() {
        // Make sure there is an internet connection to call the Check API
        if (AppConstants.isNetworkConnected(this)) {

            spotsDialog.show();

            // Add a callback for the viewModel to update the UI when it finished the API call
            studentViewModel.getCheckMutableLiveData().observe(this, response -> {
                Log.v("Success", " " + response);
                spotsDialog.dismiss();
                Toast.makeText(LecturerActivity.this, "Attendance added successfully", Toast.LENGTH_LONG).show();
            });

            // Call the Check API
            studentViewModel.addStudentCheck(request);
        } else {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }

    }

}
