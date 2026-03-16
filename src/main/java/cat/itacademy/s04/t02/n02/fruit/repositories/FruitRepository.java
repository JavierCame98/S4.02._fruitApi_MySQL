package cat.itacademy.s04.t02.n02.fruit.repositories;

import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FruitRepository extends JpaRepository <Fruit, Long> {
    boolean existsByName(String name);
    boolean existsByProviderId (Long providerId);
    Optional<Fruit> findByName(String name);
    List<Fruit> findByProviderId(Long providerId);
}
