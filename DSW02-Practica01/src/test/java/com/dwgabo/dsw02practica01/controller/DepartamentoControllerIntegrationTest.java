package com.dwgabo.dsw02practica01.controller;

import com.dwgabo.dsw02practica01.dto.CreateDepartamentoRequest;
import com.dwgabo.dsw02practica01.dto.DepartamentoPageResponse;
import com.dwgabo.dsw02practica01.dto.DepartamentoResponse;
import com.dwgabo.dsw02practica01.security.JwtTokenProvider;
import com.dwgabo.dsw02practica01.service.DepartamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartamentoController.class)
@TestPropertySource(properties = {
        "app.security.basic.username=admin",
        "app.security.basic.password=admin123"
})
@SuppressWarnings("java:S2068")
class DepartamentoControllerIntegrationTest {

    private static final String TEST_EMAIL = "empleado.demo@empresa.com";
    private static final String DEP_CLAVE = "DEP-1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartamentoService departamentoService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void crearDepartamentoResponde201() throws Exception {
        DepartamentoResponse response = new DepartamentoResponse();
        response.setClave(DEP_CLAVE);
        response.setNombre("Recursos Humanos");

        when(departamentoService.crear(any(CreateDepartamentoRequest.class))).thenReturn(response);

        CreateDepartamentoRequest request = new CreateDepartamentoRequest();
        request.setNombre("Recursos Humanos");

        mockMvc.perform(post("/api/v1/departamentos")
                        .with(SecurityMockMvcRequestPostProcessors.user(TEST_EMAIL).roles("EMPLEADO"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clave").value(DEP_CLAVE));
    }

    @Test
    void listarDepartamentosResponde200() throws Exception {
        DepartamentoResponse item = new DepartamentoResponse();
        item.setClave(DEP_CLAVE);
        item.setNombre("Operacion");

        DepartamentoPageResponse page = new DepartamentoPageResponse();
        page.setContent(List.of(item));
        page.setPage(0);
        page.setSize(10);
        page.setTotalElements(1);
        page.setTotalPages(1);

        when(departamentoService.listar(eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(get("/api/v1/departamentos?page=0&size=10")
                        .with(SecurityMockMvcRequestPostProcessors.user(TEST_EMAIL).roles("EMPLEADO")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Operacion"));
    }
}
