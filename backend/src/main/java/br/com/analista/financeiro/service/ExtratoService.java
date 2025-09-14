package br.com.analista.financeiro.service;

import br.com.analista.financeiro.model.Transacao;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExtratoService {

    private final CategorizacaoService categorizacaoService;

    public ExtratoService(CategorizacaoService categorizacaoService) {
        this.categorizacaoService = categorizacaoService;
    }

    // Padrão Regex ajustado para o formato dd/mm/yyyy, descrição, valor e tipo (D/C)
    private static final Pattern PADRAO_TRANSACAO = Pattern.compile(
            "(\\d{2}/\\d{2}/\\d{4})\\s+(.*?)\\s+([\\d.,]+)\\s+([DC])"
    );

    public List<Transacao> processarExtrato(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(document);
            return extrairTransacoesDoTexto(texto);
        }
    }

    private List<Transacao> extrairTransacoesDoTexto(String texto) {
        List<Transacao> transacoes = new ArrayList<>();
        Matcher matcher = PADRAO_TRANSACAO.matcher(texto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (matcher.find()) {
            try {
                String dataStr = matcher.group(1);
                String descricao = matcher.group(2).trim();
                String valorStr = matcher.group(3).replace(".", "").replace(",", ".");
                String tipo = matcher.group(4);

                LocalDate data = LocalDate.parse(dataStr, formatter);
                BigDecimal valor = new BigDecimal(valorStr);

                // Se for Débito (D), o valor deve ser negativo
                if ("D".equals(tipo)) {
                    valor = valor.negate();
                }

                Transacao transacao = new Transacao(data, descricao, valor);

                // A categorização é feita diretamente no objeto 'transacao'
                categorizacaoService.categorizar(transacao);

                transacoes.add(transacao);

            } catch (Exception e) {
                // Ignora linhas que não correspondem ao padrão para evitar que a aplicação pare
                System.err.println("Linha inválida ou erro de formatação no extrato: " + e.getMessage());
            }
        }
        return transacoes;
    }
}

