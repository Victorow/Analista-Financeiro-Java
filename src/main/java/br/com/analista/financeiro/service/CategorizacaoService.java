package br.com.analista.financeiro.service;

import br.com.analista.financeiro.model.Categoria;
import br.com.analista.financeiro.model.Transacao;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorizacaoService {

    public void categorizarTransacoes(List<Transacao> transacoes) {
        for (Transacao transacao : transacoes) {
            transacao.setCategoria(definirCategoriaPelaDescricao(transacao.getDescricao()));
        }
    }

    private Categoria definirCategoriaPelaDescricao(String descricao) {
        String desc = descricao.toUpperCase();

        if (contemPalavra(desc, "IFOOD", "RESTAURANTE", "MERCADO")) {
            return Categoria.ALIMENTACAO;
        }
        if (contemPalavra(desc, "UBER", "99TAXI", "POSTO", "GASOLINA")) {
            return Categoria.TRANSPORTE;
        }
        if (contemPalavra(desc, "ALUGUEL", "CONDOMINIO", "ENEL")) {
            return Categoria.MORADIA;
        }
        if (contemPalavra(desc, "NETFLIX", "SPOTIFY", "CINEMA")) {
            return Categoria.LAZER;
        }
        if (contemPalavra(desc, "SALARIO", "PAGAMENTO")) {
            return Categoria.SALARIO;
        }
        if (contemPalavra(desc, "FARMACIA", "HOSPITAL")) {
            return Categoria.SAUDE;
        }

        return Categoria.OUTROS;
    }
    
    private boolean contemPalavra(String texto, String... palavras) {
        for (String palavra : palavras) {
            if (texto.contains(palavra)) {
                return true;
            }
        }
        return false;
    }
}

