package com.example.springk8demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
@TestPropertySource(properties = {
    "spring.application.name=spring-k8-demo",
    "app.message=Hello from Configuration"
})
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloReturnsExpectedMessage() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Hello from Spring Boot\"}"));
    }

    @Test
    void infoReturnsApplicationDetails() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application").value("spring-k8-demo"))
                .andExpect(jsonPath("$.version").value("1.0"));
    }

    @Test
    void configuredMessageUsesDefaultValue() throws Exception {
        mockMvc.perform(get("/api/configured-message"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Hello from Configuration\"}"));
    }
}
