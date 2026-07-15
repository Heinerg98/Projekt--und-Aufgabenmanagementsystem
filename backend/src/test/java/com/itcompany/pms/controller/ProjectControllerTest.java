package com.itcompany.pms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest extends AbstractIntegrationTest {

    @Test
    void projektleiterCanCreateProject() throws Exception {
        String token = loginAndGetToken("leiter1", "leiter123");

        mockMvc.perform(post("/api/projects")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Neues Projekt\",\"description\":\"Beschreibung\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.projectManagerName").value("leiter1"))
            .andExpect(jsonPath("$.status").value("AKTIV"));
    }

    @Test
    void mitarbeiterCannotCreateProject() throws Exception {
        String token = loginAndGetToken("mitarbeiter1", "mit123");

        mockMvc.perform(post("/api/projects")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Unerlaubt\",\"description\":\"x\"}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminCanSeeAllProjects() throws Exception {
        String token = loginAndGetToken("admin", "admin123");

        mockMvc.perform(get("/api/projects")
                .header("X-Auth-Token", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(4)));
    }

    @Test
    void projektleiterSeesOnlyOwnProjects() throws Exception {
        String token = loginAndGetToken("leiter1", "leiter123");

        mockMvc.perform(get("/api/projects")
                .header("X-Auth-Token", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(3)))
            .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2, 4)));
    }

    @Test
    void mitarbeiterSeesOnlyMemberProjects() throws Exception {
        String token = loginAndGetToken("mitarbeiter1", "mit123");

        mockMvc.perform(get("/api/projects")
                .header("X-Auth-Token", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
            .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)));
    }

    @Test
    void projektleiterCanArchiveOwnProject() throws Exception {
        String token = loginAndGetToken("leiter1", "leiter123");

        mockMvc.perform(post("/api/projects/2/archive")
                .header("X-Auth-Token", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("ARCHIVIERT"));
    }

    @Test
    void otherProjektleiterCannotArchiveForeignProject() throws Exception {
        String token = loginAndGetToken("leiter2", "leiter123");

        mockMvc.perform(post("/api/projects/1/archive")
                .header("X-Auth-Token", token))
            .andExpect(status().isForbidden());
    }

    @Test
    void projectReturnsCorrectProgressMetrics() throws Exception {
        String token = loginAndGetToken("leiter1", "leiter123");

        mockMvc.perform(get("/api/projects/1")
                .header("X-Auth-Token", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalTasks").value(3))
            .andExpect(jsonPath("$.completedTasks").value(1))
            .andExpect(jsonPath("$.progress").value(33));
    }
}
