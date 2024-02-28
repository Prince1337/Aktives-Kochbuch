package prince.aktiveskochbuch.domain.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class GlutenfreiesRezept extends Rezept{

    private boolean glutenfrei;

    public GlutenfreiesRezept(String titel, String rezeptur, List<String> tags) {
        super(titel, rezeptur, tags, String.valueOf(Type.GLUTENFREI));
        this.glutenfrei = true;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
