package prince.aktiveskochbuch.domain.models;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class VegetarischesRezept extends Rezept{

    private boolean vegan;

    public VegetarischesRezept(String titel, String rezeptur, List<String> tags) {
        super(titel, rezeptur, tags, String.valueOf(Type.VEGETARISCH));
        this.vegan = true;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
