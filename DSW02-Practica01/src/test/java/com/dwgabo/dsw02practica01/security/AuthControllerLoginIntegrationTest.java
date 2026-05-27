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
@SuppressWarnings("java:S2068")
class AuthControllerLoginIntegrationTest {

    private static final String TEST_EMAIL = "empleado.demo@empresa.com";
    private static final String TEST_PASSWORD = "Empleado123!";
    private static final String TOKEN_TYPE_BEARER = "Bearer";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void loginExitosoResponde200ConBearer() throws Exception {
        LoginEmpleadoResponse response = new LoginEmpleadoResponse();
        response.setAccessToken("token-demo");
        response.setTokenType(TOKEN_TYPE_BEARER);
        response.setExpiresIn(3600000L);

        when(authService.login(any(LoginEmpleadoRequest.class))).thenReturn(response);

        LoginEmpleadoRequest request = new LoginEmpleadoRequest();
        request.setCorreo(TEST_EMAIL);
        request.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/api/v1/auth/empleados/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value(TOKEN_TYPE_BEARER));
    }

    @Test
    void loginSinPayloadValidoResponde400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/empleados/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
