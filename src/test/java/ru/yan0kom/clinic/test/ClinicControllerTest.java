package ru.yan0kom.clinic.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yan0kom.clinic.dto.DoctorInDto;
import ru.yan0kom.clinic.dto.PatientInDto;
import ru.yan0kom.clinic.dto.VisitInDto;
import ru.yan0kom.clinic.model.Doctor;
import ru.yan0kom.clinic.model.Patient;
import ru.yan0kom.clinic.model.Visit;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClinicControllerTest extends ControllerTestBase {
    @Test
    public void complexTest() throws Exception {
        //create some patients
        Patient p1 = createPatient("Иванов Иван Иванович");
        Patient p2 = createPatient("Петров Петр Петрович");
        Patient p3 = createPatient("Линкольн Авраам");
        Patient p4 = createPatient("Федоров Федор Федорович");
        //get existing patient
        mvc.perform(get("/api/clinic/patients/{id}", p2.getId())).andExpect(status().isOk());
        //get not existing patient
        mvc.perform(get("/api/clinic/patients/-1")).andExpect(status().isNotFound());
        //list all patients
        assertEquals(4, fromRequest(get("/api/clinic/patients"), List.class).size());
        //update existing patient
        PatientInDto patientInDto = new PatientInDto("Вашингтон Джордж");
        p3 = fromRequest(setBody(put("/api/clinic/patients/{id}", p3.getId()), patientInDto), Patient.class);
        assertEquals(patientInDto.getFullName(), p3.getFullName());
        //update not existing patient
        mvc.perform(setBody(put("/api/clinic/patients/-1"), patientInDto)).andExpect(status().isNotFound());
        //delete existing patient
        mvc.perform(delete("/api/clinic/patients/{id}", p3.getId())).andExpect(status().isOk());
        assertEquals(3, fromRequest(get("/api/clinic/patients"), List.class).size());
        //delete not existing patient
        mvc.perform(delete("/api/clinic/patients/{id}", p3.getId())).andExpect(status().isNotFound());

        //create some doctors (docdoc.ru)
        Doctor d1 = createDoctor("Короткий Игорь Валентинович ", "Пластический хирург");
        Doctor d2 = createDoctor("Кашаева Маргарита Дамировна ", "Стоматолог");
        Doctor d3 = createDoctor("Еременко Валерий Сергеевич ", "Пластический хирург");
        Doctor d4 = createDoctor("Третинник Людмила Владимировна", "Гинеколог");
        assertEquals(d1.getSpecialization(), d3.getSpecialization()); //reuse "Пластический хирург"
        //get existing doctor
        mvc.perform(get("/api/clinic/doctors/{id}", p2.getId())).andExpect(status().isOk());
        //get not existing doctor
        mvc.perform(get("/api/clinic/doctors/-1")).andExpect(status().isNotFound());
        //list all doctors
        assertEquals(4, fromRequest(get("/api/clinic/doctors"), List.class).size());
        //update existing doctor
        DoctorInDto doctorInDto = new DoctorInDto("Кадохова Вера Валерьевна", "Гинеколог");
        Doctor d4u = fromRequest(
                setBody(put("/api/clinic/doctors/{id}", d4.getId()), doctorInDto), Doctor.class);
        assertEquals(doctorInDto.getFullName(), d4u.getFullName());
        assertEquals(d4.getSpecialization(), d4u.getSpecialization());
        //update not existing doctor
        mvc.perform(setBody(put("/api/clinic/doctors/-1"), doctorInDto)).andExpect(status().isNotFound());
        //delete existing doctor
        mvc.perform(delete("/api/clinic/doctors/{id}", d4.getId())).andExpect(status().isOk());
        assertEquals(3, fromRequest(get("/api/clinic/doctors"), List.class).size());
        //delete not existing doctor
        mvc.perform(delete("/api/clinic/doctors/{id}", d4.getId())).andExpect(status().isNotFound());

        //create some visits
        Visit v1 = createVisit(LocalDateTime.of(2020, 6, 2, 9, 15),
                p1.getId(), d1.getId());
        Visit v2 = createVisit(LocalDateTime.of(2020, 6, 2, 10, 45),
                p1.getId(), d2.getId());
        Visit v3 = createVisit(LocalDateTime.of(2020, 6, 7, 11, 30),
                p2.getId(), d3.getId());
        Visit v4 = createVisit(LocalDateTime.of(2020, 6, 8, 15, 0),
                p4.getId(), d3.getId());
        //get existing visit
        mvc.perform(get("/api/clinic/visits/{id}", v2.getId())).andExpect(status().isOk());
        //get not existing visit
        mvc.perform(get("/api/clinic/visits/-1")).andExpect(status().isNotFound());
        //list all visits
        assertEquals(4, fromRequest(get("/api/clinic/visits"), List.class).size());
        //update existing visit
        VisitInDto visitInDto = new VisitInDto(LocalDateTime.of(2020, 6, 8, 16, 0),
                p4.getId(), d3.getId());
        Visit v4u = fromRequest(
                setBody(put("/api/clinic/visits/{id}", v4.getId()), visitInDto), Visit.class);
        assertEquals(visitInDto.getVisitDateTime(), v4u.getVisitDateTime());
        //update not existing visit
        mvc.perform(setBody(put("/api/clinic/visits/-1"), visitInDto)).andExpect(status().isNotFound());
        //delete existing visit
        mvc.perform(delete("/api/clinic/visits/{id}", v3.getId())).andExpect(status().isOk());
        assertEquals(3, fromRequest(get("/api/clinic/visits"), List.class).size());
        //delete not existing visit
        mvc.perform(delete("/api/clinic/visits/{id}", v3.getId())).andExpect(status().isNotFound());
    }

    private Patient createPatient(String fullName) throws Exception {
        PatientInDto body = new PatientInDto(fullName);
        Patient patient = fromRequest(setBody(post("/api/clinic/patients"), body), Patient.class);
        assertNotNull(patient.getId());
        assertEquals(fullName, patient.getFullName());
        return patient;
    }

    private Doctor createDoctor(String fullName, String specialization) throws Exception {
        DoctorInDto body = new DoctorInDto(fullName, specialization);
        Doctor doctor = fromRequest(setBody(post("/api/clinic/doctors"), body), Doctor.class);
        assertNotNull(doctor.getId());
        assertEquals(fullName, doctor.getFullName());
        assertEquals(specialization, doctor.getSpecialization().getName());
        return doctor;
    }

    private Visit createVisit(LocalDateTime visitDateTime, Long patientId, Long doctorId) throws Exception {
        VisitInDto body = new VisitInDto(visitDateTime, patientId, doctorId);
        Visit visit = fromRequest(setBody(post("/api/clinic/visits"), body), Visit.class);
        assertNotNull(visit.getId());
        assertEquals(visitDateTime, visit.getVisitDateTime());
        assertEquals(patientId, visit.getPatient().getId());
        assertEquals(doctorId, visit.getDoctor().getId());
        return visit;
    }
}
