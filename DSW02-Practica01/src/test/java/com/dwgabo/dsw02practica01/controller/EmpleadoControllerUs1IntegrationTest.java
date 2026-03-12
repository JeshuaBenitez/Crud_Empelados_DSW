package com.dwgabo.dsw02practica01.controller;

import com.dwgabo.dsw02practica01.config.SecurityConfig;
import com.dwgabo.dsw02practica01.dto.CreateEmpleadoRequest;
import com.dwgabo.dsw02practica01.dto.EmpleadoResponse;
import com.dwgabo.dsw02practica01.service.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "app.security.basic.username=admin",
        "app.security.basic.password=admin123"
})
class EmpleadoControllerUs1IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmpleadoService empleadoService;

    @Test
    void crearSinClave_yObtenerPorClaveVersionada() throws Exception {
        EmpleadoResponse response = new EmpleadoResponse();
        response.setClave("EMP-1");
        response.setNombre("Ana Pérez");
        response.setDireccion("Av. Central 123");
        response.setTelefono("555123456");

        when(empleadoService.crear(any(CreateEmpleadoRequest.class))).thenReturn(response);
        when(empleadoService.obtenerPorClave(eq("EMP-1"))).thenReturn(response);

        CreateEmpleadoRequest request = new CreateEmpleadoRequest();
        request.setNombre("Ana Pérez");
        request.setDireccion("Av. Central 123");
        request.setTelefono("555123456");

        mockMvc.perform(post("/api/v1/empleados")
                        .header(HttpHeaders.AUTHORIZATION, basicAuth("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clave").value("EMP-1"));

        mockMvc.perform(get("/api/v1/empleados/EMP-1")
                        .header(HttpHeaders.AUTHORIZATION, basicAuth("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana Pérez"));
    }

    private String basicAuth(String username, String password) {
        String token = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }
}
