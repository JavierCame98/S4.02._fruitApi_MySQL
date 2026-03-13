package cat.itacademy.s04.t02.n02.fruit.service;

import cat.itacademy.s04.t02.n02.fruit.model.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderResponseDto;

import java.util.List;

public interface ProviderService {
    ProviderResponseDto create (ProviderRequestDto providerRequestDto);
    ProviderResponseDto update (Long id, ProviderRequestDto providerRequestDto);
    void delete(Long id);
    ProviderResponseDto getById(Long id);
    List<ProviderResponseDto> getAll();

}
