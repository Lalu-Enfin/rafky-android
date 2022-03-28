package com.rafiky.connect.model.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventResponseModelClass {


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
        @SerializedName("event_id")
        private String eventId;
        @SerializedName("event_name")
        private String eventName;
        @SerializedName("event_address")
        private String eventAddress;
        @SerializedName("event_description")
        private String eventDescription;
        @SerializedName("event_date")
        private String eventDate;
        @SerializedName("event_start_time")
        private String eventStartTime;
        @SerializedName("event_end_time")
        private String eventEndTime;
        @SerializedName("event_timezone")
        private String eventTimezone;
        @SerializedName("event_logo_image")
        private String eventLogoImage;
        @SerializedName("event_cover_image")
        private List<?> eventCoverImage;
        @SerializedName("languages")
        private List<LanguagesBean> languages;
        @SerializedName("rooms")
        private List<RoomsBean> rooms;

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getEventAddress() {
            return eventAddress;
        }

        public void setEventAddress(String eventAddress) {
            this.eventAddress = eventAddress;
        }

        public String getEventDescription() {
            return eventDescription;
        }

        public void setEventDescription(String eventDescription) {
            this.eventDescription = eventDescription;
        }

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public String getEventStartTime() {
            return eventStartTime;
        }

        public void setEventStartTime(String eventStartTime) {
            this.eventStartTime = eventStartTime;
        }

        public String getEventEndTime() {
            return eventEndTime;
        }

        public void setEventEndTime(String eventEndTime) {
            this.eventEndTime = eventEndTime;
        }

        public String getEventTimezone() {
            return eventTimezone;
        }

        public void setEventTimezone(String eventTimezone) {
            this.eventTimezone = eventTimezone;
        }

        public String getEventLogoImage() {
            return eventLogoImage;
        }

        public void setEventLogoImage(String eventLogoImage) {
            this.eventLogoImage = eventLogoImage;
        }

        public List<?> getEventCoverImage() {
            return eventCoverImage;
        }

        public void setEventCoverImage(List<?> eventCoverImage) {
            this.eventCoverImage = eventCoverImage;
        }

        public List<LanguagesBean> getLanguages() {
            return languages;
        }

        public void setLanguages(List<LanguagesBean> languages) {
            this.languages = languages;
        }

        public List<RoomsBean> getRooms() {
            return rooms;
        }

        public void setRooms(List<RoomsBean> rooms) {
            this.rooms = rooms;
        }

        public static class LanguagesBean {
            @SerializedName("name")
            private String name;
            @SerializedName("id")
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public static class RoomsBean {
            @SerializedName("room_name")
            private String roomName;
            @SerializedName("room_id")
            private String roomId;
            @SerializedName("sessions")
            private List<SessionsBean> sessions;

            public String getRoomName() {
                return roomName;
            }

            public void setRoomName(String roomName) {
                this.roomName = roomName;
            }

            public String getRoomId() {
                return roomId;
            }

            public void setRoomId(String roomId) {
                this.roomId = roomId;
            }

            public List<SessionsBean> getSessions() {
                return sessions;
            }

            public void setSessions(List<SessionsBean> sessions) {
                this.sessions = sessions;
            }

            public static class SessionsBean {
                @SerializedName("opentok_session_id")
                private String opentokSessionId;
                @SerializedName("session_id")
                private String sessionId;
                @SerializedName("session_name")
                private String sessionName;
                @SerializedName("session_date")
                private String sessionDate;
                @SerializedName("session_start_time")
                private String sessionStartTime;
                @SerializedName("session_end_time")
                private String sessionEndTime;
                @SerializedName("session_private")
                private boolean sessionPrivate;
                @SerializedName("session_floor_title")
                private String sessionFloorTitle;
                @SerializedName("opentok_publisher_token")
                private String opentokPublisherToken;
                @SerializedName("opentok_listener_token")
                private String opentokListenerToken;

                public String getOpentokSessionId() {
                    return opentokSessionId;
                }

                public void setOpentokSessionId(String opentokSessionId) {
                    this.opentokSessionId = opentokSessionId;
                }

                public String getSessionId() {
                    return sessionId;
                }

                public void setSessionId(String sessionId) {
                    this.sessionId = sessionId;
                }

                public String getSessionName() {
                    return sessionName;
                }

                public void setSessionName(String sessionName) {
                    this.sessionName = sessionName;
                }

                public String getSessionDate() {
                    return sessionDate;
                }

                public void setSessionDate(String sessionDate) {
                    this.sessionDate = sessionDate;
                }

                public String getSessionStartTime() {
                    return sessionStartTime;
                }

                public void setSessionStartTime(String sessionStartTime) {
                    this.sessionStartTime = sessionStartTime;
                }

                public String getSessionEndTime() {
                    return sessionEndTime;
                }

                public void setSessionEndTime(String sessionEndTime) {
                    this.sessionEndTime = sessionEndTime;
                }

                public boolean isSessionPrivate() {
                    return sessionPrivate;
                }

                public void setSessionPrivate(boolean sessionPrivate) {
                    this.sessionPrivate = sessionPrivate;
                }

                public String getSessionFloorTitle() {
                    return sessionFloorTitle;
                }

                public void setSessionFloorTitle(String sessionFloorTitle) {
                    this.sessionFloorTitle = sessionFloorTitle;
                }

                public String getOpentokPublisherToken() {
                    return opentokPublisherToken;
                }

                public void setOpentokPublisherToken(String opentokPublisherToken) {
                    this.opentokPublisherToken = opentokPublisherToken;
                }

                public String getOpentokListenerToken() {
                    return opentokListenerToken;
                }

                public void setOpentokListenerToken(String opentokListenerToken) {
                    this.opentokListenerToken = opentokListenerToken;
                }
            }
        }
    }
}
