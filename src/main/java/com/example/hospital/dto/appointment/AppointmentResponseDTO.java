package com.example.hospital.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppointmentResponseDTO {

    private Long appointmentId;
    private String doctorName;
    private String patientName;
    private String status;
}
