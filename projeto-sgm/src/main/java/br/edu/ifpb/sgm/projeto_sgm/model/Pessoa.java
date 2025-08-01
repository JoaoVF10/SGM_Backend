package br.edu.ifpb.sgm.projeto_sgm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(length = 14, nullable = false, unique = true)
    protected String cpf;

    @Column(nullable = false)
    protected String nome;

    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false, unique = true)
    protected String emailAcademico;

    @ManyToOne(optional = false)
    protected Instituicao instituicao;

    @Column(length = 12, nullable = false, unique = true)
    protected String matricula;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_pessoa_role",
            joinColumns = @JoinColumn(name="pessoa_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private Aluno aluno;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, length = 60)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    public void setSenha(String senha) {
        this.senha = senha;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return matricula;
    }

    public String[] arrayRoles() {
        int size = roles.size();
        String[] array = new String[size];

        for(int i = 0; i < size; i++) {
            array[i] = roles.get(i).getRole();
        }
        return array;
    }
}
