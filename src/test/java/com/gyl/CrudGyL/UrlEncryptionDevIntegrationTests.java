package com.gyl.CrudGyL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("dev")
@SpringBootTest
class UrlEncryptionDevIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void devPermiteUrlsNormales() throws Exception {
        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/url-crypto/encriptar").param("url", "/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.devUrl").value("/api/ventas"))
                .andExpect(jsonPath("$.decryptedStaticPath").value("/api/ventas"));
    }
}
