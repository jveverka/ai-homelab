package com.example.iamservice.it;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserApiIT extends BaseApiIT {

    @Test
    @Order(1)
    void listUsers() throws Exception {
        String token = getAdminToken();

        var result = mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNumber())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.isArray());
        assertThat(json.size()).isGreaterThanOrEqualTo(1); // at least the admin user
    }

    @Test
    @Order(2)
    void listUsersWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isBadRequest()); // missing Authorization header
    }

    @Test
    @Order(10)
    void createUser() throws Exception {
        String token = getAdminToken();
        String body = """
                {
                    "email": "newuser@example.com",
                    "password": "SecurePass123!"
                }
                """;

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(11)
    void createUserWithBlankEmail() throws Exception {
        String token = getAdminToken();
        String body = """
                {
                    "email": "",
                    "password": "SecurePass123!"
                }
                """;

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(12)
    void createUserWithBlankPassword() throws Exception {
        String token = getAdminToken();
        String body = """
                {
                    "email": "blankpw@example.com",
                    "password": ""
                }
                """;

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(13)
    void createUserWithDuplicateEmail() throws Exception {
        String token = getAdminToken();
        String body = """
                {
                    "email": "newuser@example.com",
                    "password": "AnotherPass123!"
                }
                """;

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(20)
    void activateUser() throws Exception {
        String token = getAdminToken();
        // First create a user (order 10 already created one)
        // List users to get the UUID
        String listResult = mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", token))
                .andReturn().getResponse().getContentAsString();

        JsonNode users = objectMapper.readTree(listResult);
        // Find the newuser
        String userId = null;
        for (JsonNode u : users) {
            if (u.get("email").asText().equals("newuser@example.com")) {
                userId = u.get("uuid").asText();
                break;
            }
        }
        assertThat(userId).isNotNull();

        String body = """
                {
                    "userUUID": "%s",
                    "active": false
                }
                """.formatted(userId);

        mockMvc.perform(put("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(21)
    void activateNonexistentUser() throws Exception {
        String token = getAdminToken();
        String body = """
                {
                    "userUUID": "00000000-0000-0000-0000-000000000000",
                    "active": true
                }
                """;

        mockMvc.perform(put("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(30)
    void lastAdminCannotBeRemoved() throws Exception {
        String token = getAdminToken();
        // Get admin UUID
        String listResult = mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", token))
                .andReturn().getResponse().getContentAsString();

        JsonNode users = objectMapper.readTree(listResult);
        String adminId = null;
        for (JsonNode u : users) {
            if (u.get("email").asText().equals("admin@example.com")) {
                adminId = u.get("uuid").asText();
                break;
            }
        }
        assertThat(adminId).isNotNull();

        String body = """
                {
                    "userUUID": "%s"
                }
                """.formatted(adminId);

        mockMvc.perform(delete("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(31)
    void lastAdminCannotBeDeactivated() throws Exception {
        String token = getAdminToken();
        String listResult = mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", token))
                .andReturn().getResponse().getContentAsString();

        JsonNode users = objectMapper.readTree(listResult);
        String adminId = null;
        for (JsonNode u : users) {
            if (u.get("email").asText().equals("admin@example.com")) {
                adminId = u.get("uuid").asText();
                break;
            }
        }
        assertThat(adminId).isNotNull();

        String body = """
                {
                    "userUUID": "%s",
                    "active": false
                }
                """.formatted(adminId);

        mockMvc.perform(put("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(40)
    void removeUser() throws Exception {
        String token = getAdminToken();
        // Create a disposable user
        String createBody = """
                {
                    "email": "disposable@example.com",
                    "password": "Pass123456!"
                }
                """;
        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated());

        // Find the user
        String listResult = mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", token))
                .andReturn().getResponse().getContentAsString();
        JsonNode users = objectMapper.readTree(listResult);
        String userId = null;
        for (JsonNode u : users) {
            if (u.get("email").asText().equals("disposable@example.com")) {
                userId = u.get("uuid").asText();
                break;
            }
        }
        assertThat(userId).isNotNull();

        // Delete it
        String deleteBody = """
                {
                    "userUUID": "%s"
                }
                """.formatted(userId);

        mockMvc.perform(delete("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteBody))
                .andExpect(status().isOk());
    }
}
