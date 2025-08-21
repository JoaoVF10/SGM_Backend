package br.edu.ifpb.sgm.projeto_sgm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professor {

    @Id
    protected Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Pessoa pessoa;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "professor_id")
    private List<Disciplina> disciplinas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "coordenador_curso",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private Set<Curso> cursos;

    @Column
    private Boolean cadastrado = true;

    @Column(nullable = false)
    private boolean coordenador = false;

}
