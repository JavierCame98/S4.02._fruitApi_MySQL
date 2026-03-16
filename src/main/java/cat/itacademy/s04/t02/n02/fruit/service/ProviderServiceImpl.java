package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.exceptions.EntityAlreadyExistsException;
import cat.itacademy.s04.t02.n02.fruit.exceptions.ResourceNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.mappers.ProviderMapper;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit.repositories.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.repositories.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService{

    private final ProviderMapper providerMapper;
    private final ProviderRepository providerRepository;
    private final FruitRepository fruitRepository;

    @Override
    public ProviderResponseDto create(ProviderRequestDto providerRequestDto) {
        providerRepository.findByName(providerRequestDto.name())
                .ifPresent( fruit -> { throw new EntityAlreadyExistsException("This fruit already exists: " + providerRequestDto.name());
                });
        Provider provider = providerMapper.toEntity(providerRequestDto);
        Provider savedProvider = providerRepository.save(provider);
        return providerMapper.toResponseDto(savedProvider);
    }

    @Override
    public ProviderResponseDto update(Long id, ProviderRequestDto providerRequestDto) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Provider doesn't found with: ", id));

        providerRepository.findByName(providerRequestDto.name())
                .filter(providerFound -> !providerFound.getId().equals(id))
                .ifPresent(p -> { throw new EntityAlreadyExistsException("This provider name is already in use: " + providerRequestDto.name());
                });

        provider.setName(providerRequestDto.name());
        provider.setCountry(providerRequestDto.country());
        Provider providerUpdated = providerRepository.save(provider);
        return providerMapper.toResponseDto(providerUpdated);
    }

    @Override
    public void delete(Long id) {
       if(!providerRepository.existsById(id)){
           throw new ResourceNotFoundException("Cannot delete, ID not found: ", id);
       }

       if(fruitRepository.existsByProviderId(id)){
           throw new IllegalStateException("Cannot delete provider: It has associated fruits. " +
                   "Please reassign or delete the fruits first.");
       }

       providerRepository.deleteById(id);

    }

    @Override
    public ProviderResponseDto getById(Long id) {
        return providerRepository.findById(id)
                .map(providerMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ID: ", id));
    }

    @Override
    public List<ProviderResponseDto> getAll() {
        return providerRepository.findAll().stream()
                .map(providerMapper::toResponseDto)
                .toList();
    }
}
