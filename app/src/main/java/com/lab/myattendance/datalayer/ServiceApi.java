package com.lab.myattendance.datalayer;


import com.lab.myattendance.datalayer.model.request.CheckRequest;
import com.lab.myattendance.datalayer.model.response.LectureResponse;
import com.lab.myattendance.datalayer.model.response.CoursesResponse;
import com.lab.myattendance.datalayer.model.response.LoginResponse;
import com.lab.myattendance.datalayer.model.response.StudentAttendance;
import com.lab.myattendance.datalayer.model.response.StudentCourseAttendance;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * This Interface collect all the API calls, It is implemented implicitly by the Retrofit Lib
 * */
public interface ServiceApi {

    @POST("Students/Login")
    Single<LoginResponse> callStudentLogin(@Query("UserName") String userName, @Query("Password") String password);

    @POST("Lecturer/Login")
    Single<LoginResponse> callLecturerLogin(@Query("UserName") String userName, @Query("Password") String password);

    @GET("Lecture/GetAllLectures")
    Single<List<LectureResponse>> getAllLectures();

    @GET("Students/GetAllStudents")
    Single<List<LoginResponse>> getAllStudents();

    @GET("Course/GetAllCourses")
    Single<List<CoursesResponse>> getAllCourses();

    @POST("Lecture/AssignStudentCheck")
    Single<Integer> assignStudentCheck(@Body CheckRequest request);

    @GET("Course/GetCoursesByStudentId")
    Single<List<CoursesResponse>> getCoursesByStudentID(@Query("Id") int studentId);

    @GET("Lecture/GetLecturesByCourseId")
    Single<List<LectureResponse>> getLecturesByCourseID(@Query("Id") int courseId);

    @GET("Students/GetStudentsAttendance")
    Single<StudentAttendance> getStudentAttendance(@Query("LectureId") int lectureId);

    @GET("Lecture/GetLecturesByLecturerId")
    Single<List<LectureResponse>> getLecturesByLecturerID(@Query("Id") int lecturerId);

    @GET("Students/GetStudentAttendedLectures")
    Single<List<StudentCourseAttendance>> getLecturesByCourseAndStudent(@Query("StID") int studentID, @Query("CrsId") int courseId);

}
