package cat.itacademy.s04.t02.n02.fruit.controller;

import cat.itacademy.s04.t02.n02.fruit.exceptions.EntityAlreadyExistsException;
import cat.itacademy.s04.t02.n02.fruit.exceptions.ResourceNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit.service.FruitServiceImpl;
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

@WebMvcTest(FruitController.class)
class FruitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FruitServiceImpl fruitService;

    private FruitRequestDto fruitRequestDto;
    private FruitResponseDto fruitResponseDto;
    private List<FruitResponseDto> fruitList;

    @BeforeEach
    void setUp() {
        fruitRequestDto = new FruitRequestDto("Apple", 2.5, 1L);
        fruitResponseDto = new FruitResponseDto(1L, "Apple", 2.5, 1L);
        fruitList = Arrays.asList(fruitResponseDto);
    }

    // ==================== CREATE TESTS ====================

    @Test
    void create_WhenRequestIsValid_ShouldReturn201Created() throws Exception {
        when(fruitService.create(any(FruitRequestDto.class))).thenReturn(fruitResponseDto);

        mockMvc.perform(post("/fruits/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fruitRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.weightKg").value(2.5));
    }

    @Test
    void create_WhenRequestIsInvalid_ShouldReturn400BadRequest() throws Exception {
        FruitRequestDto invalidRequest = new FruitRequestDto("", -5.0, 1L);

        mockMvc.perform(post("/fruits/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // ==================== UPDATE TESTS ====================

    @Test
    void update_WhenIdExists_ShouldReturn200Ok() throws Exception {
        when(fruitService.update(eq(1L), any(FruitRequestDto.class))).thenReturn(fruitResponseDto);

        mockMvc.perform(put("/fruits/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fruitRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void update_WhenIdDoesNotExist_ShouldReturn404NotFound() throws Exception {
        when(fruitService.update(eq(1L), any(FruitRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Fruit", 1L));

        mockMvc.perform(put("/fruits/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fruitRequestDto)))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE TESTS ====================

    @Test
    void delete_WhenIdExists_ShouldReturn204NoContent() throws Exception {
        doNothing().when(fruitService).delete(1L);

        mockMvc.perform(delete("/fruits/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_WhenIdDoesNotExist_ShouldReturn404NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Fruit", 1L)).when(fruitService).delete(1L);

        mockMvc.perform(delete("/fruits/delete/1"))
                .andExpect(status().isNotFound());
    }

    // ==================== GET ALL TESTS ====================

    @Test
    void getAll_ShouldReturn200Ok() throws Exception {
        when(fruitService.getAll()).thenReturn(fruitList);

        mockMvc.perform(get("/fruits/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[0].weightKg").value(2.5));
    }

    // ==================== GET BY ID TESTS ====================

    @Test
    void getById_WhenIdExists_ShouldReturn200Ok() throws Exception {
        when(fruitService.getById(1L)).thenReturn(fruitResponseDto);

        mockMvc.perform(get("/fruits/getOne/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void getById_WhenIdDoesNotExist_ShouldReturn404NotFound() throws Exception {
        when(fruitService.getById(1L)).thenThrow(new ResourceNotFoundException("Fruit", 1L));

        mockMvc.perform(get("/fruits/getOne/1"))
                .andExpect(status().isNotFound());
    }

    // ==================== GET BY PROVIDER ID TESTS ====================

    @Test
    void getFruitsByProvider_ShouldReturn200Ok() throws Exception {
        when(fruitService.getFruitsByProviderId(1L)).thenReturn(fruitList);

        mockMvc.perform(get("/fruits").param("providerID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"));
    }
}
