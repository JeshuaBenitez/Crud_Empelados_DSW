package com.dwgabo.dsw02practica01.controller;

import com.dwgabo.dsw02practica01.dto.EmpleadoResponse;
import com.dwgabo.dsw02practica01.security.JwtTokenProvider;
import com.dwgabo.dsw02practica01.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@TestPropertySource(properties = {
        "app.security.basic.username=admin",
        "app.security.basic.password=admin123"
})
class EmpleadoControllerUs1IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void obtenerPropioPorClaveVersionada() throws Exception {
        EmpleadoResponse response = new EmpleadoResponse();
        response.setClave("EMP-1");
        response.setNombre("Ana Pérez");
        response.setDireccion("Av. Central 123");
        response.setTelefono("555123456");

        when(empleadoService.obtenerPorClave(eq("EMP-1"), eq("empleado.demo@empresa.com"))).thenReturn(response);

        mockMvc.perform(get("/api/v1/empleados/EMP-1")
                        .with(SecurityMockMvcRequestPostProcessors.user("empleado.demo@empresa.com").roles("EMPLEADO")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana Pérez"));
    }
}
