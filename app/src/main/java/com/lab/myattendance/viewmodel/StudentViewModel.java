package com.lab.myattendance.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.lab.myattendance.datalayer.model.request.CheckRequest;
import com.lab.myattendance.datalayer.model.response.CoursesResponse;
import com.lab.myattendance.datalayer.model.response.LoginResponse;
import com.lab.myattendance.datalayer.model.response.StudentAttendance;
import com.lab.myattendance.datalayer.remote.ServiceGenerator;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * CoursesViewModel Class that is used to call these APIs
 * 1- addStudentCheck
 * 2- getAllStudents
 * 3- getStudentAttendance
 * 4- getCoursesByStudentID
 */

public class StudentViewModel extends BaseViewModel {

    /**
     * LiveData Objects used to send the data to the activity to update the UI
     */

    private MutableLiveData<List<LoginResponse>> getAllStudentsLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> checkMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<StudentAttendance> getStudentAttendance = new MutableLiveData<>();
    private MutableLiveData<List<CoursesResponse>> getStudentCourses = new MutableLiveData<>();


    public MutableLiveData<List<CoursesResponse>> getGetStudentCourses() {
        return getStudentCourses;
    }

    public MutableLiveData<StudentAttendance> getGetStudentAttendance() {
        return getStudentAttendance;
    }

    public MutableLiveData<Integer> getCheckMutableLiveData() {
        return checkMutableLiveData;
    }

    public MutableLiveData<List<LoginResponse>> getGetAllStudentsLiveData() {
        return getAllStudentsLiveData;
    }


    /**
     * Called in the LecturerActivity and the StudentActivity to add the student CheckIn and CheckOut
     */
    public void addStudentCheck(CheckRequest request) {

        ServiceGenerator.getInstance().create().assignStudentCheck(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::checkSuccess, this::error);

    }


    /**
     * Called in the @LecturerActivity to get All the Students and can select one of them to add attendance to him
     */
    public void getAllStudents() {
        ServiceGenerator.getInstance().create().getAllStudents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::studentSuccess, this::error);
    }


    /**
     * Called in the @LecturerCourseDetails activity to get all the students attended a specific Lecture
     */

    public void getStudentAttendance(int lectureId) {
        ServiceGenerator.getInstance().create().getStudentAttendance(lectureId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::studentAttendance, this::error);

    }


    /**
     * Called in the @StudentHistory activity to get All the Course by @StudentID
     */
    public void getCoursesByStudentID(int studentId) {
        ServiceGenerator.getInstance().create().getCoursesByStudentID(studentId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::studentCourses, this::error);
    }

    private void studentCourses(List<CoursesResponse> coursesResponse) {
        getStudentCourses.setValue(coursesResponse);
    }

    private void studentAttendance(StudentAttendance studentAttendance) {
        getStudentAttendance.setValue(studentAttendance);
    }

    private void studentSuccess(List<LoginResponse> loginResponses) {
        getAllStudentsLiveData.setValue(loginResponses);
    }

    private void error(Throwable throwable) {
        stringMutableLiveData.setValue(throwable.getMessage());
    }

    private void checkSuccess(Integer response) {
        checkMutableLiveData.setValue(response);
    }
}
