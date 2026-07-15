package com.itcompany.pms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractIntegrationTest {

    @Test
    void adminCanCreateUser() throws Exception {
        String token = loginAndGetToken("admin", "admin123");

        mockMvc.perform(post("/api/users")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"neuuser\",\"password\":\"secret123\",\"email\":\"neu@itcompany.com\",\"role\":\"MITARBEITER\",\"active\":true}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("neuuser"));
    }

    @Test
    void adminCanAssignRole() throws Exception {
        String token = loginAndGetToken("admin", "admin123");

        String createResponse = mockMvc.perform(post("/api/users")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"rollentest\",\"password\":\"secret123\",\"email\":\"rolle@itcompany.com\",\"role\":\"MITARBEITER\",\"active\":true}"))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(createResponse);
        long userId = jsonNode.get("id").asLong();

        mockMvc.perform(put("/api/users/{id}", userId)
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"rollentest\",\"email\":\"rolle@itcompany.com\",\"role\":\"PROJEKTLEITER\",\"active\":true}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.role").value("PROJEKTLEITER"));
    }

    @Test
    void mitarbeiterCannotCreateUser() throws Exception {
        String token = loginAndGetToken("mitarbeiter1", "mit123");

        mockMvc.perform(post("/api/users")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"forbidden\",\"password\":\"secret123\",\"email\":\"forbidden@itcompany.com\",\"role\":\"MITARBEITER\"}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void userValidationRequiresUsernameEmailAndRole() throws Exception {
        String token = loginAndGetToken("admin", "admin123");

        mockMvc.perform(post("/api/users")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"secret123\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void duplicateUsernameReturnsBadRequest() throws Exception {
        String token = loginAndGetToken("admin", "admin123");

        mockMvc.perform(post("/api/users")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"secret123\",\"email\":\"dup@itcompany.com\",\"role\":\"MITARBEITER\",\"active\":true}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void invalidEmailFormatReturnsBadRequest() throws Exception {
        String token = loginAndGetToken("admin", "admin123");

        mockMvc.perform(post("/api/users")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"mailtest\",\"password\":\"secret123\",\"email\":\"kein-email-format\",\"role\":\"MITARBEITER\",\"active\":true}"))
            .andExpect(status().isBadRequest());
    }
}
