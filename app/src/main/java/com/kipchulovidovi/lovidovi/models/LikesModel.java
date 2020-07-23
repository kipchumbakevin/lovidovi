package com.kipchulovidovi.lovidovi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikesModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("like")
    @Expose
    private String like;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("quote_id")
    @Expose
    private Integer quoteId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
