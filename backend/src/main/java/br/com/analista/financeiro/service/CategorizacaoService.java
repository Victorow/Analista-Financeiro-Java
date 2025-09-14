package br.com.analista.financeiro.service;

import br.com.analista.financeiro.model.Categoria;
import br.com.analista.financeiro.model.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class CategorizacaoService {

    private static final List<String> PALAVRAS_CHAVE_ALIMENTACAO = Arrays.asList("IFOOD", "RESTAURANTE", "SUPERMERCADO", "PADARIA");
    private static final List<String> PALAVRAS_CHAVE_TRANSPORTE = Arrays.asList("UBER", "POSTO");
    private static final List<String> PALAVRAS_CHAVE_LAZER = Arrays.asList("NETFLIX", "CINEMARK", "ACADEMIA");
    private static final List<String> PALAVRAS_CHAVE_MORADIA = Arrays.asList("ALUGUEL", "CONTA DE LUZ", "INTERNET");
    private static final List<String> PALAVRAS_CHAVE_SAUDE = Arrays.asList("FARMACIA");

    public Transacao categorizar(Transacao transacao) {
        String descricao = transacao.getDescricao().toUpperCase();

        // 1. Verifica se é uma receita
        if (transacao.getValor().compareTo(BigDecimal.ZERO) > 0) {
            transacao.setCategoria(Categoria.RECEITAS);
            return transacao;
        }

        // 2. Se for despesa, categoriza
        if (contemPalavraChave(descricao, PALAVRAS_CHAVE_ALIMENTACAO)) {
            transacao.setCategoria(Categoria.ALIMENTACAO);
        } else if (contemPalavraChave(descricao, PALAVRAS_CHAVE_TRANSPORTE)) {
            transacao.setCategoria(Categoria.TRANSPORTE);
        } else if (contemPalavraChave(descricao, PALAVRAS_CHAVE_LAZER)) {
            transacao.setCategoria(Categoria.LAZER);
        } else if (contemPalavraChave(descricao, PALAVRAS_CHAVE_MORADIA)) {
            transacao.setCategoria(Categoria.MORADIA);
        } else if (contemPalavraChave(descricao, PALAVRAS_CHAVE_SAUDE)) {
            transacao.setCategoria(Categoria.SAUDE);
        } else {
            transacao.setCategoria(Categoria.OUTROS);
        }

        return transacao;
    }

    private boolean contemPalavraChave(String texto, List<String> palavras) {
        return palavras.stream().anyMatch(texto::contains);
    }
}

