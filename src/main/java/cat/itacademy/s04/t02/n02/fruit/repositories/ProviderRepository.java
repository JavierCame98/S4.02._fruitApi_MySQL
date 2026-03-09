package cat.itacademy.s04.t02.n02.fruit.repositories;

import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository <Provider, Long> {

    boolean existsByName (String name);
    Optional<Provider> findByName (String name);
}
