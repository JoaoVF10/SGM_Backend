package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.NivelCurso;
import lombok.Data;

import java.util.List;

@Data
public class CursoResponseDTO {

    private Long id;
    private String nome;
    private NivelCurso nivel;
    private int duracao; // em semestres, por exemplo
    private InstituicaoResponseDTO instituicao;

}
