package com.dwgabo.dsw02practica01.security;

import com.dwgabo.dsw02practica01.controller.DepartamentoController;
import com.dwgabo.dsw02practica01.dto.DepartamentoPageResponse;
import com.dwgabo.dsw02practica01.service.DepartamentoService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartamentoController.class)
@TestPropertySource(properties = {
        "app.security.basic.username=admin",
        "app.security.basic.password=admin123"
})
class DepartamentoJwtSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartamentoService departamentoService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void sinToken_responde401() throws Exception {
        mockMvc.perform(get("/api/v1/departamentos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenInvalido_responde401() throws Exception {
        mockMvc.perform(get("/api/v1/departamentos")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void autenticado_responde200() throws Exception {
        when(departamentoService.listar(eq(0), eq(10))).thenReturn(new DepartamentoPageResponse());

        mockMvc.perform(get("/api/v1/departamentos?page=0&size=10")
                        .with(SecurityMockMvcRequestPostProcessors.user("empleado.demo@empresa.com").roles("EMPLEADO")))
                .andExpect(status().isOk());
    }
}
