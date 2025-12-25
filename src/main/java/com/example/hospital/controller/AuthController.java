package com.example.hospital.controller;

import com.example.hospital.dto.auth.*;
import com.example.hospital.dto.patient.PatientRegisterRequestDTO;
import com.example.hospital.dto.doctor.DoctorRegisterRequestDTO;
import com.example.hospital.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Register a new patient
    @PostMapping("/register/patient")
    public ResponseEntity<String> registerPatient(@Valid @RequestBody PatientRegisterRequestDTO dto) {

        authService.registerPatient(dto);
        return ResponseEntity.ok("Patient registered successfully");
    }

    // Register a new doctor
    @PostMapping("/register/doctor")
    public ResponseEntity<String> registerDoctor(@Valid @RequestBody DoctorRegisterRequestDTO dto) {

        authService.registerDoctor(dto);
        return ResponseEntity.ok("Doctor registered successfully");
    }

    // Login (returns JWT token)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(authService.login(dto));
    }
}
