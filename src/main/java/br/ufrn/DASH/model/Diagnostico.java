package br.ufrn.DASH.model;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.DASH.model.interfaces.GenericEntity;
import static br.ufrn.DASH.model.interfaces.GenericEntitySortById.sortById;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Diagnostico implements GenericEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String descricao;
    
    @ManyToMany
    @JoinTable(name = "diagnostico_opcao_resposta",
    joinColumns = @JoinColumn(name = "diagnostico_id"),
    inverseJoinColumns = @JoinColumn(name = "opcao_id"))
    private List<Opcao> opcoesMarcadas = new ArrayList<Opcao>(); 
    
    @ManyToOne
    private Prontuario prontuario;
    
    public List<Opcao> getOpcoesMarcadas(){
        if(opcoesMarcadas != null)
            sortById(opcoesMarcadas);
        return opcoesMarcadas;
    }
    public static Diagnostico inconclusivo() {
        return new Diagnostico(null, "Inconclusivo", null, null);
    }
}
