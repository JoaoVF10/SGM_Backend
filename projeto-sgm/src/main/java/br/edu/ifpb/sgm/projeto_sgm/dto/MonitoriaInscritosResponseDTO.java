package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import br.edu.ifpb.sgm.projeto_sgm.model.MonitoriaInscritos;
import br.edu.ifpb.sgm.projeto_sgm.model.embeddable.MonitoriaInscritoId;
import lombok.Data;

@Data
public class MonitoriaInscritosResponseDTO {

    private MonitoriaInscritoId id;
    private MonitoriaResponseDTO monitoriaResponseDTO;
    private AlunoResponseDTO alunoResponseDTO;
    private boolean selecionado;

    public MonitoriaInscritosResponseDTO(MonitoriaInscritos inscricao) {
        this.id = inscricao.getId();
        this.monitoriaResponseDTO = new MonitoriaResponseDTO(inscricao.getMonitoria());
        this.alunoResponseDTO = new AlunoResponseDTO(inscricao.getAluno());
        this.selecionado = inscricao.isSelecionado();
    }


}
