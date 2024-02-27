package prince.aktiveskochbuch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import prince.aktiveskochbuch.adapter.db.RezeptRepository;
import prince.aktiveskochbuch.domain.models.Rezept;

import java.util.List;

@SpringBootApplication
public class AktivesKochbuchApplication {

    public static void main(String[] args) {
        SpringApplication.run(AktivesKochbuchApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RezeptRepository rezeptRepository) {
        return args -> {
            rezeptRepository.save(new Rezept(1L, "Spaghetti Bolognese", "Spaghetti, Ei, Speck, Parmesan, Pfeffer, Salz", List.of("Italienisch", "Fleisch")));
            rezeptRepository.save(new Rezept(2L, "Pancakes", "Mehl, Milch, Ei, Zucker, Salz, Butter", List.of("Amerikanisch", "Frühstück")));
            rezeptRepository.save(new Rezept(3L, "Kartoffelsalat", "Kartoffeln, Zwiebeln, Essig, Öl, Salz, Pfeffer", List.of("Deutsch", "Salat")));
            rezeptRepository.save(new Rezept(4L, "Käsespätzle", "Mehl, Ei, Milch, Käse, Zwiebeln, Salz, Pfeffer", List.of("Deutsch", "Käse")));
            rezeptRepository.save(new Rezept(5L, "Lasagne", "Lasagneplatten, Hackfleisch, Tomaten, Zwiebeln, Knoblauch, Salz, Pfeffer", List.of("Italienisch", "Fleisch")));

        };
    }

}
