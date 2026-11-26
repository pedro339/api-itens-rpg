package api_itens_rpg.repository;

import api_itens_rpg.model.ClasseDoPersonagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasseRepository extends JpaRepository<ClasseDoPersonagem, Long> {
}