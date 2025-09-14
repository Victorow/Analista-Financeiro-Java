package br.com.analista.financeiro.dto;

import br.com.analista.financeiro.model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResumoCategoriaDTO {

    private Categoria categoria;
    private BigDecimal total;

}

