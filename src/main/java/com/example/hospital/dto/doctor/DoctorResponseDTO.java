package com.example.hospital.dto.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorResponseDTO {

    private Long id;
    private String name;
    private String specialization;
}
