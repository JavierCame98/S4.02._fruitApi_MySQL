package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.exceptions.EntityAlreadyExistsException;
import cat.itacademy.s04.t02.n02.fruit.exceptions.ResourceNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.mappers.FruitMapper;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
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
class FruitServiceImplTest {

    @Mock
    private FruitRepository fruitRepository;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private FruitMapper fruitMapper;

    @InjectMocks
    private FruitServiceImpl fruitService;

    private Fruit fruit;
    private Provider provider;
    private FruitRequestDto fruitRequestDto;
    private FruitResponseDto fruitResponseDto;

    @BeforeEach
    void setUp() {
        provider = Provider.builder()
                .id(1L)
                .name("Provider1")
                .country("Spain")
                .build();

        fruit = Fruit.builder()
                .id(1L)
                .name("Apple")
                .weightKg(2.5)
                .provider(provider)
                .build();

        fruitRequestDto = new FruitRequestDto("Apple", 2.5, 1L);
        fruitResponseDto = new FruitResponseDto(1L, "Apple", 2.5, 1L);
    }

    // ==================== CREATE TESTS ====================

    @Test
    void create_WhenProviderExistsAndNameIsUnique_ShouldReturnFruit() {
        when(fruitRepository.findByName("Apple")).thenReturn(Optional.empty());
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(fruitMapper.toEntity(fruitRequestDto, provider)).thenReturn(fruit);
        when(fruitRepository.save(fruit)).thenReturn(fruit);
        when(fruitMapper.toResponseDto(fruit)).thenReturn(fruitResponseDto);

        FruitResponseDto result = fruitService.create(fruitRequestDto);

        assertNotNull(result);
        assertEquals("Apple", result.name());
        verify(fruitRepository).save(fruit);
    }

    @Test
    void create_WhenProviderDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(fruitRepository.findByName("Apple")).thenReturn(Optional.empty());
        when(providerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            fruitService.create(fruitRequestDto)
        );

        verify(fruitRepository, never()).save(any());
    }

    @Test
    void create_WhenNameAlreadyExists_ShouldThrowEntityAlreadyExistsException() {
        when(fruitRepository.findByName("Apple")).thenReturn(Optional.of(fruit));

        assertThrows(EntityAlreadyExistsException.class, () -> 
            fruitService.create(fruitRequestDto)
        );

        verify(providerRepository, never()).findById(any());
        verify(fruitRepository, never()).save(any());
    }

    // ==================== UPDATE TESTS ====================

    @Test
    void update_WhenFruitExistsAndProviderExists_ShouldReturnUpdatedFruit() {
        FruitRequestDto updateDto = new FruitRequestDto("Banana", 3.0, 1L);
        Fruit updatedFruit = Fruit.builder()
                .id(1L)
                .name("Banana")
                .weightKg(3.0)
                .provider(provider)
                .build();
        FruitResponseDto updatedResponse = new FruitResponseDto(1L, "Banana", 3.0, 1L);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));
        when(fruitRepository.findByName("Banana")).thenReturn(Optional.empty());
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(updatedFruit);
        when(fruitMapper.toResponseDto(updatedFruit)).thenReturn(updatedResponse);

        FruitResponseDto result = fruitService.update(1L, updateDto);

        assertNotNull(result);
        assertEquals("Banana", result.name());
        assertEquals(3.0, result.weightKg());
    }

    @Test
    void update_WhenFruitDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(fruitRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            fruitService.update(1L, fruitRequestDto)
        );

        verify(fruitRepository, never()).save(any());
    }

    // ==================== DELETE TESTS ====================

    @Test
    void delete_WhenFruitExists_ShouldDeleteSuccessfully() {
        when(fruitRepository.existsById(1L)).thenReturn(true);
        doNothing().when(fruitRepository).deleteById(1L);

        assertDoesNotThrow(() -> fruitService.delete(1L));

        verify(fruitRepository).deleteById(1L);
    }

    @Test
    void delete_WhenFruitDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(fruitRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> 
            fruitService.delete(1L)
        );

        verify(fruitRepository, never()).deleteById(any());
    }

    // ==================== GET BY ID TESTS ====================

    @Test
    void getById_WhenFruitExists_ShouldReturnFruit() {
        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));
        when(fruitMapper.toResponseDto(fruit)).thenReturn(fruitResponseDto);

        FruitResponseDto result = fruitService.getById(1L);

        assertNotNull(result);
        assertEquals("Apple", result.name());
    }

    @Test
    void getById_WhenFruitDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(fruitRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            fruitService.getById(1L)
        );
    }

    // ==================== GET ALL TESTS ====================

    @Test
    void getAll_WhenFruitsExist_ShouldReturnList() {
        List<Fruit> fruits = Arrays.asList(fruit);
        when(fruitRepository.findAll()).thenReturn(fruits);
        when(fruitMapper.toResponseDto(fruit)).thenReturn(fruitResponseDto);

        List<FruitResponseDto> result = fruitService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).name());
    }

    // ==================== GET BY PROVIDER ID TESTS ====================

    @Test
    void getFruitsByProviderId_WhenProviderExists_ShouldReturnFruitsList() {
        List<Fruit> fruits = Arrays.asList(fruit);
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.findByProviderId(1L)).thenReturn(fruits);
        when(fruitMapper.toResponseDto(fruit)).thenReturn(fruitResponseDto);

        List<FruitResponseDto> result = fruitService.getFruitsByProviderId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getFruitsByProviderId_WhenProviderDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(providerRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> 
            fruitService.getFruitsByProviderId(1L)
        );
    }
}
