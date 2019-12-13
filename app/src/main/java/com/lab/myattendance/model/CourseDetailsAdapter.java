package com.lab.myattendance.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.myattendance.R;
import com.lab.myattendance.datalayer.model.response.LectureResponse;
import com.lab.myattendance.datalayer.model.response.StudentCourseAttendance;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Course Lectures List adapter to update the list with the Lecture details, This adapter is used by the @StudentAttendance Activity
 */
public class CourseDetailsAdapter extends RecyclerView.Adapter<CourseDetailsAdapter.CourseViewHolder> {

    // Class fields
    private List<StudentCourseAttendance> mList = new ArrayList<>();

    // A Callback method called by the adapter to get the list row @R.layout.list_details_item
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_details_item, parent, false));
    }

    // A Callback method called by the adapter to set the data for the current List row
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        StudentCourseAttendance item = mList.get(position);

        if (item.getStrCheckIn() != null) {
            holder.checkIn.setText("Check in: " + item.getStrCheckIn());
        }
        if (item.getStrCheckOut() != null) {
            holder.checkOut.setText("Check out: " + item.getStrCheckOut());
        }
        if (item.getLectureName() != null) {
            holder.lectureName.setText("Lecture: " + item.getLectureName());
        }
    }

    // A Callback to get the list size
    @Override
    public int getItemCount() {
        return mList.size();
    }

    // Called by the @StudentAttendance activity after the viewModel update the UI with the returned result
    public void updateData(List<StudentCourseAttendance> lectureResponses) {
        mList.clear();
        mList.addAll(lectureResponses);
        notifyDataSetChanged();

    }

    // List viewHolder to reference every row in the listView
    class CourseViewHolder extends RecyclerView.ViewHolder {

        // class fields
        private TextView lectureName;
        private TextView checkIn;
        private TextView checkOut;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);

            lectureName = itemView.findViewById(R.id.lecture_name);
            checkIn = itemView.findViewById(R.id.tv_check_in);
            checkOut = itemView.findViewById(R.id.tv_check_out);
        }
    }
}