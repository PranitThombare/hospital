package com.example.hospital.controller;

import com.example.hospital.dto.patient.PatientResponseDTO;
import com.example.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // Get patient profile by ID
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientProfile(@PathVariable Long id) {

        return ResponseEntity.ok(patientService.getPatientProfile(id));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me")
    public ResponseEntity<PatientResponseDTO> getMyProfile(Authentication authentication) {

        return ResponseEntity.ok(patientService.getMyProfile(authentication.getName()));
    }


}
