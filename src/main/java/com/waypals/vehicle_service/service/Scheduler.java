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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    @Value("${email.cc}")
    String cc;

    @Value("${email.bcc}")
    String bcc;

    @Autowired
    ExcelService excelService;

    @Autowired
    RestTemplate restTemplate;



    public void sendEmail(String emailAddress, String name, String registrationNumber, String date, Model model) throws MessagingException {
//        emailAddress = "sl@waypals.com";
        Context context = new Context();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Data Missing");
        helper.setFrom(from);
        helper.setCc(cc);
        helper.setBcc(bcc);
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


    public void processVehicleNumbers(List<String> vehicleNumbers, Model model) throws MessagingException {
        int count = 1;
        for (String registrationNumber : vehicleNumbers) {
            log.info("List count  :: {}", count);
            VehicleAggregate vehicleAggregate = vehicleRepository.findByRegistrationNumber(registrationNumber);
            try{
            if (vehicleAggregate != null) {
                log.info("registrationNumber :: {}", registrationNumber);
                if(!vehicleStateRepository.existsByVehicleIdAndDateTimeDateInMillisBetween(vehicleAggregate.getVehicleId(), Instant.now().minus(10, ChronoUnit.DAYS).toEpochMilli(),Instant.now().toEpochMilli()));
                {
                    Optional<VehicleState> vehicleState = vehicleStateRepository.findTopByVehicleIdOrderByDateTimeDateInMillisDesc(vehicleAggregate.getVehicleId());
                    if(vehicleState.isPresent()){
                        List<VehicleUser> user = userRepository.findByVehicleIdInArms(vehicleAggregate.getVehicleId());
                        log.info("user size :: {}", user.size());
                        sendEmail(user.get(0).getUser().getEmail().getEmail(),user.get(0).getUser().getName().getFirstName(),vehicleAggregate.getVehicle().getMetadata().getRegistrationNo(),convertMillisToDate(vehicleState.get().getDateTime().getDateInMillis()), model);
                    }
                    else{
                        log.info("Vehicle state not present for  :: {}", vehicleAggregate.getVehicleId());
                    }
                }


            }}
            catch (Exception e){
                log.info("Exception :: {}",e.getMessage());
            }
            count+=1;
        }

    }


    @Scheduled(cron = "${cron.time}", zone = "${cron.zone}")
    public void processVehicleNumbers() throws MessagingException {
        log.info("<----------- Job Started --------------->");
        List<String> vehicleNumbers = null;
        try {
            vehicleNumbers = excelService.getVehicleNumbers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int count = 1;
        for (String registrationNumber : vehicleNumbers) {
            log.info("List count  :: {}", count);
            VehicleAggregate vehicleAggregate = vehicleRepository.findByRegistrationNumber(registrationNumber);
            try{
                if (vehicleAggregate != null) {
                    log.info("registrationNumber :: {}", registrationNumber);
                    if(!vehicleStateRepository.existsByVehicleIdAndDateTimeDateInMillisBetween(vehicleAggregate.getVehicleId(), Instant.now().minus(10, ChronoUnit.DAYS).toEpochMilli(),Instant.now().toEpochMilli()));
                    {
                        Optional<VehicleState> vehicleState = vehicleStateRepository.findTopByVehicleIdOrderByDateTimeDateInMillisDesc(vehicleAggregate.getVehicleId());
                        if(vehicleState.isPresent()){
                            List<VehicleUser> user = userRepository.findByVehicleIdInArms(vehicleAggregate.getVehicleId());
                            log.info("user size :: {}", user.size());
                            sendEmailRest(user.get(0).getUser().getEmail().getEmail(), user.get(0).getUser().getName().getFirstName(), vehicleAggregate.getVehicle().getMetadata().getRegistrationNo(), convertMillisToDate(vehicleState.get().getDateTime().getDateInMillis()));
                        }
                        else{
                            log.info("Vehicle state not present for  :: {}", vehicleAggregate.getVehicleId());
                        }
                    }


                }}
            catch (Exception e){
                log.info("Exception :: {}",e.getMessage());
            }
            count+=1;
        }
        log.info("<----------- Job Ended --------------->");

    }


    public ResponseEntity<String> sendEmailRest(String emailAddress, String name, String registrationNumber, String lastDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String requestBody = "emailAddress=" + emailAddress +
                "&name=" + name +
                "&registrationNumber=" + registrationNumber +
                "&lastDate=" + lastDate;

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/sendEmail",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return response;
    }


    public Optional<VehicleState> getLatestVehicleState(long vehicleId) {
        return vehicleStateRepository.findTopByVehicleIdOrderByDateTimeDateInMillisDesc(vehicleId);
    }
}
