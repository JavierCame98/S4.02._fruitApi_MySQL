package cat.itacademy.s04.t02.n02.fruit.controller;

import cat.itacademy.s04.t02.n02.fruit.exceptions.ResourceNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit.service.ProviderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProviderController.class)
class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProviderServiceImpl providerService;

    private ProviderRequestDto providerRequestDto;
    private ProviderResponseDto providerResponseDto;
    private List<ProviderResponseDto> providerList;

    @BeforeEach
    void setUp() {
        providerRequestDto = new ProviderRequestDto("Provider1", "Spain");
        providerResponseDto = new ProviderResponseDto(1L, "Provider1", "Spain");
        providerList = Arrays.asList(providerResponseDto);
    }

    // ==================== CREATE TESTS ====================

    @Test
    void create_WhenRequestIsValid_ShouldReturn201Created() throws Exception {
        when(providerService.create(any(ProviderRequestDto.class))).thenReturn(providerResponseDto);

        mockMvc.perform(post("/providers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(providerRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Provider1"))
                .andExpect(jsonPath("$.country").value("Spain"));
    }

    @Test
    void create_WhenRequestIsInvalid_ShouldReturn400BadRequest() throws Exception {
        ProviderRequestDto invalidRequest = new ProviderRequestDto("", "");

        mockMvc.perform(post("/providers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // ==================== UPDATE TESTS ====================

    @Test
    void update_WhenIdExists_ShouldReturn200Ok() throws Exception {
        ProviderRequestDto updateDto = new ProviderRequestDto("Provider2", "France");
        ProviderResponseDto updatedResponse = new ProviderResponseDto(1L, "Provider2", "France");

        when(providerService.update(eq(1L), any(ProviderRequestDto.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/providers/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Provider2"))
                .andExpect(jsonPath("$.country").value("France"));
    }

    @Test
    void update_WhenIdDoesNotExist_ShouldReturn404NotFound() throws Exception {
        when(providerService.update(eq(1L), any(ProviderRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Provider", 1L));

        mockMvc.perform(put("/providers/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(providerRequestDto)))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE TESTS ====================

    @Test
    void delete_WhenIdExists_ShouldReturn204NoContent() throws Exception {
        doNothing().when(providerService).delete(1L);

        mockMvc.perform(delete("/providers/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_WhenIdDoesNotExist_ShouldReturn404NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Provider", 1L)).when(providerService).delete(1L);

        mockMvc.perform(delete("/providers/delete/1"))
                .andExpect(status().isNotFound());
    }

    // ==================== GET ALL TESTS ====================

    @Test
    void getAll_ShouldReturn200Ok() throws Exception {
        when(providerService.getAll()).thenReturn(providerList);

        mockMvc.perform(get("/providers/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Provider1"))
                .andExpect(jsonPath("$[0].country").value("Spain"));
    }

    // ==================== GET BY ID TESTS ====================

    @Test
    void getById_WhenIdExists_ShouldReturn200Ok() throws Exception {
        when(providerService.getById(1L)).thenReturn(providerResponseDto);

        mockMvc.perform(get("/providers/getById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Provider1"));
    }

    @Test
    void getById_WhenIdDoesNotExist_ShouldReturn404NotFound() throws Exception {
        when(providerService.getById(1L)).thenThrow(new ResourceNotFoundException("Provider", 1L));

        mockMvc.perform(get("/providers/getById/1"))
                .andExpect(status().isNotFound());
    }
}
