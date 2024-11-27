package br.ufrn.DASH.model;

import java.util.List;
import java.util.ArrayList;

import br.ufrn.DASH.model.interfaces.GenericEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
public class Resposta implements GenericEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ElementCollection
    @Lob
    private List<String> conteudo = new ArrayList<String>();

    @OneToOne
    private Quesito quesito;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Opcao> opcoesMarcadas = new ArrayList<Opcao>();
}

