package com.example.hospital.service;

import com.example.hospital.dto.patient.PatientResponseDTO;

public interface PatientService {

    PatientResponseDTO getPatientProfile(Long patientId);
    PatientResponseDTO getMyProfile(String email);

}
