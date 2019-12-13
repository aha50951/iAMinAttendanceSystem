package com.lab.myattendance.datalayer.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class holds the Attended  and Absence Students Data returned from the API
 * */
public class StudentAttendance {

    @SerializedName("AttendedStudents")
    @Expose
    private List<LoginResponse> attendedStudents = null;
    @SerializedName("UnAttendedStudents")
    @Expose
    private List<LoginResponse> unAttendedStudents = null;

    public List<LoginResponse> getAttendedStudents() {
        return attendedStudents;
    }

    public void setAttendedStudents(List<LoginResponse> attendedStudents) {
        this.attendedStudents = attendedStudents;
    }

    public List<LoginResponse> getUnAttendedStudents() {
        return unAttendedStudents;
    }

    public void setUnAttendedStudents(List<LoginResponse> unAttendedStudents) {
        this.unAttendedStudents = unAttendedStudents;
    }

}

