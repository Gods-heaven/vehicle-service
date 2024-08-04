package com.waypals.vehicle_service.controller;

import com.waypals.vehicle_service.entity.VehicleAggregate;
import com.waypals.vehicle_service.entity.VehicleUser;
import com.waypals.vehicle_service.repository.UserRepository;
import com.waypals.vehicle_service.repository.VehicleRepository;
import com.waypals.vehicle_service.repository.VehicleStateRepository;
import com.waypals.vehicle_service.service.ExcelService;
import com.waypals.vehicle_service.service.Scheduler;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
public class VehicleController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    VehicleStateRepository vehicleStateRepository;
    @Autowired
    ExcelService excelService;

    @Autowired
    Scheduler scheduler;

    @GetMapping("/user")
    public Long getAllUsers(){
        return userRepository.count();
    }

    @GetMapping("/vehicle")
    public Long getVehicles(){
        return vehicleRepository.count();
    }

    @GetMapping("/vehicleState")
    public Long getVehicleState(){
        return vehicleStateRepository.count();
    }


    @GetMapping("/sendEmail")
    public void senEmail(Model model) throws MessagingException {
        scheduler.sendEmailUser(null,model);
    }

    @PostMapping("/vehicle-numbers")
    public ResponseEntity<String> getVehicleNumbers(Model model) {
        try {
            List<String> vehicleNumbers = excelService.getVehicleNumbers();
            scheduler.processVehicleNumbers(vehicleNumbers, model);
            return new ResponseEntity<>("Sending Mail to the user started", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
