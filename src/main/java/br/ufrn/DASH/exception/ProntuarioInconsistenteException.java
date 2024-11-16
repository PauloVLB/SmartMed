package br.ufrn.DASH.exception;

public class ProntuarioInconsistenteException extends DashException {
    String listaErros;

    public ProntuarioInconsistenteException(String listaErros) {
        this.listaErros = listaErros;
    }

    @Override
    public String getMessage() {
        return "Prontuário não pode ser finalizado pois se encontra inconsistente. Erros: " + listaErros;
    }
    
}
