package com.hcmute.HealthyCare.apicontroller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.springframework.aop.framework.AopProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Appointment;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.Patient;
import com.hcmute.HealthyCare.enums.AppointmentStatus;
import com.hcmute.HealthyCare.service.AppointmentService;
import com.hcmute.HealthyCare.service.UserService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api")
public class ApiAppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @PostMapping("/createAppointment")
    public ResponseEntity<?> createAppointment(@RequestBody JsonNode jsonData) {
        String notes = jsonData.get("notes").asText();
        AppointmentStatus status = AppointmentStatus.SCHEDULED;
        String patient_email = jsonData.get("patient").asText();
        String doctor_email = jsonData.get("doctor").asText();
        String date = jsonData.get("date").asText();
        String time = jsonData.get("time").asText();
        String datetime = date+" "+time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = null;
        try {
            localDateTime = LocalDateTime.parse(datetime, formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Patient pId = userService.loadAccount(patient_email).getPatient();
        Doctor dId = userService.loadAccount(doctor_email).getDoctor();


        Appointment newAppointment = new Appointment();
        newAppointment.setDatetime(localDateTime);
        newAppointment.setNotes(notes);
        newAppointment.setStatus(status);
        newAppointment.setDoctor(dId);
        newAppointment.setPatient(pId);

        Appointment createdAppointment = appointmentService.createAppointment(newAppointment);
        
        if (createdAppointment!= null) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", createdAppointment.getDatetime());
            map.put("notes", createdAppointment.getNotes());
            map.put("status", createdAppointment.getStatus());
            map.put("doctor", createdAppointment.getDoctor().getId());
            map.put("patient", createdAppointment.getPatient().getId());
            return ResponseEntity.ok().body(map);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/getAppointmentOfUser")
    public ResponseEntity<?> getAppointmentOfUser(@PathParam("email") String email) {
        Account account = userService.loadAccount(email);
        List<Map<String,Object>> listMap = new ArrayList<>();
        List<Appointment> appointments = new ArrayList<>();
        if (account.getDoctor() != null) {
            appointments = appointmentService.getAppointmentByDoctor(account.getDoctor().getId());
        } else {
            appointments = appointmentService.getAppointmentByPatient(account.getPatient().getId());
        }
        if (appointments != null) {
            for (Appointment appointment: appointments) {
                Map<String, Object> map = new HashMap<>();
                String pAvt = null;
                String dAvt = null;
                Long pId = appointment.getPatient().getId();
                Long dId = appointment.getDoctor().getId();

                Account patient = userService.getAccountByPatient(pId);
                if (patient != null) {
                    pAvt = patient.getAvatar();
                }

                Account doctor = userService.getAccountByDoctor(dId);
                if (doctor != null) {
                    dAvt = doctor.getAvatar();
                }
                LocalDateTime localDateTime = appointment.getDatetime();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                String date = localDateTime.format(dateFormatter);
                String time = localDateTime.format(timeFormatter);
                map.put("id", appointment.getId());
                map.put("date", "Ngày: "+ date);
                map.put("time", "Giờ: "+time);
                map.put("notes", appointment.getNotes());
                map.put("doctor", appointment.getDoctor().getName());
                map.put("doctorAvt", dAvt);
                map.put("patient", appointment.getPatient().getName());
                map.put("patientAvt", pAvt);
                map.put("status", appointment.getStatus());

                listMap.add(map);
            }
            return ResponseEntity.ok().body(listMap);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteAppointment")
    public ResponseEntity<?> deleteAppointment(@PathParam("id") Long id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/updateAppointmentStatus")
    public ResponseEntity<String> uppdateStatus(@PathParam("id") Long id, @PathParam("status") String status) {
        Appointment appointment = appointmentService.getById(id);
        if (appointment!=null) {
            appointment.setStatus(AppointmentStatus.valueOf(status));
            @SuppressWarnings("unused")
            Appointment newAppointment = appointmentService.editAppointment(appointment);
            return ResponseEntity.ok().body("Thành công");
        }
        else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/getAppointmentById")
    public ResponseEntity<?> getAppointmentById(@PathParam("id") Long id) {
        Appointment appointment = appointmentService.getById(id);
        if (appointment != null) {
            Account doctor = userService.getAccountByDoctor(appointment.getDoctor().getId());
            LocalDateTime dateTime = appointment.getDatetime();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String date = dateTime.format(dateFormatter);
            String time = dateTime.format(timeFormatter);

            Map<String, Object> map = new HashMap<>();

            map.put("pName", appointment.getPatient().getName());
            map.put("pAddress", appointment.getPatient().getAddress());
            map.put("birthday", appointment.getPatient().getBirthday());
            map.put("pPhone", appointment.getPatient().getPhone());
            map.put("gender", appointment.getPatient().getGender());
            map.put("uderlying", appointment.getPatient().getUnderlyingDisease());
            map.put("dName", appointment.getDoctor().getName());
            map.put("dAddress", appointment.getDoctor().getAddress());
            map.put("dAvt", doctor.getAvatar());
            map.put("sDate", date);
            map.put("sTime", time);
            map.put("notes", appointment.getNotes());
            
            return ResponseEntity.ok().body(map);
        }
        return ResponseEntity.notFound().build();
    }
}
