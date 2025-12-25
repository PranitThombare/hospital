package com.example.hospital.dto.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequestDTO {

//    @NotNull(message = "Patient ID is required")
//    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Appointment date is required")
    private LocalDate date;

    @NotNull(message = "Appointment time is required")
    private LocalTime time;
}
