package com.lab.myattendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lab.myattendance.R;
import com.lab.myattendance.datalayer.local.SharedPref;
import com.lab.myattendance.datalayer.model.request.CheckRequest;
import com.lab.myattendance.datalayer.model.response.LoginResponse;
import com.lab.myattendance.model.QRModel;
import com.lab.myattendance.utilies.AppConstants;
import com.lab.myattendance.utilies.EncryptionHelper;
import com.lab.myattendance.viewmodel.StudentViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * This class for the student and provides 3 actions to the student
 * 1- Check in the Lecture QR Image
 * 2- Check out the Lecture QR Image
 * 3- View the Student attendance
 *
 * @ ZXingScannerView.ResultHandler is used to Scan the QR image for the lecture and provide the scanned text, This text contains the lecture ID
 * and this ID is sent with the student ID to the API to register the CheckIn and the CheckOut
 */
public class StudentActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    // Check types
    public static final int CHECK_IN = 1;
    public static final int CHECK_OUT = 2;

    // This code is used when call the QR scanner
    private static final int MY_CAMERA_REQUEST_CODE = 10001;


    // get references for the activity Fields to update them with the values, @BindView is one of the ButterKnife  annotations
    @BindView(R.id.qrCodeScanner)
    protected ZXingScannerView qrCodeScanner;

    @BindView(R.id.tv_qr)
    protected TextView tvQRText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // By default the current Check type is CheckIn
    private int check_type = CHECK_IN;

    // Loader that appears when call the APIs
    private SpotsDialog spotsDialog;

    // ViewModel Object used to make the APIs call and update the UI with the response
    private StudentViewModel studentViewModel;

    private int studentID;
    private String studentName;

    // Date format
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        // Initialize the ViewModel and the Loader
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        spotsDialog = new SpotsDialog(this, R.style.waiting_dialog);

        // get the User data which is saved previously after the first login and is saved in a SharedPreference file
        LoginResponse loginResponse = SharedPref.getInstance(this).getLoginModel();
        if (loginResponse != null) {
            studentID = loginResponse.getId();
            studentName = loginResponse.getName() + " " + loginResponse.getLastName();
        }

        // Use ButterKnife to get the view references from the layout
        ButterKnife.bind(this);

        // set up a toolbar for the activity
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(studentName);


        setScannerProperties();

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

            // when select logout it should navigate to the main screen
            case R.id.menu_logout:
                navigateToMainActivity();
                return true;

            // when select toolbar back icon it should finish this activity and activate the previous one
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Clear the Login details and Navigate to the MainActivity to login with another user
     * */
    private void navigateToMainActivity() {
        SharedPref.getInstance(this).saveLoginModel(null);
        SharedPref.getInstance(this).setLogin(false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * add the QR scanner configurations
     */
    private void setScannerProperties() {
        List<BarcodeFormat> mList = new ArrayList<>();
        mList.add(BarcodeFormat.QR_CODE);

        qrCodeScanner.setFormats(mList);
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);
        if (Build.MANUFACTURER.equalsIgnoreCase("HUAWEI")) {
            qrCodeScanner.setAspectTolerance(0.5f);
        }
    }

    /**
     * stop the qr code camera scanner when activity is in the onPause state.
     */
    @Override
    protected void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();
    }

    /**
     * Called when press on the CheckIn button and start the Camera to scan the QR Image
     * */
    @OnClick(R.id.bt_check_in)
    protected void onCheckInClicked() {

        check_type = CHECK_IN;
        qrCodeScanner.stopCamera();
        startScan();
    }

    /**
     * Called when press on the CheckOut button and start the Camera to scan the QR Image
     * */
    @OnClick(R.id.bt_check_out)
    protected void onCheckOutClicked() {
        check_type = CHECK_OUT;
        qrCodeScanner.stopCamera();
        startScan();
    }

    /**
     * chek if the app has the permission to start the camera and open the camera to start the scanning using the scan lib,
     *
     * qrCodeScanner.setResultHandler(this) attach a call back to the scanner to get the scan text and in this case I add the current activity
     * as the callback for the Scanner so after the scanner finishes it send the update to this callback method @{handleResult(Result result)}
     * */
    private void startScan() {
        tvQRText.setText("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
                return;
            }
        }
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(this);
    }

    /**
     * Navigate to the Student History screen
     * */
    @OnClick(R.id.bt_history)
    protected void onHistoryClicked() {
        Intent intent = new Intent(this, StudentHistory.class);
        startActivity(intent);
    }

    /**
     * Called by the scanner after finishing the scan and sent the result through it
     * */
    @Override
    public void handleResult(Result result) {

        // Make sure there is an internet connection to call the Check API
        if (AppConstants.isNetworkConnected(this))
            if (result != null) {

                // the text received from the scanner is encrypted so we should decrypt it to get the data.
                String decryptedString = EncryptionHelper.getInstance().getDecryptionString(result.getText());

                // show the loader
                spotsDialog.show();

                // Parse the Scanner data in the @QRModel object
                QRModel model = new Gson().fromJson(decryptedString, QRModel.class);

                // Create the API request Object
                CheckRequest request = new CheckRequest();

                // format the date
                String formattedDate = df.format(Calendar.getInstance().getTime());

                // add the Request Object with the data
                request.setCheckTime(formattedDate);
                request.setCheckType(check_type);

                // this value @LectureID is gotten from the scanner data
                request.setLectureId(Integer.parseInt(model.getLectureId()));
                request.setStudentId(studentID);


                // Add a callback for the viewModel to update the UI when it finished the API call
                studentViewModel.getCheckMutableLiveData().observe(this, response -> {
                    // when the API call finished these actions should done
                    qrCodeScanner.stopCamera();
                    Toast.makeText(StudentActivity.this, "Checked Successfully", Toast.LENGTH_LONG).show();
                    spotsDialog.dismiss();
                });

                // Call the Student Check API
                studentViewModel.addStudentCheck(request);
                tvQRText.setText("Scanned Successfully");
                //decryptedString

            } else {
                Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            }


    }
}
