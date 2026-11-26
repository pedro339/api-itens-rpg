package api_itens_rpg.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Personagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do personagem é obrigatório")
    @Size(min = 2, max = 100)
    private String nome;

    @NotNull(message = "O nível é obrigatório")
    @Min(1)
    @Max(100)
    private Integer nivel;

    @NotNull(message = "A classe é obrigatória")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "classe_id", nullable = false)
    private ClasseDoPersonagem classe;

    public Personagem() {
    }

    public Personagem(String nome, Integer nivel, ClasseDoPersonagem classe) {
        this.nome = nome;
        this.nivel = nivel;
        this.classe = classe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public ClasseDoPersonagem getClasse() {
        return classe;
    }

    public void setClasse(ClasseDoPersonagem classe) {
        this.classe = classe;
    }
}