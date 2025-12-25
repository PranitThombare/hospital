package com.example.hospital.service.impl;

import com.example.hospital.dto.appointment.*;
import com.example.hospital.dto.doctor.DoctorResponseDTO;
import com.example.hospital.entity.*;
import com.example.hospital.entity.enums.AppointmentStatus;
import com.example.hospital.exception.AppointmentConflictException;
import com.example.hospital.exception.BadRequestException;
import com.example.hospital.exception.ResourceNotFoundException;
import com.example.hospital.exception.UnauthorizedException;
import com.example.hospital.repository.*;
import com.example.hospital.service.AppointmentService;
import com.example.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public AppointmentResponseDTO bookAppointment(AppointmentRequestDTO dto, String patientEmail) {


        Patient patient = patientRepository.findByUserEmail(patientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));


        boolean alreadyBooked = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, dto.getDate(), dto.getTime());

        if (alreadyBooked) {
            throw new AppointmentConflictException("This time slot is already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getDate());
        appointment.setAppointmentTime(dto.getTime());
        appointment.setStatus(AppointmentStatus.BOOKED);

        Appointment saved = appointmentRepository.save(appointment);

        return new AppointmentResponseDTO(saved.getId(), doctor.getName(), patient.getName(), saved.getStatus().name());
    }

    @Override
    public List<AppointmentListResponseDTO> getAppointmentsForPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return appointmentRepository.findByPatientId(patientId).stream().map(a -> new AppointmentListResponseDTO(a.getId(), a.getDoctor().getName(), patient.getName(), a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus().name())).toList();
    }

    @Override
    public List<AppointmentListResponseDTO> getAppointmentsForDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return appointmentRepository.findByDoctorId(doctorId).stream().map(a -> new AppointmentListResponseDTO(a.getId(), doctor.getName(), a.getPatient().getName(), a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus().name())).toList();
    }

    @Override
    public List<AppointmentListResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(a -> new AppointmentListResponseDTO(a.getId(), a.getDoctor().getName(), a.getPatient().getName(), a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus().name())).toList();
    }

    @Override
    public void cancelAppointment(Long appointmentId, String patientEmail) {

        Appointment appt = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // ensure this appointment belongs to logged-in patient
        if (!appt.getPatient().getUser().getEmail().equals(patientEmail)) {
            throw new UnauthorizedException("You can cancel only your own appointment");
        }

        if (appt.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Completed appointment cannot be cancelled");
        }

        appt.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appt);
    }

    @Override
    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus status, String actorEmail, boolean isAdmin) {

        Appointment appt = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // if NOT admin → must be the assigned doctor
        if (!isAdmin) {
            if (!appt.getDoctor().getUser().getEmail().equals(actorEmail)) {
                throw new UnauthorizedException("You can update only your own appointments");
            }
        }

        appt.setStatus(status);
        appointmentRepository.save(appt);
    }

    @Override
    public List<AppointmentListResponseDTO> getMyAppointments(String email) {

        Patient patient = patientRepository.findByUserEmail(email).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return appointmentRepository.findByPatientId(patient.getId()).stream().map(a -> new AppointmentListResponseDTO(a.getId(), a.getDoctor().getName(), patient.getName(), a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus().name())).toList();
    }

    @Override
    public List<AppointmentListResponseDTO> getMyDoctorAppointments(String name) {
        Doctor doctor = doctorRepository.findByUserEmail(name).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return appointmentRepository.findByDoctorId(doctor.getId()).stream().map(a -> new AppointmentListResponseDTO(a.getId(), doctor.getName(), a.getPatient().getName(), a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus().name())).toList();
    }

    @Override
    public List<DoctorResponseDTO> getAvailableDoctorsToPatient() {
        // get all doctors from doctorRepository
        List<Doctor> doctors = doctorRepository.findAll();
        // map to DoctorResponseDTO
        return doctors.stream().map(d -> new DoctorResponseDTO(d.getId(), d.getName(), d.getSpecialization())).toList();
    }


}
