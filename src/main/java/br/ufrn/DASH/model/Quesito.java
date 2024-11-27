package br.ufrn.DASH.model;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import br.ufrn.DASH.model.enums.TipoResposta;
import br.ufrn.DASH.model.interfaces.GenericEntity;
import br.ufrn.DASH.model.interfaces.Item;
import br.ufrn.DASH.model.interfaces.ItemUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Quesito implements GenericEntity, Item{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String enunciado;
    private Boolean obrigatorio;
    private Integer ordem;
    private Integer nivel;
    private TipoResposta tipoResposta;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "quesito")
    private Resposta resposta;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quesito")
    private List<Opcao> opcoes = new ArrayList<Opcao>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Opcao> opcoesHabilitadoras = new ArrayList<Opcao>(); 

    @ManyToOne
    private Secao secao;

    @ManyToOne
    private Quesito superQuesito;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Quesito> subQuesitos = new ArrayList<Quesito>();

    public List<Item> getSubItens() {
        List<Item> subItens = new ArrayList<Item>();
        subItens.addAll(this.subQuesitos);
        ItemUtils.ordenar(subItens);
        return subItens;
    }

    public Pair<Quesito, Map<Opcao, Opcao>> duplicar(Map<Opcao, Opcao> opcoesDuplicadas) {
        Quesito quesito = new Quesito();
        quesito.setEnunciado(this.enunciado);
        quesito.setObrigatorio(this.obrigatorio);
        quesito.setOrdem(this.ordem);
        quesito.setNivel(this.nivel);
        quesito.setTipoResposta(this.tipoResposta);
        
        for(Opcao opcao : this.opcoes) {
            if(opcoesDuplicadas.containsKey(opcao)) {
                quesito.getOpcoes().add(opcoesDuplicadas.get(opcao));
            } else {
                Opcao novaOpcao = opcao.duplicar();
                novaOpcao.setQuesito(quesito);
                quesito.getOpcoes().add(novaOpcao);
                opcoesDuplicadas.put(opcao, novaOpcao);
            }
        }

        for(Opcao opcaoHabilitadora : this.opcoesHabilitadoras) {
            if(opcoesDuplicadas.containsKey(opcaoHabilitadora)) {
                quesito.getOpcoesHabilitadoras().add(opcoesDuplicadas.get(opcaoHabilitadora));
            } else {
                Opcao novaOpcaoHabilitadora = opcaoHabilitadora.duplicar();
                novaOpcaoHabilitadora.setQuesito(quesito);
                quesito.getOpcoesHabilitadoras().add(novaOpcaoHabilitadora);
                opcoesDuplicadas.put(opcaoHabilitadora, novaOpcaoHabilitadora);
            }
        }

        for(Quesito subQuesito : this.subQuesitos) {
            Pair<Quesito, Map<Opcao, Opcao>> pairQuesitoMapa = subQuesito.duplicar(opcoesDuplicadas);
            Quesito novoSubQuesito = pairQuesitoMapa.getFirst();
            opcoesDuplicadas = pairQuesitoMapa.getSecond();
            if(subQuesito.getSecao() != null) {
                novoSubQuesito.setSecao(quesito.getSecao());
            }
            if(subQuesito.getSuperQuesito() != null) {
                novoSubQuesito.setSuperQuesito(quesito);
            }
            quesito.getSubQuesitos().add(novoSubQuesito);
        }

        return new Pair<Quesito, Map<Opcao, Opcao>>(quesito, opcoesDuplicadas);
    }

    public Prontuario getProntuario() {
        if(this.secao != null) {
            return this.secao.getProntuario();
        } else if(this.superQuesito != null) {
            return this.superQuesito.getProntuario();
        } else {
            return null;
        }
    }
}
