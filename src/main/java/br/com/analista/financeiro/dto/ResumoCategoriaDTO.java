package br.com.analista.financeiro.dto;

import br.com.analista.financeiro.model.Categoria;
import java.math.BigDecimal;

public record ResumoCategoriaDTO(Categoria categoria, BigDecimal total) {
}

