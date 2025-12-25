package com.example.hospital.service;

import com.example.hospital.dto.appointment.*;
import com.example.hospital.dto.doctor.DoctorResponseDTO;
import com.example.hospital.entity.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO bookAppointment(AppointmentRequestDTO dto, String patientEmail);

    List<AppointmentListResponseDTO> getAppointmentsForPatient(Long patientId);

    List<AppointmentListResponseDTO> getAppointmentsForDoctor(Long doctorId);

    List<AppointmentListResponseDTO> getAllAppointments(); // ADMIN

    void cancelAppointment(Long appointmentId, String patientEmail);

    void updateAppointmentStatus(Long appointmentId, AppointmentStatus status, String actorEmail, boolean isAdmin);

    List<AppointmentListResponseDTO> getMyAppointments(String email);

    List<AppointmentListResponseDTO> getMyDoctorAppointments(String name);

    List<DoctorResponseDTO> getAvailableDoctorsToPatient();
}
