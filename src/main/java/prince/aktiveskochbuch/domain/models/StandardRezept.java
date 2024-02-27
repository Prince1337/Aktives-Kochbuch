package prince.aktiveskochbuch.domain.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class StandardRezept extends Rezept {

    public StandardRezept(String titel, String rezeptur, List<String> tags) {
        super(titel, rezeptur, tags, String.valueOf(Type.STANDARD));
    }

}
