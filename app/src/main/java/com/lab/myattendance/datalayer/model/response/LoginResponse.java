package com.lab.myattendance.datalayer.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * This class holds the Student Data returned from the API
 */

public class LoginResponse {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("BirthDate")
    @Expose
    private String birthDate;
    @SerializedName("StrBirthDate")
    @Expose
    private String strBirthDate;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("LastName")
    @Expose
    private String lastName;

    @SerializedName("StrCheckIn")
    @Expose
    private String StrCheckIn;

    @SerializedName("StrCheckOut")
    @Expose
    private String StrCheckOut;


    public String getStrCheckIn() {
        return StrCheckIn;
    }

    public void setStrCheckIn(String strCheckIn) {
        StrCheckIn = strCheckIn;
    }

    public String getStrCheckOut() {
        return StrCheckOut;
    }

    public void setStrCheckOut(String strCheckOut) {
        StrCheckOut = strCheckOut;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getStrBirthDate() {
        return strBirthDate;
    }

    public void setStrBirthDate(String strBirthDate) {
        this.strBirthDate = strBirthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
