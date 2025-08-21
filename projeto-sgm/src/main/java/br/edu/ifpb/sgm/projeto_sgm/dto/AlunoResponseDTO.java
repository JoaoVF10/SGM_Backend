package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import lombok.Data;
import java.util.Set;

@Data
public class AlunoResponseDTO {

    protected Long id;
    protected String cpf;
    protected String nome;
    protected String email;
    protected String emailAcademico;
    protected InstituicaoResponseDTO instituicaoResponseDTO;
    protected String matricula;
    private Set<DisciplinaResponseDTO> disciplinasPagasResponseDTO;
    private Set<DisciplinaResponseDTO> disciplinasMonitoriaResponseDTO;

    public AlunoResponseDTO(Aluno aluno) {
        if (aluno != null) {
            this.id = aluno.getId();
            this.nome = aluno.getPessoa().getNome();
            this.matricula = aluno.getPessoa().getMatricula();
            this.email = aluno.getPessoa().getEmail();

        }


    }
}
