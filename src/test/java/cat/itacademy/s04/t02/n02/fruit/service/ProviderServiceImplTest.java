package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.exceptions.EntityAlreadyExistsException;
import cat.itacademy.s04.t02.n02.fruit.exceptions.ResourceNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.mappers.ProviderMapper;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit.repositories.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repositories.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServiceImplTest {

    @Mock
    private ProviderMapper providerMapper;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private ProviderServiceImpl providerService;

    private Provider provider;
    private ProviderRequestDto providerRequestDto;
    private ProviderResponseDto providerResponseDto;

    @BeforeEach
    void setUp() {
        provider = Provider.builder()
                .id(1L)
                .name("Provider1")
                .country("Spain")
                .build();

        providerRequestDto = new ProviderRequestDto("Provider1", "Spain");
        providerResponseDto = new ProviderResponseDto(1L, "Provider1", "Spain");
    }

    // ==================== CREATE TESTS ====================

    @Test
    void create_WhenNameIsUnique_ShouldReturnProvider() {
        when(providerRepository.findByName("Provider1")).thenReturn(Optional.empty());
        when(providerMapper.toEntity(providerRequestDto)).thenReturn(provider);
        when(providerRepository.save(provider)).thenReturn(provider);
        when(providerMapper.toResponseDto(provider)).thenReturn(providerResponseDto);

        ProviderResponseDto result = providerService.create(providerRequestDto);

        assertNotNull(result);
        assertEquals("Provider1", result.name());
        verify(providerRepository).save(provider);
    }

    @Test
    void create_WhenNameAlreadyExists_ShouldThrowEntityAlreadyExistsException() {
        when(providerRepository.findByName("Provider1")).thenReturn(Optional.of(provider));

        assertThrows(EntityAlreadyExistsException.class, () -> 
            providerService.create(providerRequestDto)
        );

        verify(providerRepository, never()).save(any());
    }

    // ==================== UPDATE TESTS ====================

    @Test
    void update_WhenProviderExists_ShouldReturnUpdatedProvider() {
        ProviderRequestDto updateDto = new ProviderRequestDto("Provider2", "France");
        Provider updatedProvider = Provider.builder()
                .id(1L)
                .name("Provider2")
                .country("France")
                .build();
        ProviderResponseDto updatedResponse = new ProviderResponseDto(1L, "Provider2", "France");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerRepository.findByName("Provider2")).thenReturn(Optional.empty());
        when(providerRepository.save(any(Provider.class))).thenReturn(updatedProvider);
        when(providerMapper.toResponseDto(updatedProvider)).thenReturn(updatedResponse);

        ProviderResponseDto result = providerService.update(1L, updateDto);

        assertNotNull(result);
        assertEquals("Provider2", result.name());
        assertEquals("France", result.country());
    }

    @Test
    void update_WhenProviderDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(providerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            providerService.update(1L, providerRequestDto)
        );

        verify(providerRepository, never()).save(any());
    }

    // ==================== DELETE TESTS ====================

    @Test
    void delete_WhenProviderExists_ShouldDeleteSuccessfully() {
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.existsByProviderId(1L)).thenReturn(false);
        doNothing().when(providerRepository).deleteById(1L);

        assertDoesNotThrow(() -> providerService.delete(1L));

        verify(providerRepository).deleteById(1L);
    }

    @Test
    void delete_WhenProviderHasFruits_ShouldThrowIllegalStateException() {
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.existsByProviderId(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> 
            providerService.delete(1L)
        );

        verify(providerRepository, never()).deleteById(any());
    }

    @Test
    void delete_WhenProviderDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(providerRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> 
            providerService.delete(1L)
        );

        verify(providerRepository, never()).deleteById(any());
    }

    // ==================== GET BY ID TESTS ====================

    @Test
    void getById_WhenProviderExists_ShouldReturnProvider() {
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerMapper.toResponseDto(provider)).thenReturn(providerResponseDto);

        ProviderResponseDto result = providerService.getById(1L);

        assertNotNull(result);
        assertEquals("Provider1", result.name());
    }

    @Test
    void getById_WhenProviderDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(providerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            providerService.getById(1L)
        );
    }

    // ==================== GET ALL TESTS ====================

    @Test
    void getAll_WhenProvidersExist_ShouldReturnList() {
        List<Provider> providers = Arrays.asList(provider);
        when(providerRepository.findAll()).thenReturn(providers);
        when(providerMapper.toResponseDto(provider)).thenReturn(providerResponseDto);

        List<ProviderResponseDto> result = providerService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Provider1", result.get(0).name());
    }
}
