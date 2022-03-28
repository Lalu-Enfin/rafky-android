package com.rafiky.connect.model.responsemodel;

import com.google.gson.annotations.SerializedName;

public class UserUpdateDetailsResponse {


    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("name")
        private String name;
        @SerializedName("email")
        private String email;
        @SerializedName("role")
        private String role;
        @SerializedName("start_time")
        private String startTime;
        @SerializedName("end_time")
        private String endTime;
        @SerializedName("language")
        private String language;
        @SerializedName("speaker_logo")
        private String speakerLogo;
        @SerializedName("speaker_name")
        private String speakerName;
        @SerializedName("speaker_role")
        private String speakerRole;
        @SerializedName("speaker_status")
        private boolean speakerStatus;
        @SerializedName("listener_status")
        private boolean listenerStatus;
        @SerializedName("back_end_user")
        private boolean backEndUser;
        @SerializedName("_id")
        private String id;
        @SerializedName("event_id")
        private String eventId;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("updatedAt")
        private String updatedAt;
        @SerializedName("__v")
        private int v;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getSpeakerLogo() {
            return speakerLogo;
        }

        public void setSpeakerLogo(String speakerLogo) {
            this.speakerLogo = speakerLogo;
        }

        public String getSpeakerName() {
            return speakerName;
        }

        public void setSpeakerName(String speakerName) {
            this.speakerName = speakerName;
        }

        public String getSpeakerRole() {
            return speakerRole;
        }

        public void setSpeakerRole(String speakerRole) {
            this.speakerRole = speakerRole;
        }

        public boolean isSpeakerStatus() {
            return speakerStatus;
        }

        public void setSpeakerStatus(boolean speakerStatus) {
            this.speakerStatus = speakerStatus;
        }

        public boolean isListenerStatus() {
            return listenerStatus;
        }

        public void setListenerStatus(boolean listenerStatus) {
            this.listenerStatus = listenerStatus;
        }

        public boolean isBackEndUser() {
            return backEndUser;
        }

        public void setBackEndUser(boolean backEndUser) {
            this.backEndUser = backEndUser;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
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

        public int getV() {
            return v;
        }

        public void setV(int v) {
            this.v = v;
        }
    }
}
