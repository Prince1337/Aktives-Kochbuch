package prince.aktiveskochbuch.domain.models;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GlutenfreiesRezept extends Rezept{

    private boolean glutenfrei;

    public GlutenfreiesRezept(String titel, String rezeptur, List<String> tags) {
        super(titel, rezeptur, tags, String.valueOf(Type.GLUTENFREI));
        this.glutenfrei = true;
    }

}
