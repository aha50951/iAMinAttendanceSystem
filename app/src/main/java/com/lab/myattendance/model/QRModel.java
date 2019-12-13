package com.lab.myattendance.model;


/**
 * This class is used to parse the QR Scanner Result
 */
public class QRModel {

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    public QRModel(String lectureId, String courseId) {
        this.lectureId = lectureId;
        this.courseId = courseId;
    }

    private String lectureId;
    private String courseId;
}
