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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FruitServiceImpl implements FruitService{

    private final FruitRepository fruitRepository;
    private final ProviderRepository providerRepository;
    private final FruitMapper fruitMapper;


    @Override
    public FruitResponseDto create(FruitRequestDto fruitRequestDto) {
        fruitRepository.findByName(fruitRequestDto.name())
                .ifPresent(fruit -> { throw new EntityAlreadyExistsException("This fruit already exists: " + fruitRequestDto.name());
                });

        Provider provider = providerRepository.findById(fruitRequestDto.providerId())
                .orElseThrow(() -> new ResourceNotFoundException("This provider doesn't exists: ", fruitRequestDto.providerId()));

        Fruit fruit = fruitMapper.toEntity(fruitRequestDto,provider);
        Fruit savedFuit = fruitRepository.save(fruit);
        return fruitMapper.toResponseDto(savedFuit);
    }

    @Override
    public FruitResponseDto update(Long id, FruitRequestDto fruitRequestDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public FruitRequestDto getById(Long id) {
        return null;
    }

    @Override
    public List<FruitRequestDto> getAll() {
        return List.of();
    }
}
