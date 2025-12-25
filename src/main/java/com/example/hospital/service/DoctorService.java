package com.example.hospital.service;

import com.example.hospital.dto.doctor.DoctorResponseDTO;

public interface DoctorService {

    DoctorResponseDTO getDoctorProfile(Long doctorId);
    DoctorResponseDTO getMyProfile(String email);

}
