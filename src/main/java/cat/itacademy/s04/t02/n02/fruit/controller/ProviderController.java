package cat.itacademy.s04.t02.n02.fruit.controller;

import cat.itacademy.s04.t02.n02.fruit.model.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.fruit.service.ProviderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @PutMapping("/update/{id}")
    public ResponseEntity<ProviderResponseDto> update (@PathVariable Long id, @Valid @RequestBody ProviderRequestDto providerRequestDto){
        return ResponseEntity.ok(providerServiceImpl.update(id, providerRequestDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        providerServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProviderResponseDto>> getAll(){
        List<ProviderResponseDto> providersList = providerServiceImpl.getAll();
        return ResponseEntity.ok(providersList);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProviderResponseDto> getById (@PathVariable Long id){
        ProviderResponseDto getProvider = providerServiceImpl.getById(id);
        return ResponseEntity.ok(getProvider);
    }


}
