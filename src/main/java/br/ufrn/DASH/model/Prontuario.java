package br.ufrn.DASH.model;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.DASH.model.interfaces.GenericEntity;
import br.ufrn.DASH.model.interfaces.ItemOrdenavelUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@AllArgsConstructor
@NoArgsConstructor
public class Prontuario implements GenericEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private Boolean finalizado = false;
    private Boolean ehPublico;
    private Boolean ehTemplate;

    @Column(length = 9999)
    private String diagnosticoLLM;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prontuario")
    private List<Secao> secoes = new ArrayList<Secao>();

    public List<Secao> getSecoes() {
        ItemOrdenavelUtils.ordenar(secoes);
        return secoes;
    }

    public Prontuario duplicar(Usuario novoUsuario) {
        Prontuario prontuario = new Prontuario();
        prontuario.setNome(this.nome + " - Cópia");
        prontuario.setDescricao(this.descricao);
        prontuario.setEhPublico(this.ehPublico);
        prontuario.setEhTemplate(this.ehTemplate);
        prontuario.setUsuario(novoUsuario);

        for (Secao secao : this.secoes) {
            Secao novaSecao = secao.duplicar();
            novaSecao.setProntuario(prontuario);
            prontuario.getSecoes().add(novaSecao);
        }

        return prontuario;
    }
}
