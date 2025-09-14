package br.com.analista.financeiro.repository;

import br.com.analista.financeiro.dto.ResumoCategoriaDTO;
import br.com.analista.financeiro.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    /**
     * Agrupa as transações por categoria e soma os seus valores, retornando apenas as despesas.
     * A soma é multiplicada por -1 para retornar um valor positivo para o resumo.
     * Exclui explicitamente as transações categorizadas como 'RECEITAS'.
     *
     * @return Uma lista de DTOs com a categoria e o total gasto.
     */
    @Query("SELECT new br.com.analista.financeiro.dto.ResumoCategoriaDTO(t.categoria, SUM(t.valor) * -1) FROM Transacao t WHERE t.categoria <> 'RECEITAS' GROUP BY t.categoria")
    List<ResumoCategoriaDTO> getResumoPorCategoria();

    /**
     * Agrupa as transações de receitas e soma os seus valores.
     *
     * @return Uma lista de DTOs com a categoria da receita e o total recebido.
     */
    @Query("SELECT new br.com.analista.financeiro.dto.ResumoCategoriaDTO(t.categoria, SUM(t.valor)) FROM Transacao t WHERE t.categoria = 'RECEITAS' GROUP BY t.categoria")
    List<ResumoCategoriaDTO> getResumoReceitas();
}

