package com.lab.myattendance.datalayer.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * This class holds the Student attended courses data returned from the API
 * */
public class StudentCourseAttendance {

    @SerializedName("StudentId")
    @Expose
    private Integer studentId;
    @SerializedName("LectureId")
    @Expose
    private Integer lectureId;
    @SerializedName("CheckIn")
    @Expose
    private Object checkIn;
    @SerializedName("CheckOut")
    @Expose
    private Object checkOut;
    @SerializedName("CheckType")
    @Expose
    private Object checkType;
    @SerializedName("CheckTime")
    @Expose
    private Object checkTime;
    @SerializedName("LectureName")
    @Expose
    private String lectureName;
    @SerializedName("StudentName")
    @Expose
    private String studentName;
    @SerializedName("StrCheckIn")
    @Expose
    private String strCheckIn;
    @SerializedName("StrCheckOut")
    @Expose
    private String strCheckOut;

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

    public Object getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Object checkIn) {
        this.checkIn = checkIn;
    }

    public Object getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Object checkOut) {
        this.checkOut = checkOut;
    }

    public Object getCheckType() {
        return checkType;
    }

    public void setCheckType(Object checkType) {
        this.checkType = checkType;
    }

    public Object getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Object checkTime) {
        this.checkTime = checkTime;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStrCheckIn() {
        return strCheckIn;
    }

    public void setStrCheckIn(String strCheckIn) {
        this.strCheckIn = strCheckIn;
    }

    public String getStrCheckOut() {
        return strCheckOut;
    }

    public void setStrCheckOut(String strCheckOut) {
        this.strCheckOut = strCheckOut;
    }

}
