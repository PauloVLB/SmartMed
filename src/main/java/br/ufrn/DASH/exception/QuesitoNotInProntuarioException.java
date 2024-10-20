package br.ufrn.DASH.exception;

import lombok.Getter;

@Getter
public class QuesitoNotInProntuarioException extends DashException{
    private final Long idProntuario;
    private final Long idQuesito;

    public QuesitoNotInProntuarioException(Long idProntuario, Long idQuesito) {
        super();
        this.idProntuario = idProntuario;
        this.idQuesito = idQuesito;
    }
    
    @Override
    public String getMessage() {
        return "Quesito com id " + idQuesito + " nao se encontra no prontuario com id " + idProntuario + ".";
    }
}