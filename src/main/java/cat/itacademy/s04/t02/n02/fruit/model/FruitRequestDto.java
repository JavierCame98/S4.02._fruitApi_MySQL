package cat.itacademy.s04.t02.n02.fruit.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FruitRequestDto(

        Long id,

        @NotBlank(message = "Name can't be empty")
        String name,

        @NotNull(message = "Weight can't be null")
        @Positive(message = "Weight has to be positive")
        Double weightKg,

        @NotNull(message = "Must attach a provider")
        Long providerId

){}
