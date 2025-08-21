package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.Professor;
import lombok.Data;
import java.util.List;

@Data
public class ProfessorResponseDTO {

    private Long id;
    protected String cpf;
    protected String nome;
    protected String email;
    protected String emailAcademico;
    protected String matricula;
    protected InstituicaoResponseDTO instituicaoResponseDTO;
    private List<DisciplinaResponseDTO> disciplinasResponseDTO;
    private List<CursoResponseDTO> cursosResponseDTO;

    public ProfessorResponseDTO(Professor professor) {
        if (professor != null) {
            this.id = professor.getId();
            this.nome = professor.getPessoa().getNome();
            this.email = professor.getPessoa().getEmail();

        }
    }
}
