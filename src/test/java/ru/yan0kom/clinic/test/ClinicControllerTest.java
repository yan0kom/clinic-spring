package ru.yan0kom.clinic.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.yan0kom.clinic.dto.DoctorInDto;
import ru.yan0kom.clinic.dto.PatientInDto;
import ru.yan0kom.clinic.dto.RoleInDto;
import ru.yan0kom.clinic.dto.UserInDto;
import ru.yan0kom.clinic.dto.VisitInDto;
import ru.yan0kom.clinic.model.AppRole;
import ru.yan0kom.clinic.model.Doctor;
import ru.yan0kom.clinic.model.Patient;
import ru.yan0kom.clinic.model.Visit;
import ru.yan0kom.clinic.security.Privileges;
import ru.yan0kom.clinic.service.AdminService;

@SpringBootTest
@AutoConfigureMockMvc
public class ClinicControllerTest extends ControllerTestBase {
    @Autowired
    AdminService adminService;

    private String suToken;

    @BeforeAll
    public void setup() throws Exception {
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
        AppRole role = adminService.addRole(new RoleInDto("su", new ArrayList<>(Privileges.getPrivilegesSet())));
        adminService.addUser(new UserInDto(null, "su", "su-pass", role.getId()));

        suToken = fromRequest(
                post("/oauth/token")
                        .param("grant_type", "password")
                        .param("username", "su")
                        .param("password", "su-pass")
                        .with(httpBasic(clientId, clientSecret)),
                typeRef).get("access_token");
    }

    @Test
    public void whenUnauthorized_thenFail() throws Exception {
        mvc.perform(post("/api/clinic/patients")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/clinic/patients/1")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/clinic/patients")).andExpect(status().isUnauthorized());
        mvc.perform(put("/api/clinic/patients/1")).andExpect(status().isUnauthorized());
        mvc.perform(delete("/api/clinic/patients/1")).andExpect(status().isUnauthorized());

