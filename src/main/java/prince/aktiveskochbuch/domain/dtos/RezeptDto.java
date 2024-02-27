package prince.aktiveskochbuch.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RezeptDto {

    private String titel;
    private String rezeptur;
    private List<String> tags;
    private String type;
}
