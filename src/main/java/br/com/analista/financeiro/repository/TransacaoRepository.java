package br.com.analista.financeiro.repository;

import br.com.analista.financeiro.dto.ResumoCategoriaDTO;
import br.com.analista.financeiro.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("SELECT new br.com.analista.financeiro.dto.ResumoCategoriaDTO(t.categoria, SUM(t.valor)) " +
           "FROM Transacao t GROUP BY t.categoria")
    List<ResumoCategoriaDTO> findResumoPorCategoria();
}

