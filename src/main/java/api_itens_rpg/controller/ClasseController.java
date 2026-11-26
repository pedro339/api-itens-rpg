package api_itens_rpg.controller;

import api_itens_rpg.model.ClasseDoPersonagem;
import api_itens_rpg.repository.ClasseRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClasseController {

    private final ClasseRepository repository;

    public ClasseController(ClasseRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ClasseDoPersonagem> listarTodas() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClasseDoPersonagem> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClasseDoPersonagem> cadastrar(
            @Valid @RequestBody ClasseDoPersonagem classe) {

        classe.setId(null);

        ClasseDoPersonagem novaClasse = repository.save(classe);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaClasse);
    }
}