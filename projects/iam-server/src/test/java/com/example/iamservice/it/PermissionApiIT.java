package com.example.iamservice.it;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PermissionApiIT extends BaseApiIT {

    @Test
    @Order(1)
    void listPermissions_containsDefaults() throws Exception {
        String token = getAdminToken();

        var result = mockMvc.perform(get("/api/v1/permissions")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNumber())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.size()).isGreaterThanOrEqualTo(9);

        // Verify all default permissions are present
        var permissions = new java.util.HashSet<String>();
        for (JsonNode p : json) {
            permissions.add(p.get("id").asText());
        }

        assertThat(permissions).contains(
                "iam.users.create",
                "iam.users.read",
                "iam.users.delete",
                "iam.users.setactive",
                "iam.permissions.create",
                "iam.permissions.read",
                "iam.permissions.delete",
                "iam.permissions.assign",
                "iam.permissions.unassign"
        );
    }

    @Test
    @Order(10)
    void createNewPermission() throws Exception {
        String token = getAdminToken();
        String body = """
                {
                    "id": "custom.app.feature",
                    "description": "Can use custom app feature"
                }
                """;

        var result = mockMvc.perform(post("/api/v1/permissions/create")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("custom.app.feature"))
                .andExpect(jsonPath("$.description").value("Can use custom app feature"))
                .andReturn();
    }

    @Test
    @Order(11)
    void createDuplicatePermission() throws Exception {
        String token = getAdminToken();
        String body = """
                {
                    "id": "custom.app.feature",
                    "description": "Duplicate attempt"
                }
                """;

        mockMvc.perform(post("/api/v1/permissions/create")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(20)
    void deleteNewPermission() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(delete("/api/v1/permissions/custom.app.feature")
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    @Order(21)
    void defaultPermissionCannotBeRemoved() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(delete("/api/v1/permissions/iam.users.read")
                        .header("Authorization", token))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(30)
    void assignPermissionToUser() throws Exception {
        String token = getAdminToken();

        // Create a permission first
        String createBody = """
                {
                    "id": "iam.assign.test",
                    "description": "Test assign permission"
                }
                """;
        mockMvc.perform(post("/api/v1/permissions/create")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated());

        // Create a user
        String userCreateBody = """
                {
                    "email": "assignTest@example.com",
                    "password": "Test123456!"
                }
                """;
        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userCreateBody))
                .andExpect(status().isCreated());

        // Find user UUID
        String listResult = mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", token))
                .andReturn().getResponse().getContentAsString();
        JsonNode users = objectMapper.readTree(listResult);
        String userId = null;
        for (JsonNode u : users) {
            if (u.get("email").asText().equals("assignTest@example.com")) {
                userId = u.get("uuid").asText();
                break;
            }
        }
        assertThat(userId).isNotNull();

        // Assign permission
        String assignBody = """
                {
                    "userUUID": "%s",
                    "permissionId": "iam.assign.test"
                }
                """.formatted(userId);

        mockMvc.perform(post("/api/v1/permissions")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assignBody))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(31)
    void unassignPermissionFromUser() throws Exception {
        String token = getAdminToken();

        // Find user
        String listResult = mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", token))
                .andReturn().getResponse().getContentAsString();
        JsonNode users = objectMapper.readTree(listResult);
        String userId = null;
        for (JsonNode u : users) {
            if (u.get("email").asText().equals("assignTest@example.com")) {
                userId = u.get("uuid").asText();
                break;
            }
        }
        assertThat(userId).isNotNull();

        String body = """
                {
                    "userUUID": "%s",
                    "permissionId": "iam.assign.test"
                }
                """.formatted(userId);

        mockMvc.perform(delete("/api/v1/permissions")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(40)
    void unauthorizedAccessWithoutToken() throws Exception {
        // Try to access without Authorization header → Spring returns 400 (missing header)
        mockMvc.perform(get("/api/v1/permissions"))
                .andExpect(status().is4xxClientError());
    }
}
