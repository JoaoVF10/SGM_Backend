package br.edu.ifpb.sgm.projeto_sgm.model;

import br.edu.ifpb.sgm.projeto_sgm.model.embeddable.MonitoriaInscritoId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "monitoria_inscritos")
public class MonitoriaInscritos {

    @EmbeddedId
    private MonitoriaInscritoId id = new MonitoriaInscritoId();


    @ManyToOne
    @MapsId("monitoriaId")
    @JoinColumn(name = "monitoria_id")
    @JsonIgnore
    private Monitoria monitoria;

    @ManyToOne
    @MapsId("alunoId")
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @Column(nullable = false)
    private boolean selecionado;


}
