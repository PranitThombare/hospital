package com.example.hospital.service.impl;

import com.example.hospital.dto.patient.PatientResponseDTO;
import com.example.hospital.entity.Patient;
import com.example.hospital.exception.ResourceNotFoundException;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public PatientResponseDTO getPatientProfile(Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return new PatientResponseDTO(
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getPhone()
        );
    }

    @Override
    public PatientResponseDTO getMyProfile(String email) {

        Patient patient = patientRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return new PatientResponseDTO(
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getPhone()
        );
    }
}
