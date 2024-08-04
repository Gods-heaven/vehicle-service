package com.waypals.vehicle_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document("user")
public class VehicleUser {
    @Id
    private Long id;

    private FriendsData friendsData;
    private Long userId;
    private User user;
    private List<Arm> arms;


    @Data
    public static class FriendsData {
        private List<String> listAcceptedFriends;
        private List<String> listAcceptedFriendsRequestSentByMe;
        private List<String> listRejectedFriends;
        private List<String> listPendingFriends;
        private List<String> listFriendRequestSentByMe;


    }
    @Data
    public static class User {
        private Long id;
        private boolean owner;
        private Email email;
        private PhoneNumber phoneNumber;
        private Name name;
        private String gender;
        private Address address;
        private boolean isDeactivated;
        private DateOfBirth dateOfBirth;
        private Long createdBy;
        private NotificationType notificationType;
        private int numberOfSmsNotificationReceived;
        private String licenseNumber;
        private LicenceExpiryDate licenceExpiryDate;
        private boolean passwordChanged;
        private List<String> userPushWrapper;


        @Data
        public static class Email {
            private String email;


        }
        @Data
        public static class PhoneNumber {
            private String countryCode;
            private String phoneNo;


        }
        @Data
        public static class Name {
            private String firstName;
            private String lastName;


        }
        @Data
        public static class Address {
            private String addressLine;
            private Country country;
            private Province province;
            private City city;


            @Data
            public static class Country {
                private String name;


            }
            @Data
            public static class Province {
                private String name;


            }
            @Data
            public static class City {
                private String name;
                private Zip zip;


                @Data
                public static class Zip {
                    private String code;


                }
            }
        }
        @Data
        public static class DateOfBirth {
            private Date date;
            private Long dateInMillis;


        }
        @Data
        public static class NotificationType {
            private boolean mailNotification;
            private boolean smsNotification;
            private boolean pushNotification;


        }
        @Data
        public static class LicenceExpiryDate {
            private Date date;
            private Long dateInMillis;


        }
    }
    @Data
    public static class Arm {
        private Long id;
        private Long userId;
        private Long deviceId;
        private Long vehicleId;
        private boolean isDeactivated;

    }

}
