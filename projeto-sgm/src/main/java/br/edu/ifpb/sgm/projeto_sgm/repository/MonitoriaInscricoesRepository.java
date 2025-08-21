package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import br.edu.ifpb.sgm.projeto_sgm.model.MonitoriaInscritos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitoriaInscricoesRepository extends JpaRepository<MonitoriaInscritos, Long> {
    boolean existsByAlunoAndMonitoria(Aluno aluno, Monitoria monitoria);

    List<MonitoriaInscritos> findByAluno(Aluno aluno);
}
