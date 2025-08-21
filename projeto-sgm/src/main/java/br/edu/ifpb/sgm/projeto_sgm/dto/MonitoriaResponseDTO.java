package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import lombok.Data;
import java.util.List;

@Data
public class MonitoriaResponseDTO {

    private Long id;
    private DisciplinaResponseDTO disciplinaResponseDTO;
    private int numeroVaga;
    private int numeroVagaBolsa;
    private int cargaHoraria;
    private ProfessorResponseDTO professorResponseDTO;
    private List<MonitoriaInscritosResponseDTO> monitoriaInscritosResponseDTO;
    private ProcessoSeletivoResponseDTO processoSeletivoResponseDTO;

    public MonitoriaResponseDTO(Monitoria monitoria) {
        if (monitoria != null) {
            this.id = monitoria.getId();
            this.numeroVaga = monitoria.getNumeroVaga();
            this.numeroVagaBolsa = monitoria.getNumeroVagaBolsa();
            this.cargaHoraria = monitoria.getCargaHoraria();
            this.disciplinaResponseDTO = new DisciplinaResponseDTO(monitoria.getDisciplina());
            this.professorResponseDTO = new ProfessorResponseDTO(monitoria.getProfessor());
            this.processoSeletivoResponseDTO = new ProcessoSeletivoResponseDTO(monitoria.getProcessoSeletivo());
            // Se tiver a lista de inscritos, não precisa enviar para frontend aqui (cuidado com loops)
        }


    }
}
