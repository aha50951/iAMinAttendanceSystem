package com.lab.myattendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lab.myattendance.R;
import com.lab.myattendance.datalayer.local.SharedPref;
import com.lab.myattendance.datalayer.model.response.CoursesResponse;
import com.lab.myattendance.datalayer.model.response.LectureResponse;
import com.lab.myattendance.datalayer.model.response.LoginResponse;
import com.lab.myattendance.model.QRModel;
import com.lab.myattendance.utilies.AppConstants;
import com.lab.myattendance.utilies.EncryptionHelper;
import com.lab.myattendance.utilies.QRCodeHelper;
import com.lab.myattendance.viewmodel.CoursesViewModel;
import com.lab.myattendance.viewmodel.StudentViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

/**
 * This activity used to generate the QR for a specific lecture and provides 3 actions to the student
 * 1- Call the Courses API to get All the courses
 * 2- Call the Lectures API to get all the Lectures in a specific course
 * 3- Call the QR scanner lib to generate a QR image based on the previous actions
 */

public class GenerateQRActivity extends AppCompatActivity {

    // get references for the activity Fields to update them with the values, @BindView is one of the ButterKnife  annotations
    @BindView(R.id.qrCodeImageView)
    protected ImageView imgQRCode;

    @BindView(R.id.sp_course)
    AppCompatSpinner spCourses;

    @BindView(R.id.sp_lecture)
    AppCompatSpinner spLectures;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // ViewModel Object used to make the APIs call and update the UI with the response
    private CoursesViewModel coursesViewModel;

    // Loader that appears when call the APIs
    private SpotsDialog spotsDialog;

    // Array to hold all the Courses
    private List<CoursesResponse> mCoursesList;

    // Array to hold all the Course Lectures
    private List<LectureResponse> mLecturesList;

    private int selectedLecture = 0;
    private int selectedCourse = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        // Initialize the ViewModel and the Loader
        coursesViewModel = ViewModelProviders.of(this).get(CoursesViewModel.class);
        spotsDialog = new SpotsDialog(this, R.style.waiting_dialog);

        // Use ButterKnife to get the view references from the layout
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Generate QR Code");

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
        coursesViewModel.getGetAllCoursesLiveData().observe(this, coursesResponse -> {
            if (coursesResponse != null) {
                spotsDialog.dismiss();
                this.mCoursesList = coursesResponse;
                updateCoursesSpinner();
                coursesViewModel.getGetAllCoursesLiveData().setValue(null);
            }
        });

        // Add a callback for the viewModel to update the UI when it finished the API call
        coursesViewModel.getGetCourseLecturesLiveData().observe(this, lecturesResponse -> {
            if (lecturesResponse != null) {
                spotsDialog.dismiss();
                this.mLecturesList = lecturesResponse;
                updateLecturesSpinner();
                coursesViewModel.getGetCourseLecturesLiveData().setValue(null);
            }
        });


        // Make sure there is an internet connection to call the Check API
        if (AppConstants.isNetworkConnected(this)) {
            spotsDialog.show();

            // Call the All the Courses API
            coursesViewModel.getAllCourses();
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

        // Initialize an adapter for the Lectures spinner
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
    private void updateCoursesSpinner() {
        String[] lecturesName = new String[mCoursesList.size()];
        for (int i = 0; i < mCoursesList.size(); i++) {
            lecturesName[i] = mCoursesList.get(i).getName();
        }

        // Initialize an adapter for the Courses spinner
        ArrayAdapter<String> studentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lecturesName);
        studentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // add ItemSelectedListener to get the index of the selected item
        spCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCourse = i;
                coursesViewModel.getLecturesByCourseID(mCoursesList.get(selectedCourse).getId());
                Log.v("postion", "+i");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // attach the adapter to the spinner
        spCourses.setAdapter(studentsAdapter);
    }

    /**
     * Called when press on the Generate Button
     */
    @OnClick(R.id.generateQrCodeButton)
    protected void onGenerateClicked() {

        // Create an instance from the QRModel class to hold the CourseID and LectureID
        QRModel model = new QRModel(String.valueOf(mLecturesList.get(selectedLecture).getId()), String.valueOf(mCoursesList.get(selectedCourse).getId()));

        // Convert the Object to a string to encrypt it
        String serializeString = new Gson().toJson(model);
        String encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();

        // initialize the QR image with the required configurations
        Bitmap bitmap = QRCodeHelper
                .newInstance(this)
                .setContent(encryptedString)
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2)
                .getQRCOde();
        imgQRCode.setImageBitmap(bitmap);
    }
}
