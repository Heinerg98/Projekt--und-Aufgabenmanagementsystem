package com.itcompany.pms.controller;

import com.itcompany.pms.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private AuthService authService;

    @Test
    void loginWithCorrectPasswordReturnsToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void loginWithWrongPasswordReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void loginWithUnknownUserReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"does-not-exist\",\"password\":\"x\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutInvalidatesToken() throws Exception {
        String token = loginAndGetToken("admin", "admin123");

        mockMvc.perform(post("/api/auth/logout")
                .header("X-Auth-Token", token))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/projects")
                .header("X-Auth-Token", token))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void accessWithoutTokenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/projects"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void accessWithExpiredTokenReturnsUnauthorized() throws Exception {
        String token = loginAndGetToken("admin", "admin123");

        @SuppressWarnings("unchecked")
        Map<String, Object> tokens = (Map<String, Object>) ReflectionTestUtils.getField(authService, "tokens");
        assertThat(tokens).isNotNull();

        Object sessionToken = tokens.get(token);
        assertThat(sessionToken).isNotNull();

        Long userId = (Long) ReflectionTestUtils.invokeMethod(sessionToken, "userId");
        Constructor<?> constructor = sessionToken.getClass().getDeclaredConstructor(Long.class, Instant.class);
        constructor.setAccessible(true);
        Object expiredToken = constructor.newInstance(userId, Instant.now().minusSeconds(30));
        tokens.put(token, expiredToken);

        mockMvc.perform(get("/api/projects")
                .header("X-Auth-Token", token))
            .andExpect(status().isUnauthorized());
    }
}
