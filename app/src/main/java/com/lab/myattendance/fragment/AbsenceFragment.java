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
import com.lab.myattendance.model.CourseDetailsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * This Fragment used to display the Absence Students in a specific Lecture
 */
public class AbsenceFragment extends Fragment {


    @BindView(R.id.rc_absence)
    protected RecyclerView recyclerView;

    private AttendAdapter adapter;
    private List<LoginResponse> mAbsence;

    public AbsenceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_absence, container, false);
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

        mAbsence = ((LecturerCourseDetails) getActivity()).getmAttendance().getUnAttendedStudents();
        adapter.updateData(mAbsence);
    }
}
