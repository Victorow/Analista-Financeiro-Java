package br.com.analista.financeiro.service;

import br.com.analista.financeiro.model.Transacao;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExtratoService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<Transacao> lerExtratoPdf(MultipartFile file) throws IOException {
        List<Transacao> transacoes = new ArrayList<>();

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String textoCompleto = pdfStripper.getText(document);
            String[] linhas = textoCompleto.split("\\r?\\n");

            // ATENÇÃO: Esta expressão regular é um EXEMPLO e precisa ser
            // ajustada para o layout EXATO do seu extrato bancário.
            Pattern pattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})\\s+(.+?)\\s+([\\d.,-]+)\\s*(D|C)?$");

            for (String linha : linhas) {
                Matcher matcher = pattern.matcher(linha.trim());

                if (matcher.find()) {
                    try {
                        LocalDate data = LocalDate.parse(matcher.group(1), DATE_FORMATTER);
                        String descricao = matcher.group(2).trim();
                        String valorStr = matcher.group(3).replace(".", "").replace(",", ".");
                        BigDecimal valor = new BigDecimal(valorStr);
                        
                        transacoes.add(new Transacao(data, descricao, valor));
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha: '" + linha + "'. Causa: " + e.getMessage());
                    }
                }
            }
        }
        return transacoes;
    }
}

