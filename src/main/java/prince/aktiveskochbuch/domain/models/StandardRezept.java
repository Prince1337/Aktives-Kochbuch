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
public class StandardRezept extends Rezept {

    public StandardRezept(String titel, String rezeptur, List<String> tags) {
        super(titel, rezeptur, tags, String.valueOf(Type.STANDARD));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
