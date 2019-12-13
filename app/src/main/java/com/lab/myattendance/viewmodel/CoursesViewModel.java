package com.lab.myattendance.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.lab.myattendance.datalayer.model.response.CoursesResponse;
import com.lab.myattendance.datalayer.model.response.LectureResponse;
import com.lab.myattendance.datalayer.model.response.StudentCourseAttendance;
import com.lab.myattendance.datalayer.remote.ServiceGenerator;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * CoursesViewModel Class that is used to call these APIs
 * 1- getAllLectures
 * 2- getAllCourses
 * 3- getLecturesByCourseID
 * 4- getLecturesByCourseIDAndStudentID
 */

public class CoursesViewModel extends BaseViewModel {

    public MutableLiveData<List<LectureResponse>> getGetAllLecturesLiveData() {
        return getAllLecturesLiveData;
    }

    public MutableLiveData<List<LectureResponse>> getGetLecturerLecturesLiveData() {
        return getLecturerLecturesLiveData;
    }

    public MutableLiveData<List<LectureResponse>> getGetCourseLecturesLiveData() {
        return getCourseLecturesLiveData;
    }


    public MutableLiveData<List<CoursesResponse>> getGetAllCoursesLiveData() {
        return getAllCoursesLiveData;
    }

    /**
     * LiveData Objects used to send the data to the activity to update the UI
     */
    private MutableLiveData<List<LectureResponse>> getAllLecturesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<LectureResponse>> getLecturerLecturesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<LectureResponse>> getCourseLecturesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<CoursesResponse>> getAllCoursesLiveData = new MutableLiveData<>();

    public MutableLiveData<List<StudentCourseAttendance>> getGetStudentAttendanceLiveData() {
        return getStudentAttendanceLiveData;
    }

    private MutableLiveData<List<StudentCourseAttendance>> getStudentAttendanceLiveData = new MutableLiveData<>();

    /**
     * Called in the Lecturer Activity to select any lecture and assign a student to it
     */
    public void getAllLectures() {
        ServiceGenerator.getInstance().create().getAllLectures()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::lecturesSuccess, this::error);
    }

    /**
     * Called in the GenerateQR activity to get All the Courses
     */
    public void getAllCourses() {
        ServiceGenerator.getInstance().create().getAllCourses()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::CoursesSuccess, this::error);
    }


    /**
     * Called in the GenerateQR activity to get All the Course Lectures by @CourseID, it is called after getting all the courses
     */
    public void getLecturesByCourseID(int courseId) {
        ServiceGenerator.getInstance().create().getLecturesByCourseID(courseId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::courseLectures, this::error);
    }

    /**
     * Called in the StudentAttendance Activity to get all the lectures attended by a student in a specific Course using @StudentID and @CourseID
     */
    public void getLecturesByCourseIDAndStudentID(int studentID, int courseId) {
        ServiceGenerator.getInstance().create().getLecturesByCourseAndStudent(studentID, courseId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::studentAttendance, this::error);
    }

    private void studentAttendance(List<StudentCourseAttendance> studentCourseAttendances) {
        getStudentAttendanceLiveData.setValue(studentCourseAttendances);
    }

    /**
     * Called in the LecturerCourses Activity to get the Lecturer Lectures by the @LecturerID
     */
    public void getLecturesByLecturerID(int lecturerId) {
        ServiceGenerator.getInstance().create().getLecturesByLecturerID(lecturerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::lecturerLectures, this::error);
    }


    /**
     * These are the LiveData Objects that is listened by the activity to update the Activity UI, Here we set the value to these Objects
     */
    private void CoursesSuccess(List<CoursesResponse> coursesResponses) {
        getAllCoursesLiveData.setValue(coursesResponses);
    }

    private void courseLectures(List<LectureResponse> lectureResponse) {
        getCourseLecturesLiveData.setValue(lectureResponse);
    }

    private void lecturerLectures(List<LectureResponse> lectureResponses) {
        getLecturerLecturesLiveData.setValue(lectureResponses);
    }

    private void lecturesSuccess(List<LectureResponse> lectureResponses) {
        getAllLecturesLiveData.setValue(lectureResponses);
    }

    private void error(Throwable throwable) {
        stringMutableLiveData.setValue(throwable.getMessage());
    }
}
