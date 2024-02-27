package prince.aktiveskochbuch.domain.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class Rezept {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String titel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String rezeptur;

    @ElementCollection
    @Column(name = "tag")
    private List<String> tags;

    @Column(nullable = false)
    private Type type;

    protected Rezept(String titel, String rezeptur, List<String> tags, String type) {
        this.titel = titel;
        this.rezeptur = rezeptur;
        this.tags = tags;
        this.type = Type.fromString(type);
    }

}
