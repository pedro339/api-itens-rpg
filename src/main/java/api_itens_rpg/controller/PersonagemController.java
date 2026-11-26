package api_itens_rpg.controller;

import api_itens_rpg.dto.PersonagemRequest;
import api_itens_rpg.model.ClasseDoPersonagem;
import api_itens_rpg.model.Personagem;
import api_itens_rpg.repository.ClasseRepository;
import api_itens_rpg.repository.PersonagemRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personagens")
public class PersonagemController {

    private final PersonagemRepository personagemRepository;
    private final ClasseRepository classeRepository;

    public PersonagemController(PersonagemRepository personagemRepository,
                                ClasseRepository classeRepository) {
        this.personagemRepository = personagemRepository;
        this.classeRepository = classeRepository;
    }

    @GetMapping
    public List<Personagem> listarTodos() {
        return personagemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Personagem> buscarPorId(@PathVariable Long id) {
        return personagemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(
            @Valid @RequestBody PersonagemRequest request) {

        Optional<ClasseDoPersonagem> classe =
                classeRepository.findById(request.getClasseId());

        if (classe.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("A classe informada não existe.");
        }

        Personagem personagem = new Personagem(
                request.getNome(),
                request.getNivel(),
                classe.get()
        );

        Personagem novoPersonagem = personagemRepository.save(personagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoPersonagem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonagemRequest request) {

        if (!personagemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Optional<ClasseDoPersonagem> classe =
                classeRepository.findById(request.getClasseId());

        if (classe.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("A classe informada não existe.");
        }

        Personagem personagem = new Personagem(
                request.getNome(),
                request.getNivel(),
                classe.get()
        );

        personagem.setId(id);

        return ResponseEntity.ok(personagemRepository.save(personagem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!personagemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        personagemRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}