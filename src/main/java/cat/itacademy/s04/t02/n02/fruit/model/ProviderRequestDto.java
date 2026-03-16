package cat.itacademy.s04.t02.n02.fruit.model;

import jakarta.validation.constraints.NotBlank;

public record ProviderRequestDto (@NotBlank(message = "Provider has to have a name") String name, @NotBlank(message = "Porvider has to have a country") String country)
{}
