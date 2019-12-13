package com.lab.myattendance.datalayer.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * This class holds the Course Data returned from the API
 */
public class CoursesResponse {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("IsDeleted")
    @Expose
    private Boolean isDeleted;

    @SerializedName("Image")
    @Expose
    private String Image;


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
