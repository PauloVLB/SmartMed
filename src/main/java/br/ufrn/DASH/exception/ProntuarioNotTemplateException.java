package br.ufrn.DASH.exception;

import lombok.Getter;

@Getter
public class ProntuarioNotTemplateException extends DashException{
    private final Long id;

    public ProntuarioNotTemplateException(Long id) {
        super();
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return "Prontuario com id " + id + " possui atributo ehTemplate com o valor falso.";
    }
}
