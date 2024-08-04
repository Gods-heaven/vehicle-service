package com.waypals.vehicle_service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;

import java.util.Date;

@Document(collection = "vehicleState")
@Data
public class VehicleState {

    @Id
    @Field("_id")
    private Long id;

    @Field("_class")
    private String _class;

    private Gps gps;
    private Obd obd;

    @Field("vehicleId")
    private Long vehicleId;

    private DateTime dateTime;
    private String vehicleMotionState;
    private boolean hidden;

    @Data
    public static class Gps {
        @Field("_id")
        private Long id;

        private GeoLocation geoLocation;
        private DateTime dateTime;
        private GpsSpeed gpsSpeed;
        private int satNum;
        private int satellite;

        @Data
        public static class GeoLocation {
            private Longitude longitude;
            private Latitude latitude;

            @Data
            public static class Longitude {
                private String longitude;
            }

            @Data
            public static class Latitude {
                private String latitude;
            }
        }

        @Data
        public static class GpsSpeed {
            private int speed;
            private String unit;
        }
    }

    @Data
    public static class Obd {
        @Field("_id")
        private Long id;

        private DateTime logTime;
        private ObdVehicleSpeed vehicleSpeed;
        private int engineState;
        private double obdRunningVoltage;

        @Data
        public static class ObdVehicleSpeed {
            private int speed;
            private String unit;
        }
    }

    @Data
    public static class DateTime {
        private Date date;
        @Field("dateInMillis")
        private Long dateInMillis;
    }
}

