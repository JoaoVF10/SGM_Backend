package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import br.edu.ifpb.sgm.projeto_sgm.model.MonitoriaInscritos;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import br.edu.ifpb.sgm.projeto_sgm.repository.MonitoriaInscricoesRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.PessoaRepository;
import br.edu.ifpb.sgm.projeto_sgm.service.MonitoriaServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/monitorias")
public class MonitoriaControllerImp {

    @Autowired
    private MonitoriaServiceImp monitoriaService;

    @Autowired
    PessoaRepository pessoaRepository;

    @Autowired
    private MonitoriaInscricoesRepository monitoriaInscricoesRepository;

    @PostMapping
    public ResponseEntity<MonitoriaResponseDTO> criar(@RequestBody MonitoriaRequestDTO dto) {
        return monitoriaService.salvar(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        return monitoriaService.buscarPorId(id);
    }

    @GetMapping
    public ResponseEntity<List<MonitoriaResponseDTO>> listarTodos() {
        return monitoriaService.listarTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonitoriaResponseDTO> atualizar(@PathVariable Long id,
                                                          @RequestBody MonitoriaRequestDTO dto) {
        return monitoriaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return monitoriaService.deletar(id);
    }

    @PostMapping("/{id}/inscrever")
    public ResponseEntity<?> inscrever(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        System.out.println("Email do autenticado: " + email);
        System.out.println("Usuário autenticado: " + authentication.getName());
        return monitoriaService.inscreverAluno(id, email);
    }
    @GetMapping("/inscricoes")
    public ResponseEntity<?> listarInscricoesDoAluno(@AuthenticationPrincipal UserDetails user) {
        String matricula = user.getUsername();
        return monitoriaService.listarInscricoesDoAluno(matricula);
    }


}
