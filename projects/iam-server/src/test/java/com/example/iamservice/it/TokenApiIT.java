package com.example.iamservice.it;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TokenApiIT extends BaseApiIT {

    @Test
    @Order(1)
    void loginWithValidCredentials() throws Exception {
        String body = """
                {
                    "email": "admin@example.com",
                    "password": "TestAdmin123!",
                    "duration": 3600
                }
                """;

        var result = mockMvc.perform(post("/api/v1/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.expiresAt").isNotEmpty())
                .andExpect(jsonPath("$.permissions").isArray())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("token").asText()).isNotBlank();
    }

    @Test
    @Order(2)
    void loginWithInvalidPassword() throws Exception {
        String body = """
                {
                    "email": "admin@example.com",
                    "password": "wrongpassword",
                    "duration": 3600
                }
                """;

        mockMvc.perform(post("/api/v1/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Order(3)
    void loginWithNonexistentUser() throws Exception {
        String body = """
                {
                    "email": "nobody@example.com",
                    "password": "pass123456",
                    "duration": 3600
                }
                """;

        mockMvc.perform(post("/api/v1/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Order(10)
    void introspectValidToken() throws Exception {
        String token = getAdminToken();

        var result = mockMvc.perform(get("/api/v1/tokens")
                        .header("Authorization", token))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.expiresAt").isNotEmpty())
                .andExpect(jsonPath("$.permissions").isArray())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("permissions").isArray());
        assertThat(json.get("permissions").size()).isGreaterThan(0);
    }

    @Test
    @Order(11)
    void introspectInvalidToken() throws Exception {
        mockMvc.perform(get("/api/v1/tokens")
                        .header("Authorization", "00000000-0000-0000-0000-000000000000"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Order(20)
    void logoutInvalidatesToken() throws Exception {
        String token = getAdminToken();

        // Token is valid before logout
        mockMvc.perform(get("/api/v1/tokens")
                        .header("Authorization", token))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());

        // Perform logout
        mockMvc.perform(delete("/api/v1/tokens")
                        .header("Authorization", token))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());

        // Token should be invalid after logout
        mockMvc.perform(get("/api/v1/tokens")
                        .header("Authorization", token))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Order(21)
    void logoutIsIdempotent() throws Exception {
        String token = getAdminToken();

        // First logout
        mockMvc.perform(delete("/api/v1/tokens")
                        .header("Authorization", token))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());

        // Second logout (idempotent)
        mockMvc.perform(delete("/api/v1/tokens")
                        .header("Authorization", token))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
    }
}
