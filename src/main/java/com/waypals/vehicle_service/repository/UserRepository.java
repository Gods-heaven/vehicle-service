package com.waypals.vehicle_service.repository;

import com.waypals.vehicle_service.entity.VehicleUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<VehicleUser, Long> {

    @Query("{ 'arms.vehicleId': ?0 }")
    List<VehicleUser> findByVehicleIdInArms(Long vehicleId);
}
