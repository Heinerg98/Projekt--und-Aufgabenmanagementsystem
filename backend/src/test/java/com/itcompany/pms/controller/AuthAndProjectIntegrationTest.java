package com.itcompany.pms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthAndProjectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginMitKorrektemPasswortErfolgreich() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"leiter1","password":"leiter123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("PROJEKTLEITER"));
    }

    @Test
    void loginMitFalschemPasswortUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"leiter1","password":"falsch"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutInvalidiertToken() throws Exception {
        String token = loginToken("leiter1", "leiter123");

        mockMvc.perform(post("/api/auth/logout").header("X-Auth-Token", token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/auth/me").header("X-Auth-Token", token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void projektleiterErstelltProjektErfolgreich() throws Exception {
        String token = loginToken("leiter1", "leiter123");

        mockMvc.perform(post("/api/projects")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Neues Projekt","description":"Beschreibung"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Neues Projekt"))
                .andExpect(jsonPath("$.status").value("AKTIV"));
    }

    @Test
    void mitarbeiterDarfProjektNichtErstellen() throws Exception {
        String token = loginToken("mitarbeiter1", "mitarbeiter123");

        mockMvc.perform(post("/api/projects")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Verbotenes Projekt","description":"x"}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void projektleiterSiehtNurEigeneProjekte() throws Exception {
        String token = loginToken("leiter1", "leiter123");

        MvcResult result = mockMvc.perform(get("/api/projects").header("X-Auth-Token", token))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json).hasSize(2);
        assertThat(json.toString()).doesNotContain("Legacy-Abbau");
    }

    @Test
    void adminSiehtAlleProjekte() throws Exception {
        String token = loginToken("admin", "admin123");

        mockMvc.perform(get("/api/projects").header("X-Auth-Token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void nurVerantwortlicherProjektleiterDarfArchivieren() throws Exception {
        String ownerToken = loginToken("leiter1", "leiter123");
        String otherLeadToken = loginToken("leiter2", "leiter123");

        mockMvc.perform(patch("/api/projects/1/archive").header("X-Auth-Token", ownerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ARCHIVIERT"));

        mockMvc.perform(patch("/api/projects/1/archive").header("X-Auth-Token", otherLeadToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void mitarbeiterErstelltAufgabeImEigenenProjekt() throws Exception {
        String token = loginToken("mitarbeiter1", "mitarbeiter123");

        mockMvc.perform(post("/api/projects/1/tasks")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Neue Aufgabe","description":"todo"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OFFEN"));
    }

    @Test
    void mitarbeiterErstelltAufgabeImFremdenProjektForbidden() throws Exception {
        String token = loginToken("mitarbeiter1", "mitarbeiter123");

        mockMvc.perform(post("/api/projects/2/tasks")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Verbotene Aufgabe","description":"x"}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void aufgabenStatusWechselOffenZuErledigt() throws Exception {
        String token = loginToken("mitarbeiter1", "mitarbeiter123");

        MvcResult created = mockMvc.perform(post("/api/projects/1/tasks")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Status-Test","description":"flow"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OFFEN"))
                .andReturn();

        long taskId = objectMapper.readTree(created.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(patch("/api/tasks/" + taskId + "/status")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status":"IN_BEARBEITUNG"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_BEARBEITUNG"));

        mockMvc.perform(patch("/api/tasks/" + taskId + "/status")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status":"ERLEDIGT"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ERLEDIGT"));
    }

    @Test
    void projektleiterSiehtProjektfortschritt() throws Exception {
        String token = loginToken("leiter1", "leiter123");

        mockMvc.perform(get("/api/projects/1/progress").header("X-Auth-Token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(1))
                .andExpect(jsonPath("$.completedPercentage").value(33.33333333333333));
    }

    @Test
    void projektleiterFügtMitgliedHinzuUndMitarbeiterSiehtProjektDanach() throws Exception {
        String leadToken = loginToken("leiter1", "leiter123");
        String workerToken = loginToken("mitarbeiter3", "mitarbeiter123");

        mockMvc.perform(post("/api/projects/2/members")
                        .header("X-Auth-Token", leadToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId":6}
                                """))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/api/projects").header("X-Auth-Token", workerToken))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).contains("Monitoring-Optimierung");
    }

    @Test
    void nurProjektleiterDarfMitgliederHinzufuegen() throws Exception {
        String workerToken = loginToken("mitarbeiter1", "mitarbeiter123");

        mockMvc.perform(post("/api/projects/1/members")
                        .header("X-Auth-Token", workerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId":6}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminErstelltBenutzerUndWeistRolleZu() throws Exception {
        String adminToken = loginToken("admin", "admin123");

        MvcResult result = mockMvc.perform(post("/api/users")
                        .header("X-Auth-Token", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"neuuser","password":"pw123","role":"MITARBEITER"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("neuuser"))
                .andReturn();

        long userId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(patch("/api/users/" + userId + "/role")
                        .header("X-Auth-Token", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"role":"PROJEKTLEITER"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("PROJEKTLEITER"));
    }

    @Test
    void nichtAdminDarfKeinenBenutzerErstellen() throws Exception {
        String leadToken = loginToken("leiter1", "leiter123");

        mockMvc.perform(post("/api/users")
                        .header("X-Auth-Token", leadToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"x","password":"y","role":"MITARBEITER"}
                                """))
                .andExpect(status().isForbidden());
    }

    private String loginToken(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"%s","password":"%s"}
                                """.formatted(username, password)))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }
}
