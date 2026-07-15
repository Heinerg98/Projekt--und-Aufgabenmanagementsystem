package com.itcompany.pms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectMemberTest extends AbstractIntegrationTest {

    @Test
    void projektleiterCanAddMember() throws Exception {
        String token = loginAndGetToken("leiter1", "leiter123");

        mockMvc.perform(post("/api/projects/1/members")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":6}"))
            .andExpect(status().isOk());
    }

    @Test
    void mitarbeiterCannotAddMember() throws Exception {
        String token = loginAndGetToken("mitarbeiter1", "mit123");

        mockMvc.perform(post("/api/projects/1/members")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":6}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void memberSeesProjectAfterBeingAdded() throws Exception {
        String leaderToken = loginAndGetToken("leiter1", "leiter123");

        mockMvc.perform(post("/api/projects/1/members")
                .header("X-Auth-Token", leaderToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":6}"))
            .andExpect(status().isOk());

        String memberToken = loginAndGetToken("mitarbeiter3", "mit123");

        mockMvc.perform(get("/api/projects")
                .header("X-Auth-Token", memberToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
            .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 3)));
    }

    @Test
    void duplicateMemberAddIsIgnored() throws Exception {
        String token = loginAndGetToken("leiter1", "leiter123");

        mockMvc.perform(post("/api/projects/1/members")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":4}"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/1/members")
                .header("X-Auth-Token", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[?(@.id == 4)]", org.hamcrest.Matchers.hasSize(1)));
    }

    @Test
    void addMemberRequiresValidUserId() throws Exception {
        String token = loginAndGetToken("leiter1", "leiter123");

        mockMvc.perform(post("/api/projects/1/members")
                .header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":999}"))
            .andExpect(status().isNotFound());
    }
}
