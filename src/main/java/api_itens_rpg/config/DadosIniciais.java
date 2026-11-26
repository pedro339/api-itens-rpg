package api_itens_rpg.config;

import api_itens_rpg.model.ClasseDoPersonagem;
import api_itens_rpg.repository.ClasseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DadosIniciais {

    @Bean
    CommandLineRunner inserirClassesIniciais(ClasseRepository repository) {

        return args -> {

            if (repository.count() == 0) {

                repository.saveAll(List.of(
                        new ClasseDoPersonagem("Guerreiro", "Físico"),
                        new ClasseDoPersonagem("Mago", "Mágico"),
                        new ClasseDoPersonagem("Arqueiro", "Físico à distância"),
                        new ClasseDoPersonagem("Ladino", "Físico"),
                        new ClasseDoPersonagem("Clérigo", "Mágico")
                ));
            }
        };
    }
}