package com.waypals.vehicle_service.repository;

import com.waypals.vehicle_service.entity.VehicleState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface VehicleStateRepository extends MongoRepository<VehicleState,Long> {
    Optional<VehicleState> findTopByVehicleIdOrderByDateTimeDateInMillisDesc(Long vehicleId);
    boolean existsByVehicleIdAndDateTimeDateInMillisBetween(Long vehicleId, long epochMilli, long epochMilli1);

}
