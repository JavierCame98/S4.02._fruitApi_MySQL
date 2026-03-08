package cat.itacademy.s04.t02.n02.fruit.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fruits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double weightKg;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;
}
