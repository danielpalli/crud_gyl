package com.gyl.CrudGyL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("prod")
@SpringBootTest
class UrlEncryptionProdIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void prodBloqueaUrlsNormalesYPermiteUrlsCifradasDeNegocio() throws Exception {
        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isNotFound());

        MvcResult cryptoResult = mockMvc.perform(get("/api/url-crypto/encriptar").param("url", "/api/ventas"))
                .andExpect(status().isOk())
                .andReturn();

        String prodUrl = extractJsonString(cryptoResult.getResponse().getContentAsString(), "prodUrl");

        mockMvc.perform(get(prodUrl))
                .andExpect(status().isOk());
    }

    private String extractJsonString(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            throw new IllegalStateException("No se encontró el campo " + fieldName + " en la respuesta: " + json);
        }
        return matcher.group(1);
    }
}
