package br.ufrn.DASH.model;

import br.ufrn.DASH.model.interfaces.DashEntity;
import br.ufrn.DASH.model.interfaces.Ordenavel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Opcao implements DashEntity, Ordenavel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String textoAlternativa;
    private Integer ordem;

    @ManyToOne
    private Quesito quesito;

    public Opcao duplicar() {
        Opcao opcao = new Opcao();
        opcao.setTextoAlternativa(this.textoAlternativa);
        opcao.setOrdem(this.ordem);

        return opcao;
    }
}
