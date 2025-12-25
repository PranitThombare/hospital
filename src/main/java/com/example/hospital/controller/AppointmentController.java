package com.example.hospital.controller;

import com.example.hospital.dto.appointment.*;
import com.example.hospital.dto.doctor.DoctorResponseDTO;
import com.example.hospital.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Book a new appointment
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/book")
    public ResponseEntity<AppointmentResponseDTO> bookAppointment(@Valid @RequestBody AppointmentRequestDTO dto, Authentication authentication) {

        return ResponseEntity.ok(appointmentService.bookAppointment(dto, authentication.getName()));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/available-doctors")
    public ResponseEntity<List<DoctorResponseDTO>> getAvailableDoctorsToPatient() {

        return ResponseEntity.ok(appointmentService.getAvailableDoctorsToPatient());
    }

    // PATIENT – get own appointments
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentListResponseDTO>> getPatientAppointments(@PathVariable Long patientId) {

        List<AppointmentListResponseDTO> appointments = appointmentService.getAppointmentsForPatient(patientId);
        // sort appointments by date
        appointments = appointments.stream().sorted((a1, a2) -> {
            int dateCompare = a1.getDate().compareTo(a2.getDate());
            if (dateCompare != 0) {
                return dateCompare;
            } else {
                return a1.getTime().compareTo(a2.getTime());
            }
        }).toList();

        return ResponseEntity.ok(appointments);
    }

    // DOCTOR – get own appointments
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentListResponseDTO>> getDoctorAppointments(@PathVariable Long doctorId) {
        List<AppointmentListResponseDTO> appointments = appointmentService.getAppointmentsForDoctor(doctorId);
        // sort appointments by date
        appointments = appointments.stream().sorted((a1, a2) -> {
            int dateCompare = a1.getDate().compareTo(a2.getDate());
            if (dateCompare != 0) {
                return dateCompare;
            } else {
                return a1.getTime().compareTo(a2.getTime());
            }
        }).toList();
        return ResponseEntity.ok(appointments);
    }


    // ADMIN – get all appointments
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<AppointmentListResponseDTO>> getAllAppointments() {

        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id, Authentication authentication) {

        appointmentService.cancelAppointment(id, authentication.getName());
        return ResponseEntity.ok("Appointment cancelled successfully");
    }


    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatusByDoctor(@PathVariable Long id, @Valid @RequestBody UpdateAppointmentStatusDTO dto, Authentication authentication) {

        appointmentService.updateAppointmentStatus(id, dto.getStatus(), authentication.getName(), false);
        return ResponseEntity.ok("Appointment status updated");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status/admin")
    public ResponseEntity<String> updateStatusByAdmin(@PathVariable Long id, @Valid @RequestBody UpdateAppointmentStatusDTO dto, Authentication authentication) {

        appointmentService.updateAppointmentStatus(id, dto.getStatus(), authentication.getName(), true);
        return ResponseEntity.ok("Appointment status updated by admin");
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentListResponseDTO>> getMyAppointments(Authentication authentication) {

        List<AppointmentListResponseDTO> appointments = appointmentService.getMyAppointments(authentication.getName());
        // sort appointments by date
        // ok
        appointments = appointments.stream().sorted((a1, a2) -> {
            int dateCompare = a1.getDate().compareTo(a2.getDate());
            if (dateCompare != 0) {
                return dateCompare;
            } else {
                return a1.getTime().compareTo(a2.getTime());
            }
        }).toList();

        return ResponseEntity.ok(appointments);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/doctor/my")
    public ResponseEntity<List<AppointmentListResponseDTO>> getMyDoctorAppointments(Authentication authentication) {

        return ResponseEntity.ok(appointmentService.getMyDoctorAppointments(authentication.getName()));
    }


}
