package api_itens_rpg.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "A raridade é obrigatória")
    private String raridade;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "O nível mínimo é obrigatório")
    @Min(value = 1, message = "O nível mínimo deve ser pelo menos 1")
    @Max(value = 100, message = "O nível mínimo deve ser no máximo 100")
    private Integer nivelMinimo;

    public Item() {
    }

    public Item(String nome, String raridade, BigDecimal preco, Integer nivelMinimo) {
        this.nome = nome;
        this.raridade = raridade;
        this.preco = preco;
        this.nivelMinimo = nivelMinimo;
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
    public String getRaridade() {
        return raridade;
    }
    public void setRaridade(String raridade) {
        this.raridade = raridade;
    }
    public BigDecimal getPreco() {
        return preco;
    }
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    public Integer getNivelMinimo() {
        return nivelMinimo;
    }
    public void setNivelMinimo(Integer nivelMinimo) {
        this.nivelMinimo = nivelMinimo;
    }
}
