package com.example.hospital.dto.appointment;

import com.example.hospital.entity.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAppointmentStatusDTO {
    @NotNull
    private AppointmentStatus status; // COMPLETED / CANCELLED
}
