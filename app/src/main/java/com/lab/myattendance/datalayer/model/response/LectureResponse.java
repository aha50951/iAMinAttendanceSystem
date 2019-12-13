package com.lab.myattendance.datalayer.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class holds the Lecture Data returned from the API
 * */

public class LectureResponse {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("CourseId")
    @Expose
    private Integer courseId;
    @SerializedName("LecturerId")
    @Expose
    private Integer lecturerId;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("StrDate")
    @Expose
    private String strDate;
    @SerializedName("StartTime")
    @Expose
    private StartTime startTime;
    @SerializedName("StrStartTime")
    @Expose
    private String strStartTime;
    @SerializedName("EndTime")
    @Expose
    private EndTime endTime;
    @SerializedName("StrEndTime")
    @Expose
    private String strEndTime;
    @SerializedName("CourseName")
    @Expose
    private String courseName;

    @SerializedName("CourseImage")
    @Expose
    private String CourseImage;

    @SerializedName("LecturerName")
    @Expose
    private String lecturerName;
    @SerializedName("IsDeleted")
    @Expose
    private Boolean isDeleted;


    public String getCourseImage() {
        return CourseImage;
    }

    public void setCourseImage(String courseImage) {
        CourseImage = courseImage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public StartTime getStartTime() {
        return startTime;
    }

    public void setStartTime(StartTime startTime) {
        this.startTime = startTime;
    }

    public String getStrStartTime() {
        return strStartTime;
    }

    public void setStrStartTime(String strStartTime) {
        this.strStartTime = strStartTime;
    }

    public EndTime getEndTime() {
        return endTime;
    }

    public void setEndTime(EndTime endTime) {
        this.endTime = endTime;
    }

    public String getStrEndTime() {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime) {
        this.strEndTime = strEndTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public class EndTime {

        @SerializedName("Hours")
        @Expose
        private Integer hours;
        @SerializedName("Minutes")
        @Expose
        private Integer minutes;
        @SerializedName("Seconds")
        @Expose
        private Integer seconds;
        @SerializedName("Milliseconds")
        @Expose
        private long milliseconds;
        @SerializedName("Ticks")
        @Expose
        private long ticks;
        @SerializedName("Days")
        @Expose
        private Integer days;
        @SerializedName("TotalDays")
        @Expose
        private Double totalDays;
        @SerializedName("TotalHours")
        @Expose
        private Double totalHours;
        @SerializedName("TotalMilliseconds")
        @Expose
        private long totalMilliseconds;
        @SerializedName("TotalMinutes")
        @Expose
        private Double totalMinutes;
        @SerializedName("TotalSeconds")
        @Expose
        private Double totalSeconds;

        public Integer getHours() {
            return hours;
        }

        public void setHours(Integer hours) {
            this.hours = hours;
        }

        public Integer getMinutes() {
            return minutes;
        }

        public void setMinutes(Integer minutes) {
            this.minutes = minutes;
        }

        public Integer getSeconds() {
            return seconds;
        }

        public void setSeconds(Integer seconds) {
            this.seconds = seconds;
        }

        public long getMilliseconds() {
            return milliseconds;
        }

        public void setMilliseconds(long milliseconds) {
            this.milliseconds = milliseconds;
        }

        public long getTicks() {
            return ticks;
        }

        public void setTicks(long ticks) {
            this.ticks = ticks;
        }

        public Integer getDays() {
            return days;
        }

        public void setDays(Integer days) {
            this.days = days;
        }

        public Double getTotalDays() {
            return totalDays;
        }

        public void setTotalDays(Double totalDays) {
            this.totalDays = totalDays;
        }

        public Double getTotalHours() {
            return totalHours;
        }

        public void setTotalHours(Double totalHours) {
            this.totalHours = totalHours;
        }

        public long getTotalMilliseconds() {
            return totalMilliseconds;
        }

        public void setTotalMilliseconds(long totalMilliseconds) {
            this.totalMilliseconds = totalMilliseconds;
        }

        public Double getTotalMinutes() {
            return totalMinutes;
        }

        public void setTotalMinutes(Double totalMinutes) {
            this.totalMinutes = totalMinutes;
        }

        public Double getTotalSeconds() {
            return totalSeconds;
        }

        public void setTotalSeconds(Double totalSeconds) {
            this.totalSeconds = totalSeconds;
        }

    }

    public class StartTime {

        @SerializedName("Ticks")
        @Expose
        private long ticks;
        @SerializedName("Days")
        @Expose
        private Integer days;
        @SerializedName("Hours")
        @Expose
        private Integer hours;
        @SerializedName("Milliseconds")
        @Expose
        private long milliseconds;
        @SerializedName("Minutes")
        @Expose
        private Integer minutes;
        @SerializedName("Seconds")
        @Expose
        private Integer seconds;
        @SerializedName("TotalDays")
        @Expose
        private Double totalDays;
        @SerializedName("TotalHours")
        @Expose
        private Double totalHours;
        @SerializedName("TotalMilliseconds")
        @Expose
        private long totalMilliseconds;
        @SerializedName("TotalMinutes")
        @Expose
        private Double totalMinutes;
        @SerializedName("TotalSeconds")
        @Expose
        private Double totalSeconds;

        public long getTicks() {
            return ticks;
        }

        public void setTicks(long ticks) {
            this.ticks = ticks;
        }

        public Integer getDays() {
            return days;
        }

        public void setDays(Integer days) {
            this.days = days;
        }

        public Integer getHours() {
            return hours;
        }

        public void setHours(Integer hours) {
            this.hours = hours;
        }

        public long getMilliseconds() {
            return milliseconds;
        }

        public void setMilliseconds(long milliseconds) {
            this.milliseconds = milliseconds;
        }

        public Integer getMinutes() {
            return minutes;
        }

        public void setMinutes(Integer minutes) {
            this.minutes = minutes;
        }

        public Integer getSeconds() {
            return seconds;
        }

        public void setSeconds(Integer seconds) {
            this.seconds = seconds;
        }

        public Double getTotalDays() {
            return totalDays;
        }

        public void setTotalDays(Double totalDays) {
            this.totalDays = totalDays;
        }

        public Double getTotalHours() {
            return totalHours;
        }

        public void setTotalHours(Double totalHours) {
            this.totalHours = totalHours;
        }

        public long getTotalMilliseconds() {
            return totalMilliseconds;
        }

        public void setTotalMilliseconds(long totalMilliseconds) {
            this.totalMilliseconds = totalMilliseconds;
        }

        public Double getTotalMinutes() {
            return totalMinutes;
        }

        public void setTotalMinutes(Double totalMinutes) {
            this.totalMinutes = totalMinutes;
        }

        public Double getTotalSeconds() {
            return totalSeconds;
        }

        public void setTotalSeconds(Double totalSeconds) {
            this.totalSeconds = totalSeconds;
        }
    }
}
