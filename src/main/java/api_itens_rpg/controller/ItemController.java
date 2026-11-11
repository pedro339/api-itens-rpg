package api_itens_rpg.controller;

import api_itens_rpg.model.Item;
import api_itens_rpg.repository.ItemRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/itens")
public class ItemController {

    private final ItemRepository repository;

    public ItemController(ItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Item> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Item> cadastrar(@Valid @RequestBody Item item) {
        item.setId(null);

        Item novoItem = repository.save(item);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Item item) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        item.setId(id);

        Item itemAtualizado = repository.save(item);

        return ResponseEntity.ok(itemAtualizado);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
