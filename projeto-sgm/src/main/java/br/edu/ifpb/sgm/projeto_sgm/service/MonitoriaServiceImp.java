package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.*;
import br.edu.ifpb.sgm.projeto_sgm.mapper.MonitoriaMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.*;
import br.edu.ifpb.sgm.projeto_sgm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MonitoriaServiceImp {

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProcessoSeletivoRepository processoSeletivoRepository;

    @Autowired
    private MonitoriaInscricoesRepository monitoriaInscricoesRepository;

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private MonitoriaMapper monitoriaMapper;

    @Autowired
    private PessoaRepository pessoaRepository;

    public ResponseEntity<MonitoriaResponseDTO> salvar(MonitoriaRequestDTO dto) {
        if (dto.getDisciplinaId() == null) {
            throw new IllegalArgumentException("O ID da disciplina é obrigatório.");
        }

        if (dto.getProfessorId() == null) {
            throw new IllegalArgumentException("O ID do professor é obrigatório.");
        }

        if (dto.getProcessoSeletivoId() == null) {
            throw new IllegalArgumentException("O ID do processo seletivo é obrigatório.");
        }

        Monitoria monitoria = monitoriaMapper.toEntity(dto);

        monitoria.setDisciplina(buscarDisciplina(dto.getDisciplinaId()));
        monitoria.setProfessor(buscarProfessor(dto.getProfessorId()));
        monitoria.setProcessoSeletivo(buscarProcesso(dto.getProcessoSeletivoId()));
        monitoria.setInscricoes(buscarInscritosMonitoria(dto.getInscricoesId()));

        Monitoria salva = monitoriaRepository.save(monitoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(monitoriaMapper.toResponseDTO(salva));
    }


    public ResponseEntity<MonitoriaResponseDTO> buscarPorId(Long id) {
        Monitoria monitoria = monitoriaRepository.findById(id)
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + id + " não encontrada."));
        return ResponseEntity.ok(monitoriaMapper.toResponseDTO(monitoria));
    }

    public ResponseEntity<List<MonitoriaResponseDTO>> listarTodos() {
        List<Monitoria> monitorias = monitoriaRepository.findAll();
        List<MonitoriaResponseDTO> dtos = monitorias.stream()
                .map(monitoriaMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<MonitoriaResponseDTO> atualizar(Long id, MonitoriaRequestDTO dto) {
        Monitoria monitoria = monitoriaRepository.findById(id)
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + id + " não encontrada."));

        monitoriaMapper.updateMonitoriaFromDto(dto, monitoria);

        if (dto.getDisciplinaId() != null) {
            monitoria.setDisciplina(buscarDisciplina(dto.getDisciplinaId()));
        }

        if (dto.getProfessorId() != null) {
            monitoria.setProfessor(buscarProfessor(dto.getProfessorId()));
        }

        if (dto.getInscricoesId() != null) {
            monitoria.setInscricoes(buscarInscritosMonitoria(dto.getInscricoesId()));
        }


        if (dto.getProcessoSeletivoId() != null) {
            monitoria.setProcessoSeletivo(buscarProcesso(dto.getProcessoSeletivoId()));
        }



        Monitoria atualizada = monitoriaRepository.save(monitoria);
        return ResponseEntity.ok(monitoriaMapper.toResponseDTO(atualizada));
    }

    public ResponseEntity<Void> deletar(Long id) {
        Monitoria monitoria = monitoriaRepository.findById(id)
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + id + " não encontrada."));
        monitoriaRepository.delete(monitoria);
        return ResponseEntity.noContent().build();
    }

    // Métodos auxiliares

    private Disciplina buscarDisciplina(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com ID " + id + " não encontrada."));
    }

    private Professor buscarProfessor(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + id + " não encontrado."));
    }


    private ProcessoSeletivo buscarProcesso(Long id) {
        return processoSeletivoRepository.findById(id)
                .orElseThrow(() -> new ProcessoSeletivoNotFoundException("Processo seletivo com ID " + id + " não encontrado."));
    }

    private List<MonitoriaInscritos> buscarInscritosMonitoria(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        List<Long> idsNaoEncontrados = ids.stream()
                .filter(id -> monitoriaInscricoesRepository.findById(id).isEmpty())
                .toList();

        if (!idsNaoEncontrados.isEmpty()) {
            throw new MonitoriaNotFoundException("IDs de disciplinas inválidos: " + idsNaoEncontrados);
        }

        return ids.stream()
                .map(id -> monitoriaInscricoesRepository.findById(id).get())
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> inscreverAluno(Long monitoriaId, String identificadorUsuario) {
        // Buscar Pessoa por matrícula (padrão do seu login)
        Optional<Pessoa> pessoaOpt = pessoaRepository.findByMatricula(identificadorUsuario);
        if (pessoaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Pessoa pessoa = pessoaOpt.get();

        // Verifica se é aluno
        if (pessoa.getAluno() == null) {
            return ResponseEntity.badRequest().body("Apenas alunos podem se inscrever.");
        }

        Aluno aluno = pessoa.getAluno();

        Optional<Monitoria> monitoriaOpt = monitoriaRepository.findById(monitoriaId);
        if (monitoriaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Monitoria não encontrada.");
        }

        Monitoria monitoria = monitoriaOpt.get();

        boolean jaInscrito = monitoriaInscricoesRepository.existsByAlunoAndMonitoria(aluno, monitoria);
        if (jaInscrito) {
            return ResponseEntity.badRequest().body("Você já está inscrito nesta monitoria.");
        }

        MonitoriaInscritos inscricao = new MonitoriaInscritos();
        inscricao.setAluno(aluno);
        inscricao.setMonitoria(monitoria);
        inscricao.setSelecionado(false);

        monitoriaInscricoesRepository.save(inscricao);

        return ResponseEntity.ok("Inscrição realizada com sucesso.");
    }

    public ResponseEntity<?> listarInscricoesDoAluno(String identificadorUsuario) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findByMatricula(identificadorUsuario);
        if (pessoaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Pessoa pessoa = pessoaOpt.get();

        if (pessoa.getAluno() == null) {
            return ResponseEntity.badRequest().body("Apenas alunos possuem inscrições.");
        }

        Aluno aluno = pessoa.getAluno();

        List<MonitoriaInscritos> inscricoes = monitoriaInscricoesRepository.findByAluno(aluno);

        List<MonitoriaInscritosResponseDTO> dtos = inscricoes.stream()
                .map(MonitoriaInscritosResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }

}
