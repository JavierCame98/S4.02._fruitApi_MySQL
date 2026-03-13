package cat.itacademy.s04.t02.n02.fruit.controller;


import cat.itacademy.s04.t02.n02.fruit.model.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit.service.FruitServiceImpl;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

        URI lcoation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdFruit.id())
                .toUri();

        return ResponseEntity.created(lcoation).body(createdFruit);
    }


}
