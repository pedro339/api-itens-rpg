package api_itens_rpg.dto;

import jakarta.validation.constraints.*;

public class PersonagemRequest {

    @NotBlank(message = "O nome do personagem é obrigatório")
    @Size(min = 2, max = 100)
    private String nome;

    @NotNull(message = "O nível é obrigatório")
    @Min(1)
    @Max(100)
    private Integer nivel;

    @NotNull(message = "A classe é obrigatória")
    private Long classeId;

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

    public Long getClasseId() {
        return classeId;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }
}