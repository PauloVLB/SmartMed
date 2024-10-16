package br.ufrn.DASH.model;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.DASH.model.interfaces.GenericEntity;
import br.ufrn.DASH.model.interfaces.Ordenavel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Secao implements GenericEntity, Ordenavel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    private Integer ordem;
    private Integer nivel;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Secao> subSecoes = new ArrayList<Secao>();

    @ManyToOne
    private Secao superSecao;
    
    @ManyToOne
    private Prontuario prontuario;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Quesito> quesitos = new ArrayList<Quesito>();

    public Secao duplicar() {
        Secao secao = new Secao();
        secao.setTitulo(this.titulo);
        secao.setOrdem(this.ordem);
        secao.setNivel(this.nivel);

        for (Secao subSecao : this.subSecoes) {
            Secao novaSubSecao = subSecao.duplicar();
            novaSubSecao.setSuperSecao(secao);
            secao.getSubSecoes().add(novaSubSecao);
        }

        for (Quesito quesito : this.quesitos) {
            Quesito novoQuesito = quesito.duplicar();
            novoQuesito.setSecao(secao);
            secao.getQuesitos().add(novoQuesito);
        }

        return secao;
    }
}
