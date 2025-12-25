package com.example.hospital.service;

import com.example.hospital.dto.auth.*;
import com.example.hospital.dto.patient.PatientRegisterRequestDTO;
import com.example.hospital.dto.doctor.DoctorRegisterRequestDTO;

public interface AuthService {

    void registerPatient(PatientRegisterRequestDTO dto);

    void registerDoctor(DoctorRegisterRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);
}
