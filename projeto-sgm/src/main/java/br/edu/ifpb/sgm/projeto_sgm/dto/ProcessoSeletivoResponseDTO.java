package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.ProcessoSeletivo;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProcessoSeletivoResponseDTO {

    private Long id;
    private LocalDate inicio;
    private LocalDate fim;
    private String numero;
    private InstituicaoResponseDTO instituicaoResponseDTO;

    public ProcessoSeletivoResponseDTO(ProcessoSeletivo processoSeletivo) {
        if (processoSeletivo != null) {
            this.id = processoSeletivo.getId();
            this.numero = processoSeletivo.getNumero();


        }

    }
}
