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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    @Transactional
    public FruitResponseDto update(Long id, FruitRequestDto fruitRequestDto) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This fruit doesn't exists: ", id));

        fruitRepository.findByName(fruitRequestDto.name())
                .filter(foundFruit -> !foundFruit.getId().equals(id))
                .ifPresent(f -> {
                    throw new EntityAlreadyExistsException("The name " + fruitRequestDto.name() + " is already taken by another fruit");
                });

        Provider provider = providerRepository.findById(fruitRequestDto.providerId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider", fruitRequestDto.providerId()));

        fruit.setName(fruitRequestDto.name());
        fruit.setWeightKg(fruitRequestDto.weightKg());
        fruit.setProvider(provider);

        Fruit updatedFruit = fruitRepository.save(fruit);

        return fruitMapper.toResponseDto(updatedFruit);
    }

    @Override
    public void delete(Long id) {
        if(!fruitRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot delete, ID not found: ", id);
        }
        fruitRepository.deleteById(id);

    }

    @Override
    public FruitResponseDto getById(Long id) {
        return fruitRepository.findById(id)
                .map(fruitMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ID: ", id));
    }

    @Override
    public List<FruitResponseDto> getAll() {
        return fruitRepository.findAll().stream()
                .map(fruitMapper::toResponseDto)
                .toList();
    }
}
