package com.itcompany.pms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest extends AbstractIntegrationTest {

    @Test
    void mitarbeiterCanCreateTaskInOwnProject() throws Exception {
        String token = loginAndGetToken("mitarbeiter1", "mit123");

        mockMvc.perform(post("/api/projects/1/tasks")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Neue Task\",\"description\":\"x\",\"status\":\"OFFEN\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.projectId").value(1));
    }

    @Test
    void mitarbeiterCannotCreateTaskInForeignProject() throws Exception {
        String token = loginAndGetToken("mitarbeiter1", "mit123");

        mockMvc.perform(post("/api/projects/3/tasks")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Nicht erlaubt\",\"status\":\"OFFEN\"}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void taskStatusTransitionWorks() throws Exception {
        String token = loginAndGetToken("mitarbeiter1", "mit123");

        String response = mockMvc.perform(post("/api/projects/1/tasks")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Status-Test\",\"status\":\"OFFEN\"}"))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        long taskId = json.get("id").asLong();

        mockMvc.perform(patch("/api/tasks/{taskId}/status", taskId)
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Status-Test\",\"status\":\"IN_BEARBEITUNG\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("IN_BEARBEITUNG"));

        mockMvc.perform(patch("/api/tasks/{taskId}/status", taskId)
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Status-Test\",\"status\":\"ERLEDIGT\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("ERLEDIGT"));
    }

    @Test
    void projektleiterSeesProjectProgressNumbers() throws Exception {
        String token = loginAndGetToken("leiter1", "leiter123");

        mockMvc.perform(get("/api/projects/1")
                .header("X-Auth-Token", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalTasks").value(3))
            .andExpect(jsonPath("$.completedTasks").value(1));
    }

    @Test
    void taskIsAssignedToCorrectProject() throws Exception {
        String token = loginAndGetToken("leiter1", "leiter123");

        String response = mockMvc.perform(post("/api/projects/2/tasks")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Projekt2 Task\",\"status\":\"OFFEN\",\"assignedToId\":4}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.projectId").value(2))
            .andReturn()
            .getResponse()
            .getContentAsString();

        long taskId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/projects/2/tasks")
                .header("X-Auth-Token", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[?(@.id == " + taskId + ")].projectId", hasItem(2)));
    }

    @Test
    void invalidTaskPayloadReturnsBadRequest() throws Exception {
        String token = loginAndGetToken("mitarbeiter1", "mit123");

        mockMvc.perform(post("/api/projects/1/tasks")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"\",\"status\":null}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void patchStatusWithStatusOnlyPayloadSucceeds() throws Exception {
        String token = loginAndGetToken("mitarbeiter1", "mit123");

        String response = mockMvc.perform(post("/api/projects/1/tasks")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Status-Only-Test\",\"status\":\"OFFEN\"}"))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        long taskId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(patch("/api/tasks/{taskId}/status", taskId)
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"IN_BEARBEITUNG\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("IN_BEARBEITUNG"));
    }
}
