package com.dwgabo.dsw02practica01.security;

import com.dwgabo.dsw02practica01.controller.AuthController;
import com.dwgabo.dsw02practica01.dto.LoginEmpleadoRequest;
import com.dwgabo.dsw02practica01.dto.LoginEmpleadoResponse;
import com.dwgabo.dsw02practica01.security.JwtTokenProvider;
import com.dwgabo.dsw02practica01.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerLoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void loginExitoso_responde200ConBearer() throws Exception {
        LoginEmpleadoResponse response = new LoginEmpleadoResponse();
        response.setAccessToken("token-demo");
        response.setTokenType("Bearer");
        response.setExpiresIn(3600000L);

        when(authService.login(any(LoginEmpleadoRequest.class))).thenReturn(response);

        LoginEmpleadoRequest request = new LoginEmpleadoRequest();
        request.setCorreo("empleado.demo@empresa.com");
        request.setPassword("Empleado123!");

        mockMvc.perform(post("/api/v1/auth/empleados/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void loginSinPayloadValido_responde400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/empleados/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
