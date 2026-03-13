package cat.itacademy.s04.t02.n02.fruit.mappers;


import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {

    public Provider toEntity (ProviderRequestDto providerRequestDto){
        return Provider.builder()
                .name(providerRequestDto.name())
                .country(providerRequestDto.country())
                .build();
    }

    public ProviderResponseDto toResponseDto (Provider provider){
        return new ProviderResponseDto(
                provider.getId(),
                provider.getName(),
                provider.getCountry()
        );
    }
}
