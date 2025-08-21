package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.Disciplina;
import lombok.Data;

@Data
public class DisciplinaResponseDTO {

    private Long id;
    private String nome;
    private int cargaHoraria;
    private CursoResponseDTO cursoResponseDTO;

    public DisciplinaResponseDTO(Disciplina disciplina) {
        if (disciplina != null) {
            this.id = disciplina.getId();
            this.nome = disciplina.getNome();

        }
    }

}
