package com.rafiky.connect.model.requestmodel;
public class SignalModel {
    private String method;
    private String userName;
    private String userId;
    private Boolean video;
    private Boolean audio;
    private Boolean speakerMode;
    private Boolean raiseHanded;
    private String role;
    private Boolean speaker;
    private String language;
    private String fromLanguage;
    private Boolean isSigned;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Boolean getAudio() {
        return audio;
    }

    public void setAudio(Boolean audio) {
        this.audio = audio;
    }

    public Boolean getSpeakerMode() {
        return speakerMode;
    }

    public void setSpeakerMode(Boolean speakerMode) {
        this.speakerMode = speakerMode;
    }

    public Boolean getRaiseHanded() {
        return raiseHanded;
    }

    public void setRaiseHanded(Boolean raiseHanded) {
        this.raiseHanded = raiseHanded;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Boolean speaker) {
        this.speaker = speaker;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public Boolean getSigned() {
        return isSigned;
    }

    public void setSigned(Boolean signed) {
        isSigned = signed;
    }
}
