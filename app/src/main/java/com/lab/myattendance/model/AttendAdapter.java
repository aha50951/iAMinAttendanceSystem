package com.lab.myattendance.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.myattendance.R;
import com.lab.myattendance.datalayer.model.response.LoginResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Student Attendance List adapter to update the list with the Attendance details, This adapter is used by the @LecturerCourseDetails Activity
 */
public class AttendAdapter extends RecyclerView.Adapter<AttendAdapter.CourseViewHolder> {

    // Class fields

    private List<LoginResponse> mList = new ArrayList<>();

    // A Callback method called by the adapter to get the list row @R.layout.list_attend_item2
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_attend_item2, parent, false));
    }

    // A Callback method called by the adapter to set the data for the current List row

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        LoginResponse loginResponse = mList.get(position);
        if (loginResponse != null) {
            StringBuilder name = new StringBuilder();
            if (loginResponse.getName() != null) {
                name.append(loginResponse.getName());
            }
            if (loginResponse.getLastName() != null) {
                name.append(loginResponse.getLastName());
            }

            holder.name.setText(name.toString());

            if (loginResponse.getStrCheckIn() != null) {
                holder.checkin.setText(loginResponse.getStrCheckIn());
            }
            if (loginResponse.getStrCheckOut() != null) {
                holder.checkout.setText(loginResponse.getStrCheckOut());
            }
        }

    }

    // A Callback to get the list size
    @Override
    public int getItemCount() {
        return mList.size();
    }

    // Called by the @LecturerCourseDetails activity after the viewModel update the UI with the returned result
    public void updateData(List<LoginResponse> mAttended) {
        mList.clear();
        mList.addAll(mAttended);
        notifyDataSetChanged();
    }

    // List viewHolder to reference every row in the listView
    class CourseViewHolder extends RecyclerView.ViewHolder {

        // class fields
        private TextView name;
        private TextView checkin;
        private TextView checkout;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_student_name_value);
            checkin = itemView.findViewById(R.id.tv_check_in_value);
            checkout = itemView.findViewById(R.id.tv_check_out_value);
        }
    }
}