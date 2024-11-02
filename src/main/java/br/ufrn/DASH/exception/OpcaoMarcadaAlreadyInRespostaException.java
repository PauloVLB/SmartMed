package br.ufrn.DASH.exception;

import lombok.Getter;

@Getter
public class OpcaoMarcadaAlreadyInRespostaException extends DashException {
    private final Long idResposta;
    private final Long idOpcao;

    public OpcaoMarcadaAlreadyInRespostaException(Long idResposta, Long idOpcao) {
        super();
        this.idResposta = idResposta;
        this.idOpcao = idOpcao;
    }
    
    @Override
    public String getMessage() {
        return "Resposta com id " + idResposta + " ja tem como marcada a opção de id " + idOpcao + ".";
    }
    
}
