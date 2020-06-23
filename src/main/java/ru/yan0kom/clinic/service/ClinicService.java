package ru.yan0kom.clinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yan0kom.clinic.dao.DoctorDao;
import ru.yan0kom.clinic.dao.PatientDao;
import ru.yan0kom.clinic.dao.SpecializationDao;
import ru.yan0kom.clinic.dao.VisitDao;
import ru.yan0kom.clinic.dto.DoctorInDto;
import ru.yan0kom.clinic.dto.PatientInDto;
import ru.yan0kom.clinic.dto.VisitInDto;
import ru.yan0kom.clinic.error.EntityNotFoundException;
import ru.yan0kom.clinic.model.Doctor;
import ru.yan0kom.clinic.model.Patient;
import ru.yan0kom.clinic.model.Specialization;
import ru.yan0kom.clinic.model.Visit;

import java.util.List;

@Service
public class ClinicService extends BaseService {
    @Autowired
    PatientDao patientDao;
    @Autowired
    SpecializationDao specializationDao;
    @Autowired
    DoctorDao doctorDao;
    @Autowired
    VisitDao visitDao;

    public Patient addPatient(PatientInDto patientInDto) {
        return patientDao.save(new Patient(null, patientInDto.getFullName()));
    }

    public Patient getPatient(Long id) {
        return patientDao.findById(id).orElseThrow(() -> new EntityNotFoundException(Patient.class, id));
    }

    public List<Patient> listPatients() {
        return patientDao.findAll();
    }

    public Patient updatePatient(Long id, PatientInDto patientInDto) {
        checkExistence(patientDao, Patient.class, id);
        return patientDao.save(new Patient(id, patientInDto.getFullName()));
    }

    public void deletePatient(Long id) {
        checkExistence(patientDao, Patient.class, id);
        patientDao.deleteById(id);
    }

    public Doctor addDoctor(DoctorInDto doctorInDto) {
        return doctorDao.save(
                new Doctor(null, doctorInDto.getFullName(), getSpecialization(doctorInDto.getSpecialization())));
    }

    public Doctor getDoctor(Long id) {
        return doctorDao.findById(id).orElseThrow(() -> new EntityNotFoundException(Doctor.class, id));
    }

    public List<Doctor> listDoctors() {
        return doctorDao.findAll();
    }

    public Doctor updateDoctor(Long id, DoctorInDto doctorInDto) {
        checkExistence(doctorDao, Doctor.class, id);
        return doctorDao.save(
                new Doctor(id, doctorInDto.getFullName(), getSpecialization(doctorInDto.getSpecialization())));
    }

    public void deleteDoctor(Long id) {
        checkExistence(doctorDao, Doctor.class, id);
        doctorDao.deleteById(id);
    }

    public Visit addVisit(VisitInDto visitInDto) {
        return visitDao.save(
                new Visit(null,
                        visitInDto.getVisitDateTime(),
                        getPatient(visitInDto.getPatientId()),
                        getDoctor(visitInDto.getDoctorId())));
    }

    public Visit getVisit(Long id) {
        return visitDao.findById(id).orElseThrow(() -> new EntityNotFoundException(Visit.class, id));
    }

    public List<Visit> listVisits() {
        return visitDao.findAll();
    }

    public Visit updateVisit(Long id, VisitInDto visitInDto) {
        checkExistence(visitDao, Visit.class, id);
        return visitDao.save(
                new Visit(id,
                        visitInDto.getVisitDateTime(),
                        getPatient(visitInDto.getPatientId()),
                        getDoctor(visitInDto.getDoctorId())));
    }

    public void deleteVisit(Long id) {
        checkExistence(visitDao, Visit.class, id);
        visitDao.deleteById(id);
    }

    private Specialization getSpecialization(String name) {
        return specializationDao.findByName(name)
                .orElseGet(() -> specializationDao.save(new Specialization(null, name)));
    }
}
