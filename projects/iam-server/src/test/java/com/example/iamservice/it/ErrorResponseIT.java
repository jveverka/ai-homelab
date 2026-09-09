package com.example.iamservice.it;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ErrorResponseIT extends BaseApiIT {

    @Test
    @Order(1)
    void unauthorizedErrorUsesProblemJson() throws Exception {
        var result = mockMvc.perform(post("/api/v1/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "email": "someone@example.com",
                                "password": "wrong",
                                "duration": 3600
                            }
                            """))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String contentType = result.getResponse().getContentType();
        String body = result.getResponse().getContentAsString();

        // Should use application/problem+json (Spring's ProblemDetail)
        assertThat(contentType).contains("application/problem+json");
        assertThat(body).doesNotContain("Exception");
        assertThat(body).doesNotContain("at com.example");
        assertThat(body).doesNotContain("SELECT");
        assertThat(body).doesNotContain("password");
    }

    @Test
    @Order(2)
    void notFoundErrorUsesProblemJson() throws Exception {
        String token = getAdminToken();

        var result = mockMvc.perform(put("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "userUUID": "00000000-0000-0000-0000-000000000000",
                                "active": true
                            }
                            """))
                .andExpect(status().isNotFound())
                .andReturn();

        String contentType = result.getResponse().getContentType();
        String body = result.getResponse().getContentAsString();

        assertThat(contentType).contains("application/problem+json");
        assertThat(body).doesNotContain("Exception");
        assertThat(body).doesNotContain("password");
        assertThat(body).doesNotContain("pwdhash");
    }

    @Test
    @Order(3)
    void validationErrorUsesProblemJson() throws Exception {
        String token = getAdminToken();

        var result = mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "email": "",
                                "password": ""
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andReturn();

        String contentType = result.getResponse().getContentType();
        String body = result.getResponse().getContentAsString();

        assertThat(contentType).contains("application/problem+json");
        assertThat(body).doesNotContain("Exception");
    }
}
