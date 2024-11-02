package br.ufrn.DASH.exception;

import lombok.Getter;

@Getter
public class DiagnosticoNotInProntuarioException extends DashException{
    private final Long idProntuario;
    private final Long idDiagnostico;

    public DiagnosticoNotInProntuarioException(Long idProntuario, Long idDiagnostico) {
        super();
        this.idProntuario = idProntuario;
        this.idDiagnostico = idDiagnostico;
    }
    
    @Override
    public String getMessage() {
        return "Diagnostico com id " + idDiagnostico + " nao encontrado em um prontuario com id " + idProntuario + ".";
    }
}