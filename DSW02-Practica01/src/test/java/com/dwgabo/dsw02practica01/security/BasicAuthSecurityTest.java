package com.dwgabo.dsw02practica01.security;

import com.dwgabo.dsw02practica01.config.SecurityConfig;
import com.dwgabo.dsw02practica01.controller.EmpleadoController;
import com.dwgabo.dsw02practica01.dto.EmpleadoPageResponse;
import com.dwgabo.dsw02practica01.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "app.security.basic.username=admin",
        "app.security.basic.password=admin123"
})
class BasicAuthSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Test
    void sinCredenciales_responde401() throws Exception {
        mockMvc.perform(get("/api/v1/empleados"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void conCredencialesValidas_responde200() throws Exception {
        EmpleadoPageResponse response = new EmpleadoPageResponse();
        response.setContent(List.of());
        response.setPage(0);
        response.setSize(10);
        response.setTotalElements(0);
        response.setTotalPages(0);

        when(empleadoService.listar(anyInt(), anyInt())).thenReturn(response);

        mockMvc.perform(get("/api/v1/empleados")
                        .header(HttpHeaders.AUTHORIZATION, basicAuth("admin", "admin123")))
                .andExpect(status().isOk());
    }

    private String basicAuth(String username, String password) {
        String token = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }
}
