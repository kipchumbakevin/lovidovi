package com.kipchulovidovi.lovidovi.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessagesModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("receiver_id")
    @Expose
    private String receiverId;
    @SerializedName("group_id")
    @Expose
    private Integer groupId;
    @SerializedName("chat_id")
    @Expose
    private Integer chatId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("has_media")
    @Expose
    private Integer hasMedia;
    @SerializedName("receiver_read")
    @Expose
    private Integer receiverRead;
    @SerializedName("sender_delete")
    @Expose
    private Integer senderDelete;
    @SerializedName("receiver_delete")
    @Expose
    private Integer receiverDelete;
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

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getHasMedia() {
        return hasMedia;
    }

    public void setHasMedia(Integer hasMedia) {
        this.hasMedia = hasMedia;
    }

    public Integer getReceiverRead() {
        return receiverRead;
    }

    public void setReceiverRead(Integer receiverRead) {
        this.receiverRead = receiverRead;
    }

    public Integer getSenderDelete() {
        return senderDelete;
    }

    public void setSenderDelete(Integer senderDelete) {
        this.senderDelete = senderDelete;
    }

    public Integer getReceiverDelete() {
        return receiverDelete;
    }

    public void setReceiverDelete(Integer receiverDelete) {
        this.receiverDelete = receiverDelete;
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
