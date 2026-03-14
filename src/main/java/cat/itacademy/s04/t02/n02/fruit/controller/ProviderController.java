package cat.itacademy.s04.t02.n02.fruit.controller;

import cat.itacademy.s04.t02.n02.fruit.model.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit.service.ProviderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final ProviderServiceImpl providerServiceImpl;

    public ProviderController(ProviderServiceImpl providerServiceImpl) {
        this.providerServiceImpl = providerServiceImpl;
    }

    @PostMapping("/add")
    public ResponseEntity<ProviderResponseDto> create (@Valid @RequestBody ProviderRequestDto providerRequestDto){
        ProviderResponseDto createdProvider = providerServiceImpl.create(providerRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProvider.id())
                .toUri();

        return ResponseEntity.created(location).body(createdProvider);
    }
}
