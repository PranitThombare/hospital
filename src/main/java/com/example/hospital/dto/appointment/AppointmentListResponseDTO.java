package com.example.hospital.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AppointmentListResponseDTO {

    private Long appointmentId;
    private String doctorName;
    private String patientName;
    private LocalDate date;
    private LocalTime time;
    private String status;
}
