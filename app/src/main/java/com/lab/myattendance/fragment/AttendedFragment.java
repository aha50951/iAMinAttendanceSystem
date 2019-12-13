package com.lab.myattendance.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lab.myattendance.R;
import com.lab.myattendance.activity.LecturerCourseDetails;
import com.lab.myattendance.datalayer.model.response.LoginResponse;
import com.lab.myattendance.model.AttendAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 *
 * This Fragment used to display the Attended Students in a specific Lecture
 */
public class AttendedFragment extends Fragment {


    @BindView(R.id.rc_attend)
    protected RecyclerView recyclerView;

    private AttendAdapter adapter;
    private List<LoginResponse> mAttended;

    public AttendedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attended, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ButterKnife.bind(this, view);

        adapter = new AttendAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null)
            return;

        mAttended = ((LecturerCourseDetails) getActivity()).getmAttendance().getAttendedStudents();
        adapter.updateData(mAttended);
    }
}
