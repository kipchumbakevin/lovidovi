package com.kipchulovidovi.lovidovi.models;

public class SendCodeModel {
    public String phone;

    public SendCodeModel(String phone, String appSignature) {
        this.phone = phone;
        this.appSignature = appSignature;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone = phone_number;
    }

    public String getAppSignature() {
        return appSignature;
    }

    public void setAppSignature(String appSignature) {
        this.appSignature = appSignature;
    }

    public String appSignature;
}
