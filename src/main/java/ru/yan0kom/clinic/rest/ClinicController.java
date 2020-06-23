package ru.yan0kom.clinic.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.yan0kom.clinic.dto.DoctorInDto;
import ru.yan0kom.clinic.dto.PatientInDto;
import ru.yan0kom.clinic.dto.VisitInDto;
import ru.yan0kom.clinic.model.Doctor;
import ru.yan0kom.clinic.model.Patient;
import ru.yan0kom.clinic.model.Visit;
import ru.yan0kom.clinic.security.Privileges;
import ru.yan0kom.clinic.service.ClinicService;

import java.util.List;

@RestController
@RequestMapping("/api/clinic")
public class ClinicController {
    @Autowired
    ClinicService clinicService;

    @PostMapping("patients")
    @Secured(Privileges.clinic_patients_create)
    public Patient addPatient(@RequestBody PatientInDto patientInDto) {
        return clinicService.addPatient(patientInDto);
    }

    @GetMapping("patients/{id}")
    @Secured(Privileges.clinic_patients_read)
    public Patient getPatient(@PathVariable Long id) {
        return clinicService.getPatient(id);
    }

    @GetMapping("patients")
    @Secured(Privileges.clinic_patients_read)
    public List<Patient> listPatients() {
        return clinicService.listPatients();
    }

    @PutMapping("patients/{id}")
    @Secured(Privileges.clinic_patients_update)
    public Patient updatePatient(@PathVariable Long id, @RequestBody PatientInDto patientInDto) {
        return clinicService.updatePatient(id, patientInDto);
    }

    @DeleteMapping("patients/{id}")
    @Secured(Privileges.clinic_patients_delete)
    public void deletePatient(@PathVariable Long id) {
        clinicService.deletePatient(id);
    }

    @PostMapping("doctors")
    public Doctor addDoctor(@RequestBody DoctorInDto doctorInDto) {
        return clinicService.addDoctor(doctorInDto);
    }

    @GetMapping("doctors/{id}")
    public Doctor getDoctor(@PathVariable Long id) {
        return clinicService.getDoctor(id);
    }

    @GetMapping("doctors")
    public List<Doctor> listDoctors() {
        return clinicService.listDoctors();
    }

    @PutMapping("doctors/{id}")
    public Doctor updateDoctor(@PathVariable Long id, @RequestBody DoctorInDto doctorInDto) {
        return clinicService.updateDoctor(id, doctorInDto);
    }

    @DeleteMapping("doctors/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        clinicService.deleteDoctor(id);
    }

    @PostMapping("visits")
    public Visit addVisit(@RequestBody VisitInDto visitInDto) {
        return clinicService.addVisit(visitInDto);
    }

    @GetMapping("visits/{id}")
    public Visit getVisit(@PathVariable Long id) {
        return clinicService.getVisit(id);
    }

    @GetMapping("visits")
    public List<Visit> listVisits() {
        return clinicService.listVisits();
    }

    @PutMapping("visits/{id}")
    public Visit updateVisit(@PathVariable Long id, @RequestBody VisitInDto visitInDto) {
        return clinicService.updateVisit(id, visitInDto);
    }

    @DeleteMapping("visits/{id}")
    public void deleteVisit(@PathVariable Long id) {
        clinicService.deleteVisit(id);
    }
}
