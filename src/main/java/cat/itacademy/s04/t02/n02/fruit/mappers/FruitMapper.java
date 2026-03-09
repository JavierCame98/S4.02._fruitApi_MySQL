package cat.itacademy.s04.t02.n02.fruit.mappers;

import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.model.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import org.springframework.stereotype.Component;

@Component
public class FruitMapper {

    public Fruit toEntity (FruitRequestDto fruitRequestDto, Provider provider){
        return Fruit.builder()
                .name(fruitRequestDto.name())
                .weightKg(fruitRequestDto.weightKg())
                .provider(provider)
                .build();
    }

    public FruitResponseDto toResponseDto (Fruit fruit){
        return new FruitResponseDto(
                fruit.getId(),
                fruit.getName(),
                fruit.getWeightKg(),
                fruit.getProvider().getId()
        );
    }


}
