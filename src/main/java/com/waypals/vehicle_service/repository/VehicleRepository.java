package com.waypals.vehicle_service.repository;

import com.waypals.vehicle_service.entity.VehicleAggregate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends MongoRepository<VehicleAggregate,Long> {
    Optional<VehicleAggregate> findByVehicleId(Long vehicleId);
    @Query("{'vehicle.metadata.registrationNo': ?0}")
    VehicleAggregate findByRegistrationNumber(String registrationNumber);
}
