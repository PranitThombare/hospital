package com.example.hospital.controller;

import com.example.hospital.dto.doctor.DoctorResponseDTO;
import com.example.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // Get doctor profile by ID
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorProfile(@PathVariable Long id) {

        return ResponseEntity.ok(doctorService.getDoctorProfile(id));
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/me")
    public ResponseEntity<DoctorResponseDTO> getMyProfile(Authentication authentication) {

        return ResponseEntity.ok(doctorService.getMyProfile(authentication.getName()));
    }



}
