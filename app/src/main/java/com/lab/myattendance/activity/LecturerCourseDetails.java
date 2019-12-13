package com.lab.myattendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.lab.myattendance.R;
import com.lab.myattendance.datalayer.local.SharedPref;
import com.lab.myattendance.datalayer.model.response.StudentAttendance;
import com.lab.myattendance.fragment.AbsenceFragment;
import com.lab.myattendance.fragment.AttendedFragment;
import com.lab.myattendance.utilies.AppConstants;
import com.lab.myattendance.viewmodel.CoursesViewModel;
import com.lab.myattendance.viewmodel.StudentViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;


/**
 * This Activity provides two tabs for the Attended student and the absence students in a specific lecture
 */
public class LecturerCourseDetails extends AppCompatActivity {


    public static final String LECTURE_ID = "lecture_id";
    public static final String LECTURE_NAME = "lecture_name";

    // get references for the activity Fields to update them with the values, @BindView is one of the ButterKnife  annotations
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.tabs)
    protected TabLayout tabLayout;

    @BindView(R.id.viewpager)
    protected ViewPager viewPager;

    private int lectureID;
    private String lectureName;

    // ViewModel Object used to make the APIs call and update the UI with the response

    private StudentViewModel studentViewModel;
    private SpotsDialog spotsDialog;

    // Tabs icons
    private int[] tabIcons = {
            R.drawable.ic_attend,
            R.drawable.ic_absence,
    };

    public StudentAttendance getmAttendance() {
        return mAttendance;
    }

    private StudentAttendance mAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_course_details);

        // Initialize the ViewModel and the Loader
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        spotsDialog = new SpotsDialog(this, R.style.waiting_dialog);

        // Use ButterKnife to get the view references from the layout
        ButterKnife.bind(this);

        // get these values form the previous activity
        lectureID = getIntent().getIntExtra(LECTURE_ID, 0);
        lectureName = getIntent().getStringExtra(LECTURE_NAME);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lecture: " + lectureName);

        loadLectureAttendance();
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
    private void loadLectureAttendance() {

        // Add a callback for the viewModel to update the UI when it finished the API call
        studentViewModel.getGetStudentAttendance().observe(this, response -> {
            spotsDialog.dismiss();
            mAttendance = response;

            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
        });

        // Add a callback for the viewModel to update the UI when fails after calling the API
        studentViewModel.getStringMutableLiveData().observe(this, error -> {
            Toast.makeText(LecturerCourseDetails.this, R.string.error_login, Toast.LENGTH_LONG).show();
            spotsDialog.dismiss();
        });

        // Make sure there is an internet connection to call the Check API
        if (AppConstants.isNetworkConnected(this)) {
            spotsDialog.show();

            // Call the Student Attendance API
            studentViewModel.getStudentAttendance(lectureID);
        } else {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Set the icons to the TabLayout
     * */
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    /**
     * Initialize the viewpager that holds the two fragments @AttendedFragment,@AbsenceFragment
     * */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AttendedFragment(), "Attended");
        adapter.addFragment(new AbsenceFragment(), "Absence");
        viewPager.setAdapter(adapter);
    }


    /**
     * ViewPager adapter
     * */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

