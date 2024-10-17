package br.ufrn.DASH.model;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.DASH.model.enums.TipoUsuario;
import br.ufrn.DASH.model.interfaces.DashEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Usuario implements DashEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    private String nome;
    private String login;
    private String senha;

    private TipoUsuario tipoUsuario;

    @OneToMany(mappedBy = "usuario")
    private List<Prontuario> prontuarios = new ArrayList<Prontuario>();
}