        mvc.perform(post("/api/clinic/doctors")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/clinic/doctors/1")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/clinic/doctors")).andExpect(status().isUnauthorized());
        mvc.perform(put("/api/clinic/doctors/1")).andExpect(status().isUnauthorized());
        mvc.perform(delete("/api/clinic/doctors/1")).andExpect(status().isUnauthorized());

        mvc.perform(post("/api/clinic/visits")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/clinic/visits/1")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/clinic/visits")).andExpect(status().isUnauthorized());
        mvc.perform(put("/api/clinic/visits/1")).andExpect(status().isUnauthorized());
        mvc.perform(delete("/api/clinic/visits/1")).andExpect(status().isUnauthorized());
    }

    @Test
    public void complexTest() throws Exception {
        //create some patients
        Patient p1 = createPatient("Иванов Иван Иванович");
        Patient p2 = createPatient("Петров Петр Петрович");
        Patient p3 = createPatient("Линкольн Авраам");
        Patient p4 = createPatient("Федоров Федор Федорович");
        //get existing patient
        mvcPerform(get("/api/clinic/patients/{id}", p2.getId()), suToken).andExpect(status().isOk());
        //get not existing patient
        mvcPerform(get("/api/clinic/patients/-1"), suToken).andExpect(status().isNotFound());
        //list all patients
        assertEquals(4, fromRequest(get("/api/clinic/patients"), suToken, List.class).size());
        //update existing patient
        PatientInDto patientInDto = new PatientInDto("Вашингтон Джордж");
        p3 = fromRequest(setBody(put("/api/clinic/patients/{id}", p3.getId()), patientInDto), suToken, Patient.class);
        assertEquals(patientInDto.getFullName(), p3.getFullName());
        //update not existing patient
        mvcPerform(setBody(put("/api/clinic/patients/-1"), patientInDto), suToken).andExpect(status().isNotFound());
        //delete existing patient
        mvcPerform(delete("/api/clinic/patients/{id}", p3.getId()), suToken).andExpect(status().isOk());
        assertEquals(3, fromRequest(get("/api/clinic/patients"), suToken, List.class).size());
        //delete not existing patient
        mvcPerform(delete("/api/clinic/patients/{id}", p3.getId()), suToken).andExpect(status().isNotFound());

        //create some doctors (docdoc.ru)
        Doctor d1 = createDoctor("Короткий Игорь Валентинович ", "Пластический хирург");
        Doctor d2 = createDoctor("Кашаева Маргарита Дамировна ", "Стоматолог");
        Doctor d3 = createDoctor("Еременко Валерий Сергеевич ", "Пластический хирург");
        Doctor d4 = createDoctor("Третинник Людмила Владимировна", "Гинеколог");
        assertEquals(d1.getSpecialization(), d3.getSpecialization()); //reuse "Пластический хирург"
        //get existing doctor
        mvcPerform(get("/api/clinic/doctors/{id}", p2.getId()), suToken).andExpect(status().isOk());
        //get not existing doctor
        mvcPerform(get("/api/clinic/doctors/-1"), suToken).andExpect(status().isNotFound());
        //list all doctors
        assertEquals(4, fromRequest(get("/api/clinic/doctors"), suToken, List.class).size());
        //update existing doctor
        DoctorInDto doctorInDto = new DoctorInDto("Кадохова Вера Валерьевна", "Гинеколог");
        Doctor d4u = fromRequest(
                setBody(put("/api/clinic/doctors/{id}", d4.getId()), doctorInDto), suToken, Doctor.class);
        assertEquals(doctorInDto.getFullName(), d4u.getFullName());
        assertEquals(d4.getSpecialization(), d4u.getSpecialization());
        //update not existing doctor
        mvcPerform(setBody(put("/api/clinic/doctors/-1"), doctorInDto), suToken).andExpect(status().isNotFound());
        //delete existing doctor
        mvcPerform(delete("/api/clinic/doctors/{id}", d4.getId()), suToken).andExpect(status().isOk());
        assertEquals(3, fromRequest(get("/api/clinic/doctors"), suToken, List.class).size());
        //delete not existing doctor
        mvcPerform(delete("/api/clinic/doctors/{id}", d4.getId()), suToken).andExpect(status().isNotFound());

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
        mvcPerform(get("/api/clinic/visits/{id}", v2.getId()), suToken).andExpect(status().isOk());
        //get not existing visit
        mvcPerform(get("/api/clinic/visits/-1"), suToken).andExpect(status().isNotFound());
        //list all visits
        assertEquals(4, fromRequest(get("/api/clinic/visits"), suToken, List.class).size());
        //update existing visit
        VisitInDto visitInDto = new VisitInDto(LocalDateTime.of(2020, 6, 8, 16, 0),
                p4.getId(), d3.getId());
        Visit v4u = fromRequest(
                setBody(put("/api/clinic/visits/{id}", v4.getId()), visitInDto), suToken, Visit.class);
        assertEquals(visitInDto.getVisitDateTime(), v4u.getVisitDateTime());
        //update not existing visit
        mvcPerform(setBody(put("/api/clinic/visits/-1"), visitInDto), suToken).andExpect(status().isNotFound());
        //delete existing visit
        mvcPerform(delete("/api/clinic/visits/{id}", v3.getId()), suToken).andExpect(status().isOk());
        assertEquals(3, fromRequest(get("/api/clinic/visits"), suToken, List.class).size());
        //delete not existing visit
        mvcPerform(delete("/api/clinic/visits/{id}", v3.getId()), suToken).andExpect(status().isNotFound());
    }

    private Patient createPatient(String fullName) throws Exception {
        PatientInDto body = new PatientInDto(fullName);
        Patient patient = fromRequest(setBody(post("/api/clinic/patients"), body), suToken, Patient.class);
        assertNotNull(patient.getId());
        assertEquals(fullName, patient.getFullName());
        return patient;
    }

    private Doctor createDoctor(String fullName, String specialization) throws Exception {
        DoctorInDto body = new DoctorInDto(fullName, specialization);
        Doctor doctor = fromRequest(setBody(post("/api/clinic/doctors"), body), suToken, Doctor.class);
        assertNotNull(doctor.getId());
        assertEquals(fullName, doctor.getFullName());
        assertEquals(specialization, doctor.getSpecialization().getName());
        return doctor;
    }

    private Visit createVisit(LocalDateTime visitDateTime, Long patientId, Long doctorId) throws Exception {
        VisitInDto body = new VisitInDto(visitDateTime, patientId, doctorId);
        Visit visit = fromRequest(setBody(post("/api/clinic/visits"), body), suToken, Visit.class);
        assertNotNull(visit.getId());
        assertEquals(visitDateTime, visit.getVisitDateTime());
        assertEquals(patientId, visit.getPatient().getId());
        assertEquals(doctorId, visit.getDoctor().getId());
        return visit;
    }
}
