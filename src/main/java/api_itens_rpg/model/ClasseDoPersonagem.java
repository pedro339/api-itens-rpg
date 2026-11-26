package api_itens_rpg.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class ClasseDoPersonagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da classe é obrigatório")
    @Size(min = 2, max = 50)
    private String nomeClasse;

    @NotBlank(message = "O tipo de dano é obrigatório")
    @Size(min = 2, max = 50)
    private String tipoDano;

    public ClasseDoPersonagem() {
    }

    public ClasseDoPersonagem(String nomeClasse, String tipoDano) {
        this.nomeClasse = nomeClasse;
        this.tipoDano = tipoDano;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    public String getTipoDano() {
        return tipoDano;
    }

    public void setTipoDano(String tipoDano) {
        this.tipoDano = tipoDano;
    }
}