package br.ufrn.DASH.model;

import br.ufrn.DASH.model.enums.TipoResposta;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Quesito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String enunciado;
    private Boolean obrigatorio;
    private Integer ordem;
    private Integer nivel;

    @OneToOne(mappedBy = "quesito")
    private Resposta resposta;

    private TipoResposta tipoResposta;
}
