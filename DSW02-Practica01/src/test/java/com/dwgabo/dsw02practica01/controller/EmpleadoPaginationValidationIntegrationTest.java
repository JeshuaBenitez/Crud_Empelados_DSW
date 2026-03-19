package com.dwgabo.dsw02practica01.controller;

import com.dwgabo.dsw02practica01.service.EmpleadoService;
import com.dwgabo.dsw02practica01.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@TestPropertySource(properties = {
        "app.security.basic.username=admin",
        "app.security.basic.password=admin123"
})
class EmpleadoPaginationValidationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

        @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void sinToken_responde401() throws Exception {
        mockMvc.perform(get("/api/v1/empleados?page=0&size=10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void sizeMayorA100_responde400() throws Exception {
        when(empleadoService.listarPropio("empleado.demo@empresa.com", 0, 101))
                .thenThrow(new IllegalArgumentException("El parámetro size debe estar entre 1 y 100"));

        mockMvc.perform(get("/api/v1/empleados?page=0&size=101")
                        .with(SecurityMockMvcRequestPostProcessors.user("empleado.demo@empresa.com").roles("EMPLEADO")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pageNoNumerico_responde400() throws Exception {
        mockMvc.perform(get("/api/v1/empleados?page=abc&size=10")
                        .with(SecurityMockMvcRequestPostProcessors.user("empleado.demo@empresa.com").roles("EMPLEADO")))
                .andExpect(status().isBadRequest());
    }
}
