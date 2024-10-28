package br.ufrn.DASH.model;


import java.util.List;
import java.util.ArrayList;

import br.ufrn.DASH.model.enums.TipoResposta;
import br.ufrn.DASH.model.interfaces.GenericEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quesito implements GenericEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String enunciado;
    private Boolean obrigatorio;
    private Integer ordem;
    private Integer nivel;
    private TipoResposta tipoResposta;

    @OneToOne(mappedBy = "quesito")
    private Resposta resposta;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quesito")
    private List<Opcao> opcoes = new ArrayList<Opcao>();

    @ManyToMany
    @JoinTable(name = "quesito_opcao_habilitadora",
               joinColumns = @JoinColumn(name = "quesito_id"),
               inverseJoinColumns = @JoinColumn(name = "opcao_id"))
    private List<Opcao> opcoesHabilitadoras = new ArrayList<Opcao>(); 

    @ManyToOne
    private Secao secao;

    @ManyToOne
    private Quesito superQuesito;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Quesito> subQuesitos = new ArrayList<Quesito>();

    public Quesito duplicar() {
        Quesito quesito = new Quesito();
        quesito.setEnunciado(this.enunciado);
        quesito.setObrigatorio(this.obrigatorio);
        quesito.setOrdem(this.ordem);
        quesito.setNivel(this.nivel);
        quesito.setTipoResposta(this.tipoResposta);
        
        for(Opcao opcao : this.opcoes) {
            Opcao novaOpcao = opcao.duplicar();
            novaOpcao.setQuesito(quesito);
            quesito.getOpcoes().add(novaOpcao);
        }

        for(Opcao opcaoHabilitadora : this.opcoesHabilitadoras) {
            Opcao novaOpcaoHabilitadora = opcaoHabilitadora.duplicar();
            quesito.getOpcoesHabilitadoras().add(novaOpcaoHabilitadora);
        }

        for(Quesito subQuesito : this.subQuesitos) {
            Quesito novoSubQuesito = subQuesito.duplicar();
            if(subQuesito.getSecao() != null) {
                novoSubQuesito.setSecao(quesito.getSecao());
            }
            if(subQuesito.getSuperQuesito() != null) {
                novoSubQuesito.setSuperQuesito(quesito);
            }
            quesito.getSubQuesitos().add(novoSubQuesito);
        }

        return quesito;
    }
}
