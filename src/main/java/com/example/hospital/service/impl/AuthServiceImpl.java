package com.example.hospital.service.impl;

import com.example.hospital.dto.auth.*;
import com.example.hospital.dto.patient.PatientRegisterRequestDTO;
import com.example.hospital.dto.doctor.DoctorRegisterRequestDTO;
import com.example.hospital.entity.*;
import com.example.hospital.exception.UnauthorizedException;
import com.example.hospital.repository.*;
import com.example.hospital.security.JwtUtil;
import com.example.hospital.security.UserDetailsImpl;
import com.example.hospital.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Override
    public void registerPatient(PatientRegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(role));
        userRepository.save(user);

        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setAge(dto.getAge());
        patient.setPhone(dto.getPhone());
        patient.setUser(user);

        patientRepository.save(patient);
    }

    @Override
    public void registerDoctor(DoctorRegisterRequestDTO dto) {

        Role role = roleRepository.findByName("DOCTOR")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(role));
        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setExperience(dto.getExperience());
        doctor.setUser(user);

        doctorRepository.save(doctor);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        // JWT will be added later
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if(! passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new UnauthorizedException("Invalid credentials");
        }

        // 3️⃣ Convert User → UserDetailsImpl

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // 4️⃣ Generate JWT USING UserDetailsImpl
        String token = jwtUtil.generateToken(userDetails);

        return new LoginResponseDTO(token);
    }
}
