package cat.itacademy.s04.t02.n02.fruit.controller;


import cat.itacademy.s04.t02.n02.fruit.model.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit.service.FruitServiceImpl;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/fruits")
public class FruitController {

    private final FruitServiceImpl fruitServiceImpl;
    public FruitController(FruitServiceImpl fruitServiceImpl) {
        this.fruitServiceImpl = fruitServiceImpl;
    }

    @PostMapping("/add")
    public ResponseEntity<FruitResponseDto> create (@Valid @RequestBody FruitRequestDto fruitRequestDto){
        FruitResponseDto createdFruit = fruitServiceImpl.create(fruitRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdFruit.id())
                .toUri();

        return ResponseEntity.created(location).body(createdFruit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitResponseDto> update (@PathVariable Long id, @Valid @RequestBody FruitRequestDto fruitDto){
        return ResponseEntity.ok(fruitServiceImpl.update(id, fruitDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        fruitServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FruitResponseDto>> getAll (){
        List<FruitResponseDto> fruits = fruitServiceImpl.getAll();
        return ResponseEntity.ok(fruits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitResponseDto> getById (@PathVariable Long id){
        return ResponseEntity.ok(fruitServiceImpl.getById(id));
    }


}
