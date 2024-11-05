package br.ufrn.DASH.model;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.DASH.model.interfaces.GenericEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class Opcao implements GenericEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String textoAlternativa;
    private Integer ordem;

    @ManyToOne
    private Quesito quesito;
    

    @ManyToMany(mappedBy = "opcoesMarcadas")
    private List<Diagnostico> diagnosticos = new ArrayList<Diagnostico>();

    public Opcao duplicar() {
        Opcao opcao = new Opcao();
        opcao.setTextoAlternativa(this.textoAlternativa);
        opcao.setOrdem(this.ordem);

        return opcao;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) return false;

        Opcao opcao = (Opcao) o;

        return id.equals(opcao.id);
    }
}
