package com.example.iamservice.it;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "iam.admin.email=admin@example.com",
        "iam.admin.password=TestAdmin123!"
})
public abstract class BaseApiIT extends BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected static final String ADMIN_EMAIL = "admin@example.com";
    protected static final String ADMIN_PASSWORD = "TestAdmin123!";

    /**
     * Logs in as admin and returns the token string.
     */
    protected String getAdminToken() throws Exception {
        String body = """
                {
                    "email": "%s",
                    "password": "%s",
                    "duration": 3600
                }
                """.formatted(ADMIN_EMAIL, ADMIN_PASSWORD);

        MvcResult result = mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andReturn();

        return extractToken(result);
    }

    protected String extractToken(MvcResult result) throws Exception {
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("token").asText();
    }
}
