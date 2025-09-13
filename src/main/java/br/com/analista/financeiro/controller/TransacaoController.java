package br.com.analista.financeiro.controller;

import br.com.analista.financeiro.dto.ResumoCategoriaDTO;
import br.com.analista.financeiro.model.Transacao;
import br.com.analista.financeiro.repository.TransacaoRepository;
import br.com.analista.financeiro.service.CategorizacaoService;
import br.com.analista.financeiro.service.ExtratoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private final ExtratoService extratoService;
    private final CategorizacaoService categorizacaoService;
    private final TransacaoRepository transacaoRepository;

    public TransacaoController(ExtratoService extratoService, CategorizacaoService categorizacaoService, TransacaoRepository transacaoRepository) {
        this.extratoService = extratoService;
        this.categorizacaoService = categorizacaoService;
        this.transacaoRepository = transacaoRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<Transacao>> uploadExtrato(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            List<Transacao> transacoes = extratoService.lerExtratoPdf(file);
            categorizacaoService.categorizarTransacoes(transacoes);
            List<Transacao> transacoesSalvas = transacaoRepository.saveAll(transacoes);
            return ResponseEntity.status(HttpStatus.CREATED).body(transacoesSalvas);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao processar o arquivo PDF.", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listarTodasTransacoes() {
        List<Transacao> transacoes = transacaoRepository.findAll();
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<ResumoCategoriaDTO>> getResumoPorCategoria() {
        List<ResumoCategoriaDTO> resumo = transacaoRepository.findResumoPorCategoria();
        return ResponseEntity.ok(resumo);
    }
}

