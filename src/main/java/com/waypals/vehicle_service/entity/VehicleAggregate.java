package com.waypals.vehicle_service.entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;
import java.util.List;

@Document(collection = "vehicle")
@Data
public class VehicleAggregate {

    @Id
    private String id;

    @Field("vehicleId")
    private long vehicleId;

    private Vehicle vehicle;

    @Field("isSystemFenceEnabled")
    private boolean isSystemFenceEnabled;

    private Registry registry;

    private String _class;
    @Data
    public static class Vehicle {
        private Long id;
        private Metadata metadata;
        private PurchasingDate purchasingDate;
        private boolean isDeactivated;
        private long groupId;
        private VehicleDriver vehicleDriver;
        private boolean isOwnerDriver;
        private List<String> vehicleImages;
        private Subscription subscription;

        @Data
        public static class Id {
            private long $numberLong;

        }
        @Data
        public static class Metadata {
            private String manufacturer;
            private ManufacturingDate manufacturingDate;
            private String vehicleName;
            private String registrationNo;
            private String fuelType;
            private String variant;
            private String vinNumber;
            private String chasisNumber;
            private String insuranceCmpName;
            private String policyNumber;
            private PolicyDate policyStartDate;
            private PolicyDate policyEndDate;
            private String engineNumber;
            private int seatingCapacity;
            private List<String> vehicleImages;
            private VehicleZone vehicleZone;
            private String vehicleType;


            @Data
            public static class ManufacturingDate {
                private Date date;
                private long dateInMillis;

            }
            @Data
            public static class PolicyDate {
                private Date date;
                private long dateInMillis;
            }
            @Data
            public static class VehicleZone {
                private String zone;
            }
        }
        @Data
        public static class PurchasingDate {
            private Date date;
            private long dateInMillis;
        }
        @Data
        public static class VehicleDriver {
            private String firstname;
            private String lastname;
            private String gender;
            private String licenseNumber;
            private LicenceExpiryDate licenceExpiryDate;
            private Location location;
            private int age;
            private DriverPhoneNumber driverPhoneNumber;
            @Data
            public static class LicenceExpiryDate {
                private Date date;
                private long dateInMillis;

            }
            @Data
            public static class Location {
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
            public static class DriverPhoneNumber {
                private String countryCode;
                private String driverPhoneNo;
            }
        }
        @Data
        public static class Subscription {
            private ServicePlan serviceplan;
            private SmsPlan smsPlan;
            private ServicePlanExpiry servicePlanExpiry;
            private boolean expired;
            @Data
            public static class ServicePlan {
                private String name;
                private int amount;
                private Validity validity;
                private long PalnId;
                private boolean isDefault;

                @Data
                public static class Validity {
                    private int value;
                    private String unit;
                }
            }
            @Data
            public static class SmsPlan {
                private int quantity;
                private String name;
                private int amount;
                private Validity validity;
                private boolean isDefault;

                @Data
                public static class Validity {
                    private int value;
                    private String unit;

                }
            }
            @Data
            public static class ServicePlanExpiry {
                private Date date;
                private long dateInMillis;
            }
        }
    }
    @Data
    public static class Registry {
        private SystemFenceRules systemFenceRules;
        private GeoFenceRules geoFenceRules;
        private OtherRules otherRules;

        @Data
        public static class SystemFenceRules {
            private ComplexKeys complexKeys;
            private Map map;
            @Data
            public static class ComplexKeys {
                private List<String> keySet;
            }
            @Data
            public static class Map {
            }
        }
        @Data
        public static class GeoFenceRules {
            private ComplexKeys complexKeys;
            private Map map;
            @Data
            public static class ComplexKeys {
                private List<String> keySet;
            }
            @Data
            public static class Map {
            }
        }
        @Data
        public static class OtherRules {
            private ComplexKeys complexKeys;
            private Map map;
            @Data
            public static class ComplexKeys {
                private List<String> keySet;

            }
            @Data
            public static class Map {
            }
        }
    }
}
