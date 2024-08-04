package com.waypals.vehicle_service.service;

import com.waypals.vehicle_service.entity.VehicleAggregate;
import com.waypals.vehicle_service.entity.VehicleState;
import com.waypals.vehicle_service.entity.VehicleUser;
import com.waypals.vehicle_service.repository.UserRepository;
import com.waypals.vehicle_service.repository.VehicleRepository;
import com.waypals.vehicle_service.repository.VehicleStateRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Slf4j
public class Scheduler {
    @Autowired
    UserRepository userRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    VehicleStateRepository vehicleStateRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Value("${email.active}")
    String from;


    public void sendEmailUser(VehicleUser vehicleUser, Model model) throws MessagingException {
        if(vehicleUser==null){
            for(VehicleUser user : userRepository.findAll()){
                for(VehicleUser.Arm arm: user.getArms()){

                    if(!arm.isDeactivated()){
                        log.info("!arm.isDeactivated()  :{}",!arm.isDeactivated());
                        if(!vehicleStateRepository.existsByVehicleIdAndDateTimeDateInMillisBetween(arm.getVehicleId(), Instant.now().minus(10, ChronoUnit.DAYS).toEpochMilli(),Instant.now().toEpochMilli())){
                            log.info("vehicleId :: {}",arm.getVehicleId());
                            Optional<VehicleState> vehicleState = vehicleStateRepository.findTopByVehicleIdOrderByDateTimeDateInMillisDesc(arm.getVehicleId());
                            Optional<VehicleAggregate> vehicleAggregate = vehicleRepository.findByVehicleId(arm.getVehicleId());
                            sendEmail(user.getUser().getEmail().getEmail(), user.getUser().getName().getFirstName(), vehicleAggregate.isPresent()?vehicleAggregate.get().getVehicle().getMetadata().getRegistrationNo(): "DIVU143", vehicleState.isPresent()? convertMillisToDate(vehicleState.get().getDateTime().getDateInMillis()): LocalDate.now().toString(),model);
                        }
                    }
                }
            }
        }
        else{
            for(VehicleUser.Arm arm: vehicleUser.getArms()){
                if(!arm.isDeactivated()){
                    log.info("!arm.isDeactivated()  :{}",!arm.isDeactivated());
                    if(!vehicleStateRepository.existsByVehicleIdAndDateTimeDateInMillisBetween(arm.getVehicleId(), Instant.now().minus(10, ChronoUnit.DAYS).toEpochMilli(),Instant.now().toEpochMilli())){
                        log.info("vehicleId :: {}",arm.getVehicleId());
                        Optional<VehicleState> vehicleState = vehicleStateRepository.findTopByVehicleIdOrderByDateTimeDateInMillisDesc(arm.getVehicleId());
                        Optional<VehicleAggregate> vehicleAggregate = vehicleRepository.findByVehicleId(arm.getVehicleId());
                        sendEmail(vehicleUser.getUser().getEmail().getEmail(), vehicleUser.getUser().getName().getFirstName(), vehicleAggregate.isPresent()?vehicleAggregate.get().getVehicle().getMetadata().getRegistrationNo(): "DIVU143", vehicleState.isPresent()? convertMillisToDate(vehicleState.get().getDateTime().getDateInMillis()): LocalDate.now().toString(),model);
                    }
                }
            }
        }



    }

    void sendEmail(String emailAddress, String name, String registrationNumber, String date, Model model) throws MessagingException {
        emailAddress = "sl@waypals.com";
//        emailAddress = "diviyabhardwaz@gmail.com";
        Context context = new Context();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Data Missing");
        helper.setFrom(from);
        helper.setTo(emailAddress);
        model.addAttribute("userName",name);
        model.addAttribute("vehicleRegistrationNo",registrationNumber);
        model.addAttribute("lastDataDateTime",date);
        context.setVariables(model.asMap());
        helper.setText(templateEngine.process("waypals", context), true);
        mailSender.send(message);
        log.info("Mail sent to : {}",emailAddress);
    }

    public static String convertMillisToDate(Long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dateTime.format(formatter);
    }
}
