package br.ufrn.DASH.exception;

import lombok.Getter;

@Getter
public class RespostaFullOfOpcaoException extends DashException{
    private final Long idResposta;

    public RespostaFullOfOpcaoException(Long idResposta) {
        super();
        this.idResposta = idResposta;
    }
    
    @Override
    public String getMessage() {
        return "Resposta com id " + idResposta + " nao pode ter outra opcao adicionada a ela (verificar o tipo de respota ou quantas opcoes ja foram marcadas).";
    }
}
