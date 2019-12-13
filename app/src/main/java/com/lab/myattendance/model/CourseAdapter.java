package com.lab.myattendance.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.myattendance.R;
import com.lab.myattendance.activity.LecturerCourseDetails;
import com.lab.myattendance.activity.StudentAttendanceActivity;
import com.lab.myattendance.datalayer.model.response.CoursesResponse;
import com.lab.myattendance.datalayer.model.response.LectureResponse;
import com.lab.myattendance.utilies.AppConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * This is the Course List adapter to update the list with the course details, This adapter is used by the @StudentHistory Activity
 * and LecturerCourses Activity
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.StudentViewHolder> {

    // Class fields


    // get an Activity reference to navigate to the lectures activity
    private final Activity mActivity;

    // the two login types which are used to determine which activity use this class
    public static final int LOGIN_STUDENT = 1;
    public static final int LOGIN_LECTURER = 2;

    private final String studentName;
    private final Integer studentID;

    private int mType;
    private List<CoursesResponse> mList = new ArrayList<>();
    private List<LectureResponse> mLecturesList = new ArrayList<>();


    public CourseAdapter(Activity activity, int type, String studentName, Integer studentID) {

        // initialize the class fields
        this.mActivity = activity;
        this.mType = type;
        this.studentName = studentName;
        this.studentID = studentID;
    }


    // A Callback method called by the adapter to get the list row @R.layout.list_item
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    // A Callback method called by the adapter to set the data for the current List row
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

        // If condition to determine which data should be used inCase Student we should use @mList and inCase Lecturer should use @mLecturesList
        if (mType == LOGIN_STUDENT) {
            holder.lectureName.setText(mList.get(position).getName());
            if (mList.get(position).getImage() != null) {
                byte[] b = AppConstants.fromStringToByte(mList.get(position).getImage());
                Drawable image = new BitmapDrawable(mActivity.getResources(), BitmapFactory.decodeByteArray(b, 0, b.length));
                holder.courseImage.setBackground(image);
            }

        } else {
            holder.lectureName.setText(mLecturesList.get(position).getTitle());
            holder.courseName.setText(mLecturesList.get(position).getCourseName());

            if (mLecturesList.get(position).getCourseImage() != null) {
                byte[] b = AppConstants.fromStringToByte(mLecturesList.get(position).getCourseImage());
                Drawable image = new BitmapDrawable(mActivity.getResources(), BitmapFactory.decodeByteArray(b, 0, b.length));
                holder.courseImage.setBackground(image);
            }
        }

    }

    // A Callback to get the list size
    @Override
    public int getItemCount() {
        return mList.size() == 0 ? mLecturesList.size() : mList.size();
    }


    // Called by the @StudentHistory activity after the viewModel update the UI with the returned result
    public void updateItems(List<CoursesResponse> response) {
        mList.clear();
        mList.addAll(response);
        notifyDataSetChanged();
    }

    // Called by the @LecturerCourses activity after the viewModel update the UI with the returned result
    public void updateLecturesItems(List<LectureResponse> lectureResponses) {
        mLecturesList.clear();
        mLecturesList.addAll(lectureResponses);
        notifyDataSetChanged();
    }

    // List viewHolder to reference every row in the listView
    class StudentViewHolder extends RecyclerView.ViewHolder {

        // class fields
        private TextView courseName;
        private TextView lectureName;
        private ImageView courseImage;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.tv_course_name);
            lectureName = itemView.findViewById(R.id.tv_lecture_name);

            courseImage = itemView.findViewById(R.id.img_course);


            // Add action for the List Row and based on the caller to this adapter determine what should happen
            itemView.setOnClickListener((View view) -> {
                Intent intent;

                // inCase the LecturerCourses Activity it should navigate to the @LecturerCourseDetails
                if (mType == LOGIN_LECTURER) {
                    intent = new Intent(mActivity, LecturerCourseDetails.class);
                    intent.putExtra(LecturerCourseDetails.LECTURE_ID, mLecturesList.get(getAdapterPosition()).getId());
                    intent.putExtra(LecturerCourseDetails.LECTURE_NAME, mLecturesList.get(getAdapterPosition()).getTitle());
                } else {
                    // inCase the StudentHistory Activity it should navigate to the @StudentAttendanceActivity
                    intent = new Intent(mActivity, StudentAttendanceActivity.class);
                    intent.putExtra(StudentAttendanceActivity.COURSE_ID, mList.get(getAdapterPosition()).getId());
                    intent.putExtra(StudentAttendanceActivity.STUDENT_ID, studentID);
                    intent.putExtra(StudentAttendanceActivity.STUDENT_NAME, studentName);
                    intent.putExtra(StudentAttendanceActivity.COURSE_NAME, mList.get(getAdapterPosition()).getName());
                }

                // start calling the destination activity
                mActivity.startActivity(intent);
            });
        }
    }

}
