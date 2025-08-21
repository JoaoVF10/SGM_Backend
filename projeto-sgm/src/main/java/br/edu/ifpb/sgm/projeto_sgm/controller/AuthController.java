package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.LoginResquestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.TokenDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import br.edu.ifpb.sgm.projeto_sgm.service.JwtService;
import br.edu.ifpb.sgm.projeto_sgm.service.PessoaServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    protected AuthenticationManager authenticationManager;
    protected PessoaServiceImp pessoaServiceImp;
    private JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            PessoaServiceImp pessoaServiceImp) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.pessoaServiceImp = pessoaServiceImp;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginResquestDTO login) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken(authentication);

            PessoaResponseDTO pessoaDTO = pessoaServiceImp.findDtoByMatricula(login.getUsername());
            TokenDTO tokendto = new TokenDTO(token, pessoaDTO);

            return ResponseEntity.ok(tokendto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
    // Remove a autenticação do usuário atual
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout realizado com sucesso!");
    }

}