package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.LoginResquestDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.SuapService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suap")
public class SuapController {

    private final SuapService SuapService;

    public SuapController(SuapService suapService) {
        SuapService = suapService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginSuap(@RequestBody LoginResquestDTO request) {

        try {
            String token = SuapService.obterToken(request.getUsername(), request.getPassword());
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
            }

            String dadosAluno = SuapService.consultarDadosAluno(token);
            return ResponseEntity.ok(new JSONObject(dadosAluno).toMap());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o SUAP");
        }
    }
}
