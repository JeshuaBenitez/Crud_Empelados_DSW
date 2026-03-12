package com.dwgabo.dsw02practica01.controller;

import com.dwgabo.dsw02practica01.config.SecurityConfig;
import com.dwgabo.dsw02practica01.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "app.security.basic.username=admin",
        "app.security.basic.password=admin123"
})
class EmpleadoPaginationValidationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Test
    void sizeMayorA100_responde400() throws Exception {
        when(empleadoService.listar(0, 101)).thenThrow(new IllegalArgumentException("El parámetro size debe estar entre 1 y 100"));

        mockMvc.perform(get("/api/v1/empleados?page=0&size=101")
                        .header(HttpHeaders.AUTHORIZATION, basicAuth("admin", "admin123")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pageNoNumerico_responde400() throws Exception {
        mockMvc.perform(get("/api/v1/empleados?page=abc&size=10")
                        .header(HttpHeaders.AUTHORIZATION, basicAuth("admin", "admin123")))
                .andExpect(status().isBadRequest());
    }

    private String basicAuth(String username, String password) {
        String token = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }
}
