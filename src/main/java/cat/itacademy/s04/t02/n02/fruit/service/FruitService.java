package cat.itacademy.s04.t02.n02.fruit.service;


import cat.itacademy.s04.t02.n02.fruit.model.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.FruitResponseDto;

import java.util.List;
import java.util.Optional;

public interface FruitService {

    FruitResponseDto create (FruitRequestDto fruitRequestDto);
    FruitResponseDto update (Long id, FruitRequestDto fruitRequestDto);
    void delete (Long id);
    FruitResponseDto getById (Long id);
    List<FruitResponseDto> getAll();

}
