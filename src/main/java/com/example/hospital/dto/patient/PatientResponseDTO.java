package com.example.hospital.dto.patient;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientResponseDTO {

    private Long id;
    private String name;
    private Integer age;
    private String phone;
}
