package com.example.lovidovi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatsModel {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("owner_id")
    @Expose
    private Integer ownerId;
    @SerializedName("participant_id")
    @Expose
    private String participantId;
    @SerializedName("group")
    @Expose
    private Integer group;
    @SerializedName("owner_r")
    @Expose
    private Integer ownerR;
    @SerializedName("participant_r")
    @Expose
    private Integer participantR;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("participant")
    @Expose
    private Participant participant;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("receiver")
    @Expose
    private Receiver receiver;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public Integer getOwnerR() {
        return ownerR;
    }

    public void setOwnerR(Integer ownerR) {
        this.ownerR = ownerR;
    }

    public Integer getParticipantR() {
        return participantR;
    }

    public void setParticipantR(Integer participantR) {
        this.participantR = participantR;
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

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }
}
