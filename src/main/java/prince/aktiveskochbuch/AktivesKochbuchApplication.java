package prince.aktiveskochbuch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import prince.aktiveskochbuch.adapter.db.RezeptRepository;
import prince.aktiveskochbuch.domain.models.GlutenfreiesRezept;
import prince.aktiveskochbuch.domain.models.StandardRezept;
import prince.aktiveskochbuch.domain.models.VegetarischesRezept;

import java.util.List;

@SpringBootApplication
@EnableAsync
public class AktivesKochbuchApplication {

    public static void main(String[] args) {
        SpringApplication.run(AktivesKochbuchApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RezeptRepository rezeptRepository) {
        return args -> {

            rezeptRepository.save(new StandardRezept("Spaghetti Bolognese", "Spaghetti, Ei, Speck, Parmesan, Pfeffer, Salz", List.of("Italienisch", "Fleisch")));
            rezeptRepository.save(new StandardRezept("Pasta Carbonara", "Pasta, Ei, Speck, Parmesan, Pfeffer, Salz", List.of("Südeuropäisch", "Fleisch")));

            rezeptRepository.save(new VegetarischesRezept("Vegetarische Lasagne", "Lasagneplatten, Tomatensauce, Gemüse, Bechamelsauce, Käse", List.of("Gesund", "Käse")));
            rezeptRepository.save(new VegetarischesRezept("Vegetarische Pizza", "Pizzateig, Tomatensauce, Gemüse, Käse", List.of("Gesund", "Gemüse")));

            rezeptRepository.save(new GlutenfreiesRezept("Glutenfreier Kuchen", "Glutenfreies Mehl, Ei, Zucker, Butter", List.of("Kuchen", "Glutenfrei")));

        };
    }
}
