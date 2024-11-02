package br.ufrn.DASH.exception;

import lombok.Getter;

@Getter
public class OpcaoAlreadyInDiagnosticoException extends DashException {
    private final Long idDiagnostico;
    private final Long idOpcao;

    public OpcaoAlreadyInDiagnosticoException(Long idDiagnostico, Long idOpcao) {
        super();
        this.idDiagnostico = idDiagnostico;
        this.idOpcao = idOpcao;
    }
    
    @Override
    public String getMessage() {
        return "Diagnostico com id " + idDiagnostico + " ja contem a opção de id " + idOpcao + ".";
    }
    
}
