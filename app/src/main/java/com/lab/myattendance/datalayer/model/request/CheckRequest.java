package com.lab.myattendance.datalayer.model.request;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This is a request object used to send the CheckIn and CheckOut data sent to the API
 * */
public class CheckRequest {

    @SerializedName("StudentId")
    @Expose
    private Integer studentId;
    @SerializedName("LectureId")
    @Expose
    private Integer lectureId;
    @SerializedName("CheckType")
    @Expose
    private Integer checkType;
    @SerializedName("CheckTime")
    @Expose
    private String checkTime;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getLectureId() {
        return lectureId;
    }

    public void setLectureId(Integer lectureId) {
        this.lectureId = lectureId;
    }

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

}
