package br.ufrn.DASH.exception;

import lombok.Getter;

@Getter
public class DiagnosticoAndOpcaoIncompatibleException extends DashException{
    private final Long idDiagnostico;
    private final Long idOpcao;

    public DiagnosticoAndOpcaoIncompatibleException(Long idDiagnostico, Long idOpcao) {
        super();
        this.idDiagnostico = idDiagnostico;
        this.idOpcao = idOpcao;
    }
    
    @Override
    public String getMessage() {
        return "Opcao com id " + idOpcao + " nao se encontra no mesmo prontuario que o Diagnostico com id " + idDiagnostico + ".";
    }
}
