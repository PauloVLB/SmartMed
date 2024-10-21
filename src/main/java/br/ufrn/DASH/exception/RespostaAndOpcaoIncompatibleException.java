package br.ufrn.DASH.exception;

import lombok.Getter;

@Getter
public class RespostaAndOpcaoIncompatibleException extends DashException{
    private final Long idResposta;
    private final Long idOpcao;

    public RespostaAndOpcaoIncompatibleException(Long idResposta, Long idOpcao) {
        super();
        this.idResposta = idResposta;
        this.idOpcao = idOpcao;
    }
    
    @Override
    public String getMessage() {
        return "Opcao com id " + idOpcao + " nao se encontra no mesmo quesito que a Resposta com id " + idResposta + ".";
    }
}
