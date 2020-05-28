package com.example.lovidovi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikesCountModel {

    @SerializedName("likescount")
    @Expose
    private Integer likescount;

    public Integer getLikescount() {
        return likescount;
    }

    public void setLikescount(Integer likescount) {
        this.likescount = likescount;
    }
}
