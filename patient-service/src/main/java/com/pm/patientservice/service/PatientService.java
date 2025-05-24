package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.AppException;
import com.pm.patientservice.exception.ErrorCode;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patientList = patientRepository.findAll();

        // Map from Patient to PatientResponseDTO
        List<PatientResponseDTO> patientResponseDTOS =
                patientList.stream()
                        .map(patient -> PatientMapper.toDTO(patient))
                        .toList();

        return patientResponseDTOS;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
        if (patientRepository.existsByEmail(requestDTO.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, requestDTO.getEmail());
        }

        Patient newPatient = patientRepository.save(
                PatientMapper.toModel(requestDTO)
        );

        billingServiceGrpcClient.createBillingAccount(
                newPatient.getId().toString(),
                newPatient.getName(),
                newPatient.getEmail()
        );

        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new AppException(ErrorCode.PATIENT_NOT_FOUND, id)
                );

        if (patientRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, requestDTO.getEmail());
        }

        patient.setName(requestDTO.getName());
        patient.setAddress(requestDTO.getAddress());
        patient.setEmail(requestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(requestDTO.getDateOfBirth()));

        patientRepository.save(patient);

        return PatientMapper.toDTO(patient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
