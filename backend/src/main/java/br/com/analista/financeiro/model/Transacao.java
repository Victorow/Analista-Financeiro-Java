package br.com.analista.financeiro.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private String descricao;

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    public Transacao(LocalDate data, String descricao, BigDecimal valor) {
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = Categoria.OUTROS;
    }
}

