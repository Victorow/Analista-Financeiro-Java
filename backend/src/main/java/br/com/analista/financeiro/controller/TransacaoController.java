package br.com.analista.financeiro.controller;

import br.com.analista.financeiro.dto.ResumoCategoriaDTO;
import br.com.analista.financeiro.model.Transacao;
import br.com.analista.financeiro.repository.TransacaoRepository;
import br.com.analista.financeiro.service.ExtratoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transacoes")
@CrossOrigin(origins = "*")
public class TransacaoController {

    private final ExtratoService extratoService;
    private final TransacaoRepository transacaoRepository;

    public TransacaoController(ExtratoService extratoService, TransacaoRepository transacaoRepository) {
        this.extratoService = extratoService;
        this.transacaoRepository = transacaoRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<Transacao>> uploadExtrato(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try (InputStream inputStream = file.getInputStream()) {
            transacaoRepository.deleteAll();
            List<Transacao> transacoes = extratoService.processarExtrato(inputStream);
            transacaoRepository.saveAll(transacoes);
            return ResponseEntity.ok(transacoes);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public List<Transacao> listarTransacoes() {
        return transacaoRepository.findAll();
    }

    @GetMapping("/resumo-despesas")
    public List<ResumoCategoriaDTO> getResumoDespesas() {
        return transacaoRepository.getResumoPorCategoria();
    }

    @GetMapping("/resumo-financeiro")
    public ResponseEntity<Map<String, Double>> getResumoFinanceiro() {
        double totalDespesas = transacaoRepository.getResumoPorCategoria().stream()
                .map(ResumoCategoriaDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();

        double totalReceitas = transacaoRepository.getResumoReceitas().stream()
                .map(ResumoCategoriaDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();

        double saldo = totalReceitas - totalDespesas;

        Map<String, Double> resumo = new HashMap<>();
        resumo.put("totalReceitas", totalReceitas);
        resumo.put("totalDespesas", totalDespesas);
        resumo.put("saldo", saldo);

        return ResponseEntity.ok(resumo);
    }
}

