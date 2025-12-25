package com.example.hospital.service.impl;

import com.example.hospital.dto.doctor.DoctorResponseDTO;
import com.example.hospital.entity.Doctor;
import com.example.hospital.exception.ResourceNotFoundException;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorResponseDTO getDoctorProfile(Long doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization()
        );
    }

    @Override
    public DoctorResponseDTO getMyProfile(String email) {

        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization()
        );
    }

}
