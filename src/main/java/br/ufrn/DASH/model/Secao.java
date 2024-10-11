package br.ufrn.DASH.model;

import java.util.List;
import java.util.ArrayList;

import br.ufrn.DASH.model.interfaces.GenericEntity;
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
public class Secao implements GenericEntity{
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
}
