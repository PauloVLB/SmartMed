package br.ufrn.DASH.exception;

import lombok.Getter;

@Getter
public class ProntuarioTemplateException extends DashException{
    private final Long id;

    public ProntuarioTemplateException(Long id) {
        super();
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return "Prontuario com id " + id + " foi definido como template, logo deve permanecer sem respostas.";
    }
}
